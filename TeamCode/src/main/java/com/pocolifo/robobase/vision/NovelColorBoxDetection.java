package com.pocolifo.robobase.vision;

import centerstage.Constants;
import org.checkerframework.checker.units.qual.A;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import centerstage.SpikePosition;

public class NovelColorBoxDetection extends AbstractResultCvPipeline<SpikePosition> {
    private final double[] targetColor;

    // These variables are used only in `processFrame`. They CAN NOT be declared locally.
    // Under the hood, OpenCV allocates memory for Mat every time `processFrame` is called.
    // Therefore, they must be initialized once in the instance-level.
    private ArrayList<MatOfPoint> contours;
    private Mat cannyFilter;
    private Mat colorConverted;
    private Mat hierarchy;


    public NovelColorBoxDetection(double[] targetColor) {
        this.targetColor = targetColor;
    }

    @Override
    public void init() {
        contours = new ArrayList<>();
        cannyFilter = new Mat();
        colorConverted = new Mat();
        hierarchy = new Mat();
    }

    public static double getColorDistance(double[] color1, double[] color2) {
        double sum = 0;

        // assuming 3 color channels
        for (int i = 0; 3 > i; i++) {
            double channel1 = color1[i];
            double channel2 = color2[i];
            double diff = channel2 - channel1;
            sum += diff * diff;
        }

        return sum / (256 * 256);
    }

    public static int findMiddleIndex(double[] arr) {
        int maxOnesCount = 0;
        int maxOnesIndex = 0;
        int windowSize = 5; // Adjust window size as needed

        for (int i = 0; i < arr.length - windowSize + 1; i++) {
            int onesCount = 0;
            for (int j = i; j < i + windowSize; j++) {
                if (arr[j] >= 0.7) {
                    onesCount++;
                }
            }
            if (onesCount > maxOnesCount) {
                maxOnesCount = onesCount;
                maxOnesIndex = i + (windowSize / 2);
            }
        }

        return maxOnesIndex;
    }

    /**
     * @param input the frame to be processed
     * @return the processed frame
     */
    @Override
    public synchronized Mat processFrame(Mat input) {
//        Imgproc.cvtColor(input, colorConverted, Imgproc.COLOR_BGR2RGB);

        final int d = 18;
        int thirdsX = input.cols() / d;
        int thirdsY = input.rows() / d;
        double[][] similarities = new double[input.rows()][input.cols()];

        for (int x = 0; d > x; x++) {
            for (int y = 0; d > y; y++) {
                Mat mat = input.submat(
                        y*thirdsY, (y+1)*thirdsY,
                        x*thirdsX, (x+1)*thirdsX
                );
                Scalar mean = Core.mean(mat);
//                Imgproc.rectangle(input, new Rect(x*thirdsX, y*thirdsY, thirdsX, thirdsY), mean, 3);
                similarities[y][x] = getColorDistance(mean.val, targetColor);
            }
        }

        int[] rowAverages = new int[d];

        for (int i = 0; d > i; i++) {
            rowAverages[i] = findMiddleIndex(similarities[i]);
        }

        int x = average(rowAverages);

        final int leftCenterX = d / 3;
        final int centerRightX = (d / 3) * 2;

        //depending on which third the blob's center falls into, report the result position.
        if (x < leftCenterX) {
            result = SpikePosition.LEFT;
            Imgproc.rectangle(input, new Rect(0, 0, 50, 50), new Scalar(255, 0, 0));
        } else if (x < centerRightX) {
            result = SpikePosition.CENTER;
            Imgproc.rectangle(input, new Rect(50, 0, 50, 50), new Scalar(255, 0, 0));
        } else {
            result = SpikePosition.RIGHT;
            Imgproc.rectangle(input, new Rect(100, 0, 50, 50), new Scalar(255, 0, 0));
        }

        return input;
    }

    public static int average(int[] doubles) {
        int s = 0;
        for(int d : doubles) s +=d;
        return s / doubles.length;
    }
}
