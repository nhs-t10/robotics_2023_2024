package centerstage.teleop;

import centerstage.Constants;
import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.ControllableVar;
import com.pocolifo.robobase.control.GamepadCarWheels;
import com.pocolifo.robobase.control.Pressable;
import com.pocolifo.robobase.control.Toggleable;
import com.pocolifo.robobase.motor.CarWheels;
import com.pocolifo.robobase.motor.Motor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import static centerstage.Constants.ROBOT;

@TeleOp(name = "Production " + BuildProperties.VERSION)
public class ProductionTeleOp extends TeleOpOpMode {
    private CarWheels carWheels;
    private GamepadCarWheels gamepadCarWheels;
    private Motor spinningIntake;
    private Motor leftLinearSlide;
    private Motor rightLinearSlide;
    private CRServo outtakeServo;
    private CRServo rotationLeftServo;
    private CRServo rotationRightServo;
    private Toggleable isOuttakeFlapOpen;
    private Toggleable isMicroMovementActive;
    private Pressable backboardUp;
    private Pressable backboardDown;
    private double backboardPosition;

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
        this.isMicroMovementActive = new Toggleable(() -> this.gamepad1.a);
        this.isOuttakeFlapOpen = new Toggleable(() -> this.gamepad2.right_bumper);
        this.backboardUp = new Pressable(() -> this.gamepad2.x);
        this.backboardDown = new Pressable(() -> this.gamepad2.b);

        this.outtakeServo = this.hardwareMap.get(CRServo.class, "Outtake");
        this.rotationLeftServo = this.hardwareMap.get(CRServo.class, "RotationLeft");
        this.rotationRightServo = this.hardwareMap.get(CRServo.class, "RotationRight");

        this.spinningIntake = new Motor(this.hardwareMap.get(DcMotor.class, "SpinningIntake"), Constants.MOTOR_TICK_COUNT); // Port 0 Motor Expansion Hub
        this.spinningIntake.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.leftLinearSlide = new Motor(this.hardwareMap.get(DcMotor.class, "LeftLinearSlide"), Constants.MOTOR_TICK_COUNT); // Port 1 Motor Expansion Hub
        this.leftLinearSlide.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.rightLinearSlide = new Motor(this.hardwareMap.get(DcMotor.class, "RightLinearSlide"), Constants.MOTOR_TICK_COUNT); // Port 1 Motor Expansion Hub
        this.rightLinearSlide.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    @Override
    public void loop() {
        this.gamepadCarWheels.update(this.isMicroMovementActive.processUpdates().get());

        if (this.gamepad2.dpad_up) {
            this.leftLinearSlide.drive(1);
            this.rightLinearSlide.drive(-1);
        } else if (this.gamepad2.dpad_down) {
            this.leftLinearSlide.drive(-1);
            this.rightLinearSlide.drive(1);
        } else {
            this.leftLinearSlide.stopMoving();
            this.rightLinearSlide.stopMoving();
        }

        if (this.gamepad1.right_trigger > 0) {
            this.spinningIntake.drive(1);
        } else if (this.gamepad1.left_trigger > 0) {
            this.spinningIntake.drive(-1);
        } else {
            this.spinningIntake.stopMoving();
        }

        if (this.isOuttakeFlapOpen.processUpdates().get()) {
            this.outtakeServo.setPower(1.67);  // value determined via guess and check
        } else {
            this.outtakeServo.setPower(-0.56);  // value determined via guess and check
        }

        if (this.backboardUp.get()) {
            this.backboardPosition += 0.05;
        }

        if (this.backboardDown.get()) {
            this.backboardPosition -= 0.05;
        }

        this.rotationLeftServo.setPower(backboardPosition);
        this.rotationRightServo.setPower(-backboardPosition);
    }
}
