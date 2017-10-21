package me.iwf.photopicker.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.PhotoPreview;
import me.iwf.photopicker.R;
import me.iwf.photopicker.adapter.PhotoGridAdapter;
import me.iwf.photopicker.adapter.PopupDirectoryListAdapter;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.event.OnPhotoClickListener;
import me.iwf.photopicker.utils.BimpUtils;
import me.iwf.photopicker.utils.CommonUtils;
import me.iwf.photopicker.utils.ImageCaptureManager;
import me.iwf.photopicker.utils.ImagePreference;
import me.iwf.photopicker.utils.LoadingImgUtil;
import me.iwf.photopicker.utils.MediaStoreHelper;
import me.iwf.photopicker.widget.Titlebar;

import static android.app.Activity.RESULT_OK;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_COLUMN_NUMBER;
import static me.iwf.photopicker.PhotoPicker.EXTRA_PREVIEW_ENABLED;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;

/**
 * Created by donglua on 15/5/31.
 */
public class PhotoPickerFragment extends Fragment {

    private ImageCaptureManager captureManager;
    private PhotoGridAdapter photoGridAdapter;

    private PopupDirectoryListAdapter listAdapter;
    //所有photos的路径
    private List<PhotoDirectory> directories;
    //传入的已选照片
    private ArrayList<String> originalPhotos;
    private static final int TAKE_PICTURE = 0x000000;
    private int SCROLL_THRESHOLD = 30;
    int column;
    //目录弹出框的一次最多显示的目录数目
    public static int COUNT_MAX = 5;
    private final static String EXTRA_CAMERA = "camera";
    private final static String EXTRA_COLUMN = "column";
    private final static String EXTRA_COUNT = "count";
    private final static String EXTRA_GIF = "gif";
    private final static String EXTRA_ORIGIN = "origin";
    //    private ListPopupWindow listPopupWindow;
    private Context mContext;
    private final int SDK_PERMISSION_REQUEST = 127;
    private Titlebar titlebar;
    private File mimgFile;
    private String whereFrom;
    private PopupWindow popupWindow;
    private RelativeLayout layout_bottom;
    private String SAVEDFILEPATH = "savefilepath";
    private Uri beforeCropImageUri = null;

