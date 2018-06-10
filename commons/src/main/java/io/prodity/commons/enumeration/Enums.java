package io.prodity.commons.enumeration;

import com.google.common.base.Preconditions;

public enum Enums {

    ;

    public static <T extends Enum<T>> T getFirstConstant(Class<T> enumClass) {
        Preconditions.checkNotNull(enumClass, "enumClass");
        final T[] constants = enumClass.getEnumConstants();
        Enums.verifyConstantsNotEmpty(enumClass, constants);
        return constants[0];
    }

    public static <T extends Enum<T>> T getLastConstant(Class<T> enumClass) {
        Preconditions.checkNotNull(enumClass, "enumClass");
        final T[] constants = enumClass.getEnumConstants();
        Enums.verifyConstantsNotEmpty(enumClass, constants);
        return constants[constants.length - 1];
    }

    public static <T extends Enum<T>> int getConstantIndex(T constant) {
        Preconditions.checkNotNull(constant, "constant");
        final T[] constants = constant.getDeclaringClass().getEnumConstants();
        for (int index = 0; index < constants.length; index++) {
            if (constants[index] == constant) {
                return index;
            }
        }
        throw new IllegalStateException("could not find enum constant index of constant=" + constant);
    }

    /**
     * Gets the next constant of the specified enumeration.
     *
     * @param constant the constant
     * @return the next constant, or the first constant of the enum if the specified constant is the last of the enumeration
     */
    public static <T extends Enum<T>> T getNextConstant(T constant) {
        Preconditions.checkNotNull(constant, "constant");
        final T[] constants = constant.getDeclaringClass().getEnumConstants();
        Enums.verifyConstantsNotEmpty(constant.getDeclaringClass(), constants);

        final int index = Enums.getConstantIndex(constant);
        return index == constants.length - 1 ? constants[0] : constants[index + 1];
    }

    /**
     * Gets the previous constant of the specified enumeration.
     *
     * @param constant the constant
     * @return the previous constant, or the last constant of the enum if the specified constant is the first of the enumeration
     */
    public static <T extends Enum<T>> T getPreviousConstant(T constant) {
        Preconditions.checkNotNull(constant, "constant");
        final T[] constants = constant.getDeclaringClass().getEnumConstants();
        Enums.verifyConstantsNotEmpty(constant.getDeclaringClass(), constants);

        final int index = Enums.getConstantIndex(constant);
        return index == 0 ? constants[constants.length - 1] : constants[index - 1];
    }

    private static void verifyConstantsNotEmpty(Class<? extends Enum<?>> enumClass, Object[] constants) {
        if (constants.length == 0) {
            throw new IllegalStateException("enumClass=" + enumClass.getName() + " has no constants");
        }
    }

}