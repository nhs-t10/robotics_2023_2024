package centerstage.teleop;

import static centerstage.Constants.ROBOT;

import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.GamepadCarWheels;
import com.pocolifo.robobase.control.Pressable;
import com.pocolifo.robobase.control.Toggleable;
import com.pocolifo.robobase.motor.CarWheels;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.*;

@TeleOp(name = "TeleOp " + BuildProperties.VERSION)
public class TestingTeleOp extends TeleOpOpMode {
    private CarWheels carWheels;
    private GamepadCarWheels gamepadCarWheels;
    private Toggleable useMicroMovement;
    private Pressable intake;
    private Pressable outtake;
    private CRServo intakeServo;

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

        this.useMicroMovement = new Toggleable(() -> this.gamepad1.a);

//        this.intakeServo = hardwareMap.get(CRServo.class, "intakeServo");

        this.intake = new Pressable(() -> this.gamepad1.b);

        this.outtake = new Pressable(() -> this.gamepad1.x);
    }

    @Override
    public void loop() {
        this.gamepadCarWheels.update(this.useMicroMovement.get());
        useMicroMovement.processUpdates();
//        if (this.intake.get()) {
//            intakeServo.setDirection(DcMotorSimple.Direction.FORWARD);
//            intakeServo.setPower(0.2);
//        } else if (this.outtake.get()) {
//            intakeServo.setDirection(DcMotorSimple.Direction.REVERSE);
//            intakeServo.setPower(0.2);
//        } else {
//            intakeServo.setPower(0);
//        }
    }
}
