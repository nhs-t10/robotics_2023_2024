package centerstage.auto;

import centerstage.SpikePosition;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.vision.EdgeDetection;
import com.pocolifo.robobase.vision.Webcam;

public class BaseProductionAuto extends AutonomousOpMode {
    private final EdgeDetection edgeDetection;
    private Webcam webcam;

    public BaseProductionAuto(EdgeDetection edgeDetection) {
        this.edgeDetection = edgeDetection;
    }

    @Override
    public void initialize() {
        this.webcam = new Webcam(this.hardwareMap, "Webcam");
        this.webcam.open(this.edgeDetection);
    }

    @Override
    public void run() {
        try {
            while (this.edgeDetection.getResult() == SpikePosition.NOT_FOUND) {
                Thread.sleep(500);
                System.out.println("Finding spikes...");
            }

            this.webcam.close();

            System.out.println("Detected");
            System.out.println(this.edgeDetection.getResult());
        } catch (InterruptedException ignored) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
