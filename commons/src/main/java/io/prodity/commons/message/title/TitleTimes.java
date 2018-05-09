package io.prodity.commons.message.title;

import com.google.common.base.Preconditions;

public class TitleTimes {

    public static final class Builder {

        private int fadeInTicks;
        private int stayTicks;
        private int fadeOutTicks;

        public TitleTimes.Builder setFadeInTicks(int fadeInTicks) {
            this.fadeInTicks = fadeInTicks;
            return this;
        }

        public TitleTimes.Builder setStayTicks(int stayTicks) {
            this.stayTicks = stayTicks;
            return this;
        }

        public TitleTimes.Builder setFadeOutTicks(int fadeOutTicks) {
            this.fadeOutTicks = fadeOutTicks;
            return this;
        }

        private void verify() {
            Preconditions.checkNotNull(this.fadeInTicks, "fadeInTicks");
            Preconditions.checkNotNull(this.stayTicks, "stayTicks");
            Preconditions.checkNotNull(this.fadeOutTicks, "fadeOutTicks");
        }

        public TitleTimes build() {
            this.verify();
            return new TitleTimes(this.fadeInTicks, this.stayTicks, this.fadeOutTicks);
        }

    }

    public static TitleTimes.Builder builder() {
        return new TitleTimes.Builder();
    }

    private final int fadeInTicks;
    private final int stayTicks;
    private final int fadeOutTicks;

    public TitleTimes(int fadeInTicks, int stayTicks, int fadeOutTicks) {
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

}