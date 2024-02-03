package centerstage.auto;

import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.vision.ConvolutionalColorDetection;
import com.pocolifo.robobase.vision.Webcam;

public class ConvolutionalDetectionTesting extends AutonomousOpMode {
    @Hardware(name = "Webcam")
    public Webcam webcam;

    public ConvolutionalColorDetection convolutionalColorDetection;
    @Override
    public void initialize() {
        System.out.println("init start");
        convolutionalColorDetection = new ConvolutionalColorDetection(2);
        webcam.open(convolutionalColorDetection);
        System.out.println("init end");
    }

    @Override
    public void run() {
        System.out.println("run start");
        while (true) {
            System.out.println(webcam.getPipeline().getResult());
        }
    }

    @Override
    public void stop() {
        try {
            webcam.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("run stop");
        throw new RuntimeException("stopstopstop");
    }
}
