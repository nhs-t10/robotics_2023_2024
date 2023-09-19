package com.pocolifo.robobase.motor;

import java.util.List;

/**
 * An interface that allows objects that control movement to be measured
 */
public interface MovementAware {
    /**
     * Get the most recent <strong>completed</strong> movement events. Index 0 is the most recent event, and the last index is the least
     * recent event.
     *
     * @return An ordered list of the most recent movement events that have been completed
     */
    List<MovementEvent> getPastMovementEvents();
}
