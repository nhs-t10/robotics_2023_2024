package com.pocolifo.robobase.utils;

public class StringUtils {

    /**
     * Adds n number of characters to the left of a string
     *
     * @param  str  the string to pad
     * @param  character  a character to pad with
     * @param  amount  amount of characters to pad with
     *
     * @return  the new string with padding on the left
     */
    public static String padLeft(String str, char character, int amount) {
        return getPadString(character, amount) + str;
    }

    /**
     * Adds n number of characters to the right of a string
     *
     * @param  str  the string to pad
     * @param  character  a character to pad with
     * @param  amount  amount of characters to pad with
     *
     * @return  the new string with padding on the right
     */
    public static String padRight(String str, char character, int amount) {
        return str + getPadString(character, amount);
    }

    private static String getPadString(char character, int amount) {
        return repeat(Character.toString(character), amount);
    }

    /**
     * Repeat a string n number of times
     *
     * @param  str  the string to repeat
     * @param  amount  amount of times to repeat
     *
     * @return  a new string of str repeated amount times
     */
    public static String repeat(String str, int amount) {
        if(amount <= 0) return "";

        StringBuilder result = new StringBuilder();
        for(int i = amount; i > 0; i--) result.append(str);
        return result.toString();
    }

    /**
     * Escapes all escape characters in a string
     *
     * @param  str  the string to escape
     *
     * @return  a new string made with str where all escape characters have been escaped
     */
    public static String escapeString(String str) {
        return str
                .replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\"", "\\\"")
                .replace("\t", "\\t");
    }
}
