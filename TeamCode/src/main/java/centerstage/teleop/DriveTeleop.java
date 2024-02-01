package centerstage.teleop;

import centerstage.Constants;
import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.Robot;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.GamepadCarWheels;
import com.pocolifo.robobase.control.Toggleable;
import com.pocolifo.robobase.motor.CarWheels;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Config
@TeleOp
public class DriveTeleop extends TeleOpOpMode {
    private CarWheels carWheels;
    private GamepadCarWheels g;
    public static double fl = -1, fr = 1, bl = -1, br = 1;

    @Override
    public void initialize() {
        this.carWheels = new CarWheels(
                hardwareMap,
                Constants.MOTOR_TICK_COUNT,
                9.6d,
                new Robot(
                        -1,
                        -1,
                        -1,
                        -1,
                        null,
                        false,
                        new OmniDriveCoefficients(new double[] {fl, fr, bl, br})
                ),
                "FL",
                "FR",
                "BL",
                "BR",
                "FL"
        );

        this.g = new GamepadCarWheels(this.carWheels, this.gamepad1, () -> this.gamepad1.x);
    }

    @Override
    public void loop() {
        this.g.update();
    }
}
