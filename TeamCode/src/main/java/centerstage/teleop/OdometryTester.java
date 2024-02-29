package centerstage.teleop;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Odometry Tester")
public class OdometryTester extends TeleOpOpMode {
    private NovelOdometry odometry;
    private CenterStageRobotConfiguration c;
    private NovelMecanumDriver driver;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.driver = this.c.createDriver(Constants.Coefficients.SOFTWARE_ROBOT_COEFFICIENTS);
        this.odometry = this.c.createOdometry();
    }

    @Override
    public void loop() {
        telemetry.addData("X: ",  odometry.getRelativePose().getX());
        telemetry.addData("Y: ",  odometry.getRelativePose().getY());
        telemetry.addData("θ: ",  odometry.getRelativePose().getHeading(AngleUnit.RADIANS));
        telemetry.addData("LW: ", odometry.leftEncoder.getCurrentTicks());
        telemetry.addData("RW: ", odometry.rightEncoder.getCurrentTicks());
        telemetry.addData("PW: ", odometry.perpendicularEncoder.getCurrentTicks());

        telemetry.update();
        odometry.update();
        telemetry.clear();

        this.driver.useGamepad(this.gamepad1, 2);
    }
}
