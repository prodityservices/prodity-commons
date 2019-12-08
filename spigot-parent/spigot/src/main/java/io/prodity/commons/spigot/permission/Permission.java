package io.prodity.commons.spigot.permission;

import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface Permission {

    String get();

    /**
     * Whether or not to give this {@link Permission} to all Operators (OPs).<br>
     * Default is true unless method is overridden.
     */
    default boolean giveToOps() {
        return true;
    }

    default boolean has(CommandSender commandSender) {
        return (this.giveToOps() && commandSender.isOp()) || commandSender.hasPermission(this.get());
    }

}