package com.pocolifo.robobase.dashboard.range;

public class IntegerRange extends AbstractRange<Integer> {
    public IntegerRange(int min, int max) {
        super(min, max);

        if (min > max) {
            throw new IllegalArgumentException("Min cannot be greater than max.");
        }
    }

    @Override
    public boolean isInRange(int number) {
        return number >= super.min && number <= super.max;
    }
}
