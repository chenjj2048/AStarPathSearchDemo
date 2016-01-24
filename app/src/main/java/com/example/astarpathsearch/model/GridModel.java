package com.example.astarpathsearch.model;

import android.graphics.RectF;
import android.support.annotation.IntRange;

import java.util.Random;

/**
 * Created by 彩笔怪盗基德 on 2016/1/24.
 * https://github.com/chenjj2048
 */
public abstract class GridModel {
    private final int xCounts;
    private final int yCounts;
    private final int map[][];

    public GridModel(int xCounts, int yCounts) {
        this.xCounts = constraints(xCounts, 10, 100);
        this.yCounts = constraints(yCounts, 10, 100);
        map = new int[this.xCounts][this.yCounts];
    }

    public int getXCounts() {
        return xCounts;
    }

    public int getYCounts() {
        return yCounts;
    }

    private int constraints(int value, int min, int max) {
        return (value < min) ? min : (value > max ? max : value);
    }

    public void setPoint(int column, int line, int value) {
        map[column][line] = value;
    }

    public int getPoint(int column, int line) {
        return map[column][line];
    }

    public void setLine(int line, int value) {
        for (int x = 0; x < xCounts; x++)
            map[x][line] = value;
    }

    public void setColumn(int column, int value) {
        for (int y = 0; y < yCounts; y++)
            map[column][y] = value;
    }

    public void reset(int value) {
        for (int x = 0; x < xCounts; x++)
            for (int y = 0; y < yCounts; y++)
                map[x][y] = value;
    }

    public void reset() {
        reset(0);
    }

    public abstract RectF getPointRect(@IntRange(from = 0) int column, @IntRange(from = 0) int line);

    public abstract RectF getSizeWithoutMargin();

    public void setRandomValue(int max) {
        Random random = new Random();
        for (int x = 0; x < this.getXCounts(); x++)
            for (int y = 0; y < this.getYCounts(); y++) {
                setPoint(x, y, random.nextInt(max));
            }
    }
}
