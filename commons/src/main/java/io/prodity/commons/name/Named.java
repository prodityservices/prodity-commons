package io.prodity.commons.name;

public interface Named {

    default String getName() {
        return Names.getName(this.getClass());
    }

}