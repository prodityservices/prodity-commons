package io.prodity.commons.color;

import com.google.common.base.Preconditions;

public enum Colors {

    ;

    /**
     * Verifies that the specified RGB component is between 0 & 255 inclusive
     *
     * @param componentName the name of the component
     * @param componentValue the value of the component
     * @throws IllegalStateException if the specified component is not 0-255 inclusive
     */
    public static void verifyComponent(String componentName, int componentValue) throws IllegalStateException {
        Preconditions.checkState(componentValue >= 0 && componentValue <= 255,
            "specified RGB component " + componentName + "=" + componentValue + " but must be 0-255 inclusive");
    }

}