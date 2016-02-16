package com.example.astarpathsearch.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.astarpathsearch.presenter.GridAdapter.GridMap;

import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Created by 彩笔怪盗基德 on 2016/1/27.
 * https://github.com/chenjj2048
 */
public class AStarAlgorithm {
    private static final String TAG = AStarAlgorithm.class.getCanonicalName();

    private static void addPointToQueue(@NonNull PriorityQueue<AStarNode> queue,
                                        AStarNode previousNode, AStarNode targetNode, AStarNode point) {
        if (point == null || point.hasVisited || point.getPointType() == PointType.OBSTACLE)
            return;
        beforeAddToQueue(previousNode, targetNode, point);
        queue.offer(point);
    }

    private static void beforeAddToQueue(AStarNode previous, AStarNode targetNode, AStarNode point) {
        point.hasVisited = true;
        AStarNode.tickCount++;
        point.sequenceVisit = AStarNode.tickCount;
        point.previousNode = previous;
        if (previous != null)
            point.walkedDistance = previous.walkedDistance + 1;
        point.calcTotalDistance(targetNode);
    }

    public static boolean startSearch(@NonNull GridMap grid, double weight) {
        Log.d(TAG, "搜索权重：" + weight);
        beforeSearch(grid, weight);

        final AStarNode targetNode = grid.getEndPoint();
        PriorityQueue<AStarNode> queue = new PriorityQueue<>(200);
        addPointToQueue(queue, null, targetNode, grid.getStartPoint());
        while (queue.peek() != null) {
            AStarNode current = queue.poll();
            if (current.equals(grid.getEndPoint())) {
                //找到节点
                afterSearch(grid);
                return true;
            }
            //添加周边节点到队列
            addPointToQueue(queue, current, targetNode, grid.getLeftNode(current));
            addPointToQueue(queue, current, targetNode, grid.getRightNode(current));
            addPointToQueue(queue, current, targetNode, grid.getAboveNode(current));
            addPointToQueue(queue, current, targetNode, grid.getBelowNode(current));
        }
        Log.i(TAG, "无法抵达目标点");
        return false;
    }

    private static void beforeSearch(@NonNull GridMap grid, double weight) {
        AStarNode.setWeight(weight);
        AStarNode.tickCount = 0;

        if (grid.getStartPoint() == null || grid.getEndPoint() == null)
            throw new NullPointerException("请设置起点或终点");

        Iterator<AStarNode> iterator = grid.iterator();
        for (; iterator.hasNext(); ) {
            AStarNode node = iterator.next();
            node.hasVisited = false;
            int type = node.getPointType();
            if (type == PointType.ROUTE_FOUND || type == PointType.ROUTE_VISITED)
                node.setPointType(PointType.BLANK);
        }
    }

    private static void afterSearch(GridMap grid) {
        AStarNode node;
        //遍历设置ROUTE_VISITED属性
        Iterator<AStarNode> iterator = grid.iterator();
        for (; iterator.hasNext(); ) {
            node = iterator.next();
            if (!node.hasVisited) continue;
            if (node.getPointType() != PointType.BLANK) continue;
            node.setPointType(PointType.ROUTE_VISITED);
        }

        //从终点倒推路径设置ROUTE_FOUND属性
        node = grid.getEndPoint().previousNode;
        final AStarNode startNode = grid.getStartPoint();
        while (node != null && !node.equals(startNode)) {
            node.setPointType(PointType.ROUTE_FOUND);
            node = node.previousNode;
        }

//        printRoute(grid);
    }

    @SuppressWarnings("unused")
    private static void printRoute(GridMap grid) {
        Log.i(TAG, "路径规划成功,路径如下：");
        printPreviousNode(grid, grid.getEndPoint());
    }

    private static void printPreviousNode(GridMap grid, AStarNode current) {
        if (!current.equals(grid.getStartPoint()))
            printPreviousNode(grid, current.previousNode);
        Log.i(TAG, current.toString());
    }

}
