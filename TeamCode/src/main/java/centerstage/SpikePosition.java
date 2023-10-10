package centerstage;

import androidx.annotation.NonNull;

public enum SpikePosition {
    NOT_FOUND,
    LEFT,
    CENTER,
    RIGHT;

    @NonNull
    public String toString() {
        switch (this) {
            case NOT_FOUND:
                return "NOT_FOUND";
            case LEFT:
                return "LEFT";
            case CENTER:
                return "CENTER";
            case RIGHT:
                return "RIGHT";
        }
        return "null";
    }
}
