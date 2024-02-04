package com.pocolifo.robobase.utils;

import centerstage.SpikePosition;

public enum Alliance {
    RED,
    BLUE;

    public int getAprilTagIDForAlliance(SpikePosition targetPosition) {
        switch(targetPosition) {
            case LEFT:
                return this == Alliance.BLUE ? 1 : 4;
            case CENTER:
                return this == Alliance.BLUE ? 2 : 5;
            case RIGHT:
                return this == Alliance.BLUE ? 3 : 6;
            default:
                throw new RuntimeException("Unexpected spike position");
        }
    }

    public double getAllianceSwapConstant() {
        return this == Alliance.RED ? 1 : -1;
    }
}