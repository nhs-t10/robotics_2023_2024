package com.pocolifo.robobase.motor;

import android.os.SystemClock;
import com.pocolifo.robobase.Robot;
import com.pocolifo.robobase.movement.Displacement;
import com.pocolifo.robobase.movement.DisplacementSequence;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains functions to move the robot very intuitively. Intended for use in Autonomous. The wheels must be organized
 * just like a car; this means all wheels are parallel, two on each side of the robot.
 *
 * @author youngermax
 * @see Wheel
 */
public class CarWheels implements AutoCloseable, MovementAware {
    /**
     * The {@link Robot} that is associated with these {@link CarWheels}.
     */
    public final Robot robot;

    /**
     * The <strong>front left</strong> {@link Wheel}, if looking from the back of the robot and in the same direction
     * of the robot.
     */
    public final Wheel frontLeft;

    /**
     * The <strong>front right</strong> {@link Wheel}, if looking from the back of the robot and in the same direction
     * of the robot.
     */
    public final Wheel frontRight;

    /**
     * The <strong>back left</strong> {@link Wheel}, if looking from the back of the robot and in the same direction of
     * the robot.
     */
    public final Wheel backLeft;

    /**
     * The <strong>back right</strong> {@link Wheel}, if looking from the back of the robot and in the same direction of
     * the robot.
     */
    public final Wheel backRight;

    /**
     * The distance (in centimeters) to move the wheels to rotate one degree. This value is calculated upon
     * instantiation of {@link CarWheels}, and it is not recalculated after that.
     *
     * @see CarWheels#rotate(double, double)
     */
    private final double oneDegreeRotDistCm;

    /**
     * The {@link Wheel} used for encoder-related calculations.
     */
    @Deprecated
    public final Wheel specialWheel;

    /**
     * Tracks movement events over time.
     *
     * @see MovementAware
     * @see MovementEvent
     */
    private final List<MovementEvent> pastMovementEvents;


    /**
     * Instantiate {@link CarWheels}. The {@code specialWheel} is used for encoder-related calculations.
     *
     * @param robot        The associated {@link Robot} that has these {@link CarWheels}.
     * @param frontLeft    The {@link Wheel} associated with the front left motor, if looking from the back of the robot
     *                     and in the same direction of the robot.
     * @param frontRight   The {@link Wheel} associated with the front right motor, if looking from the back of the robot
     *                     and in the same direction of the robot.
     * @param backLeft     The {@link Wheel} associated with the back left motor, if looking from the back of the robot
     *                     and in the same direction of the robot.
     * @param backRight    The {@link Wheel} associated with the back right motor, if looking from the back of the robot
     *                     and in the same direction of the robot.
     * @param specialWheel The wheel that is used for encoder-related calculations.
     * @author youngermax
     */
    public CarWheels(Robot robot, Wheel frontLeft, Wheel frontRight, Wheel backLeft, Wheel backRight,
                     Wheel specialWheel) {
        this.robot = robot;
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.specialWheel = specialWheel;

        this.oneDegreeRotDistCm = (Math.PI * Math.sqrt(Math.pow(this.robot.lengthCm, 2)
                + Math.pow(this.robot.widthCm, 2))) / 360;
        this.pastMovementEvents = new LinkedList<>();
    }

