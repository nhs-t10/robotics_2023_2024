package centerstage.auto;

import static centerstage.Constants.ROBOT;

import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.motor.CarWheels;
import com.pocolifo.robobase.vision.Webcam;
import com.pocolifo.robobase.vision.apriltag.AprilTagDetectionPipeline;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.openftc.apriltag.AprilTagDetection;

import centerstage.Constants;
import centerstage.SpikePosition;

@Autonomous(name = "MotorTester " + BuildProperties.VERSION)
public class MotorTestingAuto extends AutonomousOpMode {

DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;

    @Override
    public void initialize() {
        fl = hardwareMap.get(DcMotor.class, "FL");
        fr = hardwareMap.get(DcMotor.class, "FR");
        br = hardwareMap.get(DcMotor.class, "BR");
        bl = hardwareMap.get(DcMotor.class, "BL");
    }

    @Override
    public void run()
    {
        fl.setPower(1);
        fr.setPower(1);
        bl.setPower(1);
        br.setPower(1);
    }
}
