package io.prodity.commons.spigot.db.itemmeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.inventory.meta.ItemMeta;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class ItemMetaColumnMapper implements ColumnMapper<ItemMeta> {

    @Override
    public ItemMeta map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
        return ItemMetaCustomizer.fromBytes(r.getBytes(columnNumber));
    }

    @Override
    public ItemMeta map(ResultSet r, String columnLabel, StatementContext ctx) throws SQLException {
        return ItemMetaCustomizer.fromBytes(r.getBytes(columnLabel));
    }

}
