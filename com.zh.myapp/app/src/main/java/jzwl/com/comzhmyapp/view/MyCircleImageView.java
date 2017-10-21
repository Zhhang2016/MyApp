package jzwl.com.comzhmyapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jzwl.com.comzhmyapp.R;

/**
 * 创建日期：2017/10/17
 * 描述:自定义的圆形图片
 *
 * @author: zhaoh
 */
public class MyCircleImageView extends ImageView {

    private int type = 0;
    /**
     * 圆角大小的默认值
     */

    private Paint mBitmapPaint;
    private static final int BODER_RADIUS_DEFAULT = 10;
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;
    private int mBorderRadius = BODER_RADIUS_DEFAULT;

    //自定义注解Type的类型
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_CIRCLE, TYPE_ROUND})
    public @interface Type {
    }

    public MyCircleImageView(Context context) {
        this(context, null);
    }

    public MyCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyCircleImageView);
        type = a.getDimensionPixelSize(R.styleable.MyCircleImageView_type, TYPE_CIRCLE);
        mBorderRadius = a.getDimensionPixelSize(R.styleable.MyCircleImageView_borderRadius, BODER_RADIUS_DEFAULT);
        a.recycle();
        init();

    }
    private void init() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
    }
}
