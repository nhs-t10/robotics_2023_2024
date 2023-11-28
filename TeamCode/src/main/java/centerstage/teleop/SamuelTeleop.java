package centerstage.teleop;

import centerstage.Constants;
import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.GamepadCarWheels;
import com.pocolifo.robobase.control.Toggleable;
import com.pocolifo.robobase.motor.CarWheels;
import com.pocolifo.robobase.motor.Motor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import static centerstage.Constants.ROBOT;

@TeleOp(name = "Samuel " + BuildProperties.VERSION)
public class SamuelTeleop extends TeleOpOpMode {
    private CarWheels carWheels;
    private GamepadCarWheels gamepadCarWheels;
    private Toggleable isClawOpen;

    @Hardware(name = "Chain", ticksPerRevolution = Constants.MOTOR_TICK_COUNT)
    private Motor chainMotor;

    @Hardware(name = "Claw")
    private Servo clawServo;

    @Override
    public void initialize() {
        this.carWheels = new CarWheels(
                hardwareMap,
                1120,
                9.6d,
                ROBOT,
                "FL",
                "FR",
                "BL",
                "BR",
                "FL"
        );

        this.gamepadCarWheels = new GamepadCarWheels(
                this.carWheels,
                this.gamepad1,
                () -> this.gamepad1.a
        );
        this.isClawOpen = new Toggleable(() -> this.gamepad1.x);
    }

    @Override
    public void loop() {
        this.gamepadCarWheels.update();

        if (this.gamepad1.dpad_up) {
            this.chainMotor.drive(0.75);
        } else if (this.gamepad1.dpad_down) {
            this.chainMotor.drive(-0.75);
        } else {
            this.chainMotor.drive(0);
        }

        this.isClawOpen
                .processUpdates()
                .onToggleOn(() -> {
                    this.clawServo.setPosition(1);
                    this.clawServo.getController().pwmEnable();
                })
                .onToggleOff(() -> {
                    this.clawServo.setPosition(0);
                    this.clawServo.getController().pwmDisable();
                });
    }
}
