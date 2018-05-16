package example;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jvnet.hk2.annotations.Service;

/**
 * An implementation of RenownStorage that never saves it. When a player
 * logs off their renown is deleted.
 */
@Service
public class MemoryRenownStorage implements RenownStorage, Listener {

    private final Map<UUID, Integer> renown = new HashMap<>();

    @Override
    public CompletableFuture<Void> bulkIncrement(Collection<Player> players, int amount) {
        players.forEach(player -> this.incrementRenown(player, amount));
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> incrementRenown(Player player, int amount) {
        this.renown.compute(player.getUniqueId(), (k, v) -> v == null ? amount : v + amount);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> decrementRenown(Player player, int amount) {
        this.renown.compute(player.getUniqueId(), (k, v) -> v == null ? amount : v - amount);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Integer> getRenown(Player player) {
        return CompletableFuture.completedFuture(this.renown.getOrDefault(player.getUniqueId(), 0));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.renown.remove(event.getPlayer().getUniqueId());
    }
}
