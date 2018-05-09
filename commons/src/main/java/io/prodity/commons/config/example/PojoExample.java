package io.prodity.commons.config.example;

import io.prodity.commons.config.annotate.inject.ConfigPath;
import io.prodity.commons.config.annotate.modify.Colorize;
import io.prodity.commons.identity.Identifiable;
import java.awt.Color;

public class PojoExample implements Identifiable<String> {

    private String id;

    @Colorize
    @ConfigPath("name") //Specify a different path than field name when this object is being loaded via config
    private String displayName;

    private Color color;

    @Override
    public String getId() {
        return this.id;
    }

    public Color getColor() {
        return this.color;
    }

}