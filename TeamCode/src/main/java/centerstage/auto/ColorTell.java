package centerstage.auto;

import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.StartSide;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.motor.NovelMotor;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;
import com.pocolifo.robobase.novel.NovelMecanumDrive;
import com.pocolifo.robobase.vision.ColorTeller;
import com.pocolifo.robobase.vision.NovelYCrCbDetection;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import centerstage.BackdropAprilTagAligner;
import centerstage.Constants;
import centerstage.SpikePosition;

@Autonomous
public class ColorTell extends AutonomousOpMode {
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


    private NovelYCrCbDetection spikeDetector;
    private final Alliance alliance = Alliance.BLUE;
    private final StartSide startSide = StartSide.APRIL_TAG_SIDE;
    private NovelMecanumDrive driver;
    private BackdropAprilTagAligner aprilTagAligner;
    private IMU imu;
    private IMU.Parameters parameters;
    private ColorTeller colorizer;

    public ColorTell() {
    }

    @Override
    public void initialize() {
        System.out.println("init start");
//        this.spikeDetector = new NovelYCrCbDetection(1);
        colorizer = new ColorTeller(1);
        this.webcam.open(this.colorizer);
        this.driver = new NovelMecanumDrive(this.fl, this.fr, this.bl, this.br, new OmniDriveCoefficients(new double[]{-1, 1, 1, 1})); //Test BOT
        System.out.println("init finished but for IMU");
        imu = hardwareMap.get(IMU.class,"imu");
        parameters = new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);
    }

    @Override
    public void run() {
        for(int i = 0;i<2000;i++) {
            try {
                ColorTeller pipeline = (ColorTeller) this.webcam.getPipeline();
                do {
                    System.out.println(colorizer.colorness);
                    sleep(1000);
                } while (true);
            } catch (Throwable e) {
                System.out.println("Stopped");
            }
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
        this.driver.setVelocity(new Vector3D(0,0, 15*direction));

        while(Math.abs(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)) < 90)
        {
            System.out.println(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        }
        System.out.println("correcting..." + (imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - 90));
        this.driver.setVelocity(new Vector3D(0,0,-3*direction));
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
}
