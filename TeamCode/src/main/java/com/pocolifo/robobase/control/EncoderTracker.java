package com.pocolifo.robobase.control;

import com.pocolifo.robobase.motor.MovementAware;
import com.pocolifo.robobase.motor.MovementEvent;

import java.util.List;
import java.util.UUID;

/**
 * Unstable. Annotated with @Deprecated for now until it becomes stable.
 */
@Deprecated
public class EncoderTracker {
    private final MovementAware movementAware;
    private UUID startMovementId;
    private boolean startMeasuringAtBeginning;

    public EncoderTracker(MovementAware movementDevice) {
        this.movementAware = movementDevice;
    }

    public void startMeasuring() {
        List<MovementEvent> pastMovementEvents = movementAware.getPastMovementEvents();

        if (pastMovementEvents.isEmpty()) {
            this.startMeasuringAtBeginning = true;
            this.startMovementId = null;
        } else {
            this.startMeasuringAtBeginning = false;
            this.startMovementId = pastMovementEvents.get(0).uuid;
        }
    }

    public List<MovementEvent> getMovement() {
        return this.movementAware.getPastMovementEvents();
    }
}
