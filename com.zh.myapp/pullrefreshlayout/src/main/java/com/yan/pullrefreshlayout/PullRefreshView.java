package com.yan.pullrefreshlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * Created by yan on 2017/4/11.
 */
public class PullRefreshView extends FrameLayout implements PullRefreshLayout.OnPullListener {
    private static final String TAG = "PullView";

    public PullRefreshView(Context context) {
        super(context);
        if (contentView() == -1) {
            throw new RuntimeException("must override method contentView");
        }
        LayoutInflater.from(getContext()).inflate(contentView(), this, true);
        initView();
    }

    public PullRefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PullRefreshView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected int contentView() {
        return -1;
    }

    protected void initView() {
    }

    @Override
    public void onPullChange(float percent) {
        Log.e(TAG, "onPullChange: " + percent);
    }

    @Override
    public void onPullHoldTrigger() {
        Log.e(TAG, "onPullHoldTrigger: ");
    }

    @Override
    public void onPullHoldUnTrigger() {
        Log.e(TAG, "onPullHoldUnTrigger: ");
    }

    @Override
    public void onPullHolding() {
        Log.e(TAG, "onPullHolding: ");
    }

    @Override
    public void onPullFinish() {
        Log.e(TAG, "onPullFinish: ");
    }

    @Override
    public void onPullReset() {
        Log.e(TAG, "onPullReset: ");
    }

    @Override
    public void getRefrishState(ValueAnimator resetHeaderAnimator) {

    }

}
