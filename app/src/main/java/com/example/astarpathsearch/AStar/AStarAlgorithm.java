package com.example.astarpathsearch.AStar;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.astarpathsearch.model.GridModel;
import com.example.astarpathsearch.model.GridModel.LinkedPoint;
import com.example.astarpathsearch.model.PointType;

import java.util.PriorityQueue;

/**
 * Created by 彩笔怪盗基德 on 2016/1/27.
 * https://github.com/chenjj2048
 */
public class AStarAlgorithm {
    private static final String TAG = AStarAlgorithm.class.getSimpleName();
    public static double AStarWeight = 1;
    public static LinkedPoint startPoint;
    public static LinkedPoint targetPoint;
    private static int sequenceVisit = 0;

    public static boolean startSearch(@NonNull GridModel gridModel, @FloatRange(from = 0) double aStarWeight) {
        AStarAlgorithm.AStarWeight = aStarWeight;
        startPoint = gridModel.startPoint;
        targetPoint = gridModel.endPoint;
        sequenceVisit = 0;
        if (startPoint == null || targetPoint == null)
            throw new NullPointerException("请设置起点或终点");

        PriorityQueue<LinkedPoint> queue = new PriorityQueue<>(50);
        addPointToQueue(queue, gridModel.startPoint, gridModel.startPoint);
        while (queue.peek() != null) {
            LinkedPoint current = queue.poll();
            if (current.equals(gridModel.endPoint)) {
                //找到节点
                Log.i(TAG, "路径规划成功");
                return true;
            }
            //添加周边节点到队列
            addPointToQueue(queue, current, current.leftPoint());
            addPointToQueue(queue, current, current.rightPoint());
            addPointToQueue(queue, current, current.abovePoint());
            addPointToQueue(queue, current, current.belowPoint());
        }
        Log.i(TAG, "无法抵达目标点");
        return false;
    }

    private static void addPointToQueue(@NonNull PriorityQueue<LinkedPoint> queue,
                                        LinkedPoint from, LinkedPoint point) {
        if (point == null || point.hasVisited || point.pointType == PointType.OBSTACLE)
            return;
        point.hasVisited = true;
        point.sequenceVisit = sequenceVisit++;
        point.from = from;
        if (from !=startPoint)
            point.walkedDistance = from.walkedDistance + 1;
        point.calcTotalDistance(targetPoint);
        Log.i(TAG, point.toString());

        //添加
        queue.offer(point);
    }


}
