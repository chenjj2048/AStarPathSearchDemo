package com.example.astarpathsearch.model;

import android.graphics.Color;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;

import com.example.astarpathsearch.App;
import com.example.astarpathsearch.util.MathUtil;

/**
 * Created by 彩笔怪盗基德 on 2016/1/27.
 * https://github.com/chenjj2048
 */
public class AStarNode extends Node implements Comparable, PointType.IPointColor {
    @FloatRange(from = 0, to = 1)
    public static double weight;
    public static int tickCount;

    public boolean hasVisited;
    public int walkedDistance;
    public double lastDistance;
    public int sequenceVisit;
    public AStarNode previousNode;

    public AStarNode(int column, int line) {
        super(column, line);
        this.reset();
    }

    @FloatRange(from = 0, to = 1)
    public static double getWeight() {
        return AStarNode.weight;
    }

    public static void setWeight(@FloatRange(from = Float.NEGATIVE_INFINITY, to = Float.POSITIVE_INFINITY)
                                 double aStarWeight) {
        AStarNode.weight = MathUtil.sigmoid(aStarWeight);
    }

    public void reset() {
        super.setPointType(PointType.BLANK);
        previousNode = null;
        hasVisited = false;
        walkedDistance = 0;
        lastDistance = 0;
        sequenceVisit = 0;
        tickCount = 0;
    }

    public double evaluateDistanceToTarget(AStarNode targetNode) {
        return Math.abs(super.x - targetNode.x) + Math.abs(super.y - targetNode.y);
    }

    /**
     * WEIGHT -> [0,1]
     * WEIGHT=0的时候为广度优先，WEIGHT=1的时候指向性更强，能减少寻找点的数量
     */
    public double calcTotalDistance(AStarNode targetNode) {
        final double a = 1 - getWeight();
        final double b = getWeight();
        lastDistance = a * walkedDistance + b * evaluateDistanceToTarget(targetNode);
        return lastDistance;
    }

    /**
     * 评估两个节点哪个到目标总体的距离可能会最短
     */
    @Override
    public int compareTo(@NonNull Object another) {
        if (!(another instanceof AStarNode))
            throw new ClassCastException("传个相同的类进来啊");

        AStarNode node = (AStarNode) another;
        if (this.lastDistance < node.lastDistance)
            return -1;
        else if (this.lastDistance > node.lastDistance)
            return 1;
        else
            return 0;
    }

    @Override
    public int getColor() {
        int color;
        switch (super.getPointType()) {
            case PointType.BLANK:
                color = Color.WHITE;
                break;
            case PointType.OBSTACLE:
                color = Color.BLACK;
                break;
            case PointType.ROUTE_FOUND:
                color = Color.parseColor("#FFFFFF00");
                break;
            case PointType.START_POINT:
                color = App.getContext().getResources().getColor(android.R.color.holo_red_light);
                break;
            case PointType.END_POINT:
                color = Color.BLUE;
                break;
            case PointType.ROUTE_VISITED:
                color = Color.parseColor("#FFCFEBD0");
                break;
            default:
                throw new RuntimeException("木有这个类型");
        }
        return color;
    }
}
