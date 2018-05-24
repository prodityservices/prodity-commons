package config.example;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.color.Color;
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

//TODO comment this class.
@Named(ColorRepository.NAME)
@Config(fileName = "config.yml")
@Singleton
public class ColorRepository implements Repository<String, Color> {

    public static final String NAME = "example-color-repository";

    private static final TypeToken<String> KEY_TYPE = TypeToken.of(String.class);
    private static final TypeToken<Color> VALUE_TYPE = TypeToken.of(Color.class);

    @ConfigPath("colors")
    private Map<String, Color> colors;

    @Override
    public TypeToken<String> getKeyType() {
        return ColorRepository.KEY_TYPE;
    }

    @Override
    public TypeToken<Color> getValueType() {
        return ColorRepository.VALUE_TYPE;
    }

    @Override
    public boolean containsId(@Nullable String key) {
        return key != null && this.colors.containsKey(key);
    }

    @Nullable
    @Override
    public Color get(@Nullable String key) {
        return key == null ? null : this.colors.get(key);
    }

    @Override
    public Optional<Color> getSafely(@Nullable String key) {
        return key == null ? Optional.empty() : Optional.ofNullable(this.colors.get(key));
    }

    @Override
    public Collection<Color> getAsCollection(Iterable<String> keys) {
        Preconditions.checkNotNull(keys, "keys");
        final List<Color> colors = Lists.newArrayList();
        for (String key : keys) {
            if (this.colors.containsKey(key)) {
                colors.add(this.colors.get(key));
            }
        }

        return colors;
    }

    @Override
    public Map<String, Color> getAsMap(Iterable<String> keys) {
        final Map<String, Color> colors = Maps.newHashMap();
        for (String key : keys) {
            if (this.colors.containsKey(key)) {
                colors.put(key, this.colors.get(key));
            }
        }

        return colors;
    }

}