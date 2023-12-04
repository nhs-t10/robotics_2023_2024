package com.pocolifo.robobase.dashboard.input;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InputsManager {
    private static final Map<String, Input<?>> values = new HashMap<>();

    public static synchronized void addValue(Input<?> value) {
        if (values.containsKey(value.getName())) {
            throw new IllegalArgumentException("Input: \"" + value.getName() + "\" already registered");
        }

        values.put(value.getName(), value);
    }

    public static synchronized void setValues(Map<String, String> rawParams) {
        for (Map.Entry<String, String> rawParam : rawParams.entrySet()) {
            if (!values.containsKey(rawParam.getKey())) {
                throw new IllegalArgumentException("Inputs: \"" + rawParam.getKey() + "\" does not exist");
            }

            values.get(rawParam.getKey()).setFromString(rawParam.getValue());
        }
    }

    /**
     * Even though this is not as clean as using GSON, it minimized dependencies.
     * [name]: {
     *     "value": [value],
     *     "min": [min],
     *     "max": [max]
     * }
     *
     * @return A string in the format of JSON where each KVP is in the format {nameOfDebugValue: {value: number, }}
     */
    public static synchronized String getValuesAsJSON() {
        JsonObject object = new JsonObject();
        Gson gson = new Gson();

        values.forEach((label, input) -> object.add(label, gson.toJsonTree(input)));

        return gson.toJson(object);
    }

    public static synchronized void clear() {
        values.clear();
    }
}