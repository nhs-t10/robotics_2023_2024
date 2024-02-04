package centerstage.auto;

import android.annotation.SuppressLint;
import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
public class CounterOdometry extends AutonomousOpMode {
    private CenterStageRobotConfiguration c;
    private NovelOdometry odometry;
    private NovelMecanumDrive driver;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.odometry = this.c.createOdometry(new Pose2d(0, 0, 0));
        this.driver = this.c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
    }

    @Override
    public void run() {

        this.driver.setVelocity(
                new Vector3D(
                        -this.odometry.getX(),
                        -this.odometry.getY(),
                        -(this.odometry.getHeading() * Constants.Robot.ROBOT_DIAMETER_IN)
                )
        );
        this.odometry.update();
    }
}
