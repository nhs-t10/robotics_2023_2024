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
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class LocalizationEngine {
    private final IMU imu;
    private final AprilTagRetriever aprilTagRetriever;
    private final NovelOdometry odometry;
    private Pose mostRecentAprilTagEstimate;

    public LocalizationEngine(IMU imu, AprilTagRetriever aprilTagRetriever, NovelOdometry odometry, Pose initialPosition) {
        this.imu = imu;
        this.aprilTagRetriever = aprilTagRetriever;
        this.odometry = odometry;
        this.mostRecentAprilTagEstimate = initialPosition;
    }

    public Pose getPoseEstimate(AngleUnit angleUnit) {
        Pose aprilTagEstimate = this.estimateAprilTags();
        double heading = this.imu.getRobotYawPitchRollAngles().getYaw(angleUnit);

        this.odometry.update();

        if (aprilTagEstimate != null && !Double.isNaN(aprilTagEstimate.getX()) && !Double.isNaN(aprilTagEstimate.getY())) {
            this.mostRecentAprilTagEstimate = aprilTagEstimate;
            this.odometry.resetRelativePose(new Pose(0, 0, 0, AngleUnit.RADIANS));
        }

        Pose odometryEstimate = this.odometry.getRelativePose();

        return new Pose(
                odometryEstimate.x + this.mostRecentAprilTagEstimate.getX(),
                odometryEstimate.y + this.mostRecentAprilTagEstimate.getY(),
                heading,
                angleUnit
        );
    }

    private Pose estimateAprilTags() {
        List<AprilTagDetection> detections = this.aprilTagRetriever.getDetections();

        if (detections.isEmpty()) {
            return null;
        }

        List<Pose> estimates = new LinkedList<>();
        List<Double> weights = new LinkedList<>();
        double maxRange = 0;

        for (AprilTagDetection detection : detections) {
            maxRange = Math.max(detection.ftcPose.range, maxRange);
        }

        for (AprilTagDetection detection : detections) {
            Orientation orientation = Orientation.getOrientation(detection.rawPose.R, AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS);

            AprilTagMetadata metadata = this.aprilTagRetriever.library.lookupTag(detection.id);
            double x = metadata.fieldPosition.get(1) + detection.ftcPose.x;
            double y = metadata.fieldPosition.get(0) - Math.copySign(detection.ftcPose.y, metadata.fieldPosition.get(0));

            estimates.add(new Pose(
                    x,
                    y,
                    orientation.thirdAngle,
                    AngleUnit.RADIANS
            ));

            weights.add((maxRange - detection.ftcPose.range) * ((double) detection.decisionMargin));
        }

        List<Double> normalizedWeights = MathUtils.normalize(weights);

        return new Pose(
                MathUtils.weightedAverage(estimates.stream().map(pose -> pose.x).collect(Collectors.toList()), normalizedWeights),
                MathUtils.weightedAverage(estimates.stream().map(pose -> pose.y).collect(Collectors.toList()), normalizedWeights),
                MathUtils.weightedAverage(estimates.stream().map(pose -> pose.headingRadians).collect(Collectors.toList()), normalizedWeights),
                AngleUnit.RADIANS
        );
    }
}
