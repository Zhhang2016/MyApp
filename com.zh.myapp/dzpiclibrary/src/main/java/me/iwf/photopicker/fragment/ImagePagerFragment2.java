package me.iwf.photopicker.fragment;

/**
 * @author dongzheng
 * @version V_5.0.0
 * @date 2016/8/24
 * @description
 */

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.iwf.photopicker.R;
import me.iwf.photopicker.adapter.PhotoPagerAdapter;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.utils.ImagePreference;

public class ImagePagerFragment2 extends Fragment {
    public final static String ARG_PATH = "PATHS";
    public final static String ARG_CURRENT_ITEM = "ARG_CURRENT_ITEM";

    private ArrayList<String> paths;
    private ViewPager mViewPager;
    private PhotoPagerAdapter mPagerAdapter;
    public OnCheckBoxClickListener2 onCheckBoxClickListener2 = null;
    public final static long ANIM_DURATION = 200L;
    private int picPosition = 0;
    public final static String ARG_THUMBNAIL_TOP = "THUMBNAIL_TOP";
    public final static String ARG_THUMBNAIL_LEFT = "THUMBNAIL_LEFT";
    public final static String ARG_THUMBNAIL_WIDTH = "THUMBNAIL_WIDTH";
    public final static String ARG_THUMBNAIL_HEIGHT = "THUMBNAIL_HEIGHT";
    public final static String ARG_HAS_ANIM = "HAS_ANIM";

    private int thumbnailTop = 0;
    private int thumbnailLeft = 0;
    private int thumbnailWidth = 0;
    private int thumbnailHeight = 0;

    private boolean hasAnim = false;
    private ArrayList<String> selectedPhotoPaths;
    private final ColorMatrix colorizerMatrix = new ColorMatrix();
    public CheckBox v_selected;
    private int currentItem = 0;
    private List<Photo> photosDir = new ArrayList<>();

    public static ImagePagerFragment2 newInstance(List<String> paths, int currentItem) {

        ImagePagerFragment2 f = new ImagePagerFragment2();

        Bundle args = new Bundle();
        args.putStringArray(ARG_PATH, paths.toArray(new String[paths.size()]));
        args.putInt(ARG_CURRENT_ITEM, currentItem);
        args.putBoolean(ARG_HAS_ANIM, false);

        f.setArguments(args);

        return f;
    }


    public static ImagePagerFragment2 newInstance(List<String> paths, int currentItem, int[] screenLocation, int thumbnailWidth, int thumbnailHeight) {

        ImagePagerFragment2 f = newInstance(paths, currentItem);

        f.getArguments().putInt(ARG_THUMBNAIL_LEFT, screenLocation[0]);
        f.getArguments().putInt(ARG_THUMBNAIL_TOP, screenLocation[1]);
        f.getArguments().putInt(ARG_THUMBNAIL_WIDTH, thumbnailWidth);
        f.getArguments().putInt(ARG_THUMBNAIL_HEIGHT, thumbnailHeight);
        f.getArguments().putBoolean(ARG_HAS_ANIM, true);

        return f;
    }


