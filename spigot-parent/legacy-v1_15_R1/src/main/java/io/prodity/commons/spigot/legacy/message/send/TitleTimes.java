package io.prodity.commons.spigot.legacy.message.send;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class TitleTimes {

    public static TitleTimes of(int show, int stay, int fade) {
        return new TitleTimes(show, stay, fade);
    }

    public static TitleTimes of() {
        return new TitleTimes(0, 20, 0);
    }

    @Setter
    @Getter
    @Accessors(fluent = true, chain = true)
    private int show;

    @Setter
    @Getter
    @Accessors(fluent = true, chain = true)
    private int stay;

    @Setter
    @Getter
    @Accessors(fluent = true, chain = true)
    private int fade;

    private TitleTimes(int show, int stay, int fade) {
        this.show = show;
        this.stay = stay;
        this.fade = fade;
    }

}