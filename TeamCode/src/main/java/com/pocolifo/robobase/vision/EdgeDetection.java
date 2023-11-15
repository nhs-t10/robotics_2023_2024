package com.pocolifo.robobase.vision;

import centerstage.Constants;
import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.robotcore.external.Const;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import centerstage.SpikePosition;

public class EdgeDetection extends AbstractResultCvPipeline<SpikePosition> {
    private final Scalar ycbcrMin;
    private final Scalar ycbcrMax;

    // These variables are used only in `processFrame`. They CAN NOT be declared locally.
    // Under the hood, OpenCV allocates memory for Mat every time `processFrame` is called.
    // Therefore, they must be initialized once in the instance-level.
    private ArrayList<MatOfPoint> contours;
    private Mat convertedToycbcr;
    private Mat matchedPixels;
    private Mat hierarchy;
    private MatOfPoint biggestContour;
    private Mat _tempMask;

    public EdgeDetection(Scalar min, Scalar max) {
        this.ycbcrMin = min;
        this.ycbcrMax = max;
    }

    @Override
    public void init() {
        contours = new ArrayList<>();
        convertedToycbcr = new Mat();
        matchedPixels = new Mat();
        hierarchy = new Mat();
        biggestContour = null;
        _tempMask = null;
    }

    /**
     * @param input the frame to be processed
     * @return the processed frame
     */
    @Override
    public synchronized Mat processFrame(Mat input) {
        if(_tempMask == null)
            _tempMask = Mat.ones(input.size(), CvType.CV_8UC1);
        //convert the input to ycbcr, which is better for analysis than the default bgr.
        Imgproc.cvtColor(input, convertedToycbcr, Imgproc.COLOR_BGR2YCrCb);//Imgproc uses YCrCb, correct naming is YCbCr

        //filter the image to ONLY redish pixels
        Core.inRange(convertedToycbcr, ycbcrMin, ycbcrMax, matchedPixels);
        Core.copyTo(matchedPixels, input, _tempMask);

        //Find the biggest blob of reddish pixels.
        //It likes using a list of Matrices of points instead of something more simple, but that's ok.
        Imgproc.findContours(matchedPixels, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        //FeatureManager.logger.log("Contour Count: " + contours.size());
        //only bother continuing if there were any contours found
        System.out.println("Contours: " + contours.size());
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

        System.out.println("Biggest Contours");
        if (biggestContour == null) return input;

        Rect biggestContourRect = Imgproc.boundingRect(biggestContour);

        Imgproc.rectangle(
                input, // Buffer to draw on
                new Point(biggestContourRect.x, biggestContourRect.y), // First point which defines the rectangle
                new Point(biggestContourRect.x + biggestContourRect.width, biggestContourRect.y + biggestContourRect.height), // Second point which defines the rectangle
                new Scalar(0.5, 255, 0), // The color the rectangle is drawn in
                2); // Thickness of the rectangle lines


        int largeBlobCenterX = biggestContourRect.x + (biggestContourRect.width / 2);
        int inputWidth = input.width();

        final int leftCenterX = inputWidth / 3 + Constants.CAMERA_X_EDGE_DETECTION_OFFSET;
        final int centerRightX = (inputWidth * 2) / 3 + Constants.CAMERA_X_EDGE_DETECTION_OFFSET;

        Imgproc.line(input, new Point(leftCenterX, 0), new Point(leftCenterX, input.rows()), new Scalar(128, 0, 0), 2); //    >|<   |     //
        Imgproc.line(input, new Point(centerRightX, 0), new Point(centerRightX, input.rows()), new Scalar(128, 0, 0), 2); //     |   >|<    //

        //depending on which third the blob's center falls into, report the result position.
        if (largeBlobCenterX < leftCenterX) {
            result = SpikePosition.LEFT;
        } else if (largeBlobCenterX < centerRightX) {
            result = SpikePosition.CENTER;
        } else {
            result = SpikePosition.RIGHT;
        }

        Imgproc.putText(input,
                result.name(),
                new Point(0, 0),
                0,
                50,
                new Scalar(0, 128, 128));

        return input;
    }
}
