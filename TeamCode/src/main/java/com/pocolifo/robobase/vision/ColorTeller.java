package com.pocolifo.robobase.vision;


import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import centerstage.SpikePosition;

public class ColorTeller extends AbstractResultCvPipeline<SpikePosition> {
    private final int col;
    private Mat ycrcb;
    private int xEndLeftSide;
    private int xStartCenter;
    private int xEndCenter;
    private int xStartRightSide;
    public double colorness;


    // These variables are used only in `processFrame`. They CAN NOT be declared locally.
    // Under the hood, OpenCV allocates memory for Mat every time `processFrame` is called.
    // Therefore, they must be initialized once in the instance-level.
    public ColorTeller(int extractCol) {
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
                if (value > 130) {
                    n++;
                }
            }
        }

        return (double) n;
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

        colorness = ycrcb.get(ycrcb.rows(), ycrcb.cols())[col];
        Imgproc.rectangle(input, new Rect(ycrcb.rows(), ycrcb.cols(), 50, 50), new Scalar(255, 255, 255), 3);
        System.out.println("Redness: " + ycrcb.get(ycrcb.rows(), ycrcb.cols())[2]);
        System.out.println("Blueness: " + ycrcb.get(ycrcb.rows(), ycrcb.cols())[1]);

        return input;
    }
}
