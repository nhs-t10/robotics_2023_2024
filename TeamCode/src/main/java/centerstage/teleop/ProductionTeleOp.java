package centerstage.teleop;

import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.GamepadCarWheels;
import com.pocolifo.robobase.control.Toggleable;
import com.pocolifo.robobase.motor.CarWheels;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

import static centerstage.Constants.ROBOT;

@TeleOp(name = "Production " + BuildProperties.VERSION)
public class ProductionTeleOp extends TeleOpOpMode {
    private CarWheels carWheels;
    private GamepadCarWheels gamepadCarWheels;
    private Toggleable isMicroMovementActive;
    private CRServo pivotServo1;
    private CRServo pivotServo2;

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

        this.gamepadCarWheels = new GamepadCarWheels(this.carWheels, this.gamepad1);
        this.isMicroMovementActive = new Toggleable(() -> this.gamepad1.x);
        this.pivotServo1 = this.hardwareMap.get(CRServo.class, "Servo1");
        this.pivotServo2 = this.hardwareMap.get(CRServo.class, "Servo2");
    }

    @Override
    public void loop() {
        this.gamepadCarWheels.update(this.isMicroMovementActive.processUpdates().get());

        if (this.gamepad1.dpad_up) {
            this.pivotServo1.setPower(0.5);
            this.pivotServo2.setPower(0.5);
        } else if (this.gamepad1.dpad_down) {
            this.pivotServo1.setPower(-0.5);
            this.pivotServo2.setPower(-0.5);
        } else {
            this.pivotServo1.setPower(0);
            this.pivotServo2.setPower(0);
        }
    }
}
