package com.pocolifo.robobase.vision;

import centerstage.SpikePosition;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class NovelYCrCbDetection extends AbstractResultCvPipeline<SpikePosition> {
    private final int col;
    private Mat ycrcb;
    private int xEndLeftSide;
    private int xStartCenter;
    private int xEndCenter;
    private int xStartRightSide;


    // These variables are used only in `processFrame`. They CAN NOT be declared locally.
    // Under the hood, OpenCV allocates memory for Mat every time `processFrame` is called.
    // Therefore, they must be initialized once in the instance-level.
    public NovelYCrCbDetection(int extractCol) {
        this.col = extractCol;
    }

    @Override
    public void init() {
        ycrcb = new Mat();
    }

    public double computeColorness(Mat mat) {
        int n = 0;

        for (int r = 0; mat.rows() > r; r++) {
            for (int c = 0; mat.cols() > c; c++) {
                double value = mat.get(r, c)[col];

                // 150 is the guess-and-check magic threshold
                if (value > 150) {
                    n++;
                }
            }
        }

        return (double) n / (mat.cols() * mat.rows());
    }

    /**
     * @param input the frame to be processed
     * @return the processed frame
     */
    @Override
    public synchronized Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, ycrcb, Imgproc.COLOR_BGR2YCrCb);

        // Crop image
        ycrcb = ycrcb.submat(ycrcb.rows() / 2, ycrcb.rows(), 0, ycrcb.cols());

        // Compute divisions
        xEndLeftSide = ycrcb.cols() / 5;
        xStartCenter = ycrcb.cols() / 4;
        xEndCenter = xStartCenter * 3;
        xStartRightSide = xEndLeftSide * 4;

        // Submat sections
        Mat left = ycrcb.submat(0, ycrcb.rows(), 0, xEndLeftSide);
        Mat center = ycrcb.submat(0, ycrcb.rows(), xStartCenter, xEndCenter);
        Mat right = ycrcb.submat(0, ycrcb.rows(), xStartRightSide, ycrcb.cols());

        double leftColorness = computeColorness(left);
        double centerColorness = computeColorness(center);
        double rightColorness = computeColorness(right);

        System.out.println(leftColorness);

        // Draw visual aids
        Imgproc.line(
                input,
                new Point(xEndLeftSide, 0),
                new Point(xEndLeftSide, input.rows()),
                new Scalar(255, 0, 0)
        );

        Imgproc.line(
                input,
                new Point(xStartCenter, 0),
                new Point(xStartCenter, input.rows()),
                new Scalar(255, 0, 0)
        );

        Imgproc.line(
                input,
                new Point(xEndCenter, 0),
                new Point(xEndCenter, input.rows()),
                new Scalar(255, 0, 0)
        );

        Imgproc.line(
                input,
                new Point(xStartRightSide, 0),
                new Point(xStartRightSide, input.rows()),
                new Scalar(255, 0, 0)
        );

        if (leftColorness > centerColorness && leftColorness > rightColorness) {
            result = SpikePosition.LEFT;
            Imgproc.rectangle(input, new Rect(0, 0, 50, 50), new Scalar(
                    255, 255, 255
            ), 3);
        } else if (centerColorness > leftColorness && centerColorness > rightColorness) {
            result = SpikePosition.CENTER;
            Imgproc.rectangle(input, new Rect(50, 0, 50, 50), new Scalar(
                    255, 255, 255
            ), 3);
        } else if (rightColorness > leftColorness && rightColorness > centerColorness) {
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
