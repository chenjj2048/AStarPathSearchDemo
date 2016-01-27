package com.example.astarpathsearch.model;

import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.util.Log;

import com.example.astarpathsearch.AStar.AStarAlgorithm;
import com.example.astarpathsearch.AStar.AStarNode;
import com.example.astarpathsearch.model.GridModel.LinkedPoint;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by 彩笔怪盗基德 on 2016/1/24.
 * https://github.com/chenjj2048
 */
public class GridModel implements Iterable<LinkedPoint> {
    private static final int MIN_LINES = 3;
    private static final int MIN_COLUMNS = 3;
    private static final int MAX_LINES = 80;
    private static final int MAX_COLUMNS = 80;
    private final int LINES;
    private final int COLUMNS;
    private final LinkedPoint map[][];
    public LinkedPoint startPoint;
    public LinkedPoint endPoint;
    private Random random = new Random();

    public GridModel(int columns, int lines) {
        this.LINES = constraints(lines, MIN_LINES, MAX_LINES);
        this.COLUMNS = constraints(columns, MIN_COLUMNS, MAX_COLUMNS);
        Log.i("网格初始化", String.format("行数=%d 列数=%d", this.LINES, this.COLUMNS));

        map = new LinkedPoint[this.LINES][this.COLUMNS];
        for (int i = 0; i < this.LINES; i++)
            for (int j = 0; j < this.COLUMNS; j++)
                map[i][j] = new LinkedPoint(j, i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(LINES * COLUMNS * 2);
        LinkedPoint point;
        for (int i = 0; i < this.LINES; i++) {
            for (int j = 0; j < this.COLUMNS; j++) {
                point = map[i][j];
                sb.append(PointType.getSymbol(point.pointType));
            }
            if (i != this.LINES - 1)
                sb.append("\r\n");
        }
        return sb.toString();
    }

    @Override
    public Iterator<LinkedPoint> iterator() {
        return new GridModelIterator();
    }

    private int constraints(int value, int min, int max) {
        return (value < min) ? min : (value > max ? max : value);
    }

    public void reset() {
        Iterator<LinkedPoint> iterator = this.iterator();
        while (iterator.hasNext())
             iterator.next().reset();
    }

    public void setRandomObstacle(@FloatRange(from = 0, to = 1) double obstacleRatio) {
        Iterator<LinkedPoint> iterator = this.iterator();
        while (iterator.hasNext())
            if (random.nextFloat() < obstacleRatio)
                iterator.next().pointType = PointType.OBSTACLE;
            else
                iterator.next().pointType = PointType.BLANK;
    }

    public void setStartPointRandom() {
        int line = Math.abs(random.nextInt()) % LINES;
        int column = Math.abs(random.nextInt()) % COLUMNS;
        setStartPoint(line, column);
    }

    public void setStartPoint(int line, int column) {
        if (column != constraints(column, 0, COLUMNS - 1) || line != constraints(line, 0, LINES - 1))
            throw new RuntimeException("设置的数组越界啦");
        if (startPoint != null)
            startPoint.pointType = PointType.BLANK;
        startPoint = map[line][column];
        startPoint.pointType = PointType.START_POINT;
        ensurePointsDifferent();
    }

    public void setEndPointRandom() {
        int line = Math.abs(random.nextInt()) % LINES;
        int column = Math.abs(random.nextInt()) % COLUMNS;
        setEndPoint(line, column);
    }

    public void setEndPoint(int line, int column) {
        if (column != constraints(column, 0, COLUMNS - 1) || line != constraints(line, 0, LINES - 1))
            throw new RuntimeException("设置的数组越界啦");
        if (endPoint != null)
            endPoint.pointType = PointType.BLANK;
        endPoint = map[line][column];
        endPoint.pointType = PointType.END_POINT;
        ensurePointsDifferent();
    }

    private void ensurePointsDifferent() {
        while (startPoint.equals(endPoint))
            setEndPointRandom();
    }

    public void startAStartSearch(double aStarWeight) {
        AStarAlgorithm.startSearch(this, aStarWeight);

    }

    public interface GridInterface {
        RectF getSizeWithoutMargin();
    }

    /**
     * 精简版的迭代器，不支持多线程，不支持删除
     */
    private class GridModelIterator implements Iterator<LinkedPoint> {
        private int currentLine = 0;
        private int currentColumn = 0;

        @Override
        public boolean hasNext() {
            return currentLine < LINES;
        }

        @Override
        public LinkedPoint next() {
            if (currentLine >= LINES)
                throw new RuntimeException("没有更多了");
            int tmp_line = currentLine;
            int tmp_column = currentColumn;
            if (++currentColumn >= COLUMNS) {
                //换行
                currentColumn = 0;
                currentLine++;
            }
            return map[tmp_line][tmp_column];
        }

        @Override
        public void remove() {
            throw new RuntimeException("删除操作不可用");
        }
    }

    /**
     * 提供上下左右的节点,简化操作
     */
    public class LinkedPoint extends AStarNode {
        public LinkedPoint(int column, int line) {
            super(column, line, PointType.BLANK);
        }

        @Override
        public LinkedPoint leftPoint() {
            if (this.x > 0)
                return map[this.y][this.x - 1];
            else
                return null;
        }

        @Override
        public LinkedPoint rightPoint() {
            if (this.x < GridModel.this.COLUMNS - 1)
                return map[this.y][this.x + 1];
            else
                return null;
        }

        @Override
        public LinkedPoint abovePoint() {
            if (this.y > 0)
                return map[this.y - 1][this.x];
            else
                return null;
        }

        @Override
        public LinkedPoint belowPoint() {
            if (this.y < GridModel.this.LINES - 1)
                return map[this.y + 1][this.x];
            else
                return null;
        }
    }
}
