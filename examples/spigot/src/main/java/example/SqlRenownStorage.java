package example;

import io.prodity.commons.inject.Async;
import org.bukkit.entity.Player;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Service
public class SqlRenownStorage implements RenownStorage {
    // Async is an automatically created wrapper class for performing async operations.
    // It can be injected around any type.
    private final Async<RenownDao> async;

    @Inject
    public SqlRenownStorage(Async<RenownDao> async) {
        this.async = async;
    }

    @Override
    public CompletableFuture<Void> bulkIncrement(Collection<Player> players, int amount) {
        // If this was a real plugin we'd want to do a batched update
        return this.async.forEach(players, (dao, player) -> dao.incrementRenown(player.getUniqueId(), amount)).whenComplete(this::printError);
    }

    @Override
    public CompletableFuture<Void> incrementRenown(Player player, int amount) {
        return this.async.run(dao -> {
            dao.incrementRenown(player.getUniqueId(), amount);
        }).whenComplete(this::printError);
    }

    @Override
    public CompletableFuture<Void> decrementRenown(Player player, int amount) {
        return this.async.run(dao -> dao.incrementRenown(player.getUniqueId(), -amount)).whenComplete(this::printError);
    }

    @Override
    public CompletableFuture<Integer> getRenown(Player player) {
        return this.async.get(dao -> dao.getRenown(player.getUniqueId())).whenComplete(this::printError);
    }

    private <T> T printError(T value, Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace();
        }
        return value;
    }
}
