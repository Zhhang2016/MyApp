package me.iwf.photopicker.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPickUtils;
import me.iwf.photopicker.R;
import me.iwf.photopicker.adapter.PhotoHoriAdapter;
import me.iwf.photopicker.utils.BimpUtils;
import me.iwf.photopicker.utils.ImagePreference;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class MultiPickResultView extends FrameLayout {

    public LinearLayoutManager mLayoutManager;
    public int layoutType;

    @IntDef({ACTION_SELECT, ACTION_ONLY_SHOW})

    //Tell the compiler not to store annotation data in the .class file
    @Retention(RetentionPolicy.SOURCE)

    //Declare the NavigationMode annotation
    public @interface MultiPicAction {
    }

    public static final int ACTION_SELECT = 1;//该组件用于图片选择
    public static final int ACTION_ONLY_SHOW = 2;//该组件仅用于图片显示

    private int action;


    public int getLayouType() {
        return layouType;
    }

    public void setLayouType(int layouType) {
        this.layouType = layouType;
    }

    private int layouType = -1;
    private int maxCount;

    private boolean isCrop = false;


    android.support.v7.widget.RecyclerView recyclerView;
    public PhotoAdapter getPhotoAdapter() {
        return photoAdapter;
    }
    private PhotoAdapter photoAdapter;
    private PhotoHoriAdapter horiAdapter;
    private ArrayList<String> selectedPhotos;

    public MultiPickResultView(Context context) {
        this(context, null, 0);
    }

    public MultiPickResultView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public MultiPickResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.layoutType);
        // id = 属性名_具体属性字段名称 (此id系统自动生成)
        layoutType = typedArray.getInteger(R.styleable.layoutType_laytype, -1);
        typedArray.recycle();// 回收typearray, 提高性能
//        System.out.println("ratio:======" + layoutType);
        if (layoutType != 0) {
            setLayoutManager(context, attrs);
        } else {
            initView(context, attrs);
        }
        initData(context, attrs);
        initEvent(context, attrs);

    }


    private void initEvent(Context context, AttributeSet attrs) {


    }

    private void initData(Context context, AttributeSet attrs) {

    }

    private void initView(Context context, AttributeSet attrs) {
        recyclerView = new android.support.v7.widget.RecyclerView(context, attrs);
        recyclerView.setId(R.id._picker_pickimage_recycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        recyclerView.addItemDecoration(new BottomSpacesItemDecoration(8));
        this.addView(recyclerView);
    }

    public void setLayoutManager(Context context, AttributeSet attrs) {
        recyclerView = new android.support.v7.widget.RecyclerView(context, attrs);
        recyclerView.setId(R.id._picker_pickimage_recycler);
        mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(8, OrientationHelper.VERTICAL));
        this.addView(recyclerView);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultiPickResultView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    public void init(int count ,Activity context, @MultiPicAction int action, ArrayList<String> photos) {
        selectedPhotos = new ArrayList<>();
        this.action = action;
        if (photos != null && photos.size() > 0) {
            selectedPhotos.addAll(photos);
        }
        if (layoutType != 0) {
            horiAdapter = new PhotoHoriAdapter(context, selectedPhotos);
            horiAdapter.setAction(action);
            horiAdapter.setCrop(isCrop);
            setHoriPhotoCount(count);
            recyclerView.setAdapter(horiAdapter);
        } else {
            photoAdapter = new PhotoAdapter(context, selectedPhotos);
            photoAdapter.setAction(action);
            photoAdapter.setCrop(isCrop);
            setPhotoCount(count);
            recyclerView.setAdapter(photoAdapter);
        }

    }

    public void setAddPicIcon(String path) {
        BimpUtils.ADDPICCON = path;
    }

    public void setPhotoCount(int count) {
        ImagePreference.getInstance(getContext()).storePhotoCount(count);
        photoAdapter.setPhotoCount(count);
    }

    public void setHoriPhotoCount(int count) {
        ImagePreference.getInstance(getContext()).storePhotoCount(count);
        horiAdapter.setPhotoCount(count);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        PhotoPickUtils.onActivityResult(requestCode, resultCode, data, new PhotoPickUtils.PickHandler() {
            @Override
            public void onPickSuccess(ArrayList<String> photos) {
//                Log.e("==", "=onPickSuccess=");
                if (layoutType != 0) {
                    horiAdapter.refresh(photos);
                } else {
                    photoAdapter.refresh(photos);
                }

            }

            @Override
            public void onPreviewBack(ArrayList<String> photos) {
                if (layoutType != 0) {
                    horiAdapter.refresh(photos);
                } else {
                    photoAdapter.refresh(photos);
                }
            }

            @Override
            public void onPickFail(String error) {
//                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                selectedPhotos.clear();
                if (layoutType != 0) {
                    horiAdapter.notifyDataSetChanged();
                } else {
                    photoAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onPickCancle() {
                //Toast.makeText(getContext(),"取消选择",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void refreshPhotos() {
        if (layoutType != 0) {
            if (horiAdapter != null) {
                horiAdapter.refresh(ImagePreference.getInstance(getContext()).getImagesList(ImagePreference.UPLOADDIR));
            }
        } else {
            if (photoAdapter != null) {
                photoAdapter.refresh(ImagePreference.getInstance(getContext()).getImagesList(ImagePreference.UPLOADDIR));
            }
        }


    }

    public ArrayList<String> getPhotos() {
        return selectedPhotos;
    }

    public void setCrop(boolean crop) {
        isCrop = crop;
    }



}
