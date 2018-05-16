package feature.example;

import io.prodity.commons.spigot.plugin.ProditySpigotPlugin;
import javax.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * A scheduler that's specific to a single plugin.  A nice little utility.
 */
public class PluginScheduler {

    private final ProditySpigotPlugin plugin;
    private final BukkitScheduler scheduler;

    @Inject
    public PluginScheduler(ProditySpigotPlugin plugin, BukkitScheduler scheduler) {
        this.plugin = plugin;
        this.scheduler = scheduler;
    }

    public void runTask(Runnable runnable) {
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
        } else {
            this.scheduler.runTask(this.plugin, runnable);
        }

    }

    public void runTaskAsync(Runnable runnable) {
        this.scheduler.runTaskAsynchronously(this.plugin, runnable);
    }
}
