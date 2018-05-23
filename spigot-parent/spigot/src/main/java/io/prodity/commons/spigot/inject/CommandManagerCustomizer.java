package io.prodity.commons.spigot.inject;

import co.aikar.commands.BukkitCommandManager;
import org.jvnet.hk2.annotations.Contract;

/**
 * This has to be platform specific because the base
 * CommandManager class is a type with 6 generic parameters
 * and that's just not useful to anyone.
 */
@Contract
public interface CommandManagerCustomizer {
    void customize(BukkitCommandManager commandManager);
}
