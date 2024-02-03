package com.pocolifo.robobase.vision;

import centerstage.SpikePosition;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ConvolutionalColorDetection extends AbstractResultCvPipeline<SpikePosition> {
    private final int targetColor;
    private int cutoffLeft;
    private int cutoffRight;


    // These variables are used only in `processFrame`. They CAN NOT be declared locally.
    // Under the hood, OpenCV allocates memory for Mat every time `processFrame` is called.
    // Therefore, they must be initialized once in the instance-level.
    public ConvolutionalColorDetection(int extractCol) {
        this.targetColor = extractCol;
    }

    @Override
    public void init() {}

    public void computeCutoffs(Mat input) {
        cutoffLeft = input.cols() / 3;
        cutoffRight = cutoffLeft * 2;
    }

    public double[][] computePixelColorness(Mat input) {
        double[][] output = new double[input.rows()][input.cols()];

        for (int x = 0; x < input.cols(); x++)
            for (int y = 0; y < input.rows(); y++)
                output[y][x] = input.get(y, x)[targetColor] - input.get(y, x)[2 - targetColor];

        return output;
    }

    public double[][] performConvolution(double[][] input) {
        double[][] output = new double[input.length - 4][input[0].length - 4];

        for (int x = 2; x < input[0].length - 2; x++) {
            for (int y = 2; y < input.length - 2; y++) {
                double sum = 0;
                for (int X = -2; X < 3; X++) for (int Y = -2; Y < 3; Y++) sum += input[y + Y][x + X];
                double temp = sum / 25;
                output[y - 2][x - 2] = Math.signum(temp) * Math.pow(temp, 2);
            }
        }

        return output;
    }

    public double[] getColumnAverages(double[][] input) {
        double[] output = new double[input[0].length];

        for (int x = 0; x < input[0].length; x++) {
            double sum = 0;
            for (int y = 0; y < input.length; y++) sum += input[y][x];
            output[x] = sum / input.length;
        }

        return output;
    }

    public void calculateResult(double[] input) {

        int leftSum = 0, midSum = 0, rightSum = 0;
        for (int x = 0; x < cutoffLeft; x++) leftSum += input[x];
        for (int x = cutoffLeft; x < cutoffRight; x++) midSum += input[x];
        for (int x = cutoffRight; x < input.length; x++) rightSum += input[x];

        if (leftSum > midSum && leftSum > rightSum) {
            result = SpikePosition.LEFT;
        } else if (midSum > rightSum) {
            result = SpikePosition.CENTER;
        } else {
            result = SpikePosition.RIGHT;
        }
    }

    /**
     * @param input the frame to be processed
     * @return the processed frame
     */
    @Override
    public synchronized Mat processFrame(Mat input) {
        computeCutoffs(input);
        double[][] pixelColors = computePixelColorness(input);
        double[][] convolutionResult = performConvolution(pixelColors);
        double[] columnVals = getColumnAverages(convolutionResult);
        calculateResult(columnVals);

        // Draw visual aids
        Imgproc.line(
                input,
                new Point(cutoffLeft, 0),
                new Point(cutoffLeft, input.rows()),
                new Scalar(255, 0, 0)
        );

        Imgproc.line(
                input,
                new Point(cutoffRight, 0),
                new Point(cutoffRight, input.rows()),
                new Scalar(255, 0, 0)
        );

        if (result == SpikePosition.LEFT) {
            Imgproc.rectangle(input, new Rect(0, 0, cutoffLeft, input.rows()), new Scalar(
                    255, 255, 255
            ), 3);
            System.out.println("left");
        } else if (result == SpikePosition.CENTER) {
            Imgproc.rectangle(input, new Rect(cutoffLeft, 0, cutoffLeft, input.rows()), new Scalar(
                    255, 255, 255
            ), 3);
            System.out.println("mid");
        } else if (result == SpikePosition.RIGHT) {
            Imgproc.rectangle(input, new Rect(cutoffRight, 0, cutoffRight, input.rows()), new Scalar(
                    255, 255, 255
            ), 3);
            System.out.println("right");
        }

        return input;
    }
}
