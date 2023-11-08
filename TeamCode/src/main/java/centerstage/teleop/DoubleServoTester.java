package centerstage.teleop;

import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.Toggleable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "DoubleServoTester " + BuildProperties.VERSION)
public class DoubleServoTester extends TeleOpOpMode {
    private Toggleable RunServo;
    boolean running;
    private CRServo Servo1;
    private CRServo Servo2;

    @Override
    public void initialize() {
        this.RunServo = new Toggleable(() -> this.gamepad1.a);

//        this.intakeServo = hardwareMap.get(CRServo.class, "intakeServo");

        this.Servo1 = hardwareMap.get(CRServo.class,"Servo1");
        this.Servo1.setDirection(DcMotorSimple.Direction.FORWARD);
        this.Servo2 = hardwareMap.get(CRServo.class,"Servo2");
        this.Servo2.setDirection(DcMotorSimple.Direction.FORWARD);
        this.running = false;
    }

    @Override
    public void loop() {
        boolean runServoState = RunServo.processUpdates().get();
        if (runServoState) {
            Servo1.setPower(0.5);
            Servo2.setPower(0.5);
        } else {
            Servo1.setPower(0);
            Servo2.setPower(0);
        }
        /*
        if (RunServo.get()) {
            running = !running;
            System.out.println("A button Pressed!");
            if (running) {
                Servo1.setPower(0.5);
                Servo2.setPower(0.5);
            } else {
                Servo1.setPower(0);
                Servo2.setPower(0);
            }
            */
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
