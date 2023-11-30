package com.pocolifo.robobase.dashboard.input;

import com.pocolifo.robobase.dashboard.range.AbstractRange;

public interface Input<T extends Number> {
    String getName();
    T getValue();
    void setValue(T newValue);
    void setFromString(String value);
    AbstractRange<T> getRange();
}
