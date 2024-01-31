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

@TeleOp(name = "Driving + Slides")
public class DriveWithSlidesTeleop extends TeleOpOpMode {
    private CarWheels carWheels;
    @Hardware(name = "OR", ticksPerRevolution = Constants.MOTOR_TICK_COUNT)
    public DcMotorEx slideRightMotor;

    @Hardware(name = "OL", ticksPerRevolution = Constants.MOTOR_TICK_COUNT)
    public DcMotorEx slideLeftMotor;

    private RobotCapabilities capabilities;
    private Toggleable isMicroMovement;
    private boolean lastPressingTrigger;

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
        this.capabilities = new RobotCapabilities(slideRightMotor, slideLeftMotor);
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

        boolean useMicroMovement = this.isMicroMovement.processUpdates().get();
        float microMovementValue = useMicroMovement ? 1 : 4;

        this.carWheels.driveOmni(
                this.gamepad1.left_stick_y / microMovementValue * -1,
                this.gamepad1.left_stick_x / microMovementValue * -1,
                this.gamepad1.right_stick_x / microMovementValue * -1
        );
    }
}
