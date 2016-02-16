package com.example.astarpathsearch.util;

import android.support.annotation.FloatRange;

/**
 * Created by 彩笔怪盗基德 on 2016/2/14.
 * https://github.com/chenjj2048
 */
public class MathUtil {
    public static double constraints(double value, double min, double max) {
        return (value < min) ? min : (value > max ? max : value);
    }

    @FloatRange(from = 0, to = 1)
    public static double sigmoid(double x) {
        return 1 / (1 + Math.pow(Math.E, -x));
    }
}
