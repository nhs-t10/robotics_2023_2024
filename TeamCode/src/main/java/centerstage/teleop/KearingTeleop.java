package centerstage.teleop;

import centerstage.Constants;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.GamepadCarWheels;
import com.pocolifo.robobase.control.OneTimeControl;
import com.pocolifo.robobase.control.Pressable;
import com.pocolifo.robobase.control.Toggleable;
import com.pocolifo.robobase.motor.CarWheels;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImpl;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import static centerstage.Constants.ROBOT;

@TeleOp(name = "Kearing v2")
public class KearingTeleop extends TeleOpOpMode {
    private CarWheels carWheels;
    private GamepadCarWheels gamepadCarWheels;
    @Hardware(name = "ClawOpening")
    public CRServo clawOpening;
    @Hardware(name = "ClawRotation")
    public CRServo clawRotation;
    @Hardware(name = "Lift", ticksPerRevolution = Constants.MOTOR_TICK_COUNT)
    public DcMotorEx liftMotor;
    @Hardware(name = "Airplane")
    public CRServo airplaneLauncher;
    private Toggleable isClawOpen;
    private Toggleable isClawUp;
    private Toggleable isAirplaneLaunched;

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

        this.isClawOpen = new Toggleable(() -> this.gamepad1.a);

        this.isClawUp = new Toggleable(() -> this.gamepad1.b);

        this.isAirplaneLaunched = new Toggleable(() -> this.gamepad1.y);
    }

    @Override
    public void loop() {
        this.gamepadCarWheels.update();

        this.isClawOpen
                .onToggleOn(() -> {
                    this.clawOpening.getController().pwmEnable();
                    this.clawOpening.setDirection(DcMotorSimple.Direction.FORWARD);
                    this.clawOpening.setPower(0.01);
                    sleep(1);
                    this.clawOpening.setPower(0);
                })
                .onToggleOff(() -> {
                    this.clawOpening.setDirection(DcMotorSimple.Direction.REVERSE);
                    this.clawOpening.setPower(0.01);
                    sleep(1);
                    this.clawOpening.setPower(0);
                    this.clawOpening.getController().pwmDisable();
                })
                .processUpdates();

        /*this.isClawUp
                .onToggleOn(() -> {
                    this.liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                    this.liftMotor.setVelocity(0.25 * Constants.LIFT_MOTOR_TICK_COUNT);
                    sleep(2000);
                    this.liftMotor.setPower(0);
                    //TODO Add claw rotation
                })
                .onToggleOff(() -> {
                    //TODO Add claw rotation
                    this.liftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                    this.liftMotor.setVelocity(0.25 * Constants.LIFT_MOTOR_TICK_COUNT);
                    sleep(2500);
                    this.liftMotor.setPower(0);
                })
                .processUpdates();*/

        if (gamepad1.dpad_up) {
            telemetry.addData("Up", 0);
            telemetry.log();
            this.liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            liftMotor.setPower(0.5);
        } else if (gamepad1.dpad_down) {
            telemetry.addData("Down", 0);
            telemetry.log();
            this.liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            liftMotor.setPower(-0.5);
        } else {
            liftMotor.setPower(0);
        }

        this.isAirplaneLaunched
                .onToggleOn(() -> {
                    //this.airplaneLauncher.getController().pwmEnable();
                    //this.airplaneLauncher.setDirection(DcMotorSimple.Direction.FORWARD);
                    this.airplaneLauncher.setPower(1);
                    System.out.println(this.airplaneLauncher.getPower());
                    sleep(1000);
                    this.airplaneLauncher.setPower(-1);
                    //this.airplaneLauncher.getController().pwmDisable();
                })
                .processUpdates();
    }
}
