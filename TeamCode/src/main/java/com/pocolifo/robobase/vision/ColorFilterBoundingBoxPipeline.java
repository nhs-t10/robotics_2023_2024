package com.pocolifo.robobase.vision;

import centerstage.SpikePosition;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class ColorFilterBoundingBoxPipeline extends AbstractResultCvPipeline<SpikePosition> {
    public final Scalar minFilterColor;
    public final Scalar maxFilterColor;
    public final int colorMode;

    private Mat colorConverted;
    private Mat filteredColor;

    public ColorFilterBoundingBoxPipeline(Scalar minFilterColor, Scalar maxFilterColor, int colorMode) {
        this.minFilterColor = minFilterColor;
        this.maxFilterColor = maxFilterColor;
        this.colorMode = colorMode;
    }

    @Override
    public void init() {
        colorConverted = new Mat();
        filteredColor = new Mat();
    }

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, colorConverted, colorMode);
        Core.inRange(colorConverted, minFilterColor, maxFilterColor, filteredColor);

        input.setTo(filteredColor);

        return input;
    }

    public static class BoundingBox {
        public Scalar averageColor;
        public int x1;
        public int y1;
        public int x2;
        public int y2;
    }
}
