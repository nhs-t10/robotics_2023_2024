package centerstage.auto;

import centerstage.CenterStageRobotConfiguration;
import centerstage.Constants;
import centerstage.RobotCapabilities;
import centerstage.SpikePosition;
import com.pocolifo.robobase.Alliance;
import com.pocolifo.robobase.StartSide;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.motor.NovelMotor;
import com.pocolifo.robobase.motor.OmniDriveCoefficients;
import com.pocolifo.robobase.novel.NovelMecanumDrive;
import com.pocolifo.robobase.vision.NovelYCrCbDetection;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class BaseProductionAuto extends AutonomousOpMode {
    private final CenterStageRobotConfiguration c;
    private RobotCapabilities capabilities;
    private final NovelYCrCbDetection spikeDetector;
    private final Alliance alliance;
    private final StartSide startSide;
    private NovelMecanumDrive driver;

    public BaseProductionAuto(NovelYCrCbDetection spikeDetector, Alliance alliance, StartSide startSide) {
        this.spikeDetector = spikeDetector;
        this.alliance = alliance;
        this.startSide = startSide;
        this.c = new CenterStageRobotConfiguration(this.hardwareMap);
    }

    @Override
    public void initialize() {
        this.c.webcam.open(this.spikeDetector);
        this.driver = new NovelMecanumDrive(this.c.fl, this.c.fr, this.c.bl, this.c.br, new OmniDriveCoefficients(new double[]{1, 1, -1, 1}));
    }

    @Override
    public void run() {
        try {
            NovelYCrCbDetection pipeline = (NovelYCrCbDetection) this.c.webcam.getPipeline();
            SpikePosition spikePosition;

            do {
                spikePosition = pipeline.getResult();
                sleep(100);
            } while (spikePosition == null);

            this.c.webcam.close();

            switch (spikePosition) {
                case LEFT:
                    driveVertical(-26, 2);
                    sleep(500);
                    driveHorizontal(-16, 1);
                    break;

                case RIGHT:
                    driveVertical(-26, 2);
                    sleep(500);
                    driveHorizontal(16, 1);
                    break;

                case CENTER:
                    driveVertical(-34, 2.5);
                    break;
            }

//            this.capabilities.dropAutoPixel();

            switch (spikePosition) {
                case LEFT:
                    driveHorizontal(16, 1);
                    break;

                case RIGHT:
                    driveHorizontal(-16, 1);
                    break;

                case CENTER:
                    driveVertical(8, 0.5);
                    break;
            }

            sleep(500);

            driveHorizontal((10 + startSide.getSideSwapConstantIn()) * alliance.getAllianceSwapConstant(), 1.5+(startSide.getSideSwapConstantIn()/16));
            sleep(500);

            rotate(90* alliance.getAllianceSwapConstant(),2);

            //todo: scan!!!

            rotate(180,4);

            //todo: place!!!

//            if (this.startSide == StartSide.BACKDROP_SIDE) {
//                driveHorizontal();
//            }

        } catch (Throwable e) {
            System.out.println("Stopped");
        }

//        //todo: check this is properly aligned after going back
//        switch(spikePosition) {
//            case LEFT:
//                driveHorizontal(16,1);
//                break;
//
//            case RIGHT:
//                driveHorizontal(-16,1);
//                break;
//            case CENTER:
//                driveVertical(7,1);
//                break;
//        }
//        sleep(500);
//        rotate(90*allianceMultConstant,1);
//        sleep(500);
//        if(startSide == StartSide.BACKDROP_SIDE) {
//            driveVertical(80, 6);
//        }
//        else {
//            driveVertical(40, 3);
//        }
//        //todo: place pixels!
//        if(startSide == StartSide.BACKDROP_SIDE)
//        {
//            driveHorizontal(24*allianceMultConstant, 2);
//        }
    }

    public void driveVertical(double inches, double time) throws InterruptedException {
        this.driver.setVelocity(new Vector3D(inches / time, 0, 0));

        sleep((long) (time * 1000L));

        this.driver.stop();
    }

    public void driveHorizontal(double inches, double time) throws InterruptedException {
        this.driver.setVelocity(new Vector3D(0, inches / time, 0));

        sleep((long) (time * 1000L));

        this.driver.stop();
    }
    public void rotate(double degrees, double time) throws InterruptedException {
        //If you've done circular motion, this is velocity = omega times radius. Otherwise, look up circular motion velocity to angular velocity
        this.driver.setVelocity(new Vector3D(0,0,
                (Math.toRadians(degrees) * (Constants.ROBOT_DIAMETER_IN)/time)));
        sleep((long)time*1000);
        this.driver.stop();
    }
}