    public static PhotoPickerFragment newInstance(boolean showCamera, boolean showGif,
                                                  boolean previewEnable, int column, int maxCount, ArrayList<String> originalPhotos, String whereFrom) {
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_CAMERA, showCamera);
        args.putBoolean(EXTRA_GIF, showGif);
        args.putBoolean(EXTRA_PREVIEW_ENABLED, previewEnable);
        args.putInt(EXTRA_COLUMN, column);
        args.putInt(EXTRA_COUNT, maxCount);
        args.putString(PhotoPicker.WHERE_FROM, whereFrom);
        args.putStringArrayList(EXTRA_ORIGIN, originalPhotos);
        PhotoPickerFragment fragment = new PhotoPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mContext = context;
            if (context instanceof OnAfterTakePhotoListener) {
                onAfterTakePhotoListener = (OnAfterTakePhotoListener) context;
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnAfterTakePhotoListener) {
            onAfterTakePhotoListener = (OnAfterTakePhotoListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        directories = new ArrayList<>();
        originalPhotos = getArguments().getStringArrayList(EXTRA_ORIGIN);

        column = getArguments().getInt(EXTRA_COLUMN, DEFAULT_COLUMN_NUMBER);
        boolean showCamera = getArguments().getBoolean(EXTRA_CAMERA, true);
        boolean previewEnable = getArguments().getBoolean(EXTRA_PREVIEW_ENABLED, true);
        whereFrom = getArguments().getString(PhotoPicker.WHERE_FROM, "");

        photoGridAdapter = new PhotoGridAdapter(mContext, directories, originalPhotos, column);
        photoGridAdapter.setShowCamera(showCamera);
        photoGridAdapter.setPreviewEnable(previewEnable);
        listAdapter = new PopupDirectoryListAdapter(directories);

        Bundle mediaStoreArgs = new Bundle();

        boolean showGif = getArguments().getBoolean(EXTRA_GIF);
        mediaStoreArgs.putBoolean(EXTRA_SHOW_GIF, showGif);
        MediaStoreHelper.getPhotoDirs(getActivity(), mediaStoreArgs,
                new MediaStoreHelper.PhotosResultCallback() {
                    @Override
                    public void onResultCallback(List<PhotoDirectory> dirs) {
                        directories.clear();
                        directories.addAll(dirs);
                        photoGridAdapter.initSelectedPhotos();
                        photoGridAdapter.notifyDataSetChanged();
                        listAdapter.notifyDataSetChanged();
                        adjustHeight();
                    }
                });

        captureManager = new ImageCaptureManager(getActivity());
        if (savedInstanceState != null) {
            String path = savedInstanceState.getString(SAVEDFILEPATH);
            if (!TextUtils.isEmpty(path)) {
                mimgFile = new File(path);
            }
        }
    }

    public View pick_fragment_zhezao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.__picker_fragment_photo_picker, container, false);
        titlebar = (Titlebar) rootView.findViewById(R.id.titlebar);
        layout_bottom = (RelativeLayout) rootView.findViewById(R.id.layout_bottom);
        pick_fragment_zhezao = rootView.findViewById(R.id.pick_fragment_zhezao);
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_photos);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(column, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(photoGridAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final Button btSwitchDirectory = (Button) rootView.findViewById(R.id.button);

        Button btnPreview = (Button) rootView.findViewById(R.id.btn_preview);

        View view = inflater.inflate(R.layout.__picker_layout_listview, null);
        popupWindow = new PopupWindow(getActivity());
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setWidth(getResources().getDisplayMetrics().widthPixels);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 遮罩消失

                ((PhotoPickerActivity) getActivity()).mainactivity_zhe_zhao.setVisibility(View.GONE);


                pick_fragment_zhezao.setVisibility(View.GONE);

            }
        });
        ListView listView = (ListView) view.findViewById(R.id.__picker_listView);
        listAdapter.setOnItemClickListener(new PopupDirectoryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                popupWindow.dismiss();
                if (photoGridAdapter.currentDirectoryIndex == position) {
                    return;
                }
                PhotoDirectory directory = directories.get(position);

                btSwitchDirectory.setText(directory.getName().toLowerCase());//默认会大写，这里要改成小写

                photoGridAdapter.setCurrentDirectoryIndex(position);
//                photoGridAdapter = new PhotoGridAdapter(application, mGlideRequestManager, directories, originalPhotos, column);
//                recyclerView.setAdapter(photoGridAdapter);
                photoGridAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
            }
        });
        listView.setAdapter(listAdapter);
        popupWindow.setContentView(view);
//        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
//        popupWindow.setAnimationStyle(R.style.__picker_AnimationFromTop);

//        listPopupWindow = new ListPopupWindow(getActivity());

//        listPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//替换背景
//        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//        int widths = wm.getDefaultDisplay().getWidth();
//        listPopupWindow.setWidth(widths);//ListPopupWindow.MATCH_PARENT还是会有边距，直接拿到屏幕宽度来设置也不行，因为默认的background有左右padding值。
//        listPopupWindow.setHeight((directories.size() < COUNT_MAX ? directories.size() : COUNT_MAX) * getResources().getDimensionPixelOffset(R.dimen.__picker_item_directory_height));
//        listPopupWindow.setAnchorView(btSwitchDirectory);
//        listPopupWindow.setAdapter(listAdapter);
//        listPopupWindow.setModal(true);
//
//        listPopupWindow.setDropDownGravity(Gravity.BOTTOM);
//        listPopupWindow.setAnimationStyle(R.style.__picker_mystyle);

