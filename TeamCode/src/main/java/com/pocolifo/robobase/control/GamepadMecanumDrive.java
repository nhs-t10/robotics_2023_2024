package com.pocolifo.robobase.control;

import com.pocolifo.robobase.movement.mecanum.MecanumDrive;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Utility class which implements Omni-Drive.
 */
public class GamepadMecanumDrive {
    private final MecanumDrive mecanumDrive;
    private final Gamepad gamepad;
    public final Toggleable isMicroMovement;

    /**
     * Create a {@link GamepadMecanumDrive}.
     *
     * @param mecanumDrive  The {@link MecanumDrive} that this {@link GamepadMecanumDrive} should use.
     * @param gamepad The {@link Gamepad} that the {@link GamepadMecanumDrive} should use.
     */
    public GamepadMecanumDrive(MecanumDrive mecanumDrive, Gamepad gamepad) {
        this.mecanumDrive = mecanumDrive;
        this.gamepad = gamepad;
        this.isMicroMovement = new Toggleable(() -> this.gamepad.x);
    }

    /**
     * <p>
     *     Must be called every loop iteration to update the movement of the wheels.
     *     Movement is based upon gamepad input.
     * </p>
     *
     * <strong>Controls</strong>
     * <ul>
     *      <li>Left stick - movement: forward, backward, left and right without turning</li>
     *      <li>Right stick - rotation: clockwise and counterclockwise</li>
     *      <li>Gamepad X: toggle micro movement</li>
     * </ul>
     */
    public void update() {
        boolean useMicroMovement = this.isMicroMovement.processUpdates().get();
        double microMovementValue = useMicroMovement ? 1 : 4;
        double dpadVerticalPower = 0;
        double dpadHorizontalPower = 0;

        if (this.gamepad.dpad_up) {
            dpadVerticalPower = 1 / microMovementValue;
        } else if (this.gamepad.dpad_down) {
            dpadVerticalPower = -1 / microMovementValue;
        }

        if (this.gamepad.dpad_right) {
            dpadHorizontalPower =  1 / microMovementValue;
        } else if (this.gamepad.dpad_left) {
            dpadHorizontalPower = -1 / microMovementValue;
        }

        if (dpadVerticalPower == 0 && dpadHorizontalPower == 0) {
            this.mecanumDrive.driveOmni(
                    this.gamepad.left_stick_y / microMovementValue,
                    this.gamepad.left_stick_x / microMovementValue,
                    this.gamepad.right_stick_x / microMovementValue
            );
        } else {
            this.mecanumDrive.driveOmni(
                    dpadVerticalPower,
                    dpadHorizontalPower,
                    0
            );
        }
    }
}
