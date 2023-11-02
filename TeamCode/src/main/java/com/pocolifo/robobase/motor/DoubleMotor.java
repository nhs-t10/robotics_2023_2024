package com.pocolifo.robobase.motor;

import com.pocolifo.robobase.utils.MathUtils;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Represents a synchronized two-motor assembly, such as an intake, an outtake, linear slides, etc.
 * @author arlanz
 */
public class DoubleMotor implements AutoCloseable {
    /**
     * The {@link DcMotor} associated with this motor.
     */
    public final Motor motor1;
    public final Motor motor2;
    final double coefficient1;
    final double coefficient2;
    final int signCoefficient1;
    final int signCoefficient2;


    /**
     * Instantiate a {@link DoubleMotor}.
     * Also uses an encoder.
     *
     * @param motor1     The first {@link Motor} that is associated with this {@link DoubleMotor}.
     * @param motor2     The second {@link Motor} that is associated with this {@link DoubleMotor}.
     *                   MUST BE THE SAME KIND OF MOTOR!
     * @param motor1Coefficient Coefficient of power for motor1
     * @param motor2Coefficient Coefficient of power for motor2
     */
    public DoubleMotor(Motor motor1, Motor motor2, double motor1Coefficient, double motor2Coefficient) {
        this.motor1 = motor1;
        this.motor2 = motor2;
        this.coefficient1 = motor1Coefficient;
        this.coefficient2 = motor2Coefficient;
        this.signCoefficient1 = MathUtils.getSign(motor1Coefficient);
        this.signCoefficient2 = MathUtils.getSign(motor2Coefficient);
    }

    /**
     * Starts moving the motor at a given speed.
     *
     * @param speed Determines the speed at which the motor should move.
     * @author arlanz
     */
    public void spin(double speed) {
        motor1.drive(speed*coefficient1);
        motor2.drive(speed*coefficient2);
    }


    /**
     * Stop the movement of the motor.
     *
     * @author arlanz
     */
    public void stopMoving() {
        motor1.stopMoving();
        motor2.stopMoving();
    }

    /**
     * Gets the current encoder position
     * @author arlanz
     */
    public int getPosition() { return (motor1.getPosition()*signCoefficient1 + motor2.getPosition()*signCoefficient2)/2; }

    /**
     * gets the number of motor rotations
     */
    public double getMotorRotations(){ return getPosition() / motor1.tickCount;}

    /**
     * Closes the internal {@link Motor} devices. <strong>THIS SHOULD BE CALLED WHEN MOTORS ARE DONE BEING
     * USED!</strong>
     *
     * @author arlanz
     */
    @Override
    public void close() {
        motor1.close();
        motor2.close();
    }
}
