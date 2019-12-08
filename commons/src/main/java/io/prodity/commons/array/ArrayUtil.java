package io.prodity.commons.array;

import com.google.common.reflect.TypeToken;
import java.lang.reflect.Array;

public enum ArrayUtil {

    ;

    /**
     * Creates a new array of the specified type and length
     *
     * @param type the array type
     * @param length the array length
     * @param <T> the array type
     * @return the created array
     */
    public static <T> T[] createArray(Class<T> type, int length) {
        return (T[]) Array.newInstance(type, length);
    }

    /**
     * Creates a new array of the specified type and length
     *
     * @param typeToken the {@link TypeToken} of the array type
     * @param length the array length
     * @param <T> the array type
     * @return the created array
     */
    public static <T> T[] createArray(TypeToken<T> typeToken, int length) {
        return (T[]) Array.newInstance((Class<?>) typeToken.getType(), length);
    }

}