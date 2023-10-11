package centerstage.auto;

import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.vision.ColorTellerYCrCb;
import com.pocolifo.robobase.vision.EdgeDetection;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import centerstage.SpikePosition;

@Autonomous(name = "Color Auto " + BuildProperties.VERSION)
public class GetColorYCrCb extends AutonomousOpMode {

    private Webcam webcam;

    /**
     *
     */
    @Override
    public void initialize() {
        webcam = new Webcam(hardwareMap, "Webcam");

        webcam.open(new ColorTellerYCrCb());
    }

    /**
     *
     */
    @Override
    public void run() {
//        ColorTellerYCrCb.YCrCbResult result = ((ColorTellerYCrCb) webcam.getPipeline()).getResult();
//        if (result != null) {
//            System.out.printf("Result: %d %d%n", result.cr, result.cb);
    }
}
