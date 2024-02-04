package com.pocolifo.robobase.novel;

public class OdometryCoefficientSet {
    public final double rightCoefficient;
    public final double leftCoefficient;
    public final double perpendicularCoefficient;

    public OdometryCoefficientSet(double rightCoefficient, double leftCoefficient, double perpendicularCoefficient) {
        this.rightCoefficient = rightCoefficient;
        this.leftCoefficient = leftCoefficient;
        this.perpendicularCoefficient = perpendicularCoefficient;
    }

    public OdometryCoefficientSet() {
        this(-1, 1, 1);
    }
}
