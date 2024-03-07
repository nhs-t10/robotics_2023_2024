package com.pocolifo.robobase.vision;

import com.pocolifo.robobase.Alliance;
import centerstage.SpikePosition;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import centerstage.SpikePosition;

public class SpotDetectionPipeline extends AbstractResultCvPipeline<Integer> {
    Alliance alliance;

    // Colors Used to Display Detection Results
    static final Scalar BLUE = new Scalar(0, 0, 255);
    static final Scalar GREEN = new Scalar(0, 255, 0);
    static final Scalar BLACK = new Scalar(0, 0, 0);

    // Points Used to Determine Detection Bounds
    static final Point SPOT1_TOPLEFT_ANCHOR_POINT = new Point(0, 190);
    static final Point SPOT1_BOTTOMRIGHT_ANCHOR_POINT = new Point(30, 220);
    static final Point SPOT2_TOPLEFT_ANCHOR_POINT = new Point(305, 160);
    static final Point SPOT2_BOTTOMRIGHT_ANCHOR_POINT = new Point(335, 190);
    static final Point SPOT3_TOPLEFT_ANCHOR_POINT = new Point(610, 190);
    static final Point SPOT3_BOTTOMRIGHT_ANCHOR_POINT = new Point(640, 220);

    // Mats Used to Extract Color & Region Data from Input Frame
    Mat region1_Cr, region2_Cr, region3_Cr, region1_Cb, region2_Cb, region3_Cb;
    private final Mat ycbcr = new Mat();
    private final Mat Cr = new Mat();
    private final Mat Cb = new Mat();
    int avg1, avg2, avg3;
    private volatile SpikePosition position = SpikePosition.LEFT;
    static final Point CALIBRATION_VERTEX_LEFT_1 = new Point(0, 260);
    static final Point CALIBRATION_VERTEX_LEFT_2 = new Point(40, 240);
    static final Point CALIBRATION_VERTEX_CENTER_1 = new Point(360, 250);
    static final Point CALIBRATION_VERTEX_CENTER_2 = new Point(280, 250);
    static final Point CALIBRATION_VERTEX_RIGHT_1 = new Point(610, 250);
    static final Point CALIBRATION_VERTEX_RIGHT_2 = new Point(640, 270);
    public SpotDetectionPipeline(Alliance alliance) {
        this.alliance = alliance;
    }
    void extractInputColorChannels(Mat input) {
        // Extract Input Into Respective Color Channel Mats
        Imgproc.cvtColor(input, ycbcr, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(ycbcr, Cr, 1);
        Core.extractChannel(ycbcr, Cb, 2);
    }

    void extractRegionColorChannels() {
        // Extract Specific Region Colors from Larger Input Mats
        region1_Cr = Cr.submat(new Rect(SPOT1_TOPLEFT_ANCHOR_POINT, SPOT1_BOTTOMRIGHT_ANCHOR_POINT));
        region2_Cr = Cr.submat(new Rect(SPOT2_TOPLEFT_ANCHOR_POINT, SPOT2_BOTTOMRIGHT_ANCHOR_POINT));
        region3_Cr = Cr.submat(new Rect(SPOT3_TOPLEFT_ANCHOR_POINT, SPOT3_BOTTOMRIGHT_ANCHOR_POINT));
        region1_Cb = Cb.submat(new Rect(SPOT1_TOPLEFT_ANCHOR_POINT, SPOT1_BOTTOMRIGHT_ANCHOR_POINT));
        region2_Cb = Cb.submat(new Rect(SPOT2_TOPLEFT_ANCHOR_POINT, SPOT2_BOTTOMRIGHT_ANCHOR_POINT));
        region3_Cb = Cb.submat(new Rect(SPOT3_TOPLEFT_ANCHOR_POINT, SPOT3_BOTTOMRIGHT_ANCHOR_POINT));
    }

    @Override
    public void init(Mat firstFrame) {
        extractInputColorChannels(firstFrame);
        extractRegionColorChannels();
    }

    /**
     * @author Arlan and Chlohal and Eli
     * @param input Mat to be processed
     * @return Processed Mat
     */
    @Override
    public Mat processFrame(Mat input) {
        extractInputColorChannels(input);
        //extractRegionColorChannels();

        // Compute Difference Between Red and Blue Average Pixel Value for Each Region
        avg1 = (int) Core.mean(region1_Cr).val[0] - (int) Core.mean(region1_Cb).val[0];
        avg2 = (int) Core.mean(region2_Cr).val[0] - (int) Core.mean(region2_Cb).val[0];
        avg3 = (int) Core.mean(region3_Cr).val[0] - (int) Core.mean(region3_Cb).val[0];

        // Invert Average Values if Looking for Blue Pixels
        if (alliance == Alliance.BLUE) {
            avg1 *= -1;
            avg2 *= -1;
            avg3 *= -1;
        }

        // Draws Rectangles As Visual Aid for User
        Imgproc.rectangle(
                input, // Buffer to draw on
                SPOT1_TOPLEFT_ANCHOR_POINT, // First point which defines the rectangle
                SPOT1_BOTTOMRIGHT_ANCHOR_POINT, // Second point which defines the rectangle
                BLUE, // The color the rectangle is drawn in
                2); // Thickness of the rectangle lines
        Imgproc.rectangle(
                input,
                SPOT2_TOPLEFT_ANCHOR_POINT,
                SPOT2_BOTTOMRIGHT_ANCHOR_POINT,
                BLUE,
                2);
        Imgproc.rectangle(
                input,
                SPOT3_TOPLEFT_ANCHOR_POINT,
                SPOT3_BOTTOMRIGHT_ANCHOR_POINT,
                BLUE,
                2);


        // Find the max of the 3 averages
        if (avg1 > avg2 && avg1 > avg3) {
            position = SpikePosition.LEFT; // Record our analysis

            // Draws Solid Green Square as Visual Aid of Detected Location
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    SPOT1_TOPLEFT_ANCHOR_POINT, // First point which defines the rectangle
                    SPOT1_BOTTOMRIGHT_ANCHOR_POINT, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill
        } else if (avg2 > avg3) {
            position = SpikePosition.CENTER;

            Imgproc.rectangle(
                    input,
                    SPOT2_TOPLEFT_ANCHOR_POINT,
                    SPOT2_BOTTOMRIGHT_ANCHOR_POINT,
                    GREEN,
                    -1);
        } else {
            position = SpikePosition.RIGHT;

            Imgproc.rectangle(
                    input,
                    SPOT3_TOPLEFT_ANCHOR_POINT,
                    SPOT3_BOTTOMRIGHT_ANCHOR_POINT,
                    GREEN,
                    -1);
        }

        Imgproc.line(input,
                CALIBRATION_VERTEX_LEFT_1,
                CALIBRATION_VERTEX_LEFT_2,
                BLACK,
                2);

        Imgproc.line(input,
                CALIBRATION_VERTEX_CENTER_1,
                CALIBRATION_VERTEX_CENTER_2,
                BLACK,
                2);

        Imgproc.line(input,
                CALIBRATION_VERTEX_RIGHT_1,
                CALIBRATION_VERTEX_RIGHT_2,
                BLACK,
                2);

        // Render Edited Image (w/ Visual Aids) to Phone Camera Stream
        return input;
    }

    @Override
    public Integer getResult() {
        result =  position.ordinal();
        return super.getResult();
    }

    @Override
    public void init() {

    }
}
