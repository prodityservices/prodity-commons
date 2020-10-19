package io.prodity.commons.spigot.legacy.random;

import java.util.concurrent.ThreadLocalRandom;

public enum RandomUtil {

    ;

    public static boolean getRandomBoolean() {
        return RandomUtil.getRandom().nextBoolean();
    }

    public static int getRandomIntInRange(int minInclusive, int maxExclusive) {
        return RandomUtil.getRandom().nextInt(minInclusive, maxExclusive);
    }

    public static double getRandomDoubleInRange(double minInclusive, double maxExclusive) {
        return RandomUtil.getRandom().nextDouble(minInclusive, maxExclusive);
    }

    private static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

}