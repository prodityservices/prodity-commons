package io.prodity.commons.spigot.config;

import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.ConfigInjectionContext;
import io.prodity.commons.config.inject.deserialize.TypeElementDeserializer;
import io.prodity.commons.config.inject.deserialize.registry.ElementDeserializerModifier;
import io.prodity.commons.config.inject.deserialize.registry.ElementDeserializerRegistry;
import io.prodity.commons.inject.Export;
import javax.annotation.Nullable;
import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.enchantments.Enchantment;
import org.jvnet.hk2.annotations.Service;

@Service
@Export
public class SpigotElementDeserializerModifier implements ElementDeserializerModifier {

    @Override
    public void modify(ElementDeserializerRegistry registry) {
        registry.register(new EnchantmentDeserializer());
    }

    private class EnchantmentDeserializer extends TypeElementDeserializer<Enchantment> {

        public EnchantmentDeserializer() {
            super(TypeToken.of(Enchantment.class), 0);
        }

        @Nullable
        @Override
        public Enchantment deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) throws Throwable {
            String enchantName = node.getString();
            if (enchantName == null) {
                throw new IllegalStateException("no value present when deserializing Enchantment value");
            }
            enchantName = enchantName.toUpperCase();

            return Enchantment.getByName(enchantName);
        }

    }

}