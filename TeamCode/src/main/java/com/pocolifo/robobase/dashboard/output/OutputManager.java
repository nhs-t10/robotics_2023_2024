package com.pocolifo.robobase.dashboard.output;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OutputManager {
    private static final Map<String, Output> values = new HashMap<>();

    public static synchronized void addValue(Output value) {
        if (values.containsKey(value.getName())) {
            throw new IllegalArgumentException("Output: " + value.getName() + " already registered.");
        }
        values.put(value.getName(), value);
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
    public static synchronized String getValuesAsJSON() throws Exception {
        updateAll();

        JsonObject object = new JsonObject();
        Gson gson = new Gson();

        values.forEach((key, value) -> object.add(key, gson.toJsonTree(value.getValue())));

        return gson.toJson(object);
    }

    public static synchronized void clear() {
        values.clear();
    }
}
