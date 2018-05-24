package io.prodity.commons.db;

import org.jdbi.v3.core.Jdbi;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDCustomizer implements JdbiCustomizer {
    @Override
    public void customizeJdbi(Jdbi jdbi) {
        jdbi.registerArgument(new UUIDArgumentFactory());
        jdbi.registerColumnMapper(new UUIDColumnMapper());
    }

    public static byte[] toBytes(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return byteBuffer.array();
    }

    public static UUID fromBytes(byte[] array) {
        if (array.length != 16) {
            throw new IllegalArgumentException("Illegal byte array length: " + array.length);
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        long mostSignificant = byteBuffer.getLong();
        long leastSignificant = byteBuffer.getLong();
        return new UUID(mostSignificant, leastSignificant);
    }
}
