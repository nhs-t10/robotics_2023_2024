package com.pocolifo.robobase.control;

import java.util.function.Supplier;

public class ControllableVar {
    private final double unitsPerSecond;
    private final Supplier<Double> normalizedInput;
    private final double max;
    private final double min;
    private long lastUpdated;
    public double value;

    public ControllableVar(Supplier<Double> normalizedInput, double unitsPerSecond, double max, double min) {
        this.normalizedInput = normalizedInput;
        this.unitsPerSecond = unitsPerSecond;
        this.max = max;
        this.min = min;
    }

    public ControllableVar processUpdates() {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastUpdated;
        double secondsDifference = timeDifference / 1000d;
        double dx = secondsDifference * unitsPerSecond * normalizedInput.get();

        value += dx;

        if (value > max) {
            value = max;
        } else if (min > value) {
            value = min;
        }

        lastUpdated = currentTime;

        return this;
    }

    public double get() {
        return value;
    }
}
