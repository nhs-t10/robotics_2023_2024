package centerstage;

import org.opencv.core.Rect;

public enum SpikePosition {
    LEFT,
    CENTER,
    RIGHT;

    public static SpikePosition fromXPosition(int cameraWidth, Rect largestBoundingBox) {
        final int thirdOfCameraWidth = cameraWidth / 3;

        if (largestBoundingBox == null) {
            return null;
        }

        int x = largestBoundingBox.x + largestBoundingBox.width / 2;

        if (thirdOfCameraWidth * 2 > x) {
            return RIGHT;
        } else if (thirdOfCameraWidth > x) {
            return CENTER;
        } else {
            return LEFT;
        }
    }
}
