package com.example.astarpathsearch.view;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.astarpathsearch.R;
import com.example.astarpathsearch.interfacer.IAStarView;
import com.example.astarpathsearch.interfacer.IDrawerConfigureChangedListener;
import com.example.astarpathsearch.model.GridConfig;
import com.example.astarpathsearch.presenter.GridAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContentFragment extends Fragment implements
        IDrawerConfigureChangedListener, IAStarView {

    @Bind(R.id.button_setstartpoint)
    Button mButtonSetStartPoint;
    @Bind(R.id.button_setendpoint)
    Button mButtonSetEndPoint;
    @Bind(R.id.button_setobstacle)
    Button mButtonSetObstacle;

    private GridConfig mGridConfig = GridConfig.getInstance();
    private OnFragmentInteractionListener mListener;
    private GridAdapter mAdapter;
    private AStarView mAStarView;

    public ContentFragment() {
    }

    private void setCurrentButton(Button button) {
        changeButtonState(mButtonSetStartPoint, false);
        changeButtonState(mButtonSetEndPoint, false);
        changeButtonState(mButtonSetObstacle, false);
        changeButtonState(button, true);
    }

    private void changeButtonState(Button button, boolean selected) {
        if (button == null) return;
        if (selected)
            button.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        else
            button.setTextColor(Color.DKGRAY);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.button_clear)
    void gridClear() {
        //mAdapter.setGridSize(mGridConfig.getColumn(), mGridConfig.getLine());
        mAdapter.setObstacleRatio(0);
        //mAdapter.setStartPointRandom();
        //mAdapter.setEndPointRandom();
        mAdapter.startAStarSearch(mGridConfig.getWeight());

        setCurrentButton(null);
        mAStarView.setState(AStarView.STATE_IDLE);
    }

    @OnClick(R.id.button_randomlayout)
    void randomLayout() {
        mAdapter.setGridSize(mGridConfig.getColumn(), mGridConfig.getLine());
        mAdapter.setObstacleRatio(mGridConfig.getObscatleRatio());
        mAdapter.setStartPointRandom();
        mAdapter.setEndPointRandom();
        mAdapter.startAStarSearch(mGridConfig.getWeight());

        setCurrentButton(null);
        mAStarView.setState(AStarView.STATE_IDLE);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.button_setting)
    void openSettings() {
        mListener.openSettings();

        setCurrentButton(null);
        mAStarView.setState(AStarView.STATE_IDLE);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.button_setstartpoint)
    void setStartPoint() {
        setCurrentButton(mButtonSetStartPoint);
        mAStarView.setState(AStarView.STATE_SET_STARTPOINT);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.button_setendpoint)
    void setEndPoint() {
        setCurrentButton(mButtonSetEndPoint);
        mAStarView.setState(AStarView.STATE_SET_ENDPOINT);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.button_setobstacle)
    void setObstaclePoint() {
        setCurrentButton(mButtonSetObstacle);
        mAStarView.setState(AStarView.STATE_SET_OBSTACLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        mAStarView = (AStarView) view.findViewById(R.id.astarview);
        mAdapter = mAStarView.getAdapter();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getActivity());

        mAStarView.setOnAStarViewTouchListener(this);
        if (getActivity() instanceof OnFragmentInteractionListener)
            mListener = (OnFragmentInteractionListener) getActivity();
        else
            throw new RuntimeException("请添加该接口");

        randomLayout();
    }

    @Override
    public void onSizeChanged(int column, int line) {
        mAdapter.setGridSize(column, line);
        mAdapter.setObstacleRatio(0);
        //mAdapter.setStartPointRandom();
        //mAdapter.setEndPointRandom();
        mAdapter.startAStarSearch(mGridConfig.getWeight());
    }

    @Override
    public void onGridSizeChanged(int size) {
        //mAdapter.setGridSize(mGridConfig.getColumn(), mGridConfig.getLine());
        //mAdapter.setObstacleRatio(0);
        //mAdapter.setStartPointRandom();
        //mAdapter.setEndPointRandom();
        mAdapter.startAStarSearch(mGridConfig.getWeight());
    }

    @Override
    public void onAStarWeightChanged(double weight) {
        //mAdapter.setGridSize(mGridConfig.getColumn(), mGridConfig.getLine());
        //mAdapter.setObstacleRatio(0);
        //mAdapter.setStartPointRandom();
        //mAdapter.setEndPointRandom();
        mAdapter.startAStarSearch(weight);
    }

    @Override
    public void onObstacleRatioChanged(@FloatRange(from = 0, to = 1) double ratio) {
        //mAdapter.setGridSize(mGridConfig.getColumn(), mGridConfig.getLine());
        mAdapter.setObstacleRatio(ratio);
        //mAdapter.setStartPointRandom();
        //mAdapter.setEndPointRandom();
        mAdapter.startAStarSearch(mGridConfig.getWeight());
    }

    @Override
    public void onStartPointSelect(int x, int y) {
        mAdapter.setStartPoint(y, x);
        mAdapter.startAStarSearch(mGridConfig.getWeight());
    }

    @Override
    public void onEndPointSelect(int x, int y) {
        mAdapter.setEndPoint(y, x);
        mAdapter.startAStarSearch(mGridConfig.getWeight());
    }

    @Override
    public void onObstaclePointSelect(int x, int y) {
        mAdapter.setObstacle(y, x);
    }

    @Override
    public void requestDraw() {
        mAdapter.startAStarSearch(mGridConfig.getWeight());
    }

    public interface OnFragmentInteractionListener {
        void openSettings();
    }
}
