package com.pocolifo.robobase.dashboard.input;

import com.pocolifo.robobase.dashboard.range.AbstractRange;

public abstract class AbstractInput<T extends Number> implements Input<T> {
    private String name;
    private AbstractRange<T> range;
    private T currentValue;

    public AbstractInput(String name, T defaultValue, AbstractRange<T> range) {
        this.name = name;
        this.currentValue = defaultValue;
        this.range = range;

        InputsManager.addValue(this);
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return currentValue;
    }

    public AbstractRange<T> getRange() {
        return range;
    }

    public void setValue(T newValue) {
        currentValue = newValue;
    }

    public abstract void setFromString(String newValue);
}