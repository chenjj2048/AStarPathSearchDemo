package com.example.astarpathsearch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.astarpathsearch.model.GridModel;

/**
 * Created by 彩笔怪盗基德 on 2016/1/24.
 * https://github.com/chenjj2048
 */
public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread mDrawThread;
    private GridViewModel mGridViewModel;

    public CustomSurfaceView(Context context) {
        super(context);
        init();
    }

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        SurfaceHolder mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDrawThread.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initGrid();
        mDrawThread = new DrawThread(holder);
        mDrawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mDrawThread.stopSelf();
    }

    public void initGrid() {
        mGridViewModel = new GridViewModel(10, 20);
        mGridViewModel.setRandomValue(255);
    }

    private class DrawThread extends Thread {
        private final SurfaceHolder holder;
        int x;
        int y;
        Paint mPointPaint;
        private boolean isRunning;

        public DrawThread(SurfaceHolder holder) {
            isRunning = false;
            this.holder = holder;
            mPointPaint = new Paint();
        }

        public void stopSelf() {
            isRunning = false;
        }

        @Override
        public void run() {
            isRunning = true;
            Canvas canvas;
            while (isRunning) {
                synchronized (holder) {
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        drawBackground(canvas);
                        drawGrid(canvas, mGridViewModel);
                        holder.unlockCanvasAndPost(canvas);
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException();
                    }
                }
            }
        }

        private void drawBackground(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
        }

        private void drawGrid(Canvas canvas, GridModel gridModel) {
            for (int x = 0; x < gridModel.getXCounts(); x++)
                for (int y = 0; y < gridModel.getYCounts(); y++) {
                    mPointPaint.setColor(Color.argb(255, (x * y) % 256, 0, 0));
                    canvas.drawRect(gridModel.getPointRect(x, y), mPointPaint);
                }
        }

        public boolean onTouchEvent(MotionEvent event) {
            x = (int) event.getX();
            y = (int) event.getY();
            return true;
        }
    }

    class GridViewModel extends GridModel {
        private static final int MARGIN_X = 50;
        private static final int MARGIN_Y = 50;
        private static final int POINT_SIZE = 50;
        private static final float GAP = POINT_SIZE * 0.1f;

        public GridViewModel(int xCounts, int yCounts) {
            super(xCounts, yCounts);
        }

        @Override
        public RectF getSizeWithoutMargin() {
            float width = getXCounts() * (GAP + POINT_SIZE) - GAP;
            float height = getYCounts() * (GAP + POINT_SIZE) - GAP;
            return new RectF(0, 0, width, height);
        }

        @Override
        public RectF getPointRect(@IntRange(from = 0) int column, @IntRange(from = 0) int line) {
            final float left = MARGIN_X + column * (GAP + POINT_SIZE);
            final float top = MARGIN_Y + line * (GAP + POINT_SIZE);
            return new RectF(left, top, left + POINT_SIZE, top + POINT_SIZE);
        }


    }
}
