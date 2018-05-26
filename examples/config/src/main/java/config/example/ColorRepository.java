package config.example;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.annotate.inject.ConfigPath;
import io.prodity.commons.repository.Repository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Singleton;

// A Repository is simple a basic Object manager.
// If the objects are configured, the Repository can typically be annotated with @Config to have the values loaded in.
@Named(ColorRepository.NAME)
@Config(fileName = "config.yml")
@Singleton
public class ColorRepository implements Repository<String, IdentifiableColor> {

    public static final String NAME = "example-color-repository";

    private static final TypeToken<String> KEY_TYPE = TypeToken.of(String.class);
    private static final TypeToken<IdentifiableColor> VALUE_TYPE = TypeToken.of(IdentifiableColor.class);

    @ConfigPath("colors")
    private Map<String, IdentifiableColor> colors;

    @Override
    public TypeToken<String> getKeyType() {
        return ColorRepository.KEY_TYPE;
    }

    @Override
    public TypeToken<IdentifiableColor> getValueType() {
        return ColorRepository.VALUE_TYPE;
    }

    @Override
    public boolean containsId(@Nullable String key) {
        return key != null && this.colors.containsKey(key);
    }

    @Nullable
    @Override
    public IdentifiableColor get(@Nullable String key) {
        return key == null ? null : this.colors.get(key);
    }

    @Override
    public Optional<IdentifiableColor> getSafely(@Nullable String key) {
        return key == null ? Optional.empty() : Optional.ofNullable(this.colors.get(key));
    }

    @Override
    public Collection<IdentifiableColor> getAsCollection(Iterable<String> keys) {
        Preconditions.checkNotNull(keys, "keys");
        final List<IdentifiableColor> colors = Lists.newArrayList();
        for (String key : keys) {
            if (this.colors.containsKey(key)) {
                colors.add(this.colors.get(key));
            }
        }

        return colors;
    }

    @Override
    public Map<String, IdentifiableColor> getAsMap(Iterable<String> keys) {
        final Map<String, IdentifiableColor> colors = Maps.newHashMap();
        for (String key : keys) {
            if (this.colors.containsKey(key)) {
                colors.put(key, this.colors.get(key));
            }
        }

        return colors;
    }

}