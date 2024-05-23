package centerstage.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.bootstrap.TeleOpOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import centerstage.Constants;

@Config
@TeleOp
public class MotorTest extends TeleOpOpMode {

    DcMotor motor;
    @Override
    public void initialize() {
        motor = hardwareMap.get(DcMotor.class,"motor");
    }

    @Override
    public void loop() {
        if(gamepad1.a)
        {
            motor.setPower(1);
        }
        else if(gamepad1.b){
            motor.setPower(0);
        }
    }
}