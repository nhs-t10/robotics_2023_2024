package centerstage.auto;

import centerstage.Constants;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.movement.Path;
import com.pocolifo.robobase.movement.mecanum.MecanumDrive;
import com.pocolifo.robobase.movement.mecanum.MecanumWheels;
import com.pocolifo.robobase.reconstructor.LocalizationEngine;
import com.pocolifo.robobase.reconstructor.VirtualField;
import com.pocolifo.robobase.vision.NovelYCrCbDetection;
import com.pocolifo.robobase.vision.Webcam;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

import java.io.IOException;

public class BaseProductionAuto extends AutonomousOpMode {
    private final NovelYCrCbDetection edgeDetection;
    private final Alliance alliance;

    @Hardware(name = "Webcam")
    private Webcam webcam;
    private MecanumDrive driver;
    private VirtualField virtualField;
    private LocalizationEngine localizationEngine;

    public BaseProductionAuto(NovelYCrCbDetection edgeDetection, Alliance alliance) {
        this.edgeDetection = edgeDetection;
        this.alliance = alliance;
    }

    @Override
    public void initialize() {
        this.driver = Constants.createDriver(this);
        this.localizationEngine = new LocalizationEngine(this.imu, this.webcam.webcamDevice);
        this.localizationEngine.calibrateAprilTags(this.driver, 10);

        try {
            this.virtualField = new VirtualField("/centerstage.vf");
        } catch (IOException e) {
            System.out.println("[FATAL] Failed to load the virtual field!");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Position positionEstimate = this.localizationEngine.getPositionEstimate();

        Path path = new Path(VirtualField.optimizePath(this.virtualField.findPath(
                this.virtualField.getNearestPoint(positionEstimate.x, positionEstimate.y),
                this.virtualField.getNearestPoint(-0.929292, 1.251)
        )));

        this.driver.follow(path, 1);
    }
}
