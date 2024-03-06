package com.pocolifo.robobase;

public enum StartSide {
    APRIL_TAG_SIDE,
    BACKDROP_SIDE;


    public double getSideDistance() {
        return this == StartSide.APRIL_TAG_SIDE ? 48 : 0;
    }
    public double getSideTime() {
        return getSideDistance()/10;
    }
    public double getSideSign() {
        return this == StartSide.APRIL_TAG_SIDE ? 1 : -1;
    }
}