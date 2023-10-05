package com.pocolifo.robobase.motor;

public enum MovementType {
    MOVE_FORWARD(1),
    MOVE_LEFT(-1),
    MOVE_RIGHT(1),
    MOVE_BACKWARD(-1),
    ROTATE_CW(1),
    ROTATE_CCW(-1),
    STOP(0);

    private final int coefficient;

    MovementType(int coefficient) {
        this.coefficient = coefficient;
    }
}
