package me.iwf.photopicker.fragment;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.R;
import me.iwf.photopicker.adapter.PhotoPagerAdapter;
import me.iwf.photopicker.adapter.SelectableAdapter;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.utils.ImagePreference;

/**
 * Created by donglua on 15/6/21.
 */
public class ImagePagerFragment extends Fragment {

    public final static String ARG_PATH = "PATHS";
    public final static String ARG_CURRENT_ITEM = "ARG_CURRENT_ITEM";
    private static List<Photo> photosDir = new ArrayList<>();

    private ArrayList<String> paths;
    private ViewPager mViewPager;
    private PhotoPagerAdapter mPagerAdapter;

    public final static long ANIM_DURATION = 200L;

    public final static String ARG_THUMBNAIL_TOP = "THUMBNAIL_TOP";
    public final static String ARG_THUMBNAIL_LEFT = "THUMBNAIL_LEFT";
    public final static String ARG_THUMBNAIL_WIDTH = "THUMBNAIL_WIDTH";
    public final static String ARG_THUMBNAIL_HEIGHT = "THUMBNAIL_HEIGHT";
    public final static String ARG_HAS_ANIM = "HAS_ANIM";
    public OnCheckBoxClickListener onCheckBoxClickListener = null;
    private int thumbnailTop = 0;
    private int thumbnailLeft = 0;
    private int thumbnailWidth = 0;
    private int thumbnailHeight = 0;
    public List<Photo> photo;
    private boolean hasAnim = false;

    private final ColorMatrix colorizerMatrix = new ColorMatrix();

    private int currentItem = 0;
    private int picPosition = 0;
    public CheckBox v_selected;
    private ArrayList<String> selectedPhotoPaths;
    private boolean isDoCheck = true;//防止快速多次点击
    private Handler mHandler = new Handler();

    public static ImagePagerFragment newInstance(List<String> paths, int currentItem) {

        ImagePagerFragment f = new ImagePagerFragment();

        Bundle args = new Bundle();
        args.putStringArray(ARG_PATH, paths.toArray(new String[paths.size()]));
        args.putInt(ARG_CURRENT_ITEM, currentItem);
        args.putBoolean(ARG_HAS_ANIM, false);

        f.setArguments(args);

        return f;
    }


    public static ImagePagerFragment newInstance(List<Photo> photos, List<String> paths, int currentItem, int[] screenLocation, int thumbnailWidth, int thumbnailHeight) {

        ImagePagerFragment f = newInstance(paths, currentItem);
        photosDir = photos;
        f.getArguments().putInt(ARG_THUMBNAIL_LEFT, screenLocation[0]);
        f.getArguments().putInt(ARG_THUMBNAIL_TOP, screenLocation[1]);
        f.getArguments().putInt(ARG_THUMBNAIL_WIDTH, thumbnailWidth);
        f.getArguments().putInt(ARG_THUMBNAIL_HEIGHT, thumbnailHeight);
        f.getArguments().putBoolean(ARG_HAS_ANIM, false);
        return f;
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
            picPosition = currentItem;
        }
        getSelectedPhotoPaths();
        mPagerAdapter = new PhotoPagerAdapter(paths);

