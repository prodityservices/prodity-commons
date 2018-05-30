package io.prodity.commons.db.uuid;

import io.prodity.commons.db.JdbiCustomizer;
import java.nio.ByteBuffer;
import java.util.UUID;
import org.jdbi.v3.core.Jdbi;
import org.jvnet.hk2.annotations.Service;

@Service
public class UUIDCustomizer implements JdbiCustomizer {

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

    @Override
    public void customizeJdbi(Jdbi jdbi) {
        jdbi.registerArgument(new UUIDArgumentFactory());
        jdbi.registerColumnMapper(new UUIDColumnMapper());
    }

}
