package centerstage.teleop;

import static centerstage.Constants.ROBOT;


import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.GamepadCarWheels;
import com.pocolifo.robobase.control.Pressable;
import com.pocolifo.robobase.control.Toggleable;
import com.pocolifo.robobase.control.input.InputManager;
import com.pocolifo.robobase.control.input.nodeUtils.PriorityAsyncOpmodeComponent;
import com.pocolifo.robobase.control.input.nodes.ButtonNode;
import com.pocolifo.robobase.control.input.nodes.GradualStickNode;
import com.pocolifo.robobase.control.input.nodes.InversionNode;
import com.pocolifo.robobase.control.input.nodes.JoystickNode;
import com.pocolifo.robobase.control.input.nodes.MultiInputNode;
import com.pocolifo.robobase.control.input.nodes.MultiplyNode;
import com.pocolifo.robobase.control.input.nodes.PlusNode;
import com.pocolifo.robobase.control.input.nodes.SwitchNode;
import com.pocolifo.robobase.control.input.nodes.ToggleNode;
import com.pocolifo.robobase.motor.CarWheels;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;


//@TeleOp(name = "TeleOp " + BuildProperties.VERSION)
public class ThreadTester extends TeleOpOpMode {
    private CarWheels carWheels;
    private GamepadCarWheels gamepadCarWheels;
    private InputManager input;
    private Toggleable useMicroMovement;
    private Pressable pressTest;
    private Pressable intake;
    private Pressable outtake;
    private CRServo intakeServo;

    @Override
    @Deprecated
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
        this.pressTest = new Pressable(() -> this.gamepad1.b);
        input.registerInput("a1", new ButtonNode("a"));
        input.registerInput("b1", new ButtonNode("b"));
        input.registerInput("x1", new ButtonNode("x"));
        input.registerInput("y1", new ButtonNode("y"));
//        this.intakeServo = hardwareMap.get(CRServo.class, "intakeServo");


        input.registerInput("drivingControls",
                new PlusNode(
                        new MultiInputNode(
                                new JoystickNode("left_stick_y"),
                                new JoystickNode("left_stick_x"),
                                new JoystickNode("right_stick_x")
                        ),
                        new MultiInputNode(
                                        new JoystickNode("gamepad2left_stick_y"),
                                        new JoystickNode("gamepad2left_stick_x"),
                                        new JoystickNode("gamepad2right_stick_x")

                        )
                )
        );

        PriorityAsyncOpmodeComponent.start(() -> {
            if(input.getBool("x1"))
            {
                System.out.println();
                System.out.println("Button, threading");
            }
            if (input.getBool("y1")) {
                System.out.println();
                System.out.println("Togglable,  threading");
            }
        });
    }

    @Override
    @Deprecated
    public void loop() {
        try{
        input.update();
        carWheels.driveOmni(input.getFloatArrayOfInput("drivingControls"));
        useMicroMovement.processUpdates();
        System.out.println(pressTest.get());
        System.out.println("Pressable, sans threading");
        this.gamepadCarWheels.update(this.useMicroMovement.get());
        }
        catch (Throwable t) {
            System.out.println(t.toString());
            StackTraceElement[] e = t.getStackTrace();
            for (int i = 0; i < 3 && i < e.length; i++) {
                System.out.println(e[i].toString());
            }
            input.setIsOpModeRunning(false);
            telemetry.update();
        }
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
