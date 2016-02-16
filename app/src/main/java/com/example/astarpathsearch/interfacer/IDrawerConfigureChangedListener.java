package com.example.astarpathsearch.interfacer;

import android.support.annotation.FloatRange;

/**
 * Created by 彩笔怪盗基德 on 2016/2/16.
 * https://github.com/chenjj2048
 */
public interface IDrawerConfigureChangedListener {
    void onSizeChanged(int column, int line);

    void onGridSizeChanged(int size);

    void onAStarWeightChanged(double weight);

    void onObstacleRatioChanged(@FloatRange(from = 0, to = 1) double ratio);
}
