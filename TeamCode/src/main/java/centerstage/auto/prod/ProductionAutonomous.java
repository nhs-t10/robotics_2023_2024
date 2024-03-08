package centerstage.auto.prod;

import android.os.SystemClock;
import androidx.core.math.MathUtils;
import centerstage.*;
import centerstage.vision.SpotDetectionPipeline;
import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.Pose;
import com.pocolifo.robobase.utils.Alliance;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class ProductionAutonomous extends AutonomousOpMode {
    private CenterStageRobotConfiguration c;
    private RobotCapabilities capabilities;
    private NovelMecanumDriver driver;
    private static double timeMultiplier = 2;
    private NovelOdometry o;
    private Telemetry.Item tX, tY, tR;
    private Telemetry.Item tHD;
    private final Alliance alliance;
    private final StartSide startSide;

    public ProductionAutonomous(Alliance alliance, StartSide startSide) {
        this.alliance = alliance;
        this.startSide = startSide;
    }

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.c.spinningIntake.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.c.spinningIntake.setPower(0);

        this.capabilities = new RobotCapabilities(this.c);
        this.driver = this.c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
        this.o = this.c.createOdometry();
        Thread thread = Thread.currentThread();


        this.tX = this.telemetry.addData("x ", 0);
        this.tY = this.telemetry.addData("y ", 0);
        this.tR = this.telemetry.addData("r ", 0);
        this.tHD = this.telemetry.addData("hd ", 0);

        new Thread(() -> {
            while (thread.isAlive()) {
                this.o.update();
                this.tX.setValue(this.o.getRelativePose().getX());
                this.tY.setValue(this.o.getRelativePose().getY());
                this.tR.setValue(this.o.getRelativePose().getHeading(AngleUnit.DEGREES));
                this.telemetry.update();
            }
        }).start();

        this.c.webcam.open(new SpotDetectionPipeline(alliance));
    }

    @Override
    public void run() {
        try {
            SpotDetectionPipeline pipeline = (SpotDetectionPipeline) this.c.webcam.getPipeline();

            runPath(pipeline.getResult());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void runPath(SpikePosition spikePosition) throws InterruptedException {
        placePixel(spikePosition);
        this.o.resetRelativePose(new Pose(0, 0, this.alliance == Alliance.RED ? -90 : 90, AngleUnit.DEGREES));

        if (startSide == StartSide.APRIL_TAG_SIDE) {
            if (alliance == Alliance.BLUE) {
                driveVertical(85, 4 * timeMultiplier);
            } else {
                driveVertical(90, 4 * timeMultiplier);
            }
        } else {
            driveVertical(40, 2 * timeMultiplier);
        }
        sleep(100);
//        rotate(-90);
//        sleep(100);
//
//        switch (spikePosition) {
//            case LEFT:
//                driveHorizontal(-12 - 4, 2.5 * timeMultiplier);
//                break;
//
//            case CENTER:
//                driveHorizontal(-24 - 4, 3 * timeMultiplier);
//                break;
//
//            case RIGHT:
//                driveHorizontal(-36 - 4, 4 * timeMultiplier);
//                break;
//        }
//
//        sleep(100);
    }

    public void placePixel(SpikePosition position) throws InterruptedException {
        switch (position) {
            case LEFT:
                driveVertical(12 + 12 + 5, 1 * timeMultiplier);
                rotate(-90);
                sleep(100);
                driveVertical(-2, 0.5 * timeMultiplier);
                dropAutoPixel();
                driveVertical(2, 0.5 * timeMultiplier);
                sleep(100);

                if (alliance == Alliance.RED) {
                    driveHorizontal(12 + 5 + 8, 1 * timeMultiplier);
                } else {
                    driveHorizontal(12 + 10, 1 * timeMultiplier);
                }

                sleep(100);
                rotate(this.alliance == Alliance.RED ? -90 : 90);
                sleep(100);


                break;

            case CENTER:
                driveVertical(45, 2 * timeMultiplier);
                dropAutoPixel();
                driveVertical(7, 0.25 * timeMultiplier);
                sleep(100);
                rotate(this.alliance == Alliance.RED ? -90 : 90);
                sleep(100);
                break;

            case RIGHT:
                driveVertical(12 + 12 + 5, 1 * timeMultiplier);
                rotate(90);
                sleep(100);
                driveVertical(-5, 0.5 * timeMultiplier);
                dropAutoPixel();
                driveVertical(5, 0.5 * timeMultiplier);
                sleep(100);

                if (startSide == StartSide.BACKDROP_SIDE) {

                    driveHorizontal(-8, 1 * timeMultiplier);
                } else {

                    driveHorizontal(-16, 1 * timeMultiplier);
                }

                sleep(100);
                rotate(this.alliance == Alliance.RED ? -90 : 90);
                sleep(100);
                break;
        }
    }

    public void dropAutoPixel() throws InterruptedException {
        this.c.spinningIntake.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.c.spinningIntake.setPower(-0.2);
        SystemClock.sleep(200);
        this.c.spinningIntake.setPower(0);
    }

    public void driveVertical(double inches, double time) throws InterruptedException {
        double error = 0;
        Pose start = this.o.getRelativePose();

        do {
            this.driver.setVelocity(new Vector3D(inches / time, 0, 0));
            Pose currentPose = this.o.getRelativePose();
            double heading = currentPose.getHeading(AngleUnit.RADIANS);

            Pose deltaPose = currentPose.subtract(start);
            double forwardDistance = Math.cos(heading) * deltaPose.getY() + Math.sin(heading) * deltaPose.getX();

            error = forwardDistance - inches;
        } while (Math.abs(error) > 0.5);

        this.driver.stop();
    }

    public void driveHorizontal(double inches, double time) throws InterruptedException {
        double error = 0;
        Pose start = this.o.getRelativePose();

        do {
            this.driver.setVelocity(new Vector3D(0, inches / time, 0));
            Pose currentPose = this.o.getRelativePose();
            double heading = currentPose.getHeading(AngleUnit.RADIANS);

            Pose deltaPose = currentPose.subtract(start);
            double horizontalDistance = Math.sin(heading) * -deltaPose.getY() + Math.cos(heading) * -deltaPose.getX();

            error = horizontalDistance - inches;
            this.tHD.setValue(Math.sin(heading) * -deltaPose.getY() + " " + Math.cos(heading) * -deltaPose.getX() + " " + error);
        } while (Math.abs(error) > 0.5);

        this.driver.stop();

//        this.driver.setVelocity(new Vector3D(0, inches / time, 0));
//
//        sleep((long) (time * 1000L));
//
//        this.driver.stop();
    }

    public void rotate(double degrees) {
        double error = this.o.getRelativePose().getHeading(AngleUnit.DEGREES) - degrees;
        error = MathUtils.clamp(error, -20, 20);

        while (Math.abs(error) > 1) {
            this.driver.setVelocity(new Vector3D(0, 0, error));
            error = this.o.getRelativePose().getHeading(AngleUnit.DEGREES) - degrees;
            error = MathUtils.clamp(error, -20, 20);
        }

        this.driver.stop();
    }
}
