package centerstage.auto;

import com.pocolifo.robobase.RobotConfiguration;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class PipelineTesterConfig extends RobotConfiguration {
    @Hardware(name = "Webcam")
    public Webcam webcam;

    public PipelineTesterConfig(HardwareMap hardwareMap) {
        super(hardwareMap);
    }
}
