package centerstage.auto;

import centerstage.Constants;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.motor.CarWheels;
import com.pocolifo.robobase.movement.DisplacementSequence;
import com.pocolifo.robobase.reconstructor.PathFinder;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static centerstage.Constants.ROBOT;
import static com.pocolifo.robobase.utils.UnitUtils.inchesToCm;

import java.io.IOException;
import java.util.List;

@Autonomous(name = "Pathfinder")
public class PathfinderTest extends AutonomousOpMode {
    @Hardware(name = "Webcam")
    public Webcam webcam;

    private CarWheels carWheels;

    @Override
    public void initialize() {
        this.carWheels = new CarWheels(
                hardwareMap,
                Constants.MOTOR_TICK_COUNT,
                9.6,
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
        DisplacementSequence displacementSequence = new DisplacementSequence();
        try {
            PathFinder pathFinder = new PathFinder("points.txt");
            List<PathFinder.Point> points = pathFinder.findPath(new PathFinder.Point(20, 20), new PathFinder.Point(100, 100));
            for (PathFinder.Point point : points) {
                displacementSequence.add(point.getX(), point.getY());
            }

            this.carWheels.follow(displacementSequence, 0.5);
            this.carWheels.follow(displacementSequence.reverse(), 0.5);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}