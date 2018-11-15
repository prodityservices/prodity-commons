package io.prodity.commons.bungee.account;

import com.google.common.base.MoreObjects;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class PlayerReference {

	private final UUID id;
	private final String name;
	private final Instant lastSeen;
	@JdbiConstructor
	public PlayerReference(UUID playerId, String playerName, @Nullable Instant playerLastSeen) {
		this.id = playerId;
		this.name = playerName;
		this.lastSeen = playerLastSeen;
	}

	public PlayerReference(UUID playerId, String playerName) {
		this(playerId, playerName, Instant.now());
	}

	public static PlayerReference of(ProxiedPlayer player) {
		return new PlayerReference(player.getUniqueId(), player.getName());
	}

	public UUID getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public boolean isOnline() {
		return ProxyServer.getInstance().getPlayer(this.id) != null;
	}

	/**
	 * Gets the last time the player logged into a server sharing the database.
	 * If the player has never joined the server then the returned value is
	 * null.
	 *
	 * @return the last time the player joined
	 */
	@Nullable
	public Instant getLastSeen() {
		return this.lastSeen;
	}

	@Nullable
	public ProxiedPlayer dereference() {
		return ProxyServer.getInstance().getPlayer(this.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.name);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof PlayerReference)) {
			return false;
		}
		final PlayerReference that = (PlayerReference) object;
		return Objects.equals(this.id, that.id) &&
				Objects.equals(this.name, that.name);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", this.id)
				.add("name", this.id)
				.add("lastSeen", this.lastSeen)
				.toString();
	}

}
