package com.pocolifo.robobase.utils;

public class MathUtils {
    public static int getSign(double num)
    {
        if(num < 0) {return -1;}
        else if(num > 0) {return 1;}
        else {return 0;}
    }
}
