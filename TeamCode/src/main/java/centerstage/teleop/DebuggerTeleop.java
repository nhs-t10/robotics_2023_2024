package centerstage.teleop;

import static centerstage.Constants.ROBOT;

import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.GamepadCarWheels;
import com.pocolifo.robobase.dashboard.HTTPServer;
import com.pocolifo.robobase.dashboard.input.IntegerInput;
import com.pocolifo.robobase.dashboard.output.Output;
import com.pocolifo.robobase.dashboard.range.IntegerRange;
import com.pocolifo.robobase.motor.CarWheels;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.IOException;

import centerstage.Constants;

@TeleOp(name = "Robot Debugger")
public class DebuggerTeleop extends TeleOpOpMode {
    private CarWheels carWheels;
    private GamepadCarWheels gamepadCarWheels;
    private HTTPServer server;
    private IntegerInput i;

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
    public void start() {
        try {
            i = new IntegerInput("Speed Divisor Micro", 4, new IntegerRange(2, 10));
            new Output("Test5", () -> "Yoo it worked!");
            server = new HTTPServer();
            server.startOnSeparateThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loop() {
        if (this.gamepad1.a) {
            this.carWheels.driveOmni(i.getValue() / 10d, 0, 0);
        }
    }

    @Override
    public void stop() {
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
