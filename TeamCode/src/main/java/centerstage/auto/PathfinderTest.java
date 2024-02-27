package centerstage.auto;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.virtualfield.VirtualField;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.IOException;

@Config
@Autonomous(name = "Pathfinder")
public class PathfinderTest extends AutonomousOpMode {
    private CenterStageRobotConfiguration c;
    private NovelOdometry odometry;
    private NovelMecanumDriver driver;
    private VirtualField virtualField;

    private final Vector3D startPosition = new Vector3D(10, 10, 0);

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.odometry = this.c.createOdometry();
        this.driver = this.c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
        try {
            virtualField = new VirtualField(driver, odometry, c, startPosition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        this.c.fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.c.fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.c.br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.c.bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        virtualField.pathTo(10, 0);

        driver.stop();
    }
}