package me.iwf.photopicker.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Locale;

import me.iwf.photopicker.R;

/**
 * @author ztn
 * @version V_5.0.0
 * @date 2016年2月19日
 * @description 加载图片工具类
 */
public class LoadingImgUtil {

    public static void displayImage(ImageView imageView, String url, boolean isCenterCrop,
                                    ImageSize imageSize, float thumbnail, int defaultImgResId,
                                    boolean isDontAnimate, boolean isDontTransform) {
        try {
            if (imageView == null || imageView.getContext() == null)
                return;
            if (imageView.getContext() instanceof Activity) {
                if (((Activity) imageView.getContext()).isFinishing()) {
                    return;
                }
            }
            BitmapRequestBuilder<String, Bitmap> error = Glide.with(imageView.getContext()).load(url)
                    .asBitmap()
                    .placeholder(defaultImgResId > 0 ? defaultImgResId : R.drawable.__picker_default_weixin)
                    .error(defaultImgResId > 0 ? defaultImgResId : R.drawable.__picker_ic_broken_image_black_48dp);
            if (isCenterCrop) {
                error.centerCrop();
            }
            if (imageSize != null) {
                error.override(imageSize.width, imageSize.height);
            }
            if (thumbnail != 0) {
                error.thumbnail(thumbnail);
            }
            if (isDontAnimate) {
                error.dontAnimate();
            }
            if (isDontTransform) {
                error.dontTransform();
            }
            error.listener(new LoggingListener<String, Bitmap>());
            error.into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayImage(ImageView imageView, String url) {
        displayImage(imageView, url, false, null, 0, 0, false, false);
    }

    public static void displayImage(ImageView imageView, String url, boolean isCenterCrop, ImageSize imageSize, float thumbnail) {
        displayImage(imageView, url, isCenterCrop, imageSize, thumbnail, 0, false, false);
    }

    public static void displayImage(ImageView imageView, String url, float thumbnail, int defaultImgResId) {
        displayImage(imageView, url, true, null, thumbnail, defaultImgResId, false, false);
    }

    public static void displayImage(ImageView imageView, String url, ImageSize imageSize, float thumbnail, boolean isDontAnimate, boolean isDontTransform) {
        displayImage(imageView, url, true, imageSize, thumbnail, 0, isDontAnimate, isDontTransform);
    }

    public static void displayImage(ImageView imageView, String url, ImageSize imageSize, float thumbnail) {
        displayImage(imageView, url, true, imageSize, thumbnail, 0, false, false);
    }

    public static void resumeLoader(Context context) {
        Glide.with(context).resumeRequests();
    }

    public static void pauseLoader(Context context) {
        Glide.with(context).pauseRequests();
    }

    public static void clearView(View imageView) {
        Glide.clear(imageView);
    }

    public static void clearDiskCache(final Context context) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Glide.get(context).clearDiskCache();
            }
        }.start();
    }

    public static void clearMemoryCache(final Context context) {
        Glide.get(context).clearMemory();
    }

    public static class ImageSize {
        public int width = 0;
        public int height = 0;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    public static class LoggingListener<T, R> implements RequestListener<T, R> {

        public static final boolean DEBUG = false;

        @Override
        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            if (DEBUG) {
                android.util.Log.d("GLIDE", String.format(Locale.ROOT,
                        "onException(%s, %s, %s, %s)", e, model, target, isFirstResource), e);
            }
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
            if (DEBUG) {
                android.util.Log.d("GLIDE", String.format(Locale.ROOT,
                        "onResourceReady(%s, %s, %s, %s, %s)", resource, model, target, isFromMemoryCache, isFirstResource));
            }
            return false;
        }
    }

}