    public void setPhotoPath(List<String> paths, int currentItem) {
        this.paths.clear();
        this.paths.addAll(paths);
        this.currentItem = currentItem;
        mViewPager.setCurrentItem(currentItem);
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    public void setPhotos(List<Photo> photosDir) {
        this.photosDir.addAll(photosDir);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paths = new ArrayList<>();

        Bundle bundle = getArguments();

        if (bundle != null) {
            String[] pathArr = bundle.getStringArray(ARG_PATH);
            paths.clear();
            if (pathArr != null) {
                paths = new ArrayList<>(Arrays.asList(pathArr));
            }

            hasAnim = bundle.getBoolean(ARG_HAS_ANIM);
            currentItem = bundle.getInt(ARG_CURRENT_ITEM);
            thumbnailTop = bundle.getInt(ARG_THUMBNAIL_TOP);
            thumbnailLeft = bundle.getInt(ARG_THUMBNAIL_LEFT);
            thumbnailWidth = bundle.getInt(ARG_THUMBNAIL_WIDTH);
            thumbnailHeight = bundle.getInt(ARG_THUMBNAIL_HEIGHT);
        }
        getSelectedPhotoPaths();
        mPagerAdapter = new PhotoPagerAdapter(paths);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.__picker_picker_fragment_image_pager, container, false);
        v_selected = (CheckBox) rootView.findViewById(R.id.v_selected);
        v_selected.setChecked(true);
        mViewPager = (ViewPager) rootView.findViewById(R.id.vp_photos);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOffscreenPageLimit(5);

        // Only run the animation if we're coming from the parent activity, not if
        // we're recreated automatically by the window manager (e.g., device rotation)
        if (savedInstanceState == null && hasAnim) {
            ViewTreeObserver observer = mViewPager.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    mViewPager.getViewTreeObserver().removeOnPreDrawListener(this);

                    // Figure out where the thumbnail and full size versions are, relative
                    // to the screen and each other
                    int[] screenLocation = new int[2];
                    mViewPager.getLocationOnScreen(screenLocation);
                    thumbnailLeft = thumbnailLeft - screenLocation[0];
                    thumbnailTop = thumbnailTop - screenLocation[1];

                    runEnterAnimation();

                    return true;
                }
            });
        }

        v_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckBoxClickListener2.OnCheckBoxClick2(picPosition, photosDir.get(picPosition));
                if (selectedPhotoPaths.contains(photosDir.get(picPosition).getPath())) {
//                    BimpUtils.drr.remove(picPosition);
                    ImagePreference.getInstance(getContext()).removeOnePath(ImagePreference.DRR, picPosition);
                    selectedPhotoPaths.remove(photosDir.get(picPosition).getPath());
                } else {
                    if (ImagePreference.getInstance(getContext()).getPhotoCount() == 1) {
                        ImagePreference.getInstance(getContext()).clearImagesList(ImagePreference.DRR);
                        selectedPhotoPaths.clear();
                    }
                    ImagePreference.getInstance(getContext()).addImagesList(ImagePreference.DRR, photosDir.get(picPosition).getPath());
                    selectedPhotoPaths.add(photosDir.get(picPosition).getPath());
                }
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hasAnim = currentItem == position;
                picPosition = position;
                if (selectedPhotoPaths.contains(photosDir.get(picPosition).getPath())) {
                    v_selected.setChecked(true);
                } else {
                    v_selected.setChecked(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return rootView;
    }

    public void getSelectedPhotoPaths() {
        selectedPhotoPaths = ImagePreference.getInstance(getActivity()).getImagesList(ImagePreference.DRR);
    }

    /**
     * The enter animation scales the picture in from its previous thumbnail
     * size/location, colorizing it in parallel. In parallel, the background of the
     * activity is fading in. When the pictue is in place, the text description
     * drops down.
     */
    private void runEnterAnimation() {
        final long duration = ANIM_DURATION;

        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the thumbnail
        // size/location, from which we'll animate it common_back up
        ViewHelper.setPivotX(mViewPager, 0);
        ViewHelper.setPivotY(mViewPager, 0);
        ViewHelper.setScaleX(mViewPager, (float) thumbnailWidth / mViewPager.getWidth());
        ViewHelper.setScaleY(mViewPager, (float) thumbnailHeight / mViewPager.getHeight());
        ViewHelper.setTranslationX(mViewPager, thumbnailLeft);
        ViewHelper.setTranslationY(mViewPager, thumbnailTop);

        // Animate scale and translation to go from thumbnail to full size
        ViewPropertyAnimator.animate(mViewPager)
                .setDuration(duration)
                .scaleX(1)
                .scaleY(1)
                .translationX(0)
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator());

        // Fade in the black background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mViewPager.getBackground(), "alpha", 0, 255);
        bgAnim.setDuration(duration);
        bgAnim.start();

        // Animate a color filter to take the image from grayscale to full color.
        // This happens in parallel with the image scaling and moving into place.
        ObjectAnimator colorizer = ObjectAnimator.ofFloat(ImagePagerFragment2.this,
                "saturation", 0, 1);
        colorizer.setDuration(duration);
        colorizer.start();

    }


    /**
     * The exit animation is basically a reverse of the enter animation, except that if
     * the orientation has changed we simply scale the picture common_back into the center of
     * the screen.
     *
     * @param endAction This action gets run after the animation completes (this is
     *                  when we actually switch activities)
     */
    public void runExitAnimation(final Runnable endAction) {

        if (!getArguments().getBoolean(ARG_HAS_ANIM, false) || !hasAnim) {
            endAction.run();
            return;
        }

        final long duration = ANIM_DURATION;

        // Animate image common_back to thumbnail size/location
        ViewPropertyAnimator.animate(mViewPager)
                .setDuration(duration)
                .setInterpolator(new AccelerateInterpolator())
                .scaleX((float) thumbnailWidth / mViewPager.getWidth())
                .scaleY((float) thumbnailHeight / mViewPager.getHeight())
                .translationX(thumbnailLeft)
                .translationY(thumbnailTop)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        endAction.run();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });

        // Fade out background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mViewPager.getBackground(), "alpha", 0);
        bgAnim.setDuration(duration);
        bgAnim.start();

        // Animate a color filter to take the image common_back to grayscale,
        // in parallel with the image scaling and moving into place.
        ObjectAnimator colorizer =
                ObjectAnimator.ofFloat(ImagePagerFragment2.this, "saturation", 1, 0);
        colorizer.setDuration(duration);
        colorizer.start();
    }


    /**
     * This is called by the colorizing animator. It sets a saturation factor that is then
     * passed onto a filter on the picture's drawable.
     *
     * @param value saturation
     */
    public void setSaturation(float value) {
        colorizerMatrix.setSaturation(value);
        ColorMatrixColorFilter colorizerFilter = new ColorMatrixColorFilter(colorizerMatrix);
        mViewPager.getBackground().setColorFilter(colorizerFilter);
    }


    public ViewPager getViewPager() {
        return mViewPager;
    }


    public ArrayList<String> getPaths() {
        return paths;
    }


    public int getCurrentItem() {
        return mViewPager.getCurrentItem();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        paths.clear();
        paths = null;

        if (mViewPager != null) {
            mViewPager.setAdapter(null);
        }
    }

    public interface OnCheckBoxClickListener2 {
        /***
         * @param position 所选图片的位置
         * @param photo    所选的图片
         * @return enable check
         */
        boolean OnCheckBoxClick2(int position, Photo photo);
    }

    public void setOnCheckBoxClickListener2(OnCheckBoxClickListener2 onCheckBoxClickListener) {
        this.onCheckBoxClickListener2 = onCheckBoxClickListener;
    }
}

