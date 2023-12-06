package centerstage.teleop;

import static centerstage.Constants.ROBOT;

import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.GamepadCarWheels;
import com.pocolifo.robobase.control.Toggleable;
import com.pocolifo.robobase.motor.CarWheels;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "DebugTester " + BuildProperties.VERSION)
public class DebugTester extends TeleOpOpMode {
    GamepadCarWheels gamepadCarWheels;
    CarWheels carWheels;
    @Override
    public void initialize() {

        this.carWheels = new CarWheels(
                hardwareMap,
                1120,
                10d,
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
        telemetry.addData("Value", gamepad1.left_stick_x);
        telemetry.update();
    }
}
