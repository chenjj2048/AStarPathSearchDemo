package com.example.astarpathsearch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.astarpathsearch.interfacer.IAStarView;
import com.example.astarpathsearch.interfacer.IGridAdapter;
import com.example.astarpathsearch.model.AStarNode;
import com.example.astarpathsearch.model.GridConfig;
import com.example.astarpathsearch.presenter.GridAdapter;

/**
 * Created by 彩笔怪盗基德 on 2016/2/14.
 * https://github.com/chenjj2048
 */
public class AStarView extends View implements IGridAdapter {
    public static final int STATE_IDLE = 0;
    public static final int STATE_SET_STARTPOINT = 1;
    public static final int STATE_SET_ENDPOINT = 2;
    public static final int STATE_SET_OBSTACLE = 3;
    @SuppressWarnings("unused")
    private static final String TAG = AStarView.class.getCanonicalName();
    private AStarNode[][] map;
    private int COLOR_BACKGROUND = Color.argb(255, 200, 200, 200);
    private GridAdapter mGrid;
    private Paint mPaint;
    private RectF mRect;
    private GridConfig mConfig = GridConfig.getInstance();

    private IAStarView mListener;

    private int mState = STATE_IDLE;

    public AStarView(Context context) {
        this(context, null);
    }

    public AStarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AStarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setState(int state) {
        if (mListener == null)
            throw new RuntimeException("请先绑定Listener");
        this.mState = state;
    }

    public void setOnAStarViewTouchListener(IAStarView listener) {
        this.mListener = listener;
    }

    private void init() {
        this.mGrid = new GridAdapter(this);
        this.mPaint = new Paint();
        this.mRect = new RectF();
    }

    public GridAdapter getAdapter() {
        return mGrid;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.map == null) return;

        drawBackground(canvas);
        for (AStarNode[] nodeArray : this.map)
            for (AStarNode node : nodeArray)
                drawPoint(canvas, node);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(COLOR_BACKGROUND);
    }

    private void drawPoint(Canvas canvas, AStarNode point) {
        calcRect(point, mRect);
        refreshPaintColor(point, mPaint);
        canvas.drawRect(mRect, mPaint);
    }

    private void calcRect(AStarNode point, RectF rect) {
        rect.left = (mConfig.getSizeSquare() + mConfig.getSizeGap()) * point.x;
        rect.right = rect.left + mConfig.getSizeSquare();
        rect.top = (mConfig.getSizeSquare() + mConfig.getSizeGap()) * point.y;
        rect.bottom = rect.top + mConfig.getSizeSquare();
    }

    private void refreshPaintColor(AStarNode point, Paint paint) {
        paint.setColor(point.getColor());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = mConfig.getSizeSquare() + mConfig.getSizeGap();
        int width = size * mConfig.getColumn() - mConfig.getSizeGap();
        int height = size * mConfig.getLine() - mConfig.getSizeGap();
        width = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        height = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mState == STATE_IDLE) return true;

        final int x = convertPositionToIndex(event.getX());
        final int y = convertPositionToIndex(event.getY());

        if (x < 0 || y < 0 || x >= mConfig.getColumn() || y >= mConfig.getLine())
            return true;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (mState == STATE_SET_STARTPOINT)
                    mListener.onStartPointSelect(x, y);
                else if (mState == STATE_SET_ENDPOINT)
                    mListener.onEndPointSelect(x, y);
                else if (mState == STATE_SET_OBSTACLE)
                    mListener.onObstaclePointSelect(x, y);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mListener.requestDraw();
                break;
            default:
        }
        return true;
    }

    private int convertPositionToIndex(float pos) {
        return (int) pos / (mConfig.getSizeSquare() + mConfig.getSizeGap());
    }

    @Override
    public void onSearchFinished(AStarNode[][] map, boolean success) {
        this.map = map;
        this.requestLayout();
        invalidate();
    }

    @Override
    public void onConfigChanged(AStarNode[][] map) {

    }
}
