package centerstage.teleop;

import centerstage.Constants;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.GamepadCarWheels;
import com.pocolifo.robobase.control.Toggleable;
import com.pocolifo.robobase.motor.CarWheels;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static centerstage.Constants.ROBOT;

@TeleOp(name = "Kearing v1")
public class KearingTeleop extends TeleOpOpMode {
    private CarWheels carWheels;
    private GamepadCarWheels gamepadCarWheels;

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

        this.gamepadCarWheels = new GamepadCarWheels(this.carWheels, this.gamepad1, () -> this.gamepad1.x);
    }

    @Override
    public void loop() {
        this.gamepadCarWheels.update();
    }
}
