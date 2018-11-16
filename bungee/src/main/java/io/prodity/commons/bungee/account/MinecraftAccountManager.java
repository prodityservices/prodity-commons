package io.prodity.commons.bungee.account;

import io.prodity.commons.bungee.plugin.ProdityBungeePlugin;
import io.prodity.commons.inject.Async;
import io.prodity.commons.inject.Eager;
import io.prodity.commons.inject.Export;
import io.prodity.commons.threads.Threads;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.json.simple.parser.ParseException;
import org.jvnet.hk2.annotations.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.logging.Level;

@Service
@Export
public class MinecraftAccountManager implements Eager, Listener {

	private final Async<AccountDao> async;
	private final ProdityBungeePlugin plugin;
	private final Threads threads;

	@Inject
	public MinecraftAccountManager(Async<AccountDao> async, ProdityBungeePlugin plugin, Threads threads) {
		this.async = async;
		this.plugin = plugin;
		this.threads = threads;
	}

	/**
	 * Retrieves a PlayerReference for the player with the given UUID. No
	 * guarantee is provided for which thread this future will complete on.
	 *
	 * @param id the uuid to lookup
	 * @return a future that completes with the player reference
	 */
	public CompletableFuture<Optional<PlayerReference>> getPlayer(UUID id) {
		return this.checkOnline(id)
				.thenCompose(opt -> this.fallback(opt, id, this::checkCache))
				.thenCompose(opt -> this.fallback(opt, id, this::fetchName))
				;
	}

	private CompletableFuture<Optional<PlayerReference>> checkOnline(UUID id) {
		return CompletableFuture.supplyAsync(() -> {
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(id);
			return this.ofNullablePlayer(player);
		}, this::onMain);
	}

	private CompletableFuture<Optional<PlayerReference>> checkCache(UUID id) {
		return this.async.get(dao -> dao.getPlayer(id));
	}

	private CompletableFuture<Optional<PlayerReference>> fetchName(UUID id) {
		return CompletableFuture.supplyAsync(() -> {
			return this.fetchNewName(id).map(name -> new PlayerReference(id, name, null));
		}, this::async);
	}

	/**
	 * Retrieves a PlayerReference for the player with the given name. No guarantee
	 * is provided for which thread this future will complete on.
	 *
	 * @param name the name to lookup
	 * @return a future that completes with the player reference
	 */
	public CompletableFuture<Optional<PlayerReference>> getPlayer(String name) {
		return this.checkOnline(name)
				.thenCompose(opt -> this.fallback(opt, name, this::checkCache))
				.thenCompose(opt -> this.fallback(opt, name, this::fetchUUID))
				;
	}

	private CompletableFuture<Optional<PlayerReference>> checkOnline(String name) {
		return CompletableFuture.supplyAsync(() -> {
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
			return this.ofNullablePlayer(player);
		}, this::onMain);
	}

	private Optional<PlayerReference> ofNullablePlayer(@Nullable ProxiedPlayer player) {
		if (player == null) {
			return Optional.empty();
		}
		return Optional.of(PlayerReference.of(player));
	}

	private CompletableFuture<Optional<PlayerReference>> checkCache(String name) {
		return this.async.get(dao -> dao.getPlayer(name));
	}

	private CompletableFuture<Optional<PlayerReference>> fetchUUID(String name) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return UUIDFetcher.getUUIDOf(name).map(id -> new PlayerReference(id, name, null));
			} catch (Exception e) {
				this.plugin.getLogger().log(Level.WARNING, "Failed to retrieve UUID for " + name, e);
				return Optional.empty();
			}
		}, this::async);
	}

	private <KEY> CompletableFuture<Optional<PlayerReference>> fallback(Optional<PlayerReference> value,
																		KEY key,
																		Function<KEY, CompletableFuture<Optional<PlayerReference>>> fallback) {
		if (value.isPresent()) {
			return CompletableFuture.completedFuture(value);
		} else {
			return fallback.apply(key);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerPreLogin(ServerConnectedEvent event) {
		if (event.getPlayer().getServer() == null) {
			this.async(() -> {
				// Block to ensure all later handlers see the right value in the database
				this.async.run(dao -> dao.updateCache(new PlayerReference(event.getPlayer().getUniqueId(), event.getPlayer().getName()))).join();
				// Now asynchronously fetch new names for all duplicates.  There technically can't ever be more than one
				// duplicate.  This happens very rarely, if at all
				this.async.run(dao -> {
					List<PlayerReference> duplicates = dao.getDuplicates(event.getPlayer().getName());
					for (PlayerReference duplicate : duplicates) {
						// If we fail to get a new name, I guess we'll take care of it next time
						Optional<String> newName = this.fetchNewName(duplicate.getId());
						newName.ifPresent(s -> dao.updateCache(new PlayerReference(duplicate.getId(), s, duplicate.getLastSeen())));
					}
				});
			});
		}
	}

	private void onMain(Runnable runnable) {
		this.threads.main(runnable);
	}

	private void async(Runnable runnable) {
		this.threads.async(runnable);
	}

	private Optional<String> fetchNewName(UUID uuid) {
		try {
			return NameFetcher.getHistory(uuid).map(NameFetcher.NameHistory::getCurrentName);
		} catch (ParseException | IOException e) {
			this.plugin.getLogger().log(Level.WARNING, "Failed to retrieve new name for " + uuid, e);
			return Optional.empty();
		}
	}
}
