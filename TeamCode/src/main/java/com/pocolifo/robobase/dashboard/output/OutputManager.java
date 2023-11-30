package com.pocolifo.robobase.dashboard.output;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OutputManager {
    private static Map<String, Output> values = new HashMap<String, Output>();
    private static final ScheduledExecutorService updateScheduler = Executors.newScheduledThreadPool(1);
    private static int millisecondsBetweenFetches = 250;

    public static synchronized void addValue(Output value) {
        if (values.containsKey(value.getName())) {
            throw new IllegalArgumentException("Output: " + value.getName() + " already registered.");
        }
        values.put(value.getName(), value);
    }

    public static void setMillisecondsBetweenFetches(int mills) {
        millisecondsBetweenFetches = mills;
    }

    public static synchronized void startUpdateScheduler() {
        updateScheduler.scheduleAtFixedRate(() -> {
            try {
                updateAll();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, millisecondsBetweenFetches, millisecondsBetweenFetches, TimeUnit.MILLISECONDS);
    }

    public static synchronized void updateAll() throws Exception {
        for (Output value : values.values()) {
            value.update();
        }
    }

    /**
     * Even though this is not as clean as using GSON, it minimized dependencies.
     * @return A string in the format of JSON where each KVP is in the format {nameOfDebugValue: {value: number, readonly: boolean}}
     * @throws IOException
     */
    public static synchronized String getValuesAsJSON() {
        StringBuilder builder = new StringBuilder();

        builder.append("{");

        boolean first = true;
        for (Map.Entry<String, Output> entry : values.entrySet()) {
            if (!first) {
                builder.append(",");
            }
            first = false;
            builder.append("\"" + entry.getKey() + "\"");
            builder.append(":");
            builder.append("\""+entry.getValue().getValue()+"\"");
        }

        builder.append("}");

        return builder.toString();
    }

    public static synchronized void clear() {
        values = new HashMap<String, Output>();
    }
}
