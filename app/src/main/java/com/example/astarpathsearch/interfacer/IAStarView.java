package com.example.astarpathsearch.interfacer;

/**
 * Created by 彩笔怪盗基德 on 2016/2/14.
 * https://github.com/chenjj2048
 */
public interface IAStarView {
    void onStartPointSelect(int x, int y);

    void onEndPointSelect(int x, int y);

    void onObstaclePointSelect(int x, int y);

    void requestDraw();
}
