package com.pocolifo.robobase.dashboard.input;

import com.google.gson.annotations.SerializedName;
import com.pocolifo.robobase.dashboard.range.IntegerRange;

public class IntegerInput extends AbstractInput<Integer> {
    @SerializedName("value")
    public int value;

    @SerializedName("min")
    public int min;

    @SerializedName("max")
    public int max;


    public IntegerInput(String name, Integer defaultValue, IntegerRange range) {
        super(name, defaultValue, range);

        if (!range.isInRange(defaultValue)) {
            throw new IllegalArgumentException("Default value must be between min and max inclusive.");
        }
    }

    @Override
    public void setValue(Integer newValue) {
        if (!super.getRange().isInRange(newValue)) {
            throw new IllegalArgumentException("New value must be between min and max inclusive.");
        }

        super.setValue(newValue);
    }

    @Override
    public void setFromString(String newValue) {
        try {
            super.setValue(Integer.parseInt(newValue));
        } catch (Exception e) {
            throw new NumberFormatException("New value must be parse-able into typeof int.");
        }
    }
}