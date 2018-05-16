package example;

import com.google.common.collect.ImmutableList;
import feature.example.PluginScheduler;
import io.prodity.commons.plugin.annotate.Plugin;
import io.prodity.commons.plugin.annotate.PluginDependency;
import io.prodity.commons.spigot.inject.Task;
import io.prodity.commons.spigot.inject.TimeUnit;
import io.prodity.commons.spigot.plugin.ProditySpigotPlugin;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

@Plugin(name = "Renown", version = "1.0.0", description = "Gives players renown based on actions.")
@PluginDependency("ProdityCommons")
@PluginDependency("ExampleCustomFeature")
public class RenownPlugin extends ProditySpigotPlugin {

    // PluginScheduler is a custom feature provided by ExampleCustomFeature
    @Inject
    private PluginScheduler scheduler;

    @Inject
    private RenownStorage renownStorage;

    @Task(period = 10, unit = TimeUnit.MINUTES)
    public void givePeriodic() {
        List<Player> onlinePlayers = ImmutableList.copyOf(this.getServer().getOnlinePlayers());
        this.renownStorage.bulkIncrement(onlinePlayers, 1)
            .thenAcceptAsync(ignored -> {
                for (Player player : onlinePlayers) {
                    if (player.isValid()) {
                        player.sendMessage(ChatColor.GREEN + "You just gained 1 renown for playing on the server!");
                    }
                }
            }, this.scheduler::runTask);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPvp(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player killed = (Player) event.getEntity();
            Player killer = this.getKiller(killed);
            if (killer != null) {
                // Decrement their renown, then message them on the main thread
                this.renownStorage.decrementRenown(killer, 5)
                    .thenAcceptAsync(ignored -> {
                        if (killer.isValid()) {
                            killer.sendMessage(ChatColor.RED + "You just lost 5 renown for murder!");
                        }
                    }, this.scheduler::runTask);
            }

        }
    }

    // This isn't meant to be extensive, this is an example plugin
    @Nullable
    private Player getKiller(Player killed) {
        if (killed.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) killed.getLastDamageCause()).getDamager();
            if (damager instanceof Player) {
                return (Player) damager;
            } else if (damager instanceof Projectile) {
                Projectile projectile = (Projectile) damager;
                if (projectile.getShooter() instanceof Player) {
                    return (Player) projectile.getShooter();
                }
            }
        }
        return null;
    }

}