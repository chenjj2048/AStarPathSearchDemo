package com.example.astarpathsearch.model;

import android.graphics.RectF;

/**
 * Created by 彩笔怪盗基德 on 2016/1/27.
 * https://github.com/chenjj2048
 */
public abstract class Node {
    public final int x;
    public final int y;

    @PointType.PointSymbol
    public int pointType;

    public Node(int column, int line, @PointType.PointSymbol int pointType) {
        this.x = column;
        this.y = line;
        this.pointType = pointType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node that = (Node) o;

        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public abstract Node leftPoint();

    public abstract Node rightPoint();

    public abstract Node abovePoint();

    public abstract Node belowPoint();

    public void reset() {
        pointType = PointType.BLANK;
    }

    public interface PointInterface {
        public double distance(Node pointModel);

        public RectF getPointRect(int column, int line);
    }
}
