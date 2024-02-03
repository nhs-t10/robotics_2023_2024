package com.pocolifo.robobase.reconstructor;

import android.util.Size;
import centerstage.Constants;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class LocalizationEngine {
    private final AprilTagProcessor aprilTagProcessor;
    private final BNO055IMU imu;
    private final VisionPortal aprilTagVisionPortal;
    private Position imuInitializedPosition;

    public LocalizationEngine(BNO055IMU imu, WebcamName webcam) {
        this.imu = imu;
        this.aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)  // inches and degrees match the april tag library metadata
                .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .build();

        this.aprilTagVisionPortal = new VisionPortal.Builder()
                .setCameraResolution(new Size(Constants.CAMERA_RES_WIDTH, Constants.CAMERA_RES_HEIGHT))
                .setCamera(webcam)
                .addProcessor(this.aprilTagProcessor)
                .enableLiveView(true)
                .build();
    }

    public Position getAprilTagPositionEstimate() {
        if (this.aprilTagProcessor.getDetections().isEmpty()) return null;

        double robotPosEstimateXAvg = 0;
        double robotPosEstimateYAvg = 0;

        for (AprilTagDetection detection : this.aprilTagProcessor.getDetections()) {
            double realPosX = detection.metadata.fieldPosition.get(0);
            double realPosY = detection.metadata.fieldPosition.get(1);
            double facingRobotRadians = Math.toRadians(detection.ftcPose.bearing + 180); // flip 180 degrees to get the way the april tag is facing the robot

            robotPosEstimateXAvg += realPosX + Math.sin(facingRobotRadians) * detection.ftcPose.range;
            robotPosEstimateYAvg += realPosY + Math.cos(facingRobotRadians) * detection.ftcPose.range;
        }

        return new Position(
                DistanceUnit.METER,
                DistanceUnit.INCH.toMeters(robotPosEstimateXAvg / this.aprilTagProcessor.getDetections().size()),
                DistanceUnit.INCH.toMeters(robotPosEstimateYAvg / this.aprilTagProcessor.getDetections().size()),
                0,
                System.currentTimeMillis()
        );
    }

    public Position getIMUPositionEstimate() {
        assert this.imuInitializedPosition.unit == DistanceUnit.METER;
        Position imuPosition = this.imu.getPosition();

        return new Position(
                DistanceUnit.METER,
                this.imuInitializedPosition.x + imuPosition.unit.toMeters(imuPosition.x),
                this.imuInitializedPosition.y + imuPosition.unit.toMeters(imuPosition.y),
                0, // this.imuInitializedPosition.z + imuPosition.unit.toMeters(imuPosition.z),
                System.currentTimeMillis()
        );
    }

    // TODO: fault tolerance: if one position has a crazy position, neglect it
    public Position getPositionEstimate() {
        Position[] positionEstimate = new Position[]{
                this.getAprilTagPositionEstimate(),
                this.getIMUPositionEstimate()
        };

        Position averagePosition = new Position(DistanceUnit.METER, 0, 0, 0, System.currentTimeMillis());
        int positions = 0;

        for (Position position : positionEstimate) {
            if (position == null) continue;

            averagePosition.x += position.x;
            averagePosition.y += position.y;
            averagePosition.z += position.z;
            positions++;
        }

        return new Position(
                averagePosition.unit,
                averagePosition.x / positions,
                averagePosition.y / positions,
                averagePosition.z / positions,
                System.currentTimeMillis()
        );
    }

//    public void calibrateAprilTags(MecanumDrive drive, int calibrationSamples) {
//        drive.drive(10, 0, 0.25);
//        drive.driveOmni(0, 0, 0.2);
//
//        Position averagePosition = new Position(DistanceUnit.METER, 0, 0, 0, 0);
//        int samples = 0;
//
//        while (samples < calibrationSamples) {
//            Position aprilTagPositionEstimate = getAprilTagPositionEstimate();
//            averagePosition.x += aprilTagPositionEstimate.x;
//            averagePosition.y += aprilTagPositionEstimate.y;
//            averagePosition.z += aprilTagPositionEstimate.z;
//            samples++;
//
//            SystemClock.sleep(2000);
//        }
//
//        this.imuInitializedPosition = new Position(
//                averagePosition.unit,
//                averagePosition.x / samples,
//                averagePosition.y / samples,
//                averagePosition.z / samples,
//                System.currentTimeMillis()
//        );
//
//        drive.rotateTo(0, 0.7);
//        drive.drive(-10, 0, 0.25);
//    }
//
//    /**
//     * This calibration method relies on the facts that
//     * <ul>
//     * <li>we know where the diagonal tape marks for human players are</li>
//     * <li>we know the distance between these tape marks to the start positions</li>
//     * <li>we can move the robot precisely to a certain position</li>
//     * </ul>
//     *
//     * @param drive
//     * @param alliance
//     * @param robot
//     * @param isOnAprilTagSide
//     */
//    public void calibrateCenterStage(MecanumDrive drive, Alliance alliance, RobotSize robot, boolean isOnAprilTagSide) {
//        // 6ft = 1.8288 meters
//        // so (-1.8288, -1.8288) is the bottom corner marked with the red tape
//
//        // marked with the red tape on the field
//        final Position RED_HUMAN_PLAYER_AREA = new Position(
//                DistanceUnit.METER,
//                -1.8288 + robot.widthCm / 2,
//                -1.8288 + robot.lengthCm / 2,
//                0,
//                0
//        );
//
//        // marked with the BLUE tape in the corner of the field, just like above
//        final Position BLUE_HUMAN_PLAYER_AREA = new Position(
//                DistanceUnit.METER,
//                1.8288 - robot.widthCm / 2,
//                -1.8288 + robot.lengthCm / 2,
//                0,
//                0
//        );
//
//        int allianceCoefficient = alliance == Alliance.RED ? 1 : -1;
//        int squaresToMove = isOnAprilTagSide ? 1 : 3;
//        double distanceToMoveCm = 60.96 * squaresToMove;
//
//        drive.drive(5, 0, 0.1); // mve forward a little to not bump into the trestle
//        drive.drive(0, allianceCoefficient * distanceToMoveCm, 0.5);
//        drive.drive(0, -5, 0.1);
//
//        // RED = BLUE_HUMAN_PLAYER_AREA is not backwards, see above!!
//        this.imuInitializedPosition = alliance == Alliance.RED ? BLUE_HUMAN_PLAYER_AREA : RED_HUMAN_PLAYER_AREA;
//    }
}
