package centerstage.auto;

import com.pocolifo.robobase.BuildProperties;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.vision.ColorTellerYCbCr;
import com.pocolifo.robobase.vision.ColorTellerYCbCr;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Color Tell " + BuildProperties.VERSION)
public class GetColorYCbCr extends AutonomousOpMode {

    private Webcam webcam;

    /**
     *
     */
    @Override
    public void initialize() {
        webcam = new Webcam(hardwareMap, "Webcam");

        webcam.open(new ColorTellerYCbCr());
    }

    /**
     *
     */
    @Override
    public void run() {
        telemetry.addLine("running");
        telemetry.update();
        for(int i = 0; i < 30; i++) {
        ColorTellerYCbCr.ycbcrResult result = ((ColorTellerYCbCr) webcam.getPipeline()).getResult();
            if (result != null) {
                telemetry.addData("Cr: ", result.cr);
                telemetry.addData("Cb: ", result.cb);
                telemetry.update();
            } else {
                telemetry.addLine("Null");
                telemetry.update();
            }
            sleep(1000);
        }
    }
}
