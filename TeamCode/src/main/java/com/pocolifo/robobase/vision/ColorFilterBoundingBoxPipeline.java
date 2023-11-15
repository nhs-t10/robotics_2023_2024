package com.pocolifo.robobase.vision;

import centerstage.Constants;
import centerstage.SpikePosition;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ColorFilterBoundingBoxPipeline extends AbstractResultCvPipeline<List<Rect>> {
    public static final int DIVISOR_CONSTANT = 2;
    public final Scalar target;
    public int cameraWidth;
    public int cameraHeight;

    private Mat colorConverted;
    private Mat filteredColor;
    private Mat hierarchy;
    private Mat edges;
    private Mat resized;
    private List<MatOfPoint> contours;

    public ColorFilterBoundingBoxPipeline(Scalar target) {
        this.target = target;
        result = Collections.emptyList();
    }

    @Override
    public void init() {
        colorConverted = new Mat();
        filteredColor = new Mat(Constants.CAMERA_RES_HEIGHT / DIVISOR_CONSTANT, Constants.CAMERA_RES_WIDTH / DIVISOR_CONSTANT, CvType.CV_8UC1);
        hierarchy = new Mat();
        resized = new Mat();
        edges = new Mat();
        contours = new ArrayList<>();
    }

    public double getColorDistance(double[] input) {
        double sum = 0;
        for (int i = 0; 3 > i; i++) {
            double c1 = input[i];
            double c2 = target.val[i];
            double diff = c2 - c1;
            sum += diff * diff;
        }
        return sum / (256 * 256);
    }

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.resize(input, resized, new Size(input.size().width / DIVISOR_CONSTANT, input.size().height / DIVISOR_CONSTANT));
        Imgproc.cvtColor(resized, colorConverted, Imgproc.COLOR_BGR2YCrCb);

        for (int x = 0; colorConverted.cols() > x; x++) {
            for (int y = 0; colorConverted.rows() > y; y++) {
                double dist = getColorDistance(colorConverted.get(y, x));
                filteredColor.put(y, x, dist * 255);
            }
        }

//        Core.inRange(colorConverted, minFilterColor, maxFilterColor, filteredColor);
//        Imgproc.findContours(filteredColor, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
//        Imgproc.drawContours(filteredColor, contours, -1, new Scalar(255), 5);
//
//        this.result = contours.stream()
//                .map(Imgproc::boundingRect)
//                .sorted((r1, r2) -> r2.width * r2.height - r1.width * r2.height)
//                .collect(Collectors.toList());
//
//        this.result.forEach(rect -> Imgproc.rectangle(filteredColor, rect, new Scalar(255, 0, 255), 5));

        cameraWidth = input.cols();
        cameraHeight = input.rows();

        return filteredColor;
    }
}
