package centerstage;

import org.opencv.core.Rect;

public enum SpikePosition {
    LEFT,
    CENTER,
    RIGHT,

    NOT_FOUND;

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
    public String toString()
    {
        if(this == RIGHT)
        {
            return "right";
        }
        else if(this == LEFT)
        {
            return "left";
        }
        else if(this == CENTER)
        {
            return "center";
        }
        else
        {
            return "null";
        }
    }
}
