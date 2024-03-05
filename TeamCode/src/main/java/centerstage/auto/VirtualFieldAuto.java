package centerstage.auto;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import centerstage.RobotCapabilities;
import centerstage.SpikePosition;
import centerstage.StartSide;
import centerstage.vision.DynamicYCrCbDetection;

import com.acmerobotics.dashboard.config.Config;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.novel.hardware.NovelOdometry;
import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;
import com.pocolifo.robobase.utils.Alliance;
import com.pocolifo.robobase.virtualfield.OdometryUpdater;
import com.pocolifo.robobase.virtualfield.VirtualField;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.IOException;

@Autonomous
@Config
public class VirtualFieldAuto extends AutonomousOpMode {
    private CenterStageRobotConfiguration c;
    private RobotCapabilities capabilities;
    private NovelMecanumDriver driver;
    private DynamicYCrCbDetection spikeDetector;
    private Alliance alliance = Alliance.RED;
    private StartSide startSide = StartSide.APRIL_TAG_SIDE;
    private VirtualField virtualField;
    private OdometryUpdater updater;

    @Override
    public void initialize() {
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
        this.capabilities = new RobotCapabilities(this.c);
        this.driver = this.c.createDriver(Constants.Coefficients.PRODUCTION_COEFFICIENTS);
        this.spikeDetector = new DynamicYCrCbDetection(alliance);
        this.c.webcam.open(spikeDetector);
        NovelOdometry odometry = c.createOdometry();
        try {
            this.virtualField = new VirtualField(driver, odometry, c, getStartPosition());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.updater = new OdometryUpdater(odometry);
    }

    private Vector3D getStartPosition() {
        if (alliance == Alliance.RED && startSide == StartSide.APRIL_TAG_SIDE) {
            return new Vector3D(133, 33, 0);
        }
        if (alliance == Alliance.RED && startSide == StartSide.BACKDROP_SIDE) {
            return new Vector3D(133, 86, 0);
        }
        if (alliance == Alliance.BLUE && startSide == StartSide.APRIL_TAG_SIDE) {
            return new Vector3D(9, 33, 0);
        }
        if (alliance == Alliance.BLUE && startSide == StartSide.BACKDROP_SIDE) {
            return new Vector3D(9, 86, 0);
        }
        return Vector3D.ZERO;
    }

    private Vector3D getSpikePlacementPosition() {
        if (alliance == Alliance.RED && startSide == StartSide.APRIL_TAG_SIDE) {
            return new Vector3D(101, 36, 0);
        }
        if (alliance == Alliance.RED && startSide == StartSide.BACKDROP_SIDE) {
            return new Vector3D(101, 83, 0);
        }
        if (alliance == Alliance.BLUE && startSide == StartSide.APRIL_TAG_SIDE) {
            return new Vector3D(42, 36, 0);
        }
        if (alliance == Alliance.BLUE && startSide == StartSide.BACKDROP_SIDE) {
            return new Vector3D(42, 83, 0);
        }
        return Vector3D.ZERO;
    }

    private Vector3D getParkPosition() {
        if (alliance == Alliance.RED && startSide == StartSide.APRIL_TAG_SIDE) {
            return new Vector3D(133, 134, 0);
        }
        if (alliance == Alliance.RED && startSide == StartSide.BACKDROP_SIDE) {
            return new Vector3D(133, 134, 0);
        }
        if (alliance == Alliance.BLUE && startSide == StartSide.APRIL_TAG_SIDE) {
            return new Vector3D(9, 134, 0);
        }
        if (alliance == Alliance.BLUE && startSide == StartSide.BACKDROP_SIDE) {
            return new Vector3D(9, 134, 0);
        }
        return Vector3D.ZERO;
    }

    private Vector3D getAfterSpikePosition() {
        if (alliance == Alliance.RED && startSide == StartSide.APRIL_TAG_SIDE) {
            return new Vector3D(90, 35, 0);
        }
        if (alliance == Alliance.RED && startSide == StartSide.BACKDROP_SIDE) {
            return new Vector3D(90, 84, 0);
        }
        if (alliance == Alliance.BLUE && startSide == StartSide.APRIL_TAG_SIDE) {
            return new Vector3D(52, 35, 0);
        }
        if (alliance == Alliance.BLUE && startSide == StartSide.BACKDROP_SIDE) {
            return new Vector3D(52, 84, 0);
        }
        return Vector3D.ZERO;
    }

    @Override
    public void run() {
        updater.start();

        try {
            SpikePosition spikePosition = getSpikePosition();

            virtualField.pathTo(getSpikePlacementPosition());

            placeSpike(spikePosition);

            virtualField.pathTo(getParkPosition());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void placeSpike(SpikePosition position) throws InterruptedException {
        switch (position) {
            case LEFT:
                virtualField.rotateTo(90);
                dropSpike();
                virtualField.rotateTo(0);
                break;
            case RIGHT:
                virtualField.rotateTo(-90);
                dropSpike();
                virtualField.rotateTo(0);
                break;
            case CENTER:
                virtualField.pathTo(getAfterSpikePosition());
                dropSpike();
        }
    }

    private void dropSpike() throws InterruptedException {
        capabilities.runRoller(1);
        sleep(500);
        capabilities.runRoller(0);
    }

    private SpikePosition getSpikePosition() throws InterruptedException {
        DynamicYCrCbDetection pipeline = (DynamicYCrCbDetection) this.c.webcam.getPipeline();
        SpikePosition spikePosition;
        do {
            spikePosition = pipeline.getResult();
            sleep(100);
        } while (spikePosition == null);
        return spikePosition;
    }
}