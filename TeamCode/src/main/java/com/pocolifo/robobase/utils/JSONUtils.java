package com.pocolifo.robobase.utils;

import java.util.Arrays;

public class JSONUtils {

    public static String JSONify(String str) {
        return '\"' + StringUtils.escapeString(str) + '\"';
    }

    public static String JSONify(Object obj) {
        if(obj == null) return "null";
        else if(obj instanceof Number || obj instanceof Boolean) return obj.toString();
        else if(obj instanceof Object[]) return Arrays.toString((Object[]) obj);
        else return JSONify(obj.toString());
    }

}
