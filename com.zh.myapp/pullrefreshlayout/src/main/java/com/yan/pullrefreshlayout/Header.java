package com.yan.pullrefreshlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

/**
 * Created by yan on 2017/7/4.
 */

public class Header extends PullRefreshView  {

    private ImageView loadingView;
    private String animationName;
    private boolean isStateFinish;
    private int color;
    private boolean isHolding;
    private int[] whitePullingDownResId = {
            R.mipmap.refresh_white_pullingdown_00001, R.mipmap.refresh_white_pullingdown_00002, R.mipmap.refresh_white_pullingdown_00003,
            R.mipmap.refresh_white_pullingdown_00004, R.mipmap.refresh_white_pullingdown_00005, R.mipmap.refresh_white_pullingdown_00006,
            R.mipmap.refresh_white_pullingdown_00007, R.mipmap.refresh_white_pullingdown_00008, R.mipmap.refresh_white_pullingdown_00009,
            R.mipmap.refresh_white_pullingdown_00010, R.mipmap.refresh_white_pullingdown_00011, R.mipmap.refresh_white_pullingdown_00012,
            R.mipmap.refresh_white_pullingdown_00013, R.mipmap.refresh_white_pullingdown_00014, R.mipmap.refresh_white_pullingdown_00015,
            R.mipmap.refresh_white_pullingdown_00016, R.mipmap.refresh_white_pullingdown_00017, R.mipmap.refresh_white_pullingdown_00018,
            R.mipmap.refresh_white_pullingdown_00019, R.mipmap.refresh_white_pullingdown_00020, R.mipmap.refresh_white_pullingdown_00021,
            R.mipmap.refresh_white_pullingdown_00022, R.mipmap.refresh_white_pullingdown_00023, R.mipmap.refresh_white_pullingdown_00024,
            R.mipmap.refresh_white_pullingdown_00025, R.mipmap.refresh_white_pullingdown_00025, R.mipmap.refresh_white_pullingdown_00027,
            R.mipmap.refresh_white_pullingdown_00028, R.mipmap.refresh_white_pullingdown_00029
    };
    private int[] greenPullingDownResId = {
            R.mipmap.refresh_green_pullingdown_00001, R.mipmap.refresh_green_pullingdown_00002, R.mipmap.refresh_green_pullingdown_00003,
            R.mipmap.refresh_green_pullingdown_00004, R.mipmap.refresh_green_pullingdown_00005, R.mipmap.refresh_green_pullingdown_00006,
            R.mipmap.refresh_green_pullingdown_00007, R.mipmap.refresh_green_pullingdown_00008, R.mipmap.refresh_green_pullingdown_00009,
            R.mipmap.refresh_green_pullingdown_00010, R.mipmap.refresh_green_pullingdown_00011, R.mipmap.refresh_green_pullingdown_00012,
            R.mipmap.refresh_green_pullingdown_00013, R.mipmap.refresh_green_pullingdown_00014, R.mipmap.refresh_green_pullingdown_00015,
            R.mipmap.refresh_green_pullingdown_00016, R.mipmap.refresh_green_pullingdown_00017, R.mipmap.refresh_green_pullingdown_00018,
            R.mipmap.refresh_green_pullingdown_00019, R.mipmap.refresh_green_pullingdown_00020, R.mipmap.refresh_green_pullingdown_00021,
            R.mipmap.refresh_green_pullingdown_00022, R.mipmap.refresh_green_pullingdown_00023, R.mipmap.refresh_green_pullingdown_00024,
            R.mipmap.refresh_green_pullingdown_00025, R.mipmap.refresh_green_pullingdown_00025, R.mipmap.refresh_green_pullingdown_00027,
            R.mipmap.refresh_green_pullingdown_00028, R.mipmap.refresh_green_pullingdown_00029
    };


    public Header(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(contentView(), this, true);
        initView();


    }

    public Header(Context context) {
        super(context);

    }

    public Header(Context context, String animationName, int color) {
        this(context, animationName, color, true);
    }

    public Header(Context context, String animationName, int color, boolean withBg) {
        super(context);
        this.color = color;
    }

    @Override
    protected int contentView() {
        return R.layout.refresh_view;
    }

    @Override
    protected void initView() {
        loadingView = (ImageView) findViewById(R.id.loading_view);
    }

    @Override
    public void onPullChange(float percent) {
        super.onPullChange(percent);
        System.out.println("onPullChange percent = " + percent);
        if (isStateFinish || isHolding){
            return;
        }
        percent = Math.abs(percent);
        loadingView.setBackgroundResource(0);
        if (percent >= 1) {
            loadingView.setBackgroundResource(greenPullingDownResId[greenPullingDownResId.length - 1]);
        } else {
            loadingView.setBackgroundResource(greenPullingDownResId[(int) (greenPullingDownResId.length * percent)]);
        }

    }

    @Override
    public void onPullHoldTrigger() {
        super.onPullHoldTrigger();
        System.out.println("onPullHoldTrigger");
        loadingView.setImageResource(0);
        loadingView.setBackgroundResource(R.drawable.anim_green_pullingreleasing);
        AnimationDrawable animationDrawable = (AnimationDrawable) loadingView.getBackground();
        animationDrawable.stop();
        animationDrawable.start();
    }

    @Override
    public void onPullHoldUnTrigger() {
        super.onPullHoldUnTrigger();
        System.out.println("onPullHoldUnTrigger");
    }

    @Override
    public void onPullHolding() {
        super.onPullHolding();
        isHolding = true;
        System.out.println("onPullHolding");
        loadingView.setImageResource(0);
        loadingView.setBackgroundResource(R.drawable.anim_green_pullingreleasing);
        AnimationDrawable animationDrawable = (AnimationDrawable) loadingView.getBackground();
        animationDrawable.stop();
        animationDrawable.start();
    }

    @Override
    public void onPullFinish() {

    }

    @Override
    public void onPullReset() {
        super.onPullReset();
        //结束
        isStateFinish = false;
        isHolding = false;
        System.out.println("onPullReset");
        loadingView.setBackgroundResource(0);
    }

    @Override
    public void getRefrishState(final ValueAnimator resetHeaderAnimator) {
        // 加载中完成
        loadingView.setBackgroundResource(0);
        AnimationDrawable animationDrawable = (AnimationDrawable) loadingView.getBackground();
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
        loadingView.setBackgroundResource(R.drawable.anim_green_pullingfinish);
        animationDrawable = (AnimationDrawable) loadingView.getBackground();
        animationDrawable.stop();
        animationDrawable.start();
        System.out.println("onPullFinish");
        postDelayed(new Runnable() {
            @Override
            public void run() {
                resetHeaderAnimator.start();
            }
        }, 1000);


    }
}