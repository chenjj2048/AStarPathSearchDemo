package com.example.astarpathsearch.AStar;

import android.support.annotation.NonNull;

import com.example.astarpathsearch.model.Node;

/**
 * Created by 彩笔怪盗基德 on 2016/1/27.
 * https://github.com/chenjj2048
 */
public abstract class AStarNode extends Node implements Comparable {
    public boolean hasVisited = false;
    public int walkedDistance = 0;
    public double lastDistance = 0;
    public int sequenceVisit = 0;
    public AStarNode from;

    public AStarNode(int column, int line, int value) {
        super(column, line, value);
    }

    @Override
    public String toString() {
        return "Node{" +
                "x= " + super.x + " y= " + super.y +", "+
                ", walkedDistance=" + walkedDistance +
                ", lastDistance=" + lastDistance +
                ", sequenceVisit=" + sequenceVisit +
                '}';
    }

    private double calcEvaluatedDistance(AStarNode target) {
        return Math.abs(super.x - target.x) + Math.abs(super.y - target.y);
    }

    public double calcTotalDistance(AStarNode target) {
        lastDistance = walkedDistance + AStarAlgorithm.AStarWeight * calcEvaluatedDistance(target);
        return lastDistance;
    }

    public void reset() {
        super.reset();
        from = null;
        hasVisited = false;
        walkedDistance = 0;
        lastDistance = 0;
        sequenceVisit = 0;
    }

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

}