    /**
     * Quickly instantiate {@link CarWheels}.
     *
     * @param hardwareMap     The {@link HardwareMap} which contains the motors.
     * @param motorTickCount  The number of encoder ticks for a revolution <strong>for each motor</strong>.
     * @param wheelDiameterCm The diameter of <strong>each wheel</strong> in centimeters.
     * @param robot           The {@link Robot} that is associated with the {@code hardwareMap}.
     * @param frontLeft       The name of the front left motor, if looking from the back of the robot and in the same
     *                        direction of the robot.
     * @param frontRight      The name of the front right motor, if looking from the back of the robot and in the same
     *                        direction of the robot.
     * @param backLeft        The name of the back left motor, if looking from the back of the robot and in the same direction
     *                        of the robot.
     * @param backRight       The name of the back right motor, if looking from the back of the robot and in the same
     *                        direction of the robot.
     * @param specialWheel    The wheel that <strong>has the encoder connector connected to it and the Control Hub.
     *                        The connector is mandatory because it's how we know how far the robot has traveled!</strong>
     * @author youngermax
     * @see CarWheels#CarWheels(Robot, Wheel, Wheel, Wheel, Wheel, Wheel)
     */
    public CarWheels(HardwareMap hardwareMap, double motorTickCount, double wheelDiameterCm, Robot robot, String frontLeft,
                     String frontRight, String backLeft, String backRight, String specialWheel) {
        this(
                robot,
                new Wheel(hardwareMap.dcMotor.get(frontLeft), motorTickCount, wheelDiameterCm),
                new Wheel(hardwareMap.dcMotor.get(frontRight), motorTickCount, wheelDiameterCm),
                new Wheel(hardwareMap.dcMotor.get(backLeft), motorTickCount, wheelDiameterCm),
                new Wheel(hardwareMap.dcMotor.get(backRight), motorTickCount, wheelDiameterCm),
                new Wheel(hardwareMap.dcMotor.get(specialWheel), motorTickCount, wheelDiameterCm)
        );
    }

    /**
     * Drive the motors forward or backward.
     *
     * @param centimeters The number of centimeters to move. This should be negative to move backwards, and positive to
     *                    move forwards. If {@code power} is negative, this should be too.
     * @param horizontal  Whether the robot moves vertically or horizontally.
     * @author youngermax
     */
    public void drive(double centimeters, boolean horizontal) {
        this.drive(centimeters, 0.25, horizontal);
    }

    /**
     * Drive the motors forward or backward at a certain speed.
     *
     * @param centimeters The number of centimeters to move. This should be negative to move backwards, and positive to
     *                    move forwards. If {@code power} is negative, this should be too.
     * @param speed       The speed at which the robot should move. Should be [0, 1].
     * @param horizontal  Whether the robot moves vertically or horizontally.
     * @author youngermax
     */
    public void drive(double centimeters, double speed, boolean horizontal) {
        centimeters *= -1;
        this.setDriveTarget(centimeters);

        MovementDirection direction;

        if (centimeters >= 0 && !horizontal) {
            this.driveIndividually(speed, -speed, speed, -speed);
            direction = MovementDirection.MOVE_FORWARD;
        } else if (!horizontal) {
            this.driveIndividually(-speed, speed, -speed, speed);
            direction = MovementDirection.MOVE_BACKWARD;
        } else if (centimeters <= 0) {
            this.driveIndividually(-speed, -speed, speed, speed);
            direction = MovementDirection.MOVE_LEFT;
        } else {
            this.driveIndividually(speed, speed, -speed, -speed);
            direction = MovementDirection.MOVE_RIGHT;
        }

        this.waitForWheelsThenStop();

        this.pastMovementEvents.add(new MovementEvent(direction, Math.abs(centimeters)));
    }

    /**
     * Sets the motor speed of each wheel individually. Setting the motor speed actually moves it in real life.
     *
     * @param frontLeft  The speed to set the front left wheel to.
     * @param frontRight The speed to set the front right wheel to.
     * @param backLeft   The speed to set the back left wheel to.
     * @param backRight  The speed to set the back right wheel to.
     * @author youngermax
     */
    public void driveIndividually(double frontLeft, double frontRight, double backLeft, double backRight) {
        this.frontLeft.drive(frontLeft);
        this.frontRight.drive(frontRight);
        this.backLeft.drive(backLeft);
        this.backRight.drive(backRight);
    }

    /**
     * Sets the motor target of the special wheel. Use this to specify how far the car should go.
     *
     * @param centimeters The target to set the special wheel to, in centimeters.
     * @author youngermax
     */
    public void setDriveTarget(double centimeters) {
        this.specialWheel.setDriveTarget(centimeters);
    }

    /**
     * Rotates the robot.
     *
     * @param degrees Degrees to rotate. Positive for right (clockwise). Negative for left (counterclockwise).
     * @param power   The power to drive the motors at. Valid values are from {@code -1.0} to {@code 1.0}, inclusive.
     *                This should be negative to rotate counterclockwise, and positive to rotate clockwise.
     * @author youngermax
     */
    public void rotate(double degrees, double power) {
        double rotDistCm = this.oneDegreeRotDistCm * degrees;

        this.setDriveTarget(rotDistCm);
        this.driveIndividually(power, -power, power, -power);
        this.waitForWheelsThenStop();

        if (degrees > 0) {
            this.pastMovementEvents.add(new MovementEvent(MovementDirection.ROTATE_CW, degrees));
        } else if (0 > degrees) {
            this.pastMovementEvents.add(new MovementEvent(MovementDirection.ROTATE_CCW, degrees));
        }
    }

