package com.pocolifo.robobase;

/**
 * Robot sizing data.
 *
 * @author youngermax
 */
public class RobotSize {
    /**
     * The width of the robot in real life, in centimeters.
     * Back left wheel to back right wheel.
     */
    public final double widthCm;

    /**
     * The length of the robot in real life, in centimeters.
     * Back left wheel to front left wheel.
     */
    public final double lengthCm;

    /**
     * The height of the robot in real life, in centimeters.
     */
    public final double heightCm;

    public RobotSize(double widthCm, double lengthCm, double heightCm) {
        this.widthCm = widthCm;
        this.lengthCm = lengthCm;
        this.heightCm = heightCm;
    }
}
