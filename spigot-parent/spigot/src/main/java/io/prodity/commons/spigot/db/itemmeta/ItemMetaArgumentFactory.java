package io.prodity.commons.spigot.db.itemmeta;

import java.sql.Types;
import org.bukkit.inventory.meta.ItemMeta;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

public class ItemMetaArgumentFactory extends AbstractArgumentFactory<ItemMeta> {

    public ItemMetaArgumentFactory() {
        super(Types.BINARY);
    }

    @Override
    protected Argument build(ItemMeta value, ConfigRegistry config) {
        return new ItemMetaArgument(value);
    }

}