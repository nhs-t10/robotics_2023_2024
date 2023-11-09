package centerstage.auto;

import static centerstage.Constants.ROBOT;

import centerstage.SpikePosition;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.motor.CarWheels;
import com.pocolifo.robobase.vision.EdgeDetection;
import com.pocolifo.robobase.vision.Webcam;

public class BaseProductionAuto extends AutonomousOpMode {
    private final EdgeDetection edgeDetection;

    private CarWheels carWheels;
    private Webcam webcam;

    public BaseProductionAuto(EdgeDetection edgeDetection) {
        this.edgeDetection = edgeDetection;
    }

    @Override
    public void initialize() {
        this.webcam = new Webcam(this.hardwareMap, "Webcam");
        this.webcam.open(this.edgeDetection);

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
        System.out.println("Running edge detection");

        try {
            while (this.edgeDetection.getResult() == SpikePosition.NOT_FOUND || this.edgeDetection.getResult() == null) {
                this.sleep(500);
                System.out.println("Finding spikes...");
            }

            System.out.println("Detected");
            System.out.println(this.edgeDetection.getResult());

            switch (this.edgeDetection.getResult()) {
                case RIGHT:
                    carWheels.drive(35, false);
                    carWheels.rotateClockwise(90, 0.25);
                    break;

                case LEFT:
                    carWheels.drive(35, false);
                    carWheels.rotateCounterclockwise(90, 0.25);
                    break;

                case CENTER:
                    carWheels.drive(35, false);
                    break;
            }

            System.out.println("Closing!");
            this.webcam.close();

        } catch (InterruptedException ignored) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
