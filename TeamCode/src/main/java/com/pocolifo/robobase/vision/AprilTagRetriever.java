package com.pocolifo.robobase.vision;

import android.util.Size;
import centerstage.Constants;
import com.acmerobotics.dashboard.FtcDashboard;
import com.pocolifo.robobase.reconstructor.Pose;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.*;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

import java.util.List;

public class AprilTagRetriever {
    private final VisionPortal visionPortal;
    private final AprilTagProcessor aprilTagProcessor;
    public final AprilTagLibrary library;

    public AprilTagRetriever(Webcam webcam, AprilTagLibrary library) {
        int cameraMonitorView = webcam.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", webcam.hardwareMap.appContext.getPackageName());
        this.library = library;
        this.aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)  // inches and degrees match the april tag library metadata
                .setTagLibrary(this.library)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .build();
        this.visionPortal = new VisionPortal.Builder()
                .setCameraResolution(new Size(Constants.Webcam.CAMERA_RES_WIDTH, Constants.Webcam.CAMERA_RES_HEIGHT))
                .setCamera(webcam.webcamDevice)
                .setAutoStopLiveView(true)
                .setLiveViewContainerId(cameraMonitorView)
                .enableLiveView(true)
                .addProcessor(this.aprilTagProcessor)
                .build();
    }

    public List<AprilTagDetection> getDetections() {
        return this.aprilTagProcessor.getDetections();
    }
}
