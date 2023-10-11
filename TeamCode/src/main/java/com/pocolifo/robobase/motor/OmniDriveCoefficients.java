package com.pocolifo.robobase.motor;

public class OmniDriveCoefficients {
    public final CoefficientSet totals;
    public final CoefficientSet vertical;
    public final CoefficientSet horizontal;
    public final CoefficientSet rotational;

    /**
     * Omni driving coefficients are used...
     * <ul>
     *     <li>if hardware sets motors up backward</li>
     *     <li>to adjust motor power</li>
     *     <li>to correct drift and/or uneven motor power</li>
     *     <li>to allow for omni driving</li>
     * </ul>
     *
     * @param totals     Coefficients applied after the vertical, horizontal, or rotational coefficients are applied.
     * @param vertical   Coefficients for forward and backward movement.
     * @param horizontal Coefficients for left and right movement.
     * @param rotational Coefficients for rotating the robot.
     */
    public OmniDriveCoefficients(double[] totals, double[] vertical, double[] horizontal, double[] rotational) {
        this.totals = new CoefficientSet(totals);
        this.vertical = new CoefficientSet(vertical);
        this.horizontal = new CoefficientSet(horizontal);
        this.rotational = new CoefficientSet(rotational);
    }

    /**
     * Calculates the motor coefficients that are then passed to the motors.
     *
     * @param verticalPower   -1 is backward full speed, 1 is forward full speed.
     * @param horizontalPower -1 is left full speed, 1 is right full speed.
     * @param rotationalPower -1 is counterclockwise full speed, 1 is clockwise full speed.
     * @return The coefficients that have been multiplied by the motor powers.
     */
    public CoefficientSet calculateCoefficientsWithPower(double verticalPower, double horizontalPower, double rotationalPower) {
        // Notice how this is just matrix multiplication.
        return new CoefficientSet(
                totals.frontLeft * (verticalPower * vertical.frontLeft + horizontalPower * horizontal.frontLeft + rotationalPower * rotational.frontLeft),
                totals.frontRight * (verticalPower * vertical.frontRight + horizontalPower * horizontal.frontRight + rotationalPower * rotational.frontRight),
                totals.backLeft * (verticalPower * vertical.backLeft + horizontalPower * horizontal.backLeft + rotationalPower * rotational.backLeft),
                totals.backRight * (verticalPower * vertical.backRight + horizontalPower * horizontal.backRight + rotationalPower * rotational.backRight)
        );
    }

    public static class CoefficientSet {
        /**
         * Coefficient for the front left motor.
         */
        public final double frontLeft;

        /**
         * Coefficient for the front right motor.
         */
        public final double frontRight;

        /**
         * Coefficient for the back left motor.
         */
        public final double backLeft;

        /**
         * Coefficient for the back right motor.
         */
        public final double backRight;

        /**
         * Coefficients for each respective motor.
         * Allowed value domain: [-1, 1]
         *
         * @param frontLeft  Coefficient for front left motor.
         * @param frontRight Coefficient for front right motor.
         * @param backLeft   Coefficient for back left motor.
         * @param backRight  Coefficient for back right  motor.
         */
        public CoefficientSet(double frontLeft, double frontRight, double backLeft, double backRight) {
            this.frontLeft = frontLeft;
            this.frontRight = frontRight;
            this.backLeft = backLeft;
            this.backRight = backRight;
        }

        /**
         * Coefficients for each respective motor.
         *
         * @param coefficients Coefficients in order of motors: front left, front right, back left, and back right.
         */
        public CoefficientSet(double[] coefficients) {
            this(coefficients[0], coefficients[1], coefficients[2], coefficients[3]);
        }
    }
}
