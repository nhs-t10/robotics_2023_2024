package com.pocolifo.robobase.control;

import com.pocolifo.robobase.motor.CarWheels;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Utility class which implements Omni-Drive.
 */
public class GamepadCarWheels implements AutoCloseable {
    private final CarWheels wheels;
    private final Gamepad gamepad;
    public final Toggleable isMicroMovement;

    /**
     * Create a {@link GamepadCarWheels}.
     *
     * @param wheels  The {@link CarWheels} that the {@link GamepadCarWheels} should use.
     * @param gamepad The {@link Gamepad} that the {@link GamepadCarWheels} should use.
     */
    public GamepadCarWheels(CarWheels wheels, Gamepad gamepad, BoolSupplier microMovementCondition) {
        this.wheels = wheels;
        this.gamepad = gamepad;
        this.isMicroMovement = new Toggleable(microMovementCondition);
    }

    /**
     * <p>Must be called every loop iteration to update the movement of the wheels.
     * Movement is based upon gamepad input.</p>
     *
     * <p><strong>Controls</strong>
     * Left stick - movement: forward, backward, left and right without turning
     * Right tick - rotation: clockwise and counterclockwise</p>
     */
    public void update() {
        boolean useMicroMovement = this.isMicroMovement.processUpdates().get();
        float microMovementValue = useMicroMovement ? 1 : 4;

        this.wheels.driveOmni(
                this.gamepad.left_stick_y / microMovementValue,
                this.gamepad.left_stick_x / microMovementValue,
                this.gamepad.right_stick_x / microMovementValue
        );
    }

    /**
     * Cleans up this {@link GamepadCarWheels} instance. <strong>This should be called when this instance is no longer
     * in use!</strong>
     */
    public void close() {
        this.wheels.close();
    }
}
