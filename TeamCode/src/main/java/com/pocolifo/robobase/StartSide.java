package com.pocolifo.robobase;

public enum StartSide {
    APRIL_TAG_SIDE,
    BACKDROP_SIDE;


    public int getSideSwapConstantIn() {
        return this == StartSide.APRIL_TAG_SIDE ? 48 : 0;
    }
}