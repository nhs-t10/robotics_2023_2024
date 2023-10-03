package com.pocolifo.robobase.control.input.nodeUtils;

public class MathUtils {
    public static String[] concatArrays(String[]... arrs) {
        //get the total length of the new array
        int m = 0;
        for(String[] a : arrs) m += a.length;

        //make a result array
        String[] r = new String[m];

        int c = 0;
        for(String[] a : arrs) {
            for (String s : a) {
                r[c] = s;
                c++;
            }
        }

        return r;
    }
}
