package io.prodity.commons.spigot.legacy.utils;

public enum MathUtil {

    ;

    private final static double[] COS_CACHE = new double[361];
    private final static double[] SIN_CACHE = new double[361];

    static {
        for (int i = 0; i <= 360; i++) {
            final double radians = Math.toRadians(i);
            COS_CACHE[i] = Math.cos(radians);
            SIN_CACHE[i] = Math.sin(radians);
        }
    }

    public static int roundUp(double d) {
        return (d > (int) d) ? (int) d + 1 : (int) d;
    }

    public static int getAngle(double angle) {
        while (angle < 0) {
            angle += 360;
        }
        while (angle > 360) {
            angle -= 360;
        }
        return ((int) Math.round(angle)) % 360;
    }

    public static double fastCos(int angle) {
        MathUtil.verifyAngle(angle);
        return COS_CACHE[angle];
    }

    public static double fastSin(int angle) {
        MathUtil.verifyAngle(angle);
        return SIN_CACHE[angle];
    }

    private static void verifyAngle(int angle) throws IllegalArgumentException {
        if (angle < 0 || angle > 360) {
            throw new IllegalArgumentException("agnle=" + angle + " but must be 0-360 inclusive");
        }
    }

}
