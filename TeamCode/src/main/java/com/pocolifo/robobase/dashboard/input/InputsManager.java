package com.pocolifo.robobase.dashboard.input;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InputsManager {
    private static Map<String, Input> values = new HashMap<String, Input>();

    public static synchronized void addValue(Input value) {
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
     * @throws IOException
     */
    public static synchronized String getValuesAsJSON() {
        StringBuilder builder = new StringBuilder();

        builder.append("{");

        boolean first = true;
        for (Map.Entry<String, Input> entry : values.entrySet()) {

            if (!first) {
                builder.append(",");
            }
            first = false;

            builder.append("\"" + entry.getKey() + "\"");
            builder.append(":{");
            builder.append("\"value\":" + entry.getValue().getValue() + ",");
            builder.append("\"min\":" + entry.getValue().getRange().getMin() + ",");
            builder.append("\"max\":" + entry.getValue().getRange().getMax() + "");
            builder.append("}");
        }

        builder.append("}");

        return builder.toString();
    }

    public static synchronized void clear() {
        values = new HashMap<String, Input>();
    }
}