    /**
     * Rotates the robot clockwise. This is just for readability purposes.
     *
     * @param degrees Degrees to rotate clockwise.
     * @param power   The power to drive the motors at. Valid values are from {@code -1.0} to {@code 1.0}, inclusive.
     *                This should be negative to rotate counterclockwise, and positive to rotate clockwise.
     * @author youngermax
     * @see CarWheels#rotate(double, double)
     */
    public void rotateClockwise(double degrees, double power) {
        this.rotate(Math.abs(degrees), power);
    }

    /**
     * Rotates the robot counterclockwise. This is just for readability purposes.
     *
     * @param degrees Degrees to rotate counterclockwise.
     * @param power   The power to drive the motors at. Valid values are from {@code -1.0} to {@code 1.0}, inclusive.
     *                This should be negative to rotate counterclockwise, and positive to rotate clockwise.
     * @author youngermax
     * @see CarWheels#rotate(double, double)
     */
    public void rotateCounterclockwise(double degrees, double power) {
        this.rotate(-Math.abs(degrees), power);
    }

    /**
     * Waits for all the wheels to meet the drive target, then stops them.
     *
     * @author youngermax
     * @see Wheel#setDriveTarget(double)
     */
    private void waitForWheelsThenStop() {
        // Wait for the SPECIAL WHEEL to stop moving
        this.specialWheel.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.specialWheel.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (this.specialWheel.targetPosition - this.specialWheel.motor.getCurrentPosition() >= 0) {
            while (this.specialWheel.targetPosition > this.specialWheel.motor.getCurrentPosition()) {
            }
        } else {
            while (this.specialWheel.targetPosition < this.specialWheel.motor.getCurrentPosition()) {
            }
        }

        // Stop driving
        this.frontLeft.stopMoving();
        this.frontRight.stopMoving();
        this.backLeft.stopMoving();
        this.backRight.stopMoving();
    }

    /**
     * Drive the robot with omni-drive. Omni-drive allows the robot to move horizontally without rotating, drive
     * diagonally, and drive and rotate the robot normally.
     *
     * @param verticalPower   The power at which to move the robot forward and backward. Inclusive from -1.0 to 1.0.
     * @param horizontalPower The power at which to move the robot left and right. Inclusive from -1.0 to 1.0.
     * @param rotationalPower The power at which to rotate the robot. Inclusive from -1.0 (rotate counterclockwise) to 1.0
     *                        (clockwise).
     */
    public void driveOmni(double verticalPower, double horizontalPower, double rotationalPower) {
        // Drive the wheels to match the controller input
        OmniDriveCoefficients.CoefficientSet vals = this.robot.omniDriveCoefficients.calculateCoefficientsWithPower(
                verticalPower,
                horizontalPower,
                rotationalPower
        );

        this.driveIndividually(
                vals.frontLeft,
                vals.frontRight,
                vals.backLeft,
                vals.backRight
        );
    }

    /**
     * Drives the robot using omni-wheel control based on the provided power values for each direction.
     *
     * @param power An array of power values representing the desired speed and direction for each wheel.
     *              The array should have three elements: [powerX, powerY, powerRotation].
     *              - powerX: The lateral movement power.
     *              - powerY: The horizontal movement power.
     *              - powerRotation: The rotational movement power.
     */
    public void driveOmni(double[] power) {
        // Drive the wheels to match the controller input
        OmniDriveCoefficients.CoefficientSet vals = this.robot.omniDriveCoefficients.calculateCoefficientsWithPower(
                power[0],
                power[1],
                power[2]
        );

        this.driveIndividually(
                vals.frontLeft,
                vals.frontRight,
                vals.backLeft,
                vals.backRight
        );
    }

