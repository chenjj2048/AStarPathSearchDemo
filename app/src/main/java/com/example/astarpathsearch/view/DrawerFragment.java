package com.example.astarpathsearch.view;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.astarpathsearch.R;
import com.example.astarpathsearch.interfacer.IDrawerConfigureChangedListener;
import com.example.astarpathsearch.model.AStarNode;
import com.example.astarpathsearch.model.GridConfig;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawerFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
    @Bind(R.id.tv_size_x)
    TextView mTextViewXCount;
    @Bind(R.id.tv_size_y)
    TextView mTextViewYCount;
    @Bind(R.id.tv_grid_size)
    TextView mTextViewGridSize;
    @Bind(R.id.tv_astar_weight)
    TextView mTextViewWeight;
    @Bind(R.id.seekbar_size_x)
    SeekBar mSeekBarXCount;
    @Bind(R.id.seekbar_size_y)
    SeekBar mSeekBarYCount;
    @Bind(R.id.seekbar_grid_size)
    SeekBar mSeekBarGridSize;
    @Bind(R.id.seekbar_astar_weight)
    SeekBar mSeekBarAStarWeight;
    @Bind(R.id.seekbar_obstacle_ratio)
    SeekBar mSeekBarObstacleRatio;
    @Bind(R.id.seekbar_alpha)
    SeekBar mSeekBarAlpha;

    private GridConfig mGridConfig;

    private IDrawerConfigureChangedListener mListener;

    public DrawerFragment() {
    }

    public void setOnConfigureChangedListener(IDrawerConfigureChangedListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGridConfig = GridConfig.getInstance();
        ButterKnife.bind(this, getActivity());

        mSeekBarXCount.setOnSeekBarChangeListener(this);
        mSeekBarYCount.setOnSeekBarChangeListener(this);
        mSeekBarGridSize.setOnSeekBarChangeListener(this);
        mSeekBarAStarWeight.setOnSeekBarChangeListener(this);
        mSeekBarObstacleRatio.setOnSeekBarChangeListener(this);
        mSeekBarAlpha.setOnSeekBarChangeListener(this);

        mTextViewXCount.setTag(mTextViewXCount.getText());
        mTextViewYCount.setTag(mTextViewYCount.getText());
        mTextViewGridSize.setTag(mTextViewGridSize.getText());

        if (this.mListener == null)
            throw new RuntimeException("请添加该接口");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drawer, container, false);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekbar_size_x:
                resizeX(progress);
                break;
            case R.id.seekbar_size_y:
                resizeY(progress);
                break;
            case R.id.seekbar_grid_size:
                resizeSingleGridSize(progress);
                break;
            case R.id.seekbar_astar_weight:
                resizeAStarWeight(progress);
                break;
            case R.id.seekbar_obstacle_ratio:
                resizeObstacleRatio(progress);
                break;
            case R.id.seekbar_alpha:
                changeViewAlpha(progress);
                break;
            default:
        }
    }

    private void changeViewAlpha(int progress) {
        @FloatRange(from = 0.4, to = 1)
        final double alpha = progress / 100.0 * 0.6 + 0.4;

        if (this.getView() != null)
            this.getView().setBackgroundColor(Color.argb((int) (alpha * 255), 255, 255, 255));
    }

    private void resizeObstacleRatio(int progress) {
        @FloatRange(from = 0, to = 1)
        final double ratio = progress / 100.0;
        mGridConfig.setObscatleRatio(ratio);

        mListener.onObstacleRatioChanged(ratio);
    }

    private void resizeAStarWeight(int progress) {
        @FloatRange(from = -5, to = 5)
        final double weight = (progress - 50) / 10.0;
        mGridConfig.setWeight(weight);

        AStarNode.setWeight(weight);
        @FloatRange(from = 0, to = 1)
        final double weight2 = AStarNode.getWeight();
        mTextViewWeight.setText(String.format("权重比值 = %f", weight2 / (1 - weight2)));

        mListener.onAStarWeightChanged(mGridConfig.getWeight());
    }

    private void resizeSingleGridSize(int progress) {
        final String str;
        mGridConfig.setSizeSquare(progress);
        str = mTextViewGridSize.getTag().toString() + ":" + mGridConfig.getSizeSquare();
        if (mTextViewGridSize.getText().equals(str))
            return;
        else
            mTextViewGridSize.setText(str);

        mListener.onGridSizeChanged(mGridConfig.getSizeSquare());
    }


    private void resizeX(int progress) {
        final String str;
        mGridConfig.setColumn(progress);
        str = mTextViewXCount.getTag().toString() + ":" + mGridConfig.getColumn();
        if (mTextViewXCount.getText().equals(str))
            return;
        else
            mTextViewXCount.setText(str);

        mListener.onSizeChanged(mGridConfig.getColumn(), mGridConfig.getLine());
    }

    private void resizeY(int progress) {
        final String str;
        mGridConfig.setLine(progress);
        str = mTextViewYCount.getTag().toString() + ":" + mGridConfig.getLine();
        if (mTextViewYCount.getText().equals(str))
            return;
        else
            mTextViewYCount.setText(str);

        mListener.onSizeChanged(mGridConfig.getColumn(), mGridConfig.getLine());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }


}
