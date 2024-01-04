package com.pocolifo.robobase.vision;

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

    /**
     * @param input the frame to be processed
     * @return the processed frame
     */
    @Override
    public synchronized Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, ycrcb, Imgproc.COLOR_BGR2YCrCb);

        ycrcb = ycrcb.submat(ycrcb.rows() / 3, (ycrcb.rows() / 3) * 2, 0, ycrcb.cols());

        Mat left = ycrcb.submat(0, ycrcb.rows(), 0, ycrcb.cols() / 3);
        Core.extractChannel(left, extracted, col);
        double leftVal = Core.minMaxLoc(extracted).maxVal;

        Mat center = ycrcb.submat(0, ycrcb.rows(), ycrcb.cols() / 3, (ycrcb.cols() / 3) * 2);
        Core.extractChannel(center, extracted, col);
        double centerVal = Core.minMaxLoc(extracted).maxVal;

        Mat right = ycrcb.submat(0, ycrcb.rows(),  (ycrcb.cols() / 3) * 2, ycrcb.cols());
        Core.extractChannel(right, extracted, col);
        double rightVal = Core.minMaxLoc(extracted).maxVal;

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
