package com.pocolifo.robobase.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import centerstage.SpikePosition;

public class EdgeDetection extends AbstractResultCvPipeline<SpikePosition> {
    private final Scalar ycrcbMin;
    private final Scalar ycrcbMax;

    // These variables are used only in `processFrame`. They CAN NOT be declared locally.
    // Under the hood, OpenCV allocates memory for Mat every time `processFrame` is called.
    // Therefore, they must be initialized once in the instance-level.
    private ArrayList<MatOfPoint> contours;
    private Mat convertedToYCrCb;
    private Mat matchedPixels;
    private Mat hierarchy;
    private MatOfPoint biggestContour;


    public EdgeDetection(Scalar min, Scalar max) {
        this.ycrcbMin = min;
        this.ycrcbMax = max;
    }

    @Override
    public void init() {
        contours = new ArrayList<>();
        convertedToYCrCb = new Mat();
        matchedPixels = new Mat();
        hierarchy = new Mat();
        biggestContour = null;

    }

    /**
     * @param input the frame to be processed
     * @return the processed frame
     */
    @Override
    public synchronized Mat processFrame(Mat input) {
        //convert the input to YCrCb, which is better for analysis than the default bgr.
        Imgproc.cvtColor(input, convertedToYCrCb, Imgproc.COLOR_BGR2YCrCb);

        //filter the image to ONLY redish pixels
        Core.inRange(convertedToYCrCb, ycrcbMin, ycrcbMax, matchedPixels);

        //Find the biggest blob of reddish pixels.
        //It likes using a list of Matrices of points instead of something more simple, but that's ok.
        Imgproc.findContours(matchedPixels, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        //FeatureManager.logger.log("Contour Count: " + contours.size());
        //only bother continuing if there were any contours found
        if (contours.size() == 0) return input;

        double biggestArea = -1;
        //look through the list and find the biggest contour
        for (MatOfPoint contour : contours) {
            double area = Imgproc.boundingRect(contour).area();
            if (area > biggestArea) {
                biggestArea = area;
                biggestContour = contour;
            }
        }

        if (biggestContour == null) return input;

        Rect biggestContourRect = Imgproc.boundingRect(biggestContour);

        Imgproc.rectangle(
                input, // Buffer to draw on
                new Point(biggestContourRect.x, biggestContourRect.y), // First point which defines the rectangle
                new Point(biggestContourRect.x + biggestContourRect.width, biggestContourRect.y + biggestContourRect.height), // Second point which defines the rectangle
                new Scalar(0.5, 255, 0), // The color the rectangle is drawn in
                2); // Thickness of the rectangle lines
        Imgproc.putText(input,
                position.toString(),
                new Point(0, 0),
                0,
                10,
                new Scalar(0, 128, 128));

        int largeBlobCenterX = biggestContourRect.x + (biggestContourRect.width / 2);
        int inputWidth = input.width();

        //depending on which third the blob's center falls into, report the result position.
        if (largeBlobCenterX < inputWidth / 3) {
            result = SpikePosition.LEFT;
        } else if (largeBlobCenterX < (inputWidth * 2) / 3) {
            result = SpikePosition.CENTER;
        } else {
            result = SpikePosition.RIGHT;
        }

        return input;
    }
}
