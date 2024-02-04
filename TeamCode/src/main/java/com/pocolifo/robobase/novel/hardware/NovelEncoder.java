package com.pocolifo.robobase.novel.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;

public class NovelEncoder {
    private final DcMotor encoder;
    private final double ticksPerRevolution;
    private final double encoderDiameterIn;

    public NovelEncoder(DcMotor encoder, double encoderDiameterIn, double ticksPerRevolution) {
        this.encoder = encoder;
        this.ticksPerRevolution = ticksPerRevolution;
        this.encoderDiameterIn = encoderDiameterIn;
    }

    public int getCurrentPosition() {
        return this.encoder.getCurrentPosition();
    }

    public double encoderTicksToInches(double ticks) {
        // TODO: does this need PI in here?
        return ticks / this.ticksPerRevolution * this.encoderDiameterIn;
    }

    public double inchesToEncoderTicks(double inches) {
        // TODO: does this need PI in here?
        return (inches / this.encoderDiameterIn) * this.ticksPerRevolution;
    }
}
