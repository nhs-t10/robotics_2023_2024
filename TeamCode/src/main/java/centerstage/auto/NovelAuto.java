package centerstage.auto;

import android.os.SystemClock;
import centerstage.Constants;
import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.motor.NovelMotor;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;
import com.pocolifo.robobase.novel.NaiveConstantMotionProfile;
import com.pocolifo.robobase.novel.NovelMecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

@Config
@Autonomous
public class NovelAuto extends AutonomousOpMode {
    @Hardware(name = "FL", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor fl;

    @Hardware(name = "FR", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor fr;

    @Hardware(name = "BL", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor bl;

    @Hardware(name = "BR", wheelDiameterIn = 3.7795275590551185, ticksPerRevolution = Constants.MOTOR_TICK_COUNT, zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE)
    public NovelMotor br;

    private NovelMecanumDrive driver;

    public static int flC = -1;
    public static int frC = 1;
    public static int blC = 1;
    public static int brC = 1;
    public static int ms = 1000;
    public static double x = 4;
    public static double y = 4;
    public static double z = 4;
    public static double maxVelocity = 40;
    public static double maxAcceleration = 5;
    public static double minAcceleration = 5;
    NaiveConstantMotionProfile naiveConstantMotionProfile;

    public static double IPS = 6;

    @Override
    public void initialize() {
        this.driver = new NovelMecanumDrive(fl, fr, bl, br, new OmniDriveCoefficients(new double[]{1, 1, -1, 1}));
        naiveConstantMotionProfile = new NaiveConstantMotionProfile(
                new Vector3D(x, y, z),
                new Vector3D(0, 0, 0),
                maxVelocity,
                maxAcceleration,
                minAcceleration
        );
//        return null;
    }

    @Override
    public void run() {
        this.driver.setVelocity(new Vector3D(x, y, z));

        SystemClock.sleep(ms);

        this.driver.stop();
    }
}
