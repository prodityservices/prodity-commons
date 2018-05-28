package io.prodity.commons.spigot.thread;

import javax.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jvnet.hk2.annotations.Service;

@Service
public class Threads {

    private final BukkitScheduler scheduler;
    private final JavaPlugin plugin;

    @Inject
    public Threads(BukkitScheduler scheduler, JavaPlugin plugin) {
        this.scheduler = scheduler;
        this.plugin = plugin;
    }

    /**
     * Executes the specified {@link Runnable} on Bukkit's main {@link Thread}.
     *
     * @param runnable the {@link Runnable}
     */
    public void main(Runnable runnable) {
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
        } else {
            this.scheduler.runTask(this.plugin, runnable);
        }
    }

    /**
     * Executes the specified {@link Runnable} on a {@link Thread} separate from Bukkit's main thread.
     *
     * @param runnable the {@link Runnable}
     */
    public void async(Runnable runnable) {
        if (!Bukkit.isPrimaryThread()) {
            runnable.run();
        } else {
            this.scheduler.runTaskAsynchronously(this.plugin, runnable);
        }
    }

}