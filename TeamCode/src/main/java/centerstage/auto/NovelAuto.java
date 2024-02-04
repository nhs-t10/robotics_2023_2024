package centerstage.auto;

import android.os.SystemClock;
import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.motion.NaiveConstantMotionProfile;
import com.pocolifo.robobase.novel.motion.NovelMecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

@Config
@Autonomous
public class NovelAuto extends AutonomousOpMode {
    private NovelMecanumDrive driver;
    public static int ms = 1000;
    public static double x = 4;
    public static double y = 4;
    public static double z = 4;
    public static double maxVelocity = 40;
    public static double maxAcceleration = 5;
    public static double minAcceleration = 5;
    NaiveConstantMotionProfile naiveConstantMotionProfile;
    private CenterStageRobotConfiguration c;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.driver = new NovelMecanumDrive(c.fl, c.fr, c.bl, c.br, Constants.Coefficients.PRODUCTION_COEFFICIENTS);
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
