package centerstage.teleop;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.gamepad.GController;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.LocalizationEngine;
import com.pocolifo.robobase.reconstructor.Point;
import com.pocolifo.robobase.reconstructor.Pose;
import com.pocolifo.robobase.utils.MathUtils;
import com.pocolifo.robobase.vision.AprilTagRetriever;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@TeleOp
@Config
public class Localizer extends TeleOpOpMode {
    private CenterStageRobotConfiguration c;
    private AprilTagRetriever retriever;
    private NovelOdometry odometry;
    private NovelMecanumDriver driver;
    private GController gamepadController;
    private Telemetry.Item Ax, Ay, Ad, Ox, Oy, Od;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.retriever = new AprilTagRetriever(this.c.webcam, AprilTagGameDatabase.getCenterStageTagLibrary());
        this.odometry = this.c.createOdometry();
        this.driver = this.c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
        this.gamepadController = new GController(this.gamepad1)
                .x.initialToggleState(true).ok();  // micro-movement

        this.Ax = this.telemetry.addData("Ax: ", 0);
        this.Ay = this.telemetry.addData("Ay: ", 0);
        this.Ad = this.telemetry.addData("Adeg: ", 0);

        this.Ox = this.telemetry.addData("Ox: ", 0);
        this.Oy = this.telemetry.addData("Oy: ", 0);
        this.Od = this.telemetry.addData("Odeg: ", 0);
    }

    @Override
    public void loop() {
        this.telemetry.update();
        this.gamepadController.update();
        this.odometry.update();
        this.driver.useGamepad(this.gamepad1, this.gamepadController.x.isToggled() ? 2 : 1);

        // Localizer
        Pose aprilTagEstimate = this.estimateAprilTags();
        Pose odometryEstimate = this.odometry.getRelativePose();

        if (aprilTagEstimate != null) {
            this.Ax.setValue(aprilTagEstimate.getX());
            this.Ay.setValue(aprilTagEstimate.getY());
            this.Ad.setValue(aprilTagEstimate.getHeading(AngleUnit.DEGREES));
        }

        this.Ox.setValue(odometryEstimate.getX());
        this.Oy.setValue(odometryEstimate.getY());
        this.Od.setValue(odometryEstimate.getHeading(AngleUnit.DEGREES));
    }

    private Pose estimateAprilTags() {
        List<AprilTagDetection> detections = this.retriever.getDetections();

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

            AprilTagMetadata metadata = this.retriever.library.lookupTag(detection.id);
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
                MathUtils.weightedAverage(estimates.stream().map(Point::getX).collect(Collectors.toList()), normalizedWeights),
                MathUtils.weightedAverage(estimates.stream().map(Point::getY).collect(Collectors.toList()), normalizedWeights),
                MathUtils.weightedAverage(estimates.stream().map(pose -> pose.getHeading(AngleUnit.RADIANS)).collect(Collectors.toList()), normalizedWeights),
                AngleUnit.RADIANS
        );
    }
}
