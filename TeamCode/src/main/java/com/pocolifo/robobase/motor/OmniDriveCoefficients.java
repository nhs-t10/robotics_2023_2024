package com.pocolifo.robobase.motor;

/**
 * Omni driving coefficients are used...
 * <ul>
 *     <li>if hardware sets motors up backward</li>
 *     <li>to adjust motor power</li>
 *     <li>to correct drift and/or uneven motor power</li>
 *     <li>to allow for omni driving</li>
 * </ul>
 */
public class OmniDriveCoefficients {
    public CoefficientSet totals = new CoefficientSet(1, 1, 1, 1);
    public CoefficientSet vertical = new CoefficientSet(1, 1, 1, 1);
    public CoefficientSet horizontal = new CoefficientSet(-1, 1, 1, -1);
    public CoefficientSet rotational = new CoefficientSet(-1, 1, -1, 1);

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
