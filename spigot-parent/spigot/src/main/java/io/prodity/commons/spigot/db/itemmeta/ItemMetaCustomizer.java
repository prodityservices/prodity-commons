package io.prodity.commons.spigot.db.itemmeta;

import com.google.common.reflect.TypeToken;
import io.prodity.commons.db.JdbiCustomizer;
import io.prodity.commons.except.tryto.Try;
import io.prodity.commons.spigot.serialize.BukkitSerializer;
import javax.annotation.Nullable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jdbi.v3.core.Jdbi;
import org.jvnet.hk2.annotations.Service;

@Service
public class ItemMetaCustomizer implements JdbiCustomizer {

    private static final TypeToken<ItemMeta> ITEM_META_TYPE = TypeToken.of(ItemMeta.class);

    public static byte[] toBytes(ItemMeta itemMeta) {
        return Try.to(() -> BukkitSerializer.objectToBytes(itemMeta)).get();
    }

    @Nullable
    public static ItemMeta fromBytes(byte[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Illegal byte array length: " + array.length);
        }
        return Try.to(() -> BukkitSerializer.objectFromBytes(ItemMetaCustomizer.ITEM_META_TYPE, array)).get();
    }

    @Override
    public void customizeJdbi(Jdbi jdbi) {
        jdbi.registerArgument(new ItemMetaArgumentFactory());
        jdbi.registerColumnMapper(new ItemMetaColumnMapper());
    }

}