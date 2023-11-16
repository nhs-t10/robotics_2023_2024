package centerstage.auto;

import centerstage.Constants;
import centerstage.SpikePosition;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.motor.CarWheels;
import com.pocolifo.robobase.vision.ColorFilterBoundingBoxPipeline;
import com.pocolifo.robobase.vision.EdgeDetection;
import com.pocolifo.robobase.vision.Webcam;
import com.pocolifo.robobase.vision.apriltag.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;

import static centerstage.Constants.ROBOT;

public class ParkingAuto extends AutonomousOpMode {
    private CarWheels carWheels;

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
    }

    @Override
    public void run() {
        System.out.println("Beginning Movement");
        carWheels.driveOmni(1,0,0);
        sleep(2000);
        carWheels.driveOmni(0,0,0);
        carWheels.close();
        System.out.println("Completed Movement");
    }
}
