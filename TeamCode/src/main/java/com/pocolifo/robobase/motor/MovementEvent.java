package com.pocolifo.robobase.motor;

import java.util.UUID;

public class MovementEvent {
    public final MovementDirection direction;
    public final double amount;
    public final UUID uuid;

    public MovementEvent(MovementDirection direction, double centimeters) {
        this.direction = direction;
        this.amount = centimeters;
        this.uuid = UUID.randomUUID();
    }
}
