//package com.pocolifo.robobase.reconstructor;
//
//import com.acmerobotics.roadrunner.geometry.Pose2d;
//import com.pocolifo.robobase.novel.motion.NovelMecanumDrive;
//import com.pocolifo.robobase.novel.hardware.NovelOdometry;
//import com.qualcomm.robotcore.hardware.IMU;
//import org.firstinspires.ftc.robotcore.external.navigation.*;
//import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
//import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
//
//public class PoseEstimator {
//    private final IMU imu;
//    private final AprilTagProcessor aprilTagProcessor;
//    private final NovelOdometry odometry;
//    private final NovelMecanumDrive novelMecanumDrive;
//
//    public PoseEstimator(IMU imu, AprilTagProcessor aprilTagProcessor, NovelOdometry odometry, NovelMecanumDrive novelMecanumDrive) {
//        this.imu = imu;
//        this.aprilTagProcessor = aprilTagProcessor;
//        this.odometry = odometry;
//        this.novelMecanumDrive = novelMecanumDrive;
//    }
//
//    public void update() {
//        this.odometry.update();
//    }
//
//    public Pose2d getRelativePose() {
//        if (this.aprilTagProcessor.getDetections().isEmpty()) return null;
//
//        // April tag estimate
////        double aprilTagRobotPosEstimateXAvg = 0;
////        double aprilTagRobotPosEstimateYAvg = 0;
////
////        for (AprilTagDetection detection : this.aprilTagProcessor.getDetections()) {
////            double realPosX = detection.metadata.fieldPosition.get(0);
////            double realPosY = detection.metadata.fieldPosition.get(1);
////            double facingRobotRadians = Math.toRadians(detection.ftcPose.bearing + 180); // flip 180 degrees to get the way the april tag is facing the robot
////
////            aprilTagRobotPosEstimateXAvg += realPosX + Math.cos(facingRobotRadians) * detection.ftcPose.range;
////            aprilTagRobotPosEstimateYAvg += realPosY + Math.sin(facingRobotRadians) * detection.ftcPose.range;
////        }
////
////        aprilTagRobotPosEstimateXAvg /= this.aprilTagProcessor.getDetections().size();
////        aprilTagRobotPosEstimateYAvg /= this.aprilTagProcessor.getDetections().size();
//
//        // NovelOdometry estimate
//        double odometryX = this.odometry.getX();
//        double odometryY = this.odometry.getY();
//        double odometryRotation = this.odometry.getRotation();
//
//        return new Pose2d(
//                odometryX,
//                odometryY,
//                this.imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle
//        );
//    }
//}
