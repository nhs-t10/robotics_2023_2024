package centerstage.teleop;

import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.GamepadCarWheels;
import com.pocolifo.robobase.motor.CarWheels;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static centerstage.Constants.ROBOT;

@TeleOp(name = "TeleOp " + BuildProperties.VERSION)
public class TestingTeleOp extends TeleOpOpMode {
    private CarWheels carWheels;
    private GamepadCarWheels gamepadCarWheels;

    @Override
    public void initialize() {
        this.carWheels = new CarWheels(
                hardwareMap,
                1120,
                10d,
                ROBOT,
                "W-FL",
                "W-FR",
                "W-BL",
                "W-BR",
                "W-FL"
        );

        this.gamepadCarWheels = new GamepadCarWheels(this.carWheels, this.gamepad1);
    }

    @Override
    public void loop() {
        this.gamepadCarWheels.update(false);
    }
}
