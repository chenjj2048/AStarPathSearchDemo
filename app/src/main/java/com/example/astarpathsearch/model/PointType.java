package com.example.astarpathsearch.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 彩笔怪盗基德 on 2016/1/27.
 * https://github.com/chenjj2048
 */
public class PointType {
    public static final int BLANK = 0;
    public static final int OBSTACLE = 1;
    public static final int START_POINT = 2;
    public static final int END_POINT = 3;

    public static char getSymbol(@PointSymbol int type) {
        char result;
        switch (type) {
            case BLANK:
                result = '0';
                break;
            case OBSTACLE:
                result = '1';
                break;
            case START_POINT:
                result = 'S';
                break;
            case END_POINT:
                result = 'E';
                break;
            default:
                throw new RuntimeException("木有这个类型");
        }
        return result;
    }

    @IntDef({BLANK, OBSTACLE, START_POINT, END_POINT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PointSymbol {
    }
}
