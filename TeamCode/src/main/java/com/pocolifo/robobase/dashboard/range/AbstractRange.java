package com.pocolifo.robobase.dashboard.range;

public abstract class AbstractRange<T extends Number> {
    protected T min;
    protected T max;

    public AbstractRange(T min, T max) {
        this.min = min;
        this.max = max;
    }

    public abstract boolean isInRange(int number);

    public T getMin() {
        return min;
    }

    public T getMax() {
        return  max;
    }
}