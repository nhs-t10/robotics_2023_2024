package com.pocolifo.robobase.virtualfield;

import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.Pose;
import com.qualcomm.robotcore.hardware.IMU;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Contains methods for using odometry, IMU, and Novel to move to by precise absolute distances on the field
 * @see IMU
 * @see NovelOdometry
 * @see NovelMecanumDriver
 */
public class DistanceMovement {
    private static final double PRECISION_IN = 4;
    private static final double PERCISION_DEG = 1;
    private static final double SPEED = 5;
    private final NovelMecanumDriver movementController;
    private final NovelOdometry odometry;
    private final IMU imu;
    private double positionalDifference = -1;
    private double rotationaldifference = -1;

    public DistanceMovement(NovelMecanumDriver movementController, NovelOdometry odometry, IMU imu) {
        this.movementController = movementController;
        this.odometry = odometry;
        this.imu = imu;
    }

    private void updatePositionalAndRotationalDifference(Vector3D target, Vector3D position) {
        positionalDifference = Math.sqrt(Math.pow(position.getX() - target.getX(), 2) + Math.pow(position.getY() - target.getY(), 2));
        rotationaldifference = getRotationMovement(position.getZ(), target.getZ());
    }

    private void moveBy(Vector3D movement) {

        Vector3D position = getPosition();
        Vector3D target = position.add(movement);
        updatePositionalAndRotationalDifference(target, position);
        while (positionalDifference > PRECISION_IN || rotationaldifference > PERCISION_DEG) {
            position = getPosition();
            updatePositionalAndRotationalDifference(target, position);
            Vector3D velocity = getNewVelocity(position, target, SPEED);
            
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

    private Vector3D getPosition() {
        Vector3D odometryPosition = poseToVector3D(odometry.getRelativePose());
        double imuRotation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
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

    private static Vector3D getNewVelocity(Vector3D position, Vector3D target, double speed) {
      double horizontalMovement = target.getX() - position.getX();
      double verticalMovement = target.getY() - position.getY();

      double rotationTarget = (target.getZ() - target.getZ()) % 360;
      if (rotationTarget < -180) {
          rotationTarget += 360;
      } else if (rotationTarget > 180) {
          rotationTarget -= 360;
      }

      // Convert current rotation from degrees to radians for trigonometric functions
      double currentRotationRadians = Math.toRadians(position.getZ());

      // Calculate the absolute movements based on the current rotation
      double absoluteHorizontalMovement = horizontalMovement * Math.cos(currentRotationRadians) - verticalMovement * Math.sin(currentRotationRadians);
      double absoluteVerticalMovement = horizontalMovement * Math.sin(currentRotationRadians) + verticalMovement * Math.cos(currentRotationRadians);

      // Create a new vector for the absolute velocity and normalize it
      Vector3D absoluteVelocity = new Vector3D(absoluteVerticalMovement, absoluteHorizontalMovement * -1, rotationTarget);

      if (absoluteVelocity.equals(Vector3D.ZERO)) {
          return absoluteVelocity;
      }

      return absoluteVelocity.normalize().scalarMultiply(speed);
  }
}