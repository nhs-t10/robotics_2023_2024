package centerstage.auto;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import centerstage.TestBotConfig;
import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.novel.motion.profiling.TrapezoidalMotionProfile;
import com.pocolifo.robobase.reconstructor.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous
@Config
public class TrapezoidalTest extends AutonomousOpMode {
    private CenterStageRobotConfiguration c;
    private NovelMecanumDriver driver;
    private NovelOdometry odometry;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.driver = this.c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
        this.odometry = this.c.createOdometry();
    }

    @Override
    public void run() {
        TrapezoidalMotionProfile lateral = new TrapezoidalMotionProfile(100, 0, 120, 100);
        Telemetry.Item pos = this.telemetry.addData("pos", 0);

        while (!Thread.currentThread().isInterrupted()) {
            Pose relativePose = this.odometry.getRelativePose();
            double distance = relativePose.distanceTo(new Pose(0, 0, 0, AngleUnit.RADIANS));
            pos.setValue(relativePose.getX() + "   " + relativePose.getY());
            this.driver.setVelocity(new Vector3D(lateral.computeVelocity(distance), 0, 0));
            telemetry.update();
            this.odometry.update();
        }
    }
}
