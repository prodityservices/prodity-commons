package io.prodity.commons.config.annotate.inject;

public interface Configured {

    default Config getConfig() {
        return Configs.getConfig(this.getClass());
    }

}