package centerstage.teleop;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Odometry Tester")
public class OdometryTester extends TeleOpOpMode {
    private NovelOdometry odometry;
    private CenterStageRobotConfiguration c;
    private NovelMecanumDrive driver;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.driver = this.c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
        this.odometry = this.c.createOdometry(new Pose2d(0, 0, 0));
    }

    @Override
    public void loop() {
        telemetry.addData("X: ", odometry.getX());
        telemetry.addData("Y: ", odometry.getY());
        telemetry.addData("θ: ", odometry.getHeading());
        telemetry.addData("LW: ", odometry.leftWheel.getCurrentPosition());
        telemetry.addData("RW: ", odometry.rightWheel.getCurrentPosition());
        telemetry.addData("PW: ", odometry.perpendicularWheel.getCurrentPosition());

        telemetry.update();
        odometry.update();
        telemetry.clear();

        this.driver.useGamepad(this.gamepad1, 2);
    }
}
