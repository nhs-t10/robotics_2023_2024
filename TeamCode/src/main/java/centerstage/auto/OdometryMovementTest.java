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
    private CenterStag  eRobotConfiguration c;
    private NovelOdometry odometry;
    private NovelMecanumDriver driver;
    private DistanceMovement movement;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.driver = this.c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
        this.odometry = this.c.createOdometry();
        this.movement = new DistanceMovement(driver, odometry, c.imu);
    }

    @Override
    public void run() {
//        try {
            this.c.fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            this.c.fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            this.c.br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            this.c.bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

             movement.rotate(180);

//            Thread.sleep(2000);
//            movement.move(new Vector3D(0, 0, 990));
            System.out.println("ive moveddd");

            this.driver.stop();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
