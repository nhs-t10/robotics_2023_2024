package com.pocolifo.robobase.motor;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class OmniDriveCoefficients {
    public final CoefficientSet totals;

    /**
     * DO NOT CHANGE!!
     */
    public final CoefficientSet vertical = new CoefficientSet(1, 1, 1, 1);

    /**
     * DO NOT CHANGE!!!
     */
    public final CoefficientSet horizontal = new CoefficientSet(-1, 1, 1, -1);

    /**
     * DO NOT CHANGE!!!
     */
    public final CoefficientSet rotational = new CoefficientSet(1, -1, 1, -1);

    /**
     * Omni driving coefficients are used...
     * <ul>
     *     <li>if hardware sets motors up backward</li>
     *     <li>to adjust motor power</li>
     *     <li>to correct drift and/or uneven motor power</li>
     *     <li>to allow for omni driving</li>
     * </ul>
     *
     * @param configuredCoefficients Coefficients applied after the vertical, horizontal, or rotational coefficients are applied.
     */
    public OmniDriveCoefficients(double[] configuredCoefficients) {
        this.totals = new CoefficientSet(configuredCoefficients);
    }

    /**
     * Calculates the motor coefficients that are then passed to the motors.
     *
     * @param horizontalPower -1 is left full speed, 1 is right full speed.
     * @param verticalPower   -1 is backward full speed, 1 is forward full speed.
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

    public Vector3D calculatePowerWithCoefficients(CoefficientSet coefficientSet) {
        double horizontalPower = 0;
        double verticalPower = 0;
        double rotationalPower = 0;

        // Assuming inverse relationships for coefficient calculation:
        horizontalPower += coefficientSet.frontLeft / totals.frontLeft / horizontal.frontLeft;
        horizontalPower += coefficientSet.frontRight / totals.frontRight / horizontal.frontRight;
        horizontalPower += coefficientSet.backLeft / totals.backLeft / horizontal.backLeft;
        horizontalPower += coefficientSet.backRight / totals.backRight / horizontal.backRight;
        horizontalPower /= 4d;

        verticalPower += coefficientSet.frontLeft / totals.frontLeft / vertical.frontLeft;
        verticalPower += coefficientSet.frontRight / totals.frontRight / vertical.frontRight;
        verticalPower += coefficientSet.backLeft / totals.backLeft / vertical.backLeft;
        verticalPower += coefficientSet.backRight / totals.backRight / vertical.backRight;
        verticalPower /= 4d;

        rotationalPower += coefficientSet.frontLeft / totals.frontLeft / rotational.frontLeft;
        rotationalPower += coefficientSet.frontRight / totals.frontRight / rotational.frontRight;
        rotationalPower += coefficientSet.backLeft / totals.backLeft / rotational.backLeft;
        rotationalPower += coefficientSet.backRight / totals.backRight / rotational.backRight;
        rotationalPower /= 4d;

        return new Vector3D(horizontalPower, verticalPower, rotationalPower);
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
