package centerstage.auto;

import centerstage.BackdropAprilTagAligner;
import centerstage.Constants;
import centerstage.RobotCapabilities;
import centerstage.SpikePosition;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.StartSide;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.motor.NovelMotor;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;
import com.pocolifo.robobase.novel.NovelMecanumDrive;
import com.pocolifo.robobase.vision.NovelYCrCbDetection;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

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

    @Hardware(name = "Lift", ticksPerRevolution = Constants.MOTOR_TICK_COUNT)
    public DcMotorEx liftMotor;

    @Hardware(name = "AirplaneLauncher")
    public CRServo airplaneLauncher;

    @Hardware(name = "PixelDropper")
    public CRServo pixelDropper;

    @Hardware(name = "ClawGrip")
    public CRServo clawGrip;

    @Hardware(name = "ClawRotation")
    public CRServo clawRotation;
    private RobotCapabilities capabilities;

    private final NovelYCrCbDetection spikeDetector;
    private final Alliance alliance;
    private final StartSide startSide;
    private NovelMecanumDrive driver;
    private BackdropAprilTagAligner aprilTagAligner;

    public BaseProductionAuto(NovelYCrCbDetection spikeDetector, Alliance alliance, StartSide startSide) {
        this.spikeDetector = spikeDetector;
        this.alliance = alliance;
        this.startSide = startSide;
    }

    @Override
    public void initialize() {
        this.webcam.open(this.spikeDetector);
        this.driver = new NovelMecanumDrive(this.fl, this.fr, this.bl, this.br, new OmniDriveCoefficients(new double[]{1, 1, -1, 1}));
        this.capabilities = new RobotCapabilities(this.clawGrip, this.clawRotation, this.airplaneLauncher, this.liftMotor, this.pixelDropper);
        this.aprilTagAligner = new BackdropAprilTagAligner(this.driver, SpikePosition.RIGHT, this.webcam, this.alliance, 30, 4);
    }

    @Override
    public void run() {
        try {
            NovelYCrCbDetection pipeline = (NovelYCrCbDetection) this.webcam.getPipeline();
            SpikePosition spikePosition;

            do {
                spikePosition = pipeline.getResult();
                sleep(100);
            } while (spikePosition == null);

            switch (spikePosition) {
                case LEFT:
                    driveVertical(-26, 2);
                    sleep(500);
                    driveHorizontal(-16, 1);
                    break;

                case RIGHT:
                    driveVertical(-26, 2);
                    sleep(500);
                    driveHorizontal(16, 1);
                    break;

                case CENTER:
                    driveVertical(-34, 2.5);
                    break;
            }

            this.capabilities.dropAutoPixel();

            switch (spikePosition) {
                case LEFT:
                    driveHorizontal(16, 1);
                    break;

                case RIGHT:
                    driveHorizontal(-16, 1);
                    break;

                case CENTER:
                    driveVertical(8, 0.5);
                    break;
            }

            sleep(500);

            driveHorizontal((10 + startSide.getSideSwapConstantIn()) * alliance.getAllianceSwapConstant(), 1.5+(startSide.getSideSwapConstantIn()/16));
            sleep(500);

            rotate(90* alliance.getAllianceSwapConstant(),2);

            alignWithAprilTag();

            rotate(180,4);

            //todo: place!!!
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

    public void alignWithAprilTag() throws InterruptedException {
        while (this.aprilTagAligner.getStatus() != BackdropAprilTagAligner.AlignmentStatus.ALIGNMENT_COMPLETE) {
            this.aprilTagAligner.updateAlignment();
            sleep(1000);
        }
    }
}
