package centerstage.auto;

import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.vision.EdgeDetection;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Webcam Testing " + BuildProperties.VERSION)
public class WebcamTestingAuto extends AutonomousOpMode {

    Webcam webcam;

    /**
     *
     */
    @Override
    public void initialize() {
        webcam = new Webcam(hardwareMap, "Webcam");

        webcam.open(new EdgeDetection());
//
//        webcam.getPipeline().getResult();
    }

    /**
     *
     */
    @Override
    public void run() {
        System.out.println(webcam.getPipeline().getResult());
    }
}
