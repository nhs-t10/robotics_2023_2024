package centerstage.teleop;

import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.pocolifo.robobase.control.Toggleable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "DoubleMotorTester " + BuildProperties.VERSION)
public class DoubleMotorTester extends TeleOpOpMode {
    private Toggleable RunMotor;
    private DcMotor Motor1;
    private DcMotor Motor2;

    @Override
    public void initialize() {


        this.RunMotor = new Toggleable(() -> this.gamepad1.a);

//        this.intakeServo = hardwareMap.get(CRServo.class, "intakeServo");

        this.Motor1 = hardwareMap.get(DcMotor.class,"Motor1");

        this.Motor2 = hardwareMap.get(DcMotor.class,"Motor2");
    }

    @Override
    public void loop() {
        if(RunMotor.get())
        {
            Motor1.setPower(1);
            Motor2.setPower(-1);
        }
        else {
            Motor1.setPower(0);
            Motor2.setPower(0);
        }
        RunMotor.processUpdates();
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
