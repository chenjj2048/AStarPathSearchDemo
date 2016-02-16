package com.example.astarpathsearch.interfacer;

import com.example.astarpathsearch.model.AStarNode;

/**
 * Created by 彩笔怪盗基德 on 2016/2/14.
 * https://github.com/chenjj2048
 */
public interface IGridAdapter {
    void onSearchFinished(AStarNode[][] map, boolean success);

    void onConfigChanged(AStarNode[][] map);
}
