package com.pocolifo.robobase;

import com.pocolifo.robobase.motor.CarWheels;

/**
 * Data and other information about the robot. This should be kept as accurate and up-to-date as possible!
 *
 * @author youngermax
 */
public class Robot {
    /**
     * <p>The width of the robot in real life, in centimeters.</p>
     *
     * <p>In this case, the width should be calculated by measuring the distance from the outside of  the left set of
     * wheels to the outside of the right set of wheels, assuming this {@link Robot} is using {@link CarWheels}.</p>
     */
    public final double widthCm;

    /**
     * <p>The length of the robot in real life, in centimeters.</p>
     *
     * <p>This should be calculated by measuring the other side that isn't the height or the side which the width
     * was measured.</p>
     */
    public final double lengthCm;

    /**
     * The height of the robot in real life, in centimeters.
     */
    public final double heightCm;

    /**
     * The number of the team who built the robot.
     */
    public final int teamNumber;

    /**
     * The name of the robot.
     */
    public final String robotName;

    /**
     * If the robot has the "Warning: Robot Moves on Initialization" sticker on it.
     */
    public final boolean hasWarningSticker;

    // TODO: Add more metrics about the robot in real life.

    /**
     * Determine if the robot meets dimension requirements.
     *
     * @return True if the robot meets the dimension requirements, false if it doesn't.
     * @author youngermax
     */
    public boolean doesPassDimensionInspection() {
        // 45.72 is equivalent to 18 inches, the requirements for a robot
        return 45.72 >= widthCm && 45.72 >= lengthCm && 45.72 >= heightCm;
    }

    /**
     * Used to set motor coefficients to forward/backward, if hardware sets them up backward
     * Also used to adjust motor power, to correct drift/uneven motor power
     * DONT DELETE! (unless you know what you're doing) All driveOmni functions run through here, eventually. You've been warned.
     * Should be changed every
     * @author arlanz
     */
    public final double[][] omniDriveCoefficients =
            {
            //total:
                    {1, 1, 1, 1},
            //vertical
                    {1, 1, 1, 1},
            //horizontal
                    {1, 1, 1, 1},
            //rotational
                    {1, 1, 1, 1}
            };


    public Robot(double widthCm, double lengthCm, double heightCm, int teamNumber, String robotName, boolean hasWarningSticker) {
        this.widthCm = widthCm;
        this.lengthCm = lengthCm;
        this.heightCm = heightCm;
        this.teamNumber = teamNumber;
        this.robotName = robotName;
        this.hasWarningSticker = hasWarningSticker;
    }
}
