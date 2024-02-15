package centerstage.auto;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous
public class AbsoluteRotationTesting extends AutonomousOpMode {
    private CenterStageRobotConfiguration c;
    private NovelOdometry odometry;
    private NovelMecanumDriver driver;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.odometry = this.c.createOdometry();
        this.driver = this.c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
    }

    @Override
    public void run() {
        double target = this.getAngle() + 90;

        while (Math.abs(this.getAngle() - target) > 1) {
            this.driver.setVelocity(new Vector3D(
                    0, 0, (this.getAngle() - target)
            ));
        }

        this.driver.stop();
    }

    public double getAngle() {
        return c.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }
}
