package io.prodity.commons.spigot.db.itemmeta;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.bukkit.inventory.meta.ItemMeta;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.statement.StatementContext;

public class ItemMetaArgument implements Argument {

    private final ItemMeta itemMeta;

    public ItemMetaArgument(ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
    }

    @Override
    public void apply(int position, PreparedStatement statement, StatementContext ctx) throws SQLException {
        statement.setBytes(position, ItemMetaCustomizer.toBytes(this.itemMeta));
    }

}