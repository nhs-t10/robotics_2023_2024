package com.pocolifo.robobase.utils;

public class UnitUtils {
    public static double inchesToCm(double inches) {
        return inches * 2.54;
    }

    public static double inchesToMeters(double inches) {
        return inches * 0.0254;
    }

    public static double cmToInches(double cm) {
        return cm / 2.54;
    }
}