//        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                listPopupWindow.dismiss();
//                if (photoGridAdapter.currentDirectoryIndex == position) {
//                    return;
//                }
//                PhotoDirectory directory = directories.get(position);
//
//                btSwitchDirectory.setText(directory.getName().toLowerCase());//默认会大写，这里要改成小写
//
//                photoGridAdapter.setCurrentDirectoryIndex(position);
////                photoGridAdapter = new PhotoGridAdapter(application, mGlideRequestManager, directories, originalPhotos, column);
////                recyclerView.setAdapter(photoGridAdapter);
//                photoGridAdapter.notifyDataSetChanged();
//                recyclerView.scrollToPosition(0);
//
//            }
//        });

        photoGridAdapter.setOnPhotoClickListener(new OnPhotoClickListener() {
            @Override
            public void onClick(View v, int position, boolean showCamera) {
                final int index = showCamera ? position - 1 : position;

                List<String> photos = photoGridAdapter.getCurrentPhotoPaths();

                int[] screenLocation = new int[2];
                v.getLocationOnScreen(screenLocation);
                ImagePagerFragment imagePagerFragment =
                        ImagePagerFragment.newInstance(getPhotoGridAdapter().getCurrentPhotos(), photos, index, screenLocation, v.getWidth(),
                                v.getHeight());

                ((PhotoPickerActivity) getActivity()).addImagePagerFragment(imagePagerFragment);
                //activity 底部消失
//                PhotoPickerActivity photoPickerActivity= (PhotoPickerActivity) getActivity();
//                photoPickerActivity.rl_bottom.setVisibility(View.GONE);
//                //完成不显示显示
//                (photoPickerActivity.titlebar).setTvRightVisiable(true);
            }
        });

        photoGridAdapter.setOnCameraClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (CommonUtils.checkPermission(getActivity(), PhotoPickerFragment.this, new String[]{Manifest.permission.CAMERA}, SDK_PERMISSION_REQUEST))
                        return;
                    Intent intent = captureManager.dispatchTakePictureIntent();
                    startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btSwitchDirectory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();

