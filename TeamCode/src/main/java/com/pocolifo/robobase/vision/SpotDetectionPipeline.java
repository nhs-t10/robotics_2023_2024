package com.pocolifo.robobase.vision;

import com.pocolifo.robobase.Alliance;
import centerstage.SpikePosition;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import centerstage.SpikePosition;

public class SpotDetectionPipeline extends AbstractResultCvPipeline<Integer> {
    Alliance alliance;
    static final Scalar BLUE = new Scalar(0, 0, 255);
    static final Scalar GREEN = new Scalar(0, 255, 0);
    static final Point SPOT1_TOPLEFT_ANCHOR_POINT = new Point(10, 190);
    static final Point SPOT1_BOTTOMRIGHT_ANCHOR_POINT = new Point(40, 220);
    static final Point SPOT2_TOPLEFT_ANCHOR_POINT = new Point(305, 160);
    static final Point SPOT2_BOTTOMRIGHT_ANCHOR_POINT = new Point(335, 190);
    static final Point SPOT3_TOPLEFT_ANCHOR_POINT = new Point(600, 190);
    static final Point SPOT3_BOTTOMRIGHT_ANCHOR_POINT = new Point(630, 220);

    Mat region1_Cr, region2_Cr, region3_Cr, region1_Cb, region2_Cb, region3_Cb;
    private final Mat ycbcr = new Mat();
    private final Mat Cr = new Mat();
    private final Mat Cb = new Mat();
    int avg1, avg2, avg3;
    private volatile SpikePosition position = SpikePosition.LEFT;
    public SpotDetectionPipeline(Alliance alliance) {
        this.alliance = alliance;
    }
    void extractInputColorChannels(Mat input) {
        Imgproc.cvtColor(input, ycbcr, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(ycbcr, Cr, 1);
        Core.extractChannel(ycbcr, Cb, 2);
    }

    @Override
    public void init(Mat firstFrame) {
        /*
         * We need to call this in order to make sure the 'Cb'
         * object is initialized, so that the submats we make
         * will still be linked to it on subsequent frames. (If
         * the object were to only be initialized in processFrame,
         * then the submats would become delinked because the backing
         * buffer would be re-allocated the first time a real frame
         * was crunched)
         */
        extractInputColorChannels(firstFrame);

        /*
         * Submats are a persistent reference to a region of the parent
         * buffer. Any changes to the child affect the parent, and the
         * reverse also holds true.
         */
        region1_Cr = Cr.submat(new Rect(SPOT1_TOPLEFT_ANCHOR_POINT, SPOT1_BOTTOMRIGHT_ANCHOR_POINT));
        region2_Cr = Cr.submat(new Rect(SPOT2_TOPLEFT_ANCHOR_POINT, SPOT2_BOTTOMRIGHT_ANCHOR_POINT));
        region3_Cr = Cr.submat(new Rect(SPOT3_TOPLEFT_ANCHOR_POINT, SPOT3_BOTTOMRIGHT_ANCHOR_POINT));
        region1_Cb = Cb.submat(new Rect(SPOT1_TOPLEFT_ANCHOR_POINT, SPOT1_BOTTOMRIGHT_ANCHOR_POINT));
        region2_Cb = Cb.submat(new Rect(SPOT2_TOPLEFT_ANCHOR_POINT, SPOT2_BOTTOMRIGHT_ANCHOR_POINT));
        region3_Cb = Cb.submat(new Rect(SPOT3_TOPLEFT_ANCHOR_POINT, SPOT3_BOTTOMRIGHT_ANCHOR_POINT));
    }

    /**
     * @author Arlan and Chlohal
     * @param input Mat to be processed
     * @return Processed Mat
     */
    @Override
    public Mat processFrame(Mat input) {
        /*
         * Overview of what we're doing:
         *
         * We first convert to ycbcr color space, from RGB color space.
         * Why do we do this? Well, in the RGB color space, chroma and
         * luma are intertwined. In ycbcr, chroma and luma are separated.
         * ycbcr is a 3-channel color space, just like RGB. ycbcr's 3 channels
         * are Y, the luma channel (which essentially just a B&W image), the
         * Cr channel, which records the difference from red, and the Cb channel,
         * which records the difference from blue. Because chroma and luma are
         * not related in ycbcr, vision code written to look for certain values
         * in the Cr/Cb channels will not be severely affected by differing
         * light intensity, since that difference would most likely just be
         * reflected in the Y channel.
         *
         * After we've converted to ycbcr, we extract just the 2nd channel, the
         * Cb channel. We do this because stones are bright yellow and contrast
         * STRONGLY on the Cb channel against everything else, including SkyStones
         * (because SkyStones have a black label).
         *
         * We then take the average pixel value of 3 different regions on that Cb
         * channel, one positioned over each stone. The brightest of the 3 regions
         * is where we assume the SkyStone to be, since the normal stones show up
         * extremely darkly.
         *
         * We also draw rectangles on the screen showing where the sample regions
         * are, as well as drawing a solid rectangle over top the sample region
         * we believe is on top of the SkyStone.
         *
         * In order for this whole process to work correctly, each sample region
         * should be positioned in the center of each of the first 3 stones, and
         * be small enough such that only the stone is sampled, and not any of the
         * surroundings.
         */

        /*
         * Get the Cb channel of the input frame after conversion to ycbcr
         */
        extractInputColorChannels(input);

        /*
         * Compute the average pixel value of each submat region. We're
         * taking the average of a single channel buffer, so the value
         * we need is at index 0. We could have also taken the average
         * pixel value of the 3-channel image, and referenced the value
         * at index 2 here.
         */
        avg1 = (int) Core.mean(region1_Cr).val[0] - (int) Core.mean(region1_Cb).val[0];
        avg2 = (int) Core.mean(region2_Cr).val[0] - (int) Core.mean(region2_Cb).val[0];
        avg3 = (int) Core.mean(region3_Cr).val[0] - (int) Core.mean(region3_Cb).val[0];

        if (alliance == Alliance.BLUE) {
            avg1 *= -1;
            avg2 *= -1;
            avg3 *= -1;
        }

        /*
         * Draw a rectangle showing sample region 1 on the screen.
         * Simply a visual aid. Serves no functional purpose.
         */
        Imgproc.rectangle(
                input, // Buffer to draw on
                SPOT1_TOPLEFT_ANCHOR_POINT, // First point which defines the rectangle
                SPOT1_BOTTOMRIGHT_ANCHOR_POINT, // Second point which defines the rectangle
                BLUE, // The color the rectangle is drawn in
                2); // Thickness of the rectangle lines

        /*
         * Draw a rectangle showing sample region 2 on the screen.
         * Simply a visual aid. Serves no functional purpose.
         */
        Imgproc.rectangle(
                input, // Buffer to draw on
                SPOT2_TOPLEFT_ANCHOR_POINT, // First point which defines the rectangle
                SPOT2_BOTTOMRIGHT_ANCHOR_POINT, // Second point which defines the rectangle
                BLUE, // The color the rectangle is drawn in
                2); // Thickness of the rectangle lines

        /*
         * Draw a rectangle showing sample region 3 on the screen.
         * Simply a visual aid. Serves no functional purpose.
         */
        Imgproc.rectangle(
                input, // Buffer to draw on
                SPOT3_TOPLEFT_ANCHOR_POINT, // First point which defines the rectangle
                SPOT3_BOTTOMRIGHT_ANCHOR_POINT, // Second point which defines the rectangle
                BLUE, // The color the rectangle is drawn in
                2); // Thickness of the rectangle lines


        /*
         * Find the max of the 3 averages
         */
        if (avg1 > avg2 && avg1 > avg3) {
            position = SpikePosition.LEFT; // Record our analysis

            /*
             * Draw a solid rectangle on top of the chosen region.
             * Simply a visual aid. Serves no functional purpose.
             */
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    SPOT1_TOPLEFT_ANCHOR_POINT, // First point which defines the rectangle
                    SPOT1_BOTTOMRIGHT_ANCHOR_POINT, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill
        } else if (avg2 > avg3) {
            position = SpikePosition.CENTER; // Record our analysis

            /*
             * Draw a solid rectangle on top of the chosen region.
             * Simply a visual aid. Serves no functional purpose.
             */
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    SPOT2_TOPLEFT_ANCHOR_POINT, // First point which defines the rectangle
                    SPOT2_BOTTOMRIGHT_ANCHOR_POINT, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill
        } else {
            position = SpikePosition.RIGHT; // Record our analysis

            /*
             * Draw a solid rectangle on top of the chosen region.
             * Simply a visual aid. Serves no functional purpose.
             */
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    SPOT3_TOPLEFT_ANCHOR_POINT, // First point which defines the rectangle
                    SPOT3_BOTTOMRIGHT_ANCHOR_POINT, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill
        }

        /*
         * Render the 'input' buffer to the viewport. But note this is not
         * simply rendering the raw camera feed, because we called functions
         * to add some annotations to this buffer earlier up.
         */
        return input;
    }

    @Override
    public Integer getResult() {
        result =  position.ordinal();
        return super.getResult();
    }

    @Override
    public void init() {

    }
}
