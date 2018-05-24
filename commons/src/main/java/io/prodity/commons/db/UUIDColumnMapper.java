package io.prodity.commons.db;

import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UUIDColumnMapper implements ColumnMapper<UUID> {

    @Override
    public UUID map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
        return UUIDCustomizer.fromBytes(r.getBytes(columnNumber));
    }

    @Override
    public UUID map(ResultSet r, String columnLabel, StatementContext ctx) throws SQLException {
        return UUIDCustomizer.fromBytes(r.getBytes(columnLabel));
    }
}
