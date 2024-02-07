package com.pocolifo.robobase.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import centerstage.SpikePosition;

public class DynamicYCrCbDetection extends AbstractResultCvPipeline<SpikePosition> {
    private Mat YCrCb;
    private final boolean checkForRed;


    public DynamicYCrCbDetection(boolean checkForRed) {
        this.checkForRed = checkForRed;
    }

    @Override
    public void init() {
        YCrCb = new Mat();
    }

    private double computeDynamicColorness(Mat mat) {
        int channelIndex = checkForRed ? 1 : 2; // Use 1 for Cr (red), 2 for Cb (blue)
        int oppositeIndex = checkForRed ? 2 : 1;

        // Split the YCrCb image into its individual channels
        List<Mat> channels = new ArrayList<>();
        Core.split(mat, channels);

        Mat channelData = channels.get(channelIndex);
        Mat oppositeData = channels.get(oppositeIndex);

        int count = 0;

        byte[] channelPixels = new byte[(int) (channelData.total() * channelData.channels())];
        channelData.get(0, 0, channelPixels);
        byte[] oppositePixels = new byte[(int) (oppositeData.total() * oppositeData.channels())];
        oppositeData.get(0, 0, oppositePixels);

        for (int i = 0; i < channelPixels.length; i++) {
            double pixelValue = channelPixels[i] & 0xFF; // Convert to unsigned
            double oppositeValue = oppositePixels[i] & 0xFF; // Convert to unsigned
            double DYNAMIC_THRESHOLD = 35;
            if (pixelValue > oppositeValue && pixelValue - oppositeValue > DYNAMIC_THRESHOLD) {
                count++;
            }
        }

        return count;
    }

    @Override
    public synchronized Mat processFrame(Mat input) {
        Mat temp = new Mat();
        Imgproc.cvtColor(input, temp, Imgproc.COLOR_BGRA2BGR);
        Imgproc.cvtColor(temp, YCrCb, Imgproc.COLOR_BGR2YCrCb);

        int cols = YCrCb.cols();
        int xEndLeftSide = cols / 3;
        int xStartCenter = xEndLeftSide;
        int xEndCenter = xStartCenter + xEndLeftSide;
        int xStartRightSide = xEndCenter;

        Mat leftSection = YCrCb.submat(0, YCrCb.rows(), 0, xEndLeftSide);
        Mat centerSection = YCrCb.submat(0, YCrCb.rows(), xStartCenter, xEndCenter);
        Mat rightSection = YCrCb.submat(0, YCrCb.rows(), xStartRightSide, cols);

        double leftColorness = computeDynamicColorness(leftSection);
        double centerColorness = computeDynamicColorness(centerSection);
        double rightColorness = computeDynamicColorness(rightSection);

        if (leftColorness > centerColorness && leftColorness > rightColorness) {
            result = SpikePosition.LEFT;
        } else if (centerColorness > leftColorness && centerColorness > rightColorness) {
            result = SpikePosition.CENTER;
        } else if (rightColorness > leftColorness && rightColorness > centerColorness) {
            result = SpikePosition.RIGHT; // Will return right if not found
        } else {
            result = SpikePosition.NOT_FOUND;
        }

        // Optional: Visualization and debugging
        // Draw dividing lines between sections

        Imgproc.line(input, new Point(xEndLeftSide, 0), new Point(xEndLeftSide, input.rows()), new Scalar(0, 255, 0), 2);
        Imgproc.line(input, new Point(xEndCenter, 0), new Point(xEndCenter, input.rows()), new Scalar(0, 255, 0), 2);

        return input;
    }
}
