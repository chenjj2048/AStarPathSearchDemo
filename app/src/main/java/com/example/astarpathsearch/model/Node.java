package com.example.astarpathsearch.model;

import com.example.astarpathsearch.model.PointType.PointTypes;

/**
 * Created by 彩笔怪盗基德 on 2016/1/27.
 * https://github.com/chenjj2048
 */
public class Node {
    public final int x;
    public final int y;
    private int hash;

    @PointTypes
    private int pointType;

    public Node(int column, int line) {
        this.x = column;
        this.y = line;
        this.pointType = com.example.astarpathsearch.model.PointType.BLANK;
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
        return (hash != 0) ? hash : (hash = 31 * x + y);
    }

    @PointTypes
    public int getPointType() {
        return this.pointType;
    }

    public void setPointType(@PointTypes int pointType) {
        this.pointType = pointType;
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
