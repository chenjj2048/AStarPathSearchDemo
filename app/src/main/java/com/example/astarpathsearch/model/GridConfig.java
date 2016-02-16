package com.example.astarpathsearch.model;

import android.support.annotation.FloatRange;

import com.example.astarpathsearch.util.MathUtil;

/**
 * Created by 彩笔怪盗基德 on 2016/2/15.
 * https://github.com/chenjj2048
 */
public class GridConfig {
    private static final int MIN_LINES = 5;
    private static final int MIN_COLUMNS = 5;
    private static final int MAX_LINES = 100;
    private static final int MAX_COLUMNS = 100;

    private static volatile GridConfig instance;
    private int column = 30, line = 30;
    private int sizeSquare = 20;
    private int sizeGap = sizeSquare / 5;
    @FloatRange(from = -5, to = 5)
    private double weight = 0;
    @FloatRange(from = 0, to = 1)
    private double obscatleRatio = 0.2;

    private GridConfig() {
    }

    public static GridConfig getInstance() {
        if (instance == null) {
            synchronized (GridConfig.class) {
                if (instance == null)
                    instance = new GridConfig();
            }
        }
        return instance;
    }

    private void validityCheck() {
        column = (int) MathUtil.constraints(column, MIN_COLUMNS, MAX_COLUMNS);
        line = (int) MathUtil.constraints(line, MIN_LINES, MAX_LINES);
        sizeSquare = (int) MathUtil.constraints(sizeSquare, 5, 100);
        sizeGap = (int) MathUtil.constraints(sizeGap, 1, 20);
        weight = MathUtil.constraints(weight, -5, 5);
        obscatleRatio = MathUtil.constraints(obscatleRatio, 0, 1);
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
        validityCheck();
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
        validityCheck();
    }

    public int getSizeSquare() {
        return sizeSquare;
    }

    public void setSizeSquare(int sizeSquare) {
        this.sizeSquare = sizeSquare;
        validityCheck();
    }

    public int getSizeGap() {
        return sizeGap;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
        validityCheck();
    }

    public double getObscatleRatio() {
        return obscatleRatio;
    }

    public void setObscatleRatio(@FloatRange(from = 0, to = 1) double obscatleRatio) {
        this.obscatleRatio = obscatleRatio;
        validityCheck();
    }
}
