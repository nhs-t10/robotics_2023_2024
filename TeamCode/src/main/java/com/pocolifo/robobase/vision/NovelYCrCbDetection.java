package com.pocolifo.robobase.vision;

import android.media.ImageReader;

import centerstage.SpikePosition;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class NovelYCrCbDetection extends AbstractResultCvPipeline<SpikePosition> {
    private final int col;
    private Mat ycrcb;
    private Mat extracted;

    // These variables are used only in `processFrame`. They CAN NOT be declared locally.
    // Under the hood, OpenCV allocates memory for Mat every time `processFrame` is called.
    // Therefore, they must be initialized once in the instance-level.
    public NovelYCrCbDetection(int extractCol) {
        this.col = extractCol;
    }

    @Override
    public void init() {
        ycrcb = new Mat();
        extracted = new Mat();
    }

    public int countThreshold(Mat m, double threshold) {
        int n = 0;

        for (int r = 0; m.rows() > r; r++) {
            for (int c = 0; m.cols() > c; c++) {
                if (m.get(r, c)[col] > threshold) {
                    n++;
                }
            }
        }

        return n;
    }

    /**
     * @param input the frame to be processed
     * @return the processed frame
     */
    @Override
    public synchronized Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, ycrcb, Imgproc.COLOR_BGR2YCrCb);

        ycrcb = ycrcb.submat(ycrcb.rows() / 2, ycrcb.rows(), 0, ycrcb.cols());

        Mat left = ycrcb.submat(0, ycrcb.rows(), 0, ycrcb.cols() / 4);
        // Core.(left, extracted, col);
        double leftVal = countThreshold(left, 0.3);
        // double leftVal = Core.mean(left).val[col] * left.cols() * left.rows();

        Mat center = ycrcb.submat(0, ycrcb.rows(), ycrcb.cols() / 3, (ycrcb.cols() / 3) * 2);
        // Core.extractChannel(center, extracted, col);
//        double centerVal = Core.mean(center).val[col] * center.cols() * center.rows();
        double centerVal = countThreshold(center, 0.3);

        Mat right = ycrcb.submat(0, ycrcb.rows(),  (ycrcb.cols() / 4) * 3, ycrcb.cols());
        // Core.extractChannel(right, extracted, col);
//        double rightVal = Core.mean(right).val[col] * right.cols() * right.rows();
        double rightVal = countThreshold(right, 0.8);

        Imgproc.line(input, new Point(ycrcb.cols() / 4, input.rows() / 2), new Point(ycrcb.cols() / 4, input.rows()), new Scalar(255, 0, 0));
        Imgproc.line(input, new Point(ycrcb.cols() / 4 * 3, input.rows() / 2), new Point(ycrcb.cols() / 4 * 3, input.rows()), new Scalar(255, 0, 0));

        if (leftVal > centerVal && leftVal > rightVal) {
            result = SpikePosition.LEFT;
            Imgproc.rectangle(input, new Rect(0, 0, 50, 50), new Scalar(
                    255, 255, 255
            ), 3);
        } else if (centerVal > leftVal && centerVal > rightVal) {
            result = SpikePosition.CENTER;
            Imgproc.rectangle(input, new Rect(50, 0, 50, 50), new Scalar(
                    255, 255, 255
            ), 3);
        } else if (rightVal > leftVal && rightVal > centerVal) {
            result = SpikePosition.RIGHT;
            Imgproc.rectangle(input, new Rect(100, 0, 50, 50), new Scalar(
                    255, 255, 255
            ), 3);
        } else {
            result = SpikePosition.NOT_FOUND;
        }

        return input;
    }
}
