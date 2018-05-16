package example;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;
import org.jvnet.hk2.annotations.Contract;

/**
 * Stores renown for each player.  All methods return a CompletableFuture to support
 * asynchronous storage operations.
 */
@Contract
public interface RenownStorage {

    CompletableFuture<Void> bulkIncrement(Collection<Player> players, int amount);

    CompletableFuture<Void> incrementRenown(Player player, int amount);

    CompletableFuture<Void> decrementRenown(Player player, int amount);

    CompletableFuture<Integer> getRenown(Player player);
}
