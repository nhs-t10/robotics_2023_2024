package com.pocolifo.robobase.reconstructor;

import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.utils.MathUtils;
import com.pocolifo.robobase.vision.AprilTagRetriever;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class LocalizationEngine {
    private final OdometryLocalizerThread odometryLocalizerThread;
    private final AprilTagLocalizerThread aprilTagLocalizerThread;
    private final IMU imu;

    public LocalizationEngine(IMU imu, AprilTagRetriever aprilTagRetriever, NovelOdometry odometry, Pose initialPosition) {
        this.imu = imu;
        this.aprilTagLocalizerThread = new AprilTagLocalizerThread(aprilTagRetriever);
        this.aprilTagLocalizerThread.start();
        this.odometryLocalizerThread = new OdometryLocalizerThread(initialPosition, odometry);
        this.odometryLocalizerThread.start();
    }

    public Pose getPoseEstimate(AngleUnit angleUnit) {
        Pose aprilTagEstimate = this.aprilTagLocalizerThread.getCurrentEstimate();
        Point odometryEstimate = this.odometryLocalizerThread.getCurrentEstimate();
        double heading = this.imu.getRobotYawPitchRollAngles().getYaw(angleUnit);

        if (aprilTagEstimate == null) {
            return new Pose(
                    odometryEstimate.x,
                    odometryEstimate.y,
                    heading,
                    angleUnit
            );
        } else {
            this.odometryLocalizerThread.resetOdometry(aprilTagEstimate);

            return aprilTagEstimate;
        }
    }

    public static class OdometryLocalizerThread extends Thread {
        private final NovelOdometry odometry;
        private Point referencePosition;

        public OdometryLocalizerThread(Point referencePosition, NovelOdometry odometry) {
            this.odometry = odometry;
            this.resetOdometry(referencePosition);
        }

        @Override
        public void run() {
            while (!this.isInterrupted()) {
                this.odometry.update();
            }
        }

        public void resetOdometry(Point referencePosition) {
            this.odometry.resetRelativePose();
            this.referencePosition = referencePosition;
        }

        public Point getCurrentEstimate() {
            Pose relativePoseToReference = this.odometry.getRelativePose();

            return new Point(
                    relativePoseToReference.getX() + this.referencePosition.x,
                    relativePoseToReference.getY() + this.referencePosition.y
            );
        }
    }

    public static class AprilTagLocalizerThread extends Thread {
        private final AprilTagRetriever aprilTagRetriever;
        private Pose currentEstimate;

        public AprilTagLocalizerThread(AprilTagRetriever aprilTagRetriever) {
            this.aprilTagRetriever = aprilTagRetriever;
        }


        @Override
        public void run() {
            while (!this.isInterrupted()) {
                List<AprilTagDetection> detections = this.aprilTagRetriever.getDetections();

                if (detections.isEmpty()) {
                    this.currentEstimate = null;
                    continue;
                }

                List<Pose> estimates = new LinkedList<>();
                List<Double> weights = new LinkedList<>();

                for (int i = 0; i < detections.size(); i++) {
                    AprilTagDetection detection = detections.get(i);
                    Orientation orientation = Orientation.getOrientation(detection.rawPose.R, AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS);

                    estimates.add(new Pose(
                            detection.rawPose.y,
                            detection.rawPose.x,
                            orientation.thirdAngle,
                            AngleUnit.RADIANS
                    ));

                    weights.add((double) detection.decisionMargin);
                }

                List<Double> normalizedWeights = MathUtils.normalize(weights);

                this.currentEstimate = new Pose(
                        MathUtils.weightedAverage(estimates.stream().map(pose -> pose.x).collect(Collectors.toList()), normalizedWeights),
                        MathUtils.weightedAverage(estimates.stream().map(pose -> pose.y).collect(Collectors.toList()), normalizedWeights),
                        MathUtils.weightedAverage(estimates.stream().map(pose -> pose.headingRadians).collect(Collectors.toList()), normalizedWeights),
                        AngleUnit.RADIANS
                );
            }
        }

        public Pose getCurrentEstimate() {
            return currentEstimate;
        }
    }
}