    /**
     * Closes the internal motors.
     *
     * @author youngermax
     */
    @Override
    public void close() {
        this.frontLeft.close();
        this.frontRight.close();
        this.backLeft.close();
        this.backRight.close();
    }

    @Override
    public List<MovementEvent> getPastMovementEvents() {
        return this.pastMovementEvents;
    }

    /**
     * Executes a sequence of displacements, driving the robot to each target position with a specified power.
     *
     * @param sequence The sequence of displacements to follow, containing the target positions in both lateral (yCm) and
     *                 horizontal (xCm) directions.
     * @param power    The power at which the robot should move during each displacement, ranging from -1.0 (full reverse)
     *                 to 1.0 (full forward). A power of 0.0 indicates no motion.
     */
    public void follow(DisplacementSequence sequence, double power) {

        for (int i = 0; i < sequence.displacements.size(); i++) {
            Displacement displacement = sequence.displacements.get(i);

            System.out.printf("Displace %d: (%f, %f)%n", i, displacement.xCm, displacement.yCm);

            drive(displacement.yCm, displacement.xCm, power);

            while (!isCloseToTargetPosition(10)) {
                SystemClock.sleep(10);
            }
            this.frontLeft.stopMoving();
            this.frontRight.stopMoving();
            this.backLeft.stopMoving();
            this.backRight.stopMoving();
            SystemClock.sleep(500);
        }
    }

    /**
     * Checks whether each wheel motor is close to its target position within a specified threshold.
     *
     * @param tickThreshold The acceptable difference (in ticks) between the current position and the target position
     *                      for each wheel motor to be considered close to the target.
     * @return {@code true} if all wheel motors are close to their target positions within the specified threshold;
     *         {@code false} otherwise.
     */
    private boolean isCloseToTargetPosition(int tickThreshold) {
        // Is each wheel in the target position threshold?

        boolean fl = Math.abs(this.frontLeft.motor.getTargetPosition() - this.frontLeft.motor.getCurrentPosition()) < Math.abs(tickThreshold);
        if (!fl) return false;

        boolean bl = Math.abs(this.backLeft.motor.getTargetPosition() - this.backLeft.motor.getCurrentPosition()) < Math.abs(tickThreshold);
        if (!bl) return false;

        boolean fr = Math.abs(this.frontRight.motor.getTargetPosition() - this.frontRight.motor.getCurrentPosition()) < Math.abs(tickThreshold);
        if (!fr) return false;

        boolean br = Math.abs(this.backRight.motor.getTargetPosition() - this.backRight.motor.getCurrentPosition()) < Math.abs(tickThreshold);
        if (!br) return false;

        return true;
    }

    /**
     * Drives the robot in a specified lateral and horizontal direction at a given speed using omni-wheel drive.
     *
     * @param lateralCm      The lateral distance to drive in centimeters.
     * @param horizontalCm   The horizontal distance to drive in centimeters.
     * @param speed          The speed at which the robot should move, ranging from -1.0 (full reverse) to 1.0 (full forward).
     *                       A speed of 0.0 indicates no motion.
     */
    public void drive(double lateralCm, double horizontalCm, double speed) {
        double distanceCm = Math.hypot(lateralCm, horizontalCm);
        double powerX = (horizontalCm / distanceCm) * speed; // cos
        double powerY = (lateralCm / distanceCm) * speed; // sin

        OmniDriveCoefficients.CoefficientSet coefficientSet = this.robot.omniDriveCoefficients.calculateCoefficientsWithPower(powerY, powerX, 0);

        this.frontLeft.setDriveTarget(distanceCm * Math.signum(coefficientSet.frontLeft));
        this.frontLeft.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.frontLeft.motor.setPower(coefficientSet.frontLeft);

        this.backLeft.setDriveTarget(distanceCm * Math.signum(coefficientSet.backLeft));
        this.backLeft.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.backLeft.motor.setPower(coefficientSet.backLeft);

        this.frontRight.setDriveTarget(distanceCm * Math.signum(coefficientSet.frontRight));
        this.frontRight.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.frontRight.motor.setPower(coefficientSet.frontRight);

        this.backRight.setDriveTarget(distanceCm * Math.signum(coefficientSet.backRight));
        this.backRight.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.backRight.motor.setPower(coefficientSet.backRight);
    }
}
