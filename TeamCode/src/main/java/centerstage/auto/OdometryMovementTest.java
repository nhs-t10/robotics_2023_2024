package centerstage.auto;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import com.pocolifo.robobase.virtualfield.DistanceMovement;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.virtualfield.OdometryUpdater;

@Autonomous
public class OdometryMovementTest extends AutonomousOpMode {
    private CenterStageRobotConfiguration c;
    private NovelOdometry odometry;
    private NovelMecanumDriver driver;
    private DistanceMovement movement;
    private OdometryUpdater updater;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.driver = this.c.createDriver(Constants.Coefficients.SOFTWARE_ROBOT_COEFFICIENTS);
        this.odometry = this.c.createOdometry();
        this.movement = new DistanceMovement(driver, odometry, c.imu);
        this.updater = new OdometryUpdater(odometry);
    }

    @Override
    public void run() {
        updater.start();

        this.c.fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.c.fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.c.br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.c.bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        movement.rotate(180);

        System.out.println("ive moveddd");

        this.driver.stop();
    }
}
