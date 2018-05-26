package io.prodity.commons.command;

import co.aikar.commands.BaseCommand;
import org.jvnet.hk2.annotations.Contract;

/**
 * By extending this class and annotating your class with @Service it will
 * automatically get registered.  If you for any reason use Aikar's @Dependent
 * annotation I'll shoot you.  Because ProdityCommands are registered when the
 * plugin is enabled, they're eagerly instantiated.
 */
@Contract
public abstract class ProdityCommand extends BaseCommand {

}
