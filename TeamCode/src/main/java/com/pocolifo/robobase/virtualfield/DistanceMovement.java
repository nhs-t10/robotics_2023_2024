package com.pocolifo.robobase.virtualfield;

import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.Pose;
import com.qualcomm.robotcore.hardware.IMU;

import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Contains methods for using odometry, IMU, and Novel to move to by precise absolute distances on the field
 * @see IMU
 * @see NovelOdometry
 * @see NovelMecanumDriver
 */
public class DistanceMovement {
    private static final double PRECISION_IN = 0.1;
    private static final double PERCISION_DEG = 0.2;
    private final NovelMecanumDriver movementController;
    private final NovelOdometry odometry;
    private final IMU imu;
    private double positionalDifference = -1;
    private double rotationaldifference = -1;
    private double startRotation;
    private Vector3D velocity;
    private double speed;

    public DistanceMovement(NovelMecanumDriver movementController, NovelOdometry odometry, IMU imu, double startRotation, double speed) {
        this.movementController = movementController;
        this.odometry = odometry;
        this.imu = imu;
        this.startRotation = startRotation;
        this.speed = speed;
    }

    private void updatePositionalAndRotationalDifference(Vector3D target, Vector3D position) {
        positionalDifference = Math.sqrt(Math.pow(position.getX() - target.getX(), 2) + Math.pow(position.getY() - target.getY(), 2));
        rotationaldifference = Math.abs(DistanceMovement.getRotationMovement(position.getZ(), target.getZ()));
    }

    private void moveBy(Vector3D movement) {

        Vector3D position = getPosition();
        Vector3D target = position.add(movement);
        updatePositionalAndRotationalDifference(target, position);
        while (positionalDifference > PRECISION_IN || rotationaldifference > PERCISION_DEG) {
            position = getPosition();
            updatePositionalAndRotationalDifference(target, position);
            Vector3D velocity = getNewVelocity(position, target, speed);
            
            movementController.setVelocity(velocity);
        }
        movementController.setVelocity(Vector3D.ZERO);
    }

    /**
     * Rotates by a number of relative degrees
     * @param degrees degrees to rotate by
     */
    public void rotate(double degrees) {
        moveBy(new Vector3D(0, 0, degrees));
    }

    /**
     * Transforms the robot by absolute x and y distances
     * @param x absolute x distance to move by
     * @param y absolute y distance to move by
     */
    public void transform(double x, double y) {
        moveBy(new Vector3D(x, y, 0));
    }

    /**
     * Moves the robot by absolute x and y distances and rotates a number of degrees
     * @param x absolute x distance to move by
     * @param y absolute y distance to move by
     * @param degrees degrees to rotate by
     */
    public void move(double x, double y, double degrees) {
        moveBy(new Vector3D(x, y, degrees));
    }

    private Vector3D poseToVector3D(Pose pose) {
        return new Vector3D(pose.getX(), pose.getY(), pose.getHeading(AngleUnit.DEGREES));
    }

    public Vector3D getPosition() {
        Vector3D odometryPosition = poseToVector3D(odometry.getAbsolutePose(startRotation));
        double imuRotation = -imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        if (imuRotation < 0) {
            imuRotation += 360;
        }
        return new Vector3D(odometryPosition.getX(), odometryPosition.getY(), imuRotation);
    }

    private static double getRotationMovement(double current, double target) {
        double delta = (target - current) % 360;
        if (delta < -180) {
            delta += 360;
        } else if (delta > 180) {
            delta -= 360;
        }
        return delta;
    }

    private Vector3D getNewVelocity(Vector3D position, Vector3D target, double speed) {

        double horizontalMovement = target.getX() - position.getX();
        double verticalMovement = target.getY() - position.getY();
        double rotationTarget = getRotationMovement(position.getZ(), target.getZ());

        double currentRotationRadians = Math.toRadians(position.getZ()) + this.startRotation;

        double horizontal = (horizontalMovement * Math.cos(currentRotationRadians) + verticalMovement * -Math.sin(currentRotationRadians));
        double vertical = (verticalMovement * -Math.cos(-currentRotationRadians) + horizontalMovement * Math.sin(-currentRotationRadians));

        Vector3D absoluteVelocity = new Vector3D(vertical, horizontal, rotationTarget / 4); // Lower rotation so it does not block x/y movement completely when normalized
        this.velocity = absoluteVelocity;

        if (absoluteVelocity.equals(Vector3D.ZERO)) {
            return Vector3D.ZERO;
        }

        // Slow speed down when close to target
        if (rotationaldifference + positionalDifference < speed / 5) {
            speed = 3;
        }

        absoluteVelocity = absoluteVelocity.normalize().scalarMultiply(speed);
        
        return absoluteVelocity;
    }

    public double getRotationaldifference() {
        return rotationaldifference;
    }

    public double getPositionalDifference() {
        return positionalDifference;
    }

    public Vector3D getVelocity() {
        return velocity;
    }
}