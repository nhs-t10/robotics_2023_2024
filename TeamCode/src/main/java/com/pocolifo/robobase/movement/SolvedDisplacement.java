package com.pocolifo.robobase.movement;

import com.pocolifo.robobase.motor.CarWheels;

class SolvedDisplacement extends Displacement {
    public final int startAtTick;
    public final int durationTicks;
    public final int endAtTick;

    public SolvedDisplacement(Displacement displacement, int startAtTick, int durationTicks) {
        super(displacement.xCm, displacement.yCm);
        this.startAtTick = startAtTick;
        this.durationTicks = durationTicks;
        this.endAtTick = startAtTick + durationTicks;
    }
}
