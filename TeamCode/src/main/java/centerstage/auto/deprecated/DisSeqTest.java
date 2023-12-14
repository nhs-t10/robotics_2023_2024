package centerstage.auto.deprecated;

import android.renderscript.RSIllegalArgumentException;
import centerstage.Constants;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.motor.CarWheels;
import com.pocolifo.robobase.movement.DisplacementSequence;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static centerstage.Constants.ROBOT;
import static com.pocolifo.robobase.utils.UnitUtils.inchesToCm;

@Autonomous(name = "kafka")
public class DisSeqTest extends AutonomousOpMode {
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
        displacementSequence.add(0, inchesToCm(59));
        displacementSequence.add(inchesToCm(75), 0);
        displacementSequence.add(0, inchesToCm(-73));
        displacementSequence.add(inchesToCm(250), 0);

        this.carWheels.follow(displacementSequence, 0.5);
        this.carWheels.follow(displacementSequence.reverse(), 0.5);
    }
}
