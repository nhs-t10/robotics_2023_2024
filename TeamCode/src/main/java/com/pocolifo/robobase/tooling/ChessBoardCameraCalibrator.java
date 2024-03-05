package com.pocolifo.robobase.tooling;

import android.graphics.Canvas;
import centerstage.Constants;
import com.pocolifo.robobase.bootstrap.AutonomousOpMode;
import com.pocolifo.robobase.bootstrap.Hardware;
import com.pocolifo.robobase.utils.RobotConfiguration;
import com.pocolifo.robobase.vision.Webcam;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

@Autonomous(group = "robobase")
public class ChessBoardCameraCalibrator extends AutonomousOpMode {
    private VisionPortal visionPortal;
    private ChessBoardHardwareConfig c;

    @Override
    public void initialize() {
        int cameraMonitorView = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        this.c = new ChessBoardHardwareConfig(this.hardwareMap);
        this.visionPortal = new VisionPortal.Builder()
                .setCameraResolution(new android.util.Size(Constants.Webcam.CAMERA_RES_WIDTH, Constants.Webcam.CAMERA_RES_HEIGHT))
                .setCamera(c.webcam.webcamDevice)
                .setAutoStopLiveView(true)
                .setLiveViewContainerId(cameraMonitorView)
                .enableLiveView(true)
                .addProcessor(new ChessBoardProcessor())
                .build();
    }

    @Override
    public void run() {

    }

    public static class ChessBoardHardwareConfig extends RobotConfiguration {
        public ChessBoardHardwareConfig(HardwareMap hardwareMap) {
            super(hardwareMap);
        }

        @Hardware(name = "Webcam")
        public Webcam webcam;
    }

    public static class ChessBoardProcessor implements VisionProcessor {
        private static final Size patternSize = new Size(7, 6);
        private static final Size winSize = new Size(11, 11);
        private static final Size zeroZone = new Size(-1, -1);
        private final TermCriteria termCriteria = new TermCriteria(new double[]{
                TermCriteria.MAX_ITER + TermCriteria.EPS, 30, 0.001
        });
        private static final int sampleCount = 20;
        private Mat colorConverted;
        private List<Mat> objectPoints;
        private List<Mat> imagePoints;

        @Override
        public void init(int width, int height, CameraCalibration calibration) {
            this.colorConverted = new Mat();
            this.objectPoints = new ArrayList<>();
            this.imagePoints = new ArrayList<>();
        }

        @Override
        public Object processFrame(Mat frame, long captureTimeNanos) {
            if (this.objectPoints.size() > sampleCount && this.imagePoints.size() > sampleCount) {
                Imgproc.cvtColor(frame, this.colorConverted, Imgproc.COLOR_BGR2GRAY);

                MatOfPoint2f chessBoard = new MatOfPoint2f();
                boolean foundCorners = Calib3d.findChessboardCorners(this.colorConverted, patternSize, chessBoard);

                if (foundCorners) {
                    Mat corners = new Mat();
                    Imgproc.cornerSubPix(this.colorConverted, corners, winSize, zeroZone, termCriteria);
                    this.imagePoints.add(corners);
                    // TODO: draw chessboard
                } else {
                    chessBoard.release();
                }
            } else {
//                Calib3d.calibrateCamera(this.objectPoints, this.imagePoints, )
            }

            return frame;
        }

        @Override
        public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

        }
    }
}