        PhotoPickerActivity photoPickerActivity= (PhotoPickerActivity) getActivity();
        photoPickerActivity.rl_bottom.setVisibility(View.GONE);
        //完成不显示显示
        (photoPickerActivity.titlebar).setTvRightVisiable(true);
        (photoPickerActivity.titlebar).setTitle("相机胶卷",null);

    }

    @Override
    public void onPause() {
        super.onPause();
        PhotoPickerActivity photoPickerActivity= (PhotoPickerActivity) getActivity();
        photoPickerActivity.rl_bottom.setVisibility(View.VISIBLE);
        //完成不显示显示
        (photoPickerActivity.titlebar).setTvRightVisiable(false);
        (photoPickerActivity.titlebar).setTitle("相机胶卷", getActivity().getResources().getDrawable(R.drawable.main_xiala));
    }

    public void getSelectedPhotoPaths() {
        selectedPhotoPaths = new ArrayList<>(SelectableAdapter.selectedPhotos.size());
        for (Photo photo : SelectableAdapter.selectedPhotos) {
            selectedPhotoPaths.add(photo.getPath());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.__picker_picker_fragment_image_pager, container, false);
        v_selected = (CheckBox) rootView.findViewById(R.id.v_selected);
        mViewPager = (ViewPager) rootView.findViewById(R.id.vp_photos);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOffscreenPageLimit(5);
        if (selectedPhotoPaths.contains(paths.get(currentItem))) {
            v_selected.setChecked(true);
        }

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
        v_selected.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isDoCheck = true;
                return false;
            }
        });
        v_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isCheckBoxChecked) {
                try {
                    if (!isDoCheck)
                        return;
                    ImagePreference instance = ImagePreference.getInstance(getContext());
                    int isCheck = onCheckBoxClickListener.OnCheckBoxClick(picPosition, photosDir.get(picPosition), isCheckBoxChecked);
                    if (isCheck == 0) {
                        isDoCheck = false;
                        v_selected.setChecked(!isCheckBoxChecked);
                        return;
                    }
                    if (isCheckBoxChecked) {
                        if (isCheckBoxChecked && isCheck == 1) {
//                        v_selected.setChecked(true);
                            if (instance.getPhotoCount() == 1) {
                                selectedPhotoPaths.clear();
                                instance.clearImagesList(ImagePreference.DRR);
                            }
                            selectedPhotoPaths.add(paths.get(picPosition));
                            instance.addImagesList(ImagePreference.DRR, paths.get(picPosition));
                        }
                    } else {
                        if (!isCheckBoxChecked && isCheck == 2) {
                            if (selectedPhotoPaths.contains(paths.get(picPosition))) {
                                selectedPhotoPaths.remove(paths.get(picPosition));
                                instance.removeOnePath(ImagePreference.DRR, paths.get(picPosition));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        v_selected.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isDoCheck) {
//                    isDoCheck = false;
//                    boolean isCheck = onCheckBoxClickListener.OnCheckBoxClick(picPosition, photosDir.get(picPosition), isCheckBoxChecked);
//                    ImagePreference instance = ImagePreference.getInstance(getContext());
//                    if (isCheck) {
//                        v_selected.setChecked(true);
//                        if (instance.getPhotoCount() == 1) {
//                            selectedPhotoPaths.clear();
//                            instance.clearImagesList(ImagePreference.DRR);
//                        }
//                        selectedPhotoPaths.add(paths.get(picPosition));
//                        instance.addImagesList(ImagePreference.DRR, paths.get(picPosition));
//                        System.out.println();
//                    } else {
//                        v_selected.setChecked(false);
//                        if (selectedPhotoPaths.contains(paths.get(picPosition))) {
//                            selectedPhotoPaths.remove(paths.get(picPosition));
//                            instance.removeOnePath(ImagePreference.DRR, paths.get(picPosition));
//                        }
//                    }
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            isDoCheck = true;
//                        }
//                    }, 500);
//                }
//            }
//        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                isDoCheck = false;
                hasAnim = currentItem == position;
                picPosition = position;
                if (selectedPhotoPaths.contains(paths.get(position))) {
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
        ObjectAnimator colorizer = ObjectAnimator.ofFloat(ImagePagerFragment.this,
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
                ObjectAnimator.ofFloat(ImagePagerFragment.this, "saturation", 1, 0);
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

    public interface OnCheckBoxClickListener {

        /***
         * @param position          所选图片的位置
         * @param path              所选的图片
         * @param isCheckBoxChecked
         * @return 0 超出范围；1 选中；2 取消选中
         */
        int OnCheckBoxClick(int position, Photo path, boolean isCheckBoxChecked);
    }

    public void setOnCheckBoxClickListener(OnCheckBoxClickListener onCheckBoxClickListener) {
        this.onCheckBoxClickListener = onCheckBoxClickListener;
    }
}
