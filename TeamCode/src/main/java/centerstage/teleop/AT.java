package centerstage.teleop;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.gamepad.GController;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.LocalizationEngine;
import com.pocolifo.robobase.reconstructor.Pose;
import com.pocolifo.robobase.vision.AprilTagRetriever;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;

@TeleOp
@Config
public class AT extends TeleOpOpMode {
    private CenterStageRobotConfiguration c;
    private AprilTagRetriever retriever;
    private NovelOdometry odometry;
    private NovelMecanumDriver driver;
    private GController gamepadController;
    private LocalizationEngine localizer;
    private Telemetry.Item tX, tY, tR, EX, EY, ER;
    volatile boolean correcting = false;
    public static Thread mainThread;

    public Pose target;

    @Override
    public void initialize() {
        mainThread = Thread.currentThread();
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.retriever = new AprilTagRetriever(this.c.webcam, AprilTagGameDatabase.getCenterStageTagLibrary());
        this.odometry = this.c.createOdometry();
        this.driver = this.c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
        this.localizer = new LocalizationEngine(this.c.imu, this.retriever, this.odometry, new Pose(0, 0, 0, AngleUnit.RADIANS));
        this.gamepadController = new GController(this.gamepad1)
                .x.initialToggleState(true).ok()  // micro-movement
                .y.onPress(this::moveToPosition).ok()
                .a.onPress(this::rememberLocation).ok();

        this.tX = this.telemetry.addData("x: ", 0);
        this.tY = this.telemetry.addData("y: ", 0);
        this.tR = this.telemetry.addData("deg: ", 0);

        this.EX = this.telemetry.addData("Ex: ", 0);
        this.EY = this.telemetry.addData("Ey: ", 0);
        this.ER = this.telemetry.addData("Edeg: ", 0);
    }

    private void rememberLocation() {
        target = this.localizer.getPoseEstimate(AngleUnit.RADIANS);
        System.out.println("localized");
    }

    public void moveToPosition() {
        new Thread(() -> {
            Pose current = this.localizer.getPoseEstimate(AngleUnit.RADIANS);
            correcting = true;

            while (current.distanceTo(target) > 2 && mainThread.isAlive()) {
                current = this.localizer.getPoseEstimate(AngleUnit.RADIANS);
                Pose difference = target.subtract(current);

                double dx = difference.getX();
                double dy = difference.getY();
                double dhRadians = difference.getHeading(AngleUnit.RADIANS);
                double dh = (dhRadians / (2 * Math.PI)) * Constants.Robot.ROBOT_DIAMETER_IN;

                EX.setValue(dx);
                EY.setValue(dy);
                ER.setValue(dh);

                this.driver.setVelocity(new Vector3D(
                        dx,
                        -dy,
                        -dh
                ));

                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
            correcting = false;
            this.driver.stop();

        }).start();
    }

    @Override
    public void loop() {
        Pose poseEstimate = this.localizer.getPoseEstimate(AngleUnit.RADIANS);

        if (!correcting) {
            this.driver.useGamepad(this.gamepad1, this.gamepadController.x.isToggled() ? 2 : 1);
        }
        this.tX.setValue(poseEstimate.getX());
        this.tY.setValue(poseEstimate.getY());
        this.tR.setValue(poseEstimate.getHeading(AngleUnit.DEGREES));
        this.telemetry.update();
        this.gamepadController.update();

        TelemetryPacket p = new TelemetryPacket();
        p.fieldOverlay()
                .drawGrid(0, 0, 144, 144, 7, 7)

                .setStroke("#ff0000")
                .setStrokeWidth(2)
                .strokeRect(poseEstimate.getX() - 6, poseEstimate.getY() - 6, 12, 12);


        FtcDashboard.getInstance().sendTelemetryPacket(p);


//        List<AprilTagDetection> detections = this.retriever.getDetections();
//        this.odometry.update();
//        Pose relativePose = this.odometry.getRelativePose();
//
//        this.telemetry.addLine("odometry");
//        this.telemetry.addLine().addData("x: ", relativePose.getX());
//        this.telemetry.addLine().addData("y: ", relativePose.getY());
//
//        for (int i = 0; i < detections.size(); i++) {
//            AprilTagDetection detection = detections.get(i);
//            this.telemetry.addLine("#" + detection.id);
//            this.telemetry.addLine().addData("x: ", detection.ftcPose.x);
//            this.telemetry.addLine().addData("y: ", detection.ftcPose.y);
//            this.telemetry.addLine().addData("range: ", detection.ftcPose.range);
//            this.telemetry.addLine().addData("bearing: ", detection.ftcPose.bearing);
//
//            AprilTagMetadata metadata = this.retriever.library.lookupTag(detection.id);
//            float y = metadata.fieldPosition.get(0);
//            this.telemetry.addLine().addData("fx: ", metadata.fieldPosition.get(1));
//            this.telemetry.addLine().addData("fy: ", metadata.fieldPosition.get(0));
//            this.telemetry.addLine().addData("ax: ", metadata.fieldPosition.get(1)+detection.ftcPose.x);
//            this.telemetry.addLine().addData("ay: ", metadata.fieldPosition.get(0)-Math.copySign(detection.ftcPose.y, y));
//        }

    }
}
