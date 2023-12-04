package centerstage;

import com.pocolifo.robobase.reconstructor.VirtualField;

/**
 * Contains a list of virtual waypoints for the CenterStage FTC season. Meant to be used with {@link VirtualField}.
 */
public class CenterStageWaypoints {
    public static final VirtualField.Waypoint RED_APRIL_TAG_SIDE_SPAWN = new VirtualField.Waypoint(1.42968, -0.929292);
    public static final VirtualField.Waypoint RED_BACKDROP_SIDE_SPAWN = new VirtualField.Waypoint(1.42968, 0.289908);

    public static final VirtualField.Waypoint BLUE_APRIL_TAG_SIDE_SPAWN = new VirtualField.Waypoint(-1.42968, -0.929292);
    public static final VirtualField.Waypoint BLUE_BACKDROP_SIDE_SPAWN = new VirtualField.Waypoint(-1.42968, 0.289908);

    public static final VirtualField.Waypoint IN_FRONT_RED_BACKDROP_MIDDLE = new VirtualField.Waypoint(0.89355, 1.21523);
    public static final VirtualField.Waypoint IN_FRONT_BLUE_BACKDROP_MIDDLE = new VirtualField.Waypoint(-0.89355, 1.21523);

    public static final VirtualField.Waypoint RED_BACKDROP_PARKING = new VirtualField.Waypoint(1.57265, 1.46542);
    public static final VirtualField.Waypoint BLUE_BACKDROP_PARKING = new VirtualField.Waypoint(1.57265, 1.46542);

    public static final VirtualField.Waypoint BLUE_SUBSTATION = new VirtualField.Waypoint(1.57265, -1.46542);
    public static final VirtualField.Waypoint RED_SUBSTATION = new VirtualField.Waypoint(-1.57265, -1.46542);
}
