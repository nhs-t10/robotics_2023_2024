package centerstage.auto;

import centerstage.BackdropAprilTagAligner;
import centerstage.Constants;
import centerstage.RobotCapabilities;
import centerstage.SpikePosition;
import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.motor.NovelMotor;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;
import com.pocolifo.robobase.novel.NovelMecanumDrive;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
@Config
public class AprilTagAlignmentTesting extends AutonomousOpMode {
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

    private RobotCapabilities capabilities;
    private NovelMecanumDrive driver;
    private BackdropAprilTagAligner aprilTagAligner;

    public static String allianceName = "BLUE";
    public static String spikePositionName = "RIGHT";

    public static int pixelThreshold = 30;
    public static int alignmentSpeed = 4;
    public static int updateInterval = 100;

    @Override
    public void initialize() {
        this.driver = new NovelMecanumDrive(this.fl, this.fr, this.bl, this.br, new OmniDriveCoefficients(new double[]{-1, 1, 1, 1}));
        this.aprilTagAligner = new BackdropAprilTagAligner(this.driver, SpikePosition.valueOf(spikePositionName), this.webcam, Alliance.valueOf(allianceName), pixelThreshold, alignmentSpeed);
    }

    @Override
    public void run() {
        try {
            while (this.aprilTagAligner.isNotAligned()) {
                this.aprilTagAligner.updateAlignment();
                sleep(updateInterval);
            }
        } catch (InterruptedException ignored) {}
    }
}
