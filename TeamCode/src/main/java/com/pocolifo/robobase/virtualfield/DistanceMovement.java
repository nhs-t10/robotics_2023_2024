package com.pocolifo.robobase.virtualfield;

import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.Pose;

public class DistanceMovement {
    private static final double PRECISION_IN = 0.5;
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

    public void updatePositionalAndRotationalDifference(Vector3D movement, Vector3D position) {
        positionalDifference = Math.sqrt(Math.pow(position.getX() - target.getX(), 2) + Math.pow(position.getY() - movement.getY(), 2));
        rotationaldifference = Math.abs(position.getZ() - movement.getZ());
    }

    private void moveBy(Vector3D movement) {

        updatePositionalAndRotationalDifference(movement);
        Vector3D position = getPosition();

        while (positionalDifference > PRECISION_IN || rotationaldifference > PERCISION_DEG) {
            position = getPosition();
            updatePositionalAndRotationalDifference(movement, position);
            System.out.println(position);
            Vector3D velocity = getNewVelocity(position, movement, SPEED);
            
            movementController.setVelocity(velocity);
        }
        movementController.setVelocity(Vector3D.ZERO);
    }

    public void rotate(double degrees) {
        moveBy(new Vector3D(0, 0, degrees));
    }

    public void transform(double x, double y) {
        moveBy(new Vector3D(x, y, 0));
    }

    public void move(double x, double y, double degrees) {
        moveBy(new Vector3D(x, y, degrees));
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