package jzwl.com.comzhmyapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 创建日期：2017/10/17
 * 描述:  自定义ViewGroup
 *
 * @author: zhaoh
 */
public class MyViewGroup extends ViewGroup {
    private static final String TAG = "MyViewGoup";
//用做布局文件时，一定要添加。
    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context) {
        this(context, null);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int height = 0;
        int count = getChildCount();
        View child;
        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            child.layout(0, height, child.getMeasuredWidth(), height + child.getMeasuredHeight());
            height += child.getMeasuredHeight();
        }


        Log.e(TAG, "------------------onLayout-----------------------");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "------------------onMeasure-----------------------");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "------------------onDraw-----------------------");
    }
}
