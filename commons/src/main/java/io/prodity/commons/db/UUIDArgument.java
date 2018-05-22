package io.prodity.commons.db;

import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class UUIDArgument implements Argument {

    private final UUID uuid;

    public UUIDArgument(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void apply(int position, PreparedStatement statement, StatementContext ctx) throws SQLException {
        statement.setBytes(position, UUIDFeature.toBytes(this.uuid));
    }


}
