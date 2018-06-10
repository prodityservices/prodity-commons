package io.prodity.commons.enumeration;

import com.google.common.base.Preconditions;

public enum Enums {

    ;

    public static <T extends Enum<T>> T getFirstConstant(Class<T> enumClass) {
        Preconditions.checkNotNull(enumClass, "enumClass");
        final T[] constants = enumClass.getEnumConstants();
        if (constants.length == 0) {
            throw new IllegalStateException("enumClass=" + enumClass.getName() + " has no constants");
        }
        return constants[0];
    }

    public static <T extends Enum<T>> T getLastConstant(Class<T> enumClass) {
        Preconditions.checkNotNull(enumClass, "enumClass");
        final T[] constants = enumClass.getEnumConstants();
        if (constants.length == 0) {
            throw new IllegalStateException("enumClass=" + enumClass.getName() + " has no constants");
        }
        return constants[constants.length - 1];
    }

}