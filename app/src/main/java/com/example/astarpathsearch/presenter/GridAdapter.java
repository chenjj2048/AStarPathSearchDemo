package com.example.astarpathsearch.presenter;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.astarpathsearch.interfacer.IGridAdapter;
import com.example.astarpathsearch.model.AStarAlgorithm;
import com.example.astarpathsearch.model.AStarNode;
import com.example.astarpathsearch.model.PointType;

import java.util.Iterator;
import java.util.Random;

import static com.example.astarpathsearch.util.MathUtil.constraints;

/**
 * Created by 彩笔怪盗基德 on 2016/1/24.
 * https://github.com/chenjj2048
 */
public class GridAdapter implements Iterable<AStarNode> {
    private GridMap mGrid;
    private IGridAdapter mListener;
    private Random mRandom;

    public GridAdapter(IGridAdapter listener) {
        this.mListener = listener;
        this.mRandom = new Random();
        this.mGrid = new GridMap();
    }

    @Override
    public Iterator<AStarNode> iterator() {
        return mGrid.iterator();
    }

    private void notifyGridChanged() {
        if (mListener != null)
            mListener.onConfigChanged(mGrid.map);
    }

    public void setGridSize(int column, int line) {
        mGrid.initGridNodes(line, column);
        Log.i("网格初始化", String.format("行数=%d 列数=%d", mGrid.getLines(), mGrid.getColumns()));
        notifyGridChanged();
    }

    public void setObstacle(int line, int column) {
        AStarNode node = mGrid.getNode(line, column);
        int type = node.getPointType();
        if (type != PointType.START_POINT && type != PointType.END_POINT)
            node.setPointType(PointType.OBSTACLE);
        notifyGridChanged();
    }

    public void setObstacleRatio(@FloatRange(from = 0, to = 1) double obstacleRatio) {
        Iterator<AStarNode> iterator = this.iterator();
        for (; iterator.hasNext(); ) {
            AStarNode node = iterator.next();
            int type = node.getPointType();
            if (type == PointType.START_POINT || type == PointType.END_POINT) continue;
            if (mRandom.nextFloat() < obstacleRatio)
                node.setPointType(PointType.OBSTACLE);
            else
                node.setPointType(PointType.BLANK);
        }
        notifyGridChanged();
    }

    public void setStartPoint(int line, int column) {
        mGrid.setStartPoint(line, column);
        ensurePointsDifferent();
    }

    public void setEndPoint(int line, int column) {
        mGrid.setEndPoint(line, column);
        ensurePointsDifferent();
    }

    public void setStartPointRandom() {
        int line = Math.abs(mRandom.nextInt()) % mGrid.getLines();
        int column = Math.abs(mRandom.nextInt()) % mGrid.getColumns();
        setStartPoint(line, column);
    }

    public void setEndPointRandom() {
        int line = Math.abs(mRandom.nextInt()) % mGrid.getLines();
        int column = Math.abs(mRandom.nextInt()) % mGrid.getColumns();
        setEndPoint(line, column);
    }

    /**
     * @param aStarWeight (-infinity，+infinity)
     *                    <0:偏向广度优先
     *                    >0:指向性更强，但可能并非最短路径
     */
    public void startAStarSearch(double aStarWeight) {
        boolean success = AStarAlgorithm.startSearch(mGrid, aStarWeight);
        if (mListener != null)
            mListener.onSearchFinished(mGrid.map, success);
    }

    private void ensurePointsDifferent() {
        while (mGrid.getStartPoint().equals(mGrid.getEndPoint()))
            setEndPointRandom();
        notifyGridChanged();
    }

    public static class GridMap implements Iterable<AStarNode> {
        private int lines;
        private int columns;
        private AStarNode map[][];
        private AStarNode startPoint;
        private AStarNode endPoint;

        @Override
        public Iterator<AStarNode> iterator() {
            return new GridIterator();
        }

        public AStarNode getStartPoint() {
            return startPoint;
        }

        private void setStartPoint(AStarNode newNode) {
            if (this.startPoint != null)
                this.startPoint.setPointType(PointType.BLANK);

            newNode.setPointType(PointType.START_POINT);
            this.startPoint = newNode;
        }

        public void setStartPoint(int line, int column) {
            if (column != constraints(column, 0, this.columns - 1) || line != constraints(line, 0, this.lines - 1))
                throw new RuntimeException("设置的数组越界啦");

            AStarNode node = getNode(line, column);
            setStartPoint(node);
        }

        public AStarNode getEndPoint() {
            return endPoint;
        }

        private void setEndPoint(AStarNode newNode) {
            if (this.endPoint != null)
                this.endPoint.setPointType(PointType.BLANK);

            newNode.setPointType(PointType.END_POINT);
            this.endPoint = newNode;
        }

        public void setEndPoint(int line, int column) {
            if (column != constraints(column, 0, this.columns - 1) || line != constraints(line, 0, this.lines - 1))
                throw new RuntimeException("设置的数组越界啦");

            AStarNode node = getNode(line, column);
            setEndPoint(node);
        }

        public AStarNode getNode(int line, int column) {
            if (map == null)
                throw new NullPointerException("请初始化节点");
            return map[line][column];
        }

        public int getLines() {
            return this.lines;
        }

        public int getColumns() {
            return this.columns;
        }

        public void initGridNodes(int lines, int columns) {
            this.lines = lines;
            this.columns = columns;
            this.map = new AStarNode[lines][columns];
            for (int i = 0; i < lines; i++)
                for (int j = 0; j < columns; j++)
                    this.map[i][j] = new AStarNode(j, i);
        }

        public AStarNode getLeftNode(@NonNull AStarNode current) {
            if (current.x > 0)
                return map[current.y][current.x - 1];
            else
                return null;
        }

        public AStarNode getRightNode(AStarNode current) {
            if (current.x < this.columns - 1)
                return map[current.y][current.x + 1];
            else
                return null;
        }

        public AStarNode getAboveNode(AStarNode current) {
            if (current.y > 0)
                return map[current.y - 1][current.x];
            else
                return null;
        }

        public AStarNode getBelowNode(AStarNode current) {
            if (current.y < this.lines - 1)
                return map[current.y + 1][current.x];
            else
                return null;
        }

        public class GridIterator implements Iterator<AStarNode> {
            private int currentLine = 0;
            private int currentColumn = 0;

            @Override
            public boolean hasNext() {
                return currentLine < GridMap.this.getLines();
            }

            @Override
            public AStarNode next() {
                if (currentLine >= GridMap.this.getLines())
                    throw new RuntimeException("没有更多了");
                int temp_line = currentLine;
                int temp_column = currentColumn;
                if (++currentColumn >= GridMap.this.getColumns()) {
                    //换行
                    currentColumn = 0;
                    currentLine++;
                }
                return GridMap.this.getNode(temp_line, temp_column);
            }

            @Override
            public void remove() {
                throw new RuntimeException("删除操作不可用");
            }
        }
    }
}
