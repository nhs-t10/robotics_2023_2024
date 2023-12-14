package centerstage.auto;

import centerstage.Constants;
import centerstage.SpikePosition;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.StartSide;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.motor.CarWheels;
import com.pocolifo.robobase.vision.NovelYCrCbDetection;
import com.pocolifo.robobase.vision.Webcam;
import roadrunner.drive.RRInterface;

import static centerstage.Constants.ROBOT;

public class BaseProductionAuto extends AutonomousOpMode {
    private final NovelYCrCbDetection spikeDetector;
    private final Alliance alliance;
    @Hardware(name = "Webcam")
    public Webcam webcam;
    private final StartSide startSide;
    private RRInterface rr;
    private Pose2d startPose;

    public BaseProductionAuto(NovelYCrCbDetection spikeDetector, Alliance alliance, StartSide startSide) {
        this.spikeDetector = spikeDetector;
        this.alliance = alliance;
        this.startSide = startSide;
    }

    @Override
    public void initialize() {
        this.webcam.open(this.spikeDetector);
        this.rr = new RRInterface(hardwareMap);
        this.startPose = new Pose2d(0, 0);
    }

    @Override
    public void run() {
        SpikePosition result = ((NovelYCrCbDetection) this.webcam.getPipeline()).getResult();

        this.rr.trajectoryBuilder(this.startPose).forward(20);
    }

//    public void moveToTargetPosition(SpikePosition spikePosition) {
//        carWheels.drive(50, false);
//        switch (spikePosition) {
//            case RIGHT:
//                carWheels.rotateClockwise(90, 0.5);
//                break;
//            case LEFT:
//                carWheels.rotateCounterclockwise(90, 0.5);
//                break;
//        }
//        //TODO: Drop Pixel Here
//
//        if (this.startSide == StartSide.BACKDROP_SIDE) {
//            switch (alliance) {
//                case RED:
//                    carWheels.drive(50, true);
//                    break;
//                case BLUE:
//                    carWheels.drive(-50, true);
//                    break;
//            }
//        }
//    }
}
