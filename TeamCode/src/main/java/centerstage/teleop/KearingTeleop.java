package centerstage.teleop;

import centerstage.Constants;
import centerstage.RobotCapabilities;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.GamepadCarWheels;
import com.pocolifo.robobase.control.Pressable;
import com.pocolifo.robobase.control.Toggleable;
import com.pocolifo.robobase.motor.CarWheels;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import static centerstage.Constants.ROBOT;

@TeleOp(name = "Kearing v2")
public class KearingTeleop extends TeleOpOpMode {
    private CarWheels carWheels;
    private GamepadCarWheels gamepadCarWheels;

    @Hardware(name = "Lift", ticksPerRevolution = Constants.MOTOR_TICK_COUNT)
    public DcMotorEx liftMotor;

    @Hardware(name = "AirplaneLauncher")
    public CRServo airplaneLauncher;

    @Hardware(name = "PixelDropper")
    public CRServo pixelDropper;

    @Hardware(name = "ClawGrip")
    public CRServo clawGrip;

    @Hardware(name = "ClawRotation")
    public CRServo clawRotation;
    private RobotCapabilities capabilities;
    private Toggleable isGripping;
    private Pressable launchAirplane;
    private Pressable moveToDropPosition;
    private Pressable moveToCollectPosition;
    private Toggleable isMicroMovement;
    private boolean lastPressingTrigger;
    private boolean lastPressingBumper;
    private Pressable dropAutoPixel;

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

        this.isMicroMovement = new Toggleable(() -> this.gamepad1.x);
        this.capabilities = new RobotCapabilities(clawGrip, clawRotation, airplaneLauncher, liftMotor, pixelDropper);
        this.isGripping = new Toggleable(() -> this.gamepad1.y, true);
        this.launchAirplane = new Pressable(() -> this.gamepad1.b);
        this.moveToDropPosition = new Pressable(() -> this.gamepad1.right_stick_button);
        this.moveToCollectPosition = new Pressable(() -> this.gamepad1.left_stick_button);
        this.dropAutoPixel = new Pressable(() -> this.gamepad1.a);
    }

    @Override
    public void loop() {
        if (this.gamepad1.right_trigger > 0) {
            this.capabilities.upLift(this.gamepad1.right_trigger);
            this.lastPressingTrigger = true;
        } else if (this.gamepad1.left_trigger > 0) {
            this.capabilities.downLift(this.gamepad1.left_trigger);
            this.lastPressingTrigger = true;
        } else if (this.lastPressingTrigger) {
            this.capabilities.stopLift();
            this.lastPressingTrigger = false;
        }

        if (this.gamepad1.right_bumper) {
            this.capabilities.rotateForward();
            this.lastPressingBumper = true;
        } else if (this.gamepad1.left_bumper) {
            this.capabilities.rotateBackward();
            this.lastPressingBumper = true;
        } else if (this.lastPressingBumper) {
            this.capabilities.stopRotating();
            this.lastPressingBumper = false;
        }

        this.isGripping
                .onToggleOff(() -> this.capabilities.openGrip())
                .onToggleOn(() -> this.capabilities.closeGrip())
                .processUpdates();

        if (this.launchAirplane.get()) {
            this.capabilities.launchAirplane();
        }

        if (this.moveToCollectPosition.get()) {
            this.capabilities.moveToCollectPosition();
        }

        if (this.moveToDropPosition.get()) {
            this.capabilities.moveToDropPosition();
        }

        if (this.dropAutoPixel.get()) {
            this.capabilities.dropAutoPixel();
        }

        boolean useMicroMovement = this.isMicroMovement.processUpdates().get();
        float microMovementValue = useMicroMovement ? 1 : 4;

        this.carWheels.driveOmni(
                this.gamepad1.left_stick_y / microMovementValue * -1,
                this.gamepad1.left_stick_x / microMovementValue * -1,
                this.gamepad1.right_stick_x / microMovementValue * -1
        );
    }
}
