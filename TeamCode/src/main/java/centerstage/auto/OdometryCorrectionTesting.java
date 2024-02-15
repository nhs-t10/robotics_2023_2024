package centerstage.auto;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.reconstructor.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous
public class OdometryCorrectionTesting extends AutonomousOpMode {
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
        this.c.fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.c.fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.c.br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.c.bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        long end = System.currentTimeMillis() + 2000;

        while (!this.isStopRequested() && end > System.currentTimeMillis()) {
            this.odometry.update();
        }

        this.c.fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.c.fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.c.br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.c.bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Pose pose = this.odometry.getRelativePose();

        this.driver.setVelocity(
                new Vector3D(
                        -pose.getX(),
                        -pose.getY(),
                        -(pose.getHeading(AngleUnit.RADIANS) * Constants.Robot.ROBOT_DIAMETER_IN)
                )
        );

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.driver.stop();
    }
}
