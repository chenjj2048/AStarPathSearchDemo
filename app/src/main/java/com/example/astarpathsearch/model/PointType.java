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
    public static final int ROUTE_FOUND = 4;
    public static final int ROUTE_VISITED = 5;  //非起点至终点路线上的访问过的点，与AStarNode.hasVisited属性不同

    @IntDef({BLANK, OBSTACLE, START_POINT, END_POINT, ROUTE_FOUND, ROUTE_VISITED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PointTypes {
    }

    public interface IPointColor {
        int getColor();
    }
}
