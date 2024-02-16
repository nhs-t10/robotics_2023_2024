package centerstage.auto;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import com.pocolifo.robobase.virtualfield.DistanceMovement;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

@Autonomous
public class OdometryMovementTest extends AutonomousOpMode {
    private CenterStageRobotConfiguration c;
    private NovelOdometry odometry;
    private NovelMecanumDriver driver;
    private DistanceMovement movement;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.odometry = this.c.createOdometry();
        this.driver = this.c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
        this.movement = new DistanceMovement(driver, odometry);
    }

    @Override
    public void run() {
        try {
            this.c.fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            this.c.fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            this.c.br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            this.c.bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            movement.move(new Vector3D(0, 0, 90));

            Thread.sleep(2);
            movement.move(new Vector3D(0, 0, 990));

            this.driver.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
