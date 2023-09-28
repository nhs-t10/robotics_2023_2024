package com.pocolifo.robobase.utils;

public class StringUtils {

    public static String padLeft(String str, char character, int amount) {
        return getPadString(character, amount) + str;
    }

    public static String padRight(String str, char character, int amount) {
        return str + getPadString(character, amount);
    }

    private static String getPadString(char character, int amount) {
        return repeat(Character.toString(character), amount);
    }


    public static String repeat(String str, int amount) {
        if(amount <= 0) return "";

        StringBuilder result = new StringBuilder();
        for(int i = amount; i > 0; i--) result.append(str);
        return result.toString();
    }
}
