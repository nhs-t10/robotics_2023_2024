package com.pocolifo.robobase.novel.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;

public class NovelEncoder {
    private final DcMotor encoder;
    private final double ticksPerRevolution;
    private final double encoderDiameterIn;

    public NovelEncoder(DcMotor encoder, double encoderDiameterIn, double ticksPerRevolution) {
        this.encoder = encoder;
        this.encoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.ticksPerRevolution = ticksPerRevolution;
        this.encoderDiameterIn = encoderDiameterIn;
    }

    public int getCurrentTicks() {
        return this.encoder.getCurrentPosition();
    }

    public double getCurrentInches() {
        return this.getCurrentTicks() / this.ticksPerRevolution * this.encoderDiameterIn * Math.PI;
    }
}
