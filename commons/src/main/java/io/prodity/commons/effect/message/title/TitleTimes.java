package io.prodity.commons.effect.message.title;

import com.google.common.base.MoreObjects;
import io.prodity.commons.config.annotate.deserialize.ConfigDefault;
import io.prodity.commons.config.annotate.deserialize.ConfigDeserializable;
import io.prodity.commons.config.annotate.inject.ConfigInject;
import io.prodity.commons.config.annotate.inject.ConfigPath;
import java.util.Objects;
import java.util.function.Supplier;

@ConfigDeserializable
public class TitleTimes {

    private static final class DefaultValue implements Supplier<Integer> {

        @Override
        public Integer get() {
            return 0;
        }

    }

    private final int fadeInTicks;
    private final int stayTicks;
    private final int fadeOutTicks;

    @ConfigInject
    public TitleTimes(
        @ConfigPath("fade-in")
        @ConfigDefault(DefaultValue.class)
            int fadeInTicks,
        @ConfigDefault(DefaultValue.class)
        @ConfigPath("stay")
            int stayTicks,
        @ConfigDefault(DefaultValue.class)
        @ConfigPath("fade-out")
            int fadeOutTicks
    ) {
        this.fadeInTicks = fadeInTicks;
        this.stayTicks = stayTicks;
        this.fadeOutTicks = fadeOutTicks;
    }

    public int getFadeInTicks() {
        return this.fadeInTicks;
    }

    public int getStayTicks() {
        return this.stayTicks;
    }

    public int getFadeOutTicks() {
        return this.fadeOutTicks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fadeInTicks, this.stayTicks, this.fadeOutTicks);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TitleTimes)) {
            return false;
        }
        final TitleTimes that = (TitleTimes) object;
        return Objects.equals(this.fadeInTicks, that.fadeInTicks) &&
            Objects.equals(this.stayTicks, that.stayTicks) &&
            Objects.equals(this.fadeOutTicks, that.fadeOutTicks);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("fadeInTicks", this.fadeInTicks)
            .add("stayTicks", this.stayTicks)
            .add("fadeOutTicks", this.fadeOutTicks)
            .toString();
    }

}