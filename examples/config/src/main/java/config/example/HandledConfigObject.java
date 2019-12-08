package config.example;

import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.ConfigInjectionContext;
import io.prodity.commons.config.inject.deserialize.TypeElementDeserializer;
import java.util.regex.Pattern;
import ninja.leaping.configurate.ConfigurationNode;

// This will act as a type that is handled manually when being deserialized from a config.
public interface HandledConfigObject {

    final class MutableHandledConfigObject implements HandledConfigObject {

        //TypeElementDeserializer is an implementation of ElementDeserializer that deserializes based on the specified TypeToken
        static final class Deserializer extends TypeElementDeserializer<MutableHandledConfigObject> {

            public Deserializer(int priority) {
                super(TypeToken.of(MutableHandledConfigObject.class), priority);
            }

            @Override
            public MutableHandledConfigObject deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) {
                return new MutableHandledConfigObject(node.getString());
            }

        }

        private String name;
        private int amount;

        public MutableHandledConfigObject(String nameAndAmount) {
            final String[] split = nameAndAmount.split(Pattern.quote("-"));
            this.name = split[0];
            this.amount = Integer.parseInt(split[1]);
        }

        @Override
        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int getAmount() {
            return this.amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

    }

    final class ImmutableHandledConfigObject implements HandledConfigObject {

        static final class Deserializer extends TypeElementDeserializer<ImmutableHandledConfigObject> {

            public Deserializer(int priority) {
                super(TypeToken.of(ImmutableHandledConfigObject.class), priority);
            }

            @Override
            public ImmutableHandledConfigObject deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) {
                return new ImmutableHandledConfigObject(node.getString());
            }

        }

        private final String name;
        private final int amount;

        public ImmutableHandledConfigObject(String nameAndAmount) {
            final String[] split = nameAndAmount.split(Pattern.quote("-"));
            this.name = split[0];
            this.amount = Integer.parseInt(split[1]);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getAmount() {
            return this.amount;
        }

    }

    String getName();

    int getAmount();


}