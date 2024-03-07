package com.pocolifo.robobase.motor;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Represents a synchronized two-motor assembly, such as an intake, an outtake, linear slides, etc.
 * @author arlanz
 */
public class DoubleCRServo implements AutoCloseable {
    /**
     * The {@link DcMotor}s associated with this motor.
     */
    public final CRServo servo1;
    public final CRServo servo2;
    /**
     * The power coefficients of the motors
     * */
    final double speed;
    /**
     * The signs of the motor coefficients
     */


    /**
     * Instantiate a {@link DoubleCRServo}.
     * Also uses an encoder.
     *
     * @param servo1     The first {@link CRServo} that is associated with this {@link DoubleCRServo}.
     * @param servo2     The second {@link CRServo} that is associated with this {@link DoubleCRServo}.
     *                   MUST BE THE SAME KIND OF MOTOR!
     * @param servoPowerCoefficient Coefficient of power for servos
     */
    public DoubleCRServo(CRServo servo1, CRServo servo2, double servoPowerCoefficient) {
        this.servo1 = servo1;
        this.servo2 = servo2;
        this.speed = servoPowerCoefficient;
    }

    /**
     * Starts moving the motor at a given speed.
     *
     * @param direction Determines the direction the motor should rotate.
     * @author arlanz
     */
    public void spin(DcMotorSimple.Direction direction) {
        servo1.setDirection(direction);
        servo2.setDirection(direction.inverted());
        servo1.setPower(speed);
        servo2.setPower(speed);
    }


    /**
     * Stop the movement of the motor.
     *
     * @author arlanz
     */
    public void stopMoving() {
        servo1.setPower(0);
        servo2.setPower(0);
    }

    /**
     * Closes the internal {@link CRServo} devices. <strong>THIS SHOULD BE CALLED WHEN MOTORS ARE DONE BEING
     * USED!</strong>
     *
     * @author arlanz
     */
    @Override
    public void close() {
        servo1.close();
        servo2.close();
    }
}
