package example;

import co.aikar.commands.annotation.CommandAlias;
import feature.example.PluginScheduler;
import io.prodity.commons.command.ProdityCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Service
public class RenownCommand extends ProdityCommand {
    private final RenownStorage storage;
    private final PluginScheduler scheduler;

    @Inject
    public RenownCommand(RenownStorage storage, PluginScheduler scheduler) {
        this.storage = storage;
        this.scheduler = scheduler;
    }

    @CommandAlias("renown")
    public void getRenown(Player player) {
        this.storage.getRenown(player)
                    .thenAcceptAsync(optRenown -> {
                        int renown = optRenown.orElse(0);
                        if (player.isValid()) {
                            ChatColor color = renown >= 0 ? ChatColor.GREEN : ChatColor.RED;
                            player.sendMessage(color + "You have " + renown + " renown!");
                        }
               }, this.scheduler::runTask);
    }
}
