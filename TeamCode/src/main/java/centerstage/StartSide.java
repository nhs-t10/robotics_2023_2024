package centerstage;

public enum StartSide {
    APRIL_TAG_SIDE,
    BACKDROP_SIDE;


    public double getSideSwapConstantIn() {
        return this == StartSide.APRIL_TAG_SIDE ? 48 : 0;
    }
    public double getSideSign() {
        return this == StartSide.APRIL_TAG_SIDE ? 1 : -1;
    }
}