//                if (listPopupWindow.isShowing()) {
//                    listPopupWindow.dismiss();
//                } else if (!getActivity().isFinishing()) {
////                    adjustHeight();
//                    listPopupWindow.show();
//                    listPopupWindow.getListView().setVerticalScrollBarEnabled(false);
//                    //去掉滑动条,listview 在show之后才建立，所以需要该方法在show之后调用，否则会空指针
//                }
            }
        });


        //预览按钮
        btnPreview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPreview();
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Log.d(">>> Picker >>>", "dy = " + dy);
                if (Math.abs(dy) > SCROLL_THRESHOLD) {
                    if (getActivity() != null) {
                        LoadingImgUtil.pauseLoader(getActivity().getApplicationContext());
                    }
//                    mGlideRequestManager.pauseRequests();
                } else {
                    if (getActivity() != null) {
                        LoadingImgUtil.resumeLoader(getActivity().getApplicationContext());
                    }
//                    mGlideRequestManager.resumeRequests();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    mGlideRequestManager.resumeRequests();
                    if (getActivity() != null) {
                        LoadingImgUtil.resumeLoader(getActivity().getApplicationContext());
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {


        } else {
            //相当于Fragment的onPause
            //activity 底部消失

        }

    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            PhotoPickerActivity photoPickerActivity= (PhotoPickerActivity) getActivity();
//            photoPickerActivity.rl_bottom.setVisibility(View.VISIBLE);
//            //完成显示显示
//            (photoPickerActivity.titlebar).setTvRightVisiable(false);
//        } else {
//            //相当于Fragment的onPause
//        }
//    }


    public void gotoPreview() {
        if (photoGridAdapter.getSelectedPhotoPaths().size() > 0) {
            PhotoPreview.builder()
                    .setPhotos(photoGridAdapter.getSelectedPhotoPaths())
                    .setCurrentItem(0)
                    .setShowDeleteButton(false)
                    .start(getActivity());
        } else {
            CommonUtils.showToast(getActivity(), "还没有选择图片");
        }
    }

    public void showPopupWindow() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else if (!getActivity().isFinishing()) {
            adjustHeight();

//            ..
//            popupWindow.showAsDropDown(layout_bottom);
            popupWindow.showAsDropDown(((PhotoPickerActivity) getActivity()).getTitlebar());


            //显示执照
            ((PhotoPickerActivity) getActivity()).mainactivity_zhe_zhao.setVisibility(View.VISIBLE);


            pick_fragment_zhezao.setVisibility(View.VISIBLE);


        }
    }

    private int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }

    private int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Status height:" + height);
        return height;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            switch (requestCode) {
                case SDK_PERMISSION_REQUEST:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = captureManager.dispatchTakePictureIntent();
                        startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
                    } else {
                        showtDialog(getActivity(), "请前往设置页面打开使用相机的权限");
                    }
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    break;
            }
        } catch (Exception e) {

        }
    }

    public void showtDialog(Context context, String content) {
        try {
            final Dialog alertDialog = new Dialog(context, R.style.__picker_textDialogStyle);// 创建自定义样式dialog
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.__picker_layout_sure_dialog_bg, null);// 得到加载view
            TextView tv_content = (TextView) v.findViewById(R.id.content);
            Button btnSure = (Button) v.findViewById(R.id.btnSure);
            tv_content.setText(content);

            btnSure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);// 不可以用“返回键”取消
            alertDialog.setContentView(v);// 设置布局
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            captureManager.galleryAddPic();
            if (PhotoPicker.SINGLECHOOSECROP.equals(whereFrom) || PhotoPicker.MULTICHOOSEONECROP.equals(whereFrom)) {
                if (!TextUtils.isEmpty(captureManager.getCurrentPhotoPath())) {
                    File file = new File(captureManager.getCurrentPhotoPath());
                    if (file.isFile() && file.exists()) {
                        startPhotoZoom(file);
                    }
                }
            } else {
                if (onAfterTakePhotoListener != null) {
                    onAfterTakePhotoListener.onAfterTakePhoto(captureManager.getCurrentPhotoPath());
                }
            }
        } else if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                if (onAfterTakePhotoListener != null && mimgFile != null) {
                    onAfterTakePhotoListener.onAfterPhotoZoom(captureManager.getCurrentPhotoPath(), mimgFile.getAbsolutePath());
                }
            } else {
                if (beforeCropImageUri != null && getActivity() != null) {
                    getActivity().getContentResolver().delete(beforeCropImageUri, null, null);
                }
            }
        } else if (requestCode == PhotoPreview.REQUEST_CODE) {
            ArrayList<String> imagesList = ImagePreference.getInstance(getActivity()).getImagesList(ImagePreference.DRR);
            if (imagesList.size() != photoGridAdapter.getSelectedPhotoPaths().size()) {
                List<Photo> currentPhotos = photoGridAdapter.getCurrentPhotos();
                List<Photo> selectedPhotos = photoGridAdapter.getSelectedPhotos();
                for (int i = selectedPhotos.size() - 1; i >= 0; i--) {
                    Photo photo = selectedPhotos.get(i);
                    if (!imagesList.contains(photo.getPath())) {
                        int position = currentPhotos.indexOf(photo);
                        if (photoGridAdapter.showCamera()) {
                            position++;
                        }
                        selectedPhotos.remove(i);
                        photoGridAdapter.notifyItemChanged(position);
                    }
                }
            }
        }
    }

    public void startPhotoZoom(File file) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            // 设置裁剪
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputX", 640);
            intent.putExtra("outputY", 640);
            intent.putExtra("return-data", false);
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true);//黑边
            File dir = new File(BimpUtils.savePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            mimgFile = new File(dir, CommonUtils.getCurrentTimeForFileName() + ".png");
            beforeCropImageUri = CommonUtils.getImageContentUri(getActivity(), file);
            intent.setDataAndType(beforeCropImageUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mimgFile));
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, 123);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PhotoGridAdapter getPhotoGridAdapter() {
        return photoGridAdapter;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        captureManager.onSaveInstanceState(outState);
        if (mimgFile != null && outState != null) {
            outState.putString(SAVEDFILEPATH, mimgFile.getAbsolutePath());
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        captureManager.onRestoreInstanceState(savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

    public ArrayList<String> getSelectedPhotoPaths() {
        return photoGridAdapter.getSelectedPhotoPaths();
    }

    public void adjustHeight() {
        if (listAdapter == null) return;
        int count = listAdapter.getCount();
        count = count < COUNT_MAX ? count : COUNT_MAX;
        if (popupWindow != null) {
            popupWindow.setHeight(count * getResources().getDimensionPixelOffset(R.dimen.__picker_item_directory_height));
        }

//        if (listPopupWindow != null) {
//            listPopupWindow.setHeight(count * getResources().getDimensionPixelOffset(R.dimen.__picker_item_directory_height));
//        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (directories == null) {
            return;
        }

        for (PhotoDirectory directory : directories) {
            directory.getPhotoPaths().clear();
            directory.getPhotos().clear();
            directory.setPhotos(null);
        }
        directories.clear();
        directories = null;
    }

    private OnAfterTakePhotoListener onAfterTakePhotoListener = null;

    public interface OnAfterTakePhotoListener {
        void onAfterPhotoZoom(String sourceFile, String zoomFilePath);

        void onAfterTakePhoto(String currentPhotoPath);
    }
}
