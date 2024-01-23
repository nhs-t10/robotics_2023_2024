package centerstage.teleop;

import centerstage.Constants;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.motor.CarWheels;
import com.pocolifo.robobase.novel.Odometry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static centerstage.Constants.ROBOT;

@TeleOp(name = "Odometry Tester")
public class OdometryTester extends TeleOpOpMode {
    private CarWheels carWheels;
    private Odometry odometry;

    @Override
    public void initialize() {
        this.carWheels = new CarWheels(
                hardwareMap,
                Constants.MOTOR_TICK_COUNT,
                9.6d,
                ROBOT,
                "FL",
                "FR",
                "BL",
                "BR",
                "FL"
        );

        this.odometry = new Odometry(hardwareMap, new  Pose2d(0, 0,0),
                "OR",
                "OL",
                "OT");
    }

    @Override
    public void loop() {
        telemetry.addData("X: ", odometry.getX());
        telemetry.addData("Y: ", odometry.getY());
        telemetry.addData("θ: ", odometry.getRotation());
        telemetry.addData("LW: ", odometry.leftWheel.getCurrentPosition());
        telemetry.update();
        odometry.update();
        telemetry.clear();

        this.carWheels.driveOmni(
                this.gamepad1.left_stick_y * -1 / 2,
                this.gamepad1.left_stick_x * -1 / 2,
                this.gamepad1.right_stick_x * -1 / 2
        );
    }
}
