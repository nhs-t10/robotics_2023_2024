package centerstage.auto;

import android.os.SystemClock;

import centerstage.BackdropAprilTagAligner;
import centerstage.Constants;
import centerstage.RobotCapabilities;
import centerstage.SpikePosition;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.StartSide;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.motor.NovelMotor;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;
import com.pocolifo.robobase.novel.NovelMecanumDrive;
import com.pocolifo.robobase.novel.Odometry;
import com.pocolifo.robobase.vision.NovelYCrCbDetection;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class BaseProductionAuto extends AutonomousOpMode {
    @Hardware(name = "Webcam")
    public Webcam webcam;

    @Hardware(name = "FL", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor fl;

    @Hardware(name = "FR", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor fr;

    @Hardware(name = "BL", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor bl;

    @Hardware(name = "BR", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor br;

    @Hardware(name = "Spintake", zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor spintake;

    private final NovelYCrCbDetection spikeDetector;
    private final Alliance alliance;
    private final StartSide startSide;
    private NovelMecanumDrive driver;
    private BackdropAprilTagAligner aprilTagAligner;
    private IMU imu;
    private IMU.Parameters parameters;
//    private Odometry odometry;


    public BaseProductionAuto(NovelYCrCbDetection spikeDetector, Alliance alliance, StartSide startSide, Pose2d startPosition) {
        this.spikeDetector = spikeDetector;
        this.alliance = alliance;
        this.startSide = startSide;
//        this.odometry = new Odometry(hardwareMap, startPosition,
//                "OR",
//                "OL",
//                "OP");
    }

    @Override
    public void initialize() {
        System.out.println("init start");
        this.webcam.open(this.spikeDetector);
        this.driver = new NovelMecanumDrive(this.fl, this.fr, this.bl, this.br, new OmniDriveCoefficients(new double[]{-1, 1, -1, 1})); //REAL BOT
        System.out.println("init finished but for IMU");
        imu = hardwareMap.get(IMU.class,"imu");
        parameters = new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);
    }

    @Override
    public void run() {
        try {
            System.out.println("StartingAuto");
            NovelYCrCbDetection pipeline = (NovelYCrCbDetection) this.webcam.getPipeline();
            SpikePosition spikePosition;
            do {
                spikePosition = pipeline.getResult();
                sleep(100);
            } while (spikePosition == null);
            System.out.println(spikePosition.toString());

            switch (spikePosition) {
                case LEFT:
                    System.out.println("left");
                    driveVertical(-27, 2);
                    sleep(500);
                    rotateIMU(90);
                    driveVertical(2, 0.5);
                    dropPixel();
                    driveVertical(-2, 0.5);
                    rotateIMU(-90);
                    //driveHorizontal(16, 1);
                    break;

                case RIGHT:
                    System.out.println("right");
                    driveVertical(-27, 2);
                    sleep(500);
                    rotateIMU(-90);
                    driveVertical(2, 0.5);
                    dropPixel();
                    driveVertical(-2, 0.5);
                    rotateIMU(90);
                    //driveHorizontal(-16, 1);
                    break;

                case CENTER:
                    System.out.println("center");
                    driveVertical(-51, 3);
                    dropPixel();
                    break;
            }

            SystemClock.sleep(500);

            //Reset to neutral position - GOOD
            switch (spikePosition) {
                case LEFT:
                    //driveHorizontal(-16, 1);
                    break;

                case RIGHT:
                    //driveHorizontal(16, 1);
                    break;

                case CENTER:
                    driveVertical(24, 1.25);
                    break;
            }

            SystemClock.sleep(500);

            driveHorizontal((42 + startSide.getSideSwapConstantIn()) * alliance.getAllianceSwapConstant(), 1.5 + (startSide.getSideSwapConstantIn() / 16));
            SystemClock.sleep(500);

            rotateIMU(90 * alliance.getAllianceSwapConstant());
            SystemClock.sleep(500);

            //this.aprilTagAligner = new BackdropAprilTagAligner(this.driver, SpikePosition.RIGHT, this.webcam, this.alliance, 30, 4);
            //alignWithAprilTag();
            switch (spikePosition) {
                case LEFT:
                    driveHorizontal(8, 0.5);
                    break;

                case RIGHT:
                    driveHorizontal(-8, 0.5);
                    break;

                case CENTER:
                    break;
            }

            SystemClock.sleep(500);
            rotateIMU(90);
            rotateIMU(90);

            //todo: place!!!

            switch (spikePosition){
                case LEFT:
                    driveHorizontal(-8,0.5);
                case RIGHT:
                    driveHorizontal(8,0.5);
                case CENTER:
                    //do nothing
            }
            driveHorizontal(24*alliance.getAllianceSwapConstant(),1.5);
        } catch (Throwable e) {
            System.out.println("Stopped");
        }
    }

    @Override
    public void stop() {
        try {
            this.webcam.close();
        } catch (Exception ignored) {}
    }

    public void driveVertical(double inches, double time) throws InterruptedException {
        this.driver.setVelocity(new Vector3D(inches / time, 0, 0));

        sleep((long) (time * 1000L));

        this.driver.stop();
    }

    public void driveHorizontal(double inches, double time) throws InterruptedException {
        this.driver.setVelocity(new Vector3D(0, inches / time, 0));

        sleep((long) (time * 1000L));

        this.driver.stop();
    }
    public void rotate(double degrees, double time) throws InterruptedException {
        //If you've done circular motion, this is velocity = omega times radius. Otherwise, look up circular motion velocity to angular velocity
        this.driver.setVelocity(new Vector3D(0,0,
                (Math.toRadians(degrees) * (Constants.ROBOT_DIAMETER_IN)/time)));
        sleep((long)time*1000);
        this.driver.stop();
    }
    public void rotateIMU(double degrees) throws InterruptedException {
        int direction = 1;
        if(degrees < 0)
        {
            direction = -1;
        }
        imu.resetYaw();
        //If you've done circular motion, this is velocity = omega times radius. Otherwise, look up circular motion velocity to angular velocity
        this.driver.setVelocity(new Vector3D(0,0, 20*direction));

        while(Math.abs(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)) < 90)
        {
            System.out.println(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        }
        System.out.println("correcting..." + (imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - 90));
        this.driver.setVelocity(new Vector3D(0,0,-4*direction));
        while(Math.abs(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)) > degrees*direction)
        {
            System.out.println(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        }
        this.driver.stop();
    }


    public void alignWithAprilTag() throws InterruptedException {
        while (this.aprilTagAligner.getStatus() != BackdropAprilTagAligner.AlignmentStatus.ALIGNMENT_COMPLETE) {
            this.aprilTagAligner.updateAlignment();
            sleep(1000);
        }
    }
    public void dropPixel()
    {
        //todo: fix power
        spintake.setPower(-0.1);
        SystemClock.sleep(1000);
        spintake.setPower(0);
    }
}
