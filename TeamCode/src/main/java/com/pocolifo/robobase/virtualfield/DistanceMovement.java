package com.pocolifo.robobase.virtualfield;

import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.Pose;
import com.qualcomm.robotcore.hardware.IMU;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class DistanceMovement {
    private static final double PRECISION_IN = 0.5;
    private static final double PERCISION_DEG = 1;
    private static final double SPEED = 5;
    private final NovelMecanumDriver movementController;
    private final NovelOdometry odometry;
    private final IMU imu;
    private double positionalDifference = -1;
    private double rotationaldifference = -1;
    private Vector3D position;

    public DistanceMovement(NovelMecanumDriver movementController, NovelOdometry odometry, IMU imu) {
        this.movementController = movementController;
        this.odometry = odometry;
        this.imu = imu;
    }

    public void updatePositionalAndRotationalDifference(Vector3D target) {
        position = getPosition();
        odometry.update();
        positionalDifference = Math.sqrt(Math.pow(position.getX() - target.getX(), 2) + Math.pow(position.getY() - target.getY(), 2));
        rotationaldifference = Math.abs(position.getZ() - target.getZ());
    }

    private void goToPosition(Vector3D target) {

        updatePositionalAndRotationalDifference(target);

        while (positionalDifference > PRECISION_IN || rotationaldifference > PERCISION_DEG) {
            updatePositionalAndRotationalDifference(target);
            System.out.println(position);
            Vector3D velocity = getNewVelocity(position, target, SPEED);
            
            movementController.setVelocity(velocity);
        }
        movementController.setVelocity(Vector3D.ZERO);
    }

    public void rotateTo(double degrees) {
        goToPosition(new Vector3D(getPosition().getX(), getPosition().getY(), degrees));
    }

    public void transformTo(double x, double y) {
        goToPosition(new Vector3D(x, y, getPosition().getZ()));
    }

    public void moveTo(double x, double y, double degrees) {
        goToPosition(new Vector3D(x, y, degrees));
    }

    public Vector3D poseToVector3D(Pose pose) {
        return new Vector3D(pose.getX(), pose.getY(), pose.getHeading(AngleUnit.DEGREES));
    }

    public Vector3D getPosition() {
        Vector3D odometryPosition = poseToVector3D(odometry.getRelativePose());
        double imuRotation = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        return new Vector3D(odometryPosition.getX(), odometryPosition.getY(), imuRotation);
    }

    private static Vector3D getNewVelocity(Vector3D position, Vector3D target, double speed) {
      double horizontalMovement = target.getX() - position.getX();
      double verticalMovement = target.getY() - position.getY();

      double targetRotation = target.getZ() - position.getZ();

      // Convert current rotation from degrees to radians for trigonometric functions
      double currentRotationRadians = Math.toRadians(position.getZ());

      // Calculate the absolute movements based on the current rotation
      double absoluteHorizontalMovement = horizontalMovement * Math.cos(currentRotationRadians) - verticalMovement * Math.sin(currentRotationRadians);
      double absoluteVerticalMovement = horizontalMovement * Math.sin(currentRotationRadians) + verticalMovement * Math.cos(currentRotationRadians);
      
      // Create a new vector for the absolute velocity and normalize it
      Vector3D absoluteVelocity = new Vector3D(absoluteHorizontalMovement, absoluteVerticalMovement, targetRotation)
              .normalize().scalarMultiply(speed);

      return absoluteVelocity;
  }
}