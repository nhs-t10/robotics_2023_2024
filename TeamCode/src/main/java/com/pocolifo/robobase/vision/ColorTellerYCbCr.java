package com.pocolifo.robobase.vision;


import static com.pocolifo.robobase.vision.RegionBasedAverage.BLUE;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ColorTellerYCbCr extends AbstractResultCvPipeline<ColorTellerYCbCr.ycbcrResult> {
    private static final Point TopLeftAnchorPoint = new Point(300, 318); //Base Picture is 600 horizontalCm 480 when taken on the robot.
    private static final int REGION_WIDTH = 20; //1cm
    private static final int REGION_HEIGHT = 20; //1cm
    private static final Point BottomRightAnchorPoint = new Point(TopLeftAnchorPoint.x + REGION_WIDTH, TopLeftAnchorPoint.y + REGION_HEIGHT);

    // Working variables. Because of memory concerns, we're not allowed to make ANY non-primitive variables within the `processFrame` method.
    //Mat is what you see
    Mat ycbcr = new Mat(), Region_Cr = new Mat(), Region_Cb = new Mat(), Cr = new Mat(), Cb = new Mat();

    private void inputToCr(Mat input) {
        Imgproc.cvtColor(input, ycbcr, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(ycbcr, Cr, 1);
    }

    private void inputToCb(Mat input) {
        Imgproc.cvtColor(input, ycbcr, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(ycbcr, Cb, 2);
    }

    private void drawGrid(int width, int height, Mat input) {
        final Scalar GREEN = new Scalar(0, 255, 0);
        final Scalar RED = new Scalar(255, 0, 0);
        Point TopLeftThing = new Point(0, 0);
        Point BottomRightThing = new Point(width, 0);

        for (int currentHeight = 0; currentHeight < height; currentHeight += 20) {
            TopLeftThing.y = currentHeight;
            BottomRightThing.y = currentHeight;

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    TopLeftThing, // First point which defines the rectangle
                    BottomRightThing, // Second point which defines the rectangle
                    currentHeight % 100 == 0 ? RED : GREEN, // The color the rectangle is drawn in
                    1); // Thickness of the rectangle lines
        }

        TopLeftThing.x = 0;
        TopLeftThing.y = 0;
        BottomRightThing.x = 0;
        BottomRightThing.y = height;

        for (int currentWidth = 0; currentWidth < width; currentWidth += 20) {
            TopLeftThing.x = currentWidth;
            BottomRightThing.x = currentWidth;

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    TopLeftThing, // First point which defines the rectangle
                    BottomRightThing, // Second point which defines the rectangle
                    currentWidth % 100 == 0 ? RED : GREEN, // The color the rectangle is drawn in
                    1); // Thickness of the rectangle lines
        }
    }

    @Override
    public void init(Mat firstFrame) {
        inputToCr(firstFrame);
        Region_Cr = Cr.submat(new Rect(TopLeftAnchorPoint, BottomRightAnchorPoint));
        inputToCb(firstFrame);
        Region_Cb = Cb.submat(new Rect(TopLeftAnchorPoint, BottomRightAnchorPoint));
    }

    @Override
    public Mat processFrame(Mat input) {
        inputToCr(input);
        inputToCb(input);

        result = new ycbcrResult();
        result.cr = (int) Core.mean(Region_Cr).val[0];
        result.cb = (int) Core.mean(Region_Cb).val[0];

        drawGrid(640, 480, input);

        Imgproc.rectangle(
                input, // Buffer to draw on
                TopLeftAnchorPoint, // First point which defines the rectangle
                BottomRightAnchorPoint, // Second point which defines the rectangle
                BLUE, // The color the rectangle is drawn in
                2); // Thickness of the rectangle lines

        return input;
    }

    @Override
    public ycbcrResult getResult()
    {
        return result;
    }

    @Override
    public void init() {}

    public static class ycbcrResult {
        public int cr;
        public int cb;
    }
}