package me.iwf.photopicker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.R;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.event.OnItemCheckListener;
import me.iwf.photopicker.event.OnPhotoClickListener;
import me.iwf.photopicker.utils.LoadingImgUtil;
import me.iwf.photopicker.utils.MediaStoreHelper;

/**
 * Created by donglua on 15/5/31.
 */
public class PhotoGridAdapter extends SelectableAdapter<PhotoGridAdapter.PhotoViewHolder> {

    private LayoutInflater inflater;

    private OnItemCheckListener onItemCheckListener = null;
    private OnPhotoClickListener onPhotoClickListener = null;
    private View.OnClickListener onCameraClickListener = null;

    public final static int ITEM_TYPE_CAMERA = 100;
    public final static int ITEM_TYPE_PHOTO = 101;
    private final static int COL_NUMBER_DEFAULT = 3;

    private boolean hasCamera = true;
    private boolean previewEnable = true;

    private int imageSize;
    private int columnNumber = COL_NUMBER_DEFAULT;

    public PhotoGridAdapter(Context context, List<PhotoDirectory> photoDirectories) {
        this.photoDirectories = photoDirectories;
        inflater = LayoutInflater.from(context);
        setColumnNumber(context, columnNumber);
        this.context = context;
    }

    public PhotoGridAdapter(Context context, List<PhotoDirectory> photoDirectories, ArrayList<String> orginalPhotos, int colNum) {
        this(context, photoDirectories);
        setColumnNumber(context, colNum);
        setOriginalPhotos(orginalPhotos);
    }

    public void initSelectedPhotos() {
        if (photoDirectories != null && !photoDirectories.isEmpty()) {
            List<Photo> photos = photoDirectories.get(0).getPhotos();
            if (photos != null && !photos.isEmpty() && originalPhotos != null && !originalPhotos.isEmpty()) {
                selectedPhotos.clear();
                for (String path : originalPhotos) {
                    for (Photo photo : photos) {
                        if (photo.getPath().equals(path)) {
                            selectedPhotos.add(photo);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void setOriginalPhotos(ArrayList<String> originalPhotos) {
        this.originalPhotos = originalPhotos;
    }

    private void setColumnNumber(Context context, int columnNumber) {
        this.columnNumber = columnNumber;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = 100;
    }

    @Override
    public int getItemViewType(int position) {
        return (showCamera() && position == 0) ? ITEM_TYPE_CAMERA : ITEM_TYPE_PHOTO;
    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.__picker_item_photo, parent, false);
        PhotoViewHolder holder = new PhotoViewHolder(itemView);
        if (viewType == ITEM_TYPE_CAMERA) {
            holder.vSelected.setVisibility(View.GONE);
            holder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER);

            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onCameraClickListener != null) {
                        onCameraClickListener.onClick(view);
                    }
                }
            });
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {

        holder.iv_delete.setVisibility(View.GONE);

        holder.vSelected.setTag(0);
        if (getItemViewType(position) == ITEM_TYPE_PHOTO) {
//            holder.texture.setVisibility(View.GONE);
            List<Photo> photos = getCurrentPhotos();
            final Photo photo;

            if (showCamera()) {
                photo = photos.get(position - 1);
            } else {
                photo = photos.get(position);
            }
            LoadingImgUtil.displayImage(holder.ivPhoto, photo.getPath(), new LoadingImgUtil.ImageSize(imageSize, imageSize), 0.5f, true, false);

            final boolean isChecked = isSelected(photo);

            if (isChecked) {

                holder.tv_pic_number.setVisibility(View.VISIBLE);


                //  遍历当前已有集合
                int currentposition = 0;
                for (int i = 0; i < getSelectedPhotos().size(); i++) {
                    if (photo.getId() == getSelectedPhotos().get(i).getId()) {
                        holder.tv_pic_number.setText(currentposition + 1 + "");  // 选中集合
                        break;
                    } else {
                        currentposition++;
                    }
                }

            } else {
                holder.tv_pic_number.setVisibility(View.GONE);
            }


            holder.vSelected.setSelected(isChecked);
            holder.cover.setSelected(isChecked);

            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPhotoClickListener != null) {
                        int pos = holder.getAdapterPosition();
                        if (previewEnable) {
                            onPhotoClickListener.onClick(view, pos, showCamera());
                        } else {
                            holder.vSelected.performClick();
                        }
                    }
                }
            });
//            holder.vSelected.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (((int) (holder.vSelected.getTag())) == 0) {//防止快速多次点击
//                        holder.vSelected.setTag(1);
//                        int pos = holder.getAdapterPosition();
//                        boolean isEnable = true;
//                        if (onItemCheckListener != null) {
//                            isEnable = onItemCheckListener.OnItemCheck(pos, photo, isChecked, getSelectedPhotos().size());
//                        }
//                        if (isEnable) {
//
//
//
//                            toggleSelection(photo);
//                            //数据改变跟新控件
////                            notifyItemChanged(pos);
//                            notifyDataSetChanged(); // 全部刷新
//
//                        }else{
//                            holder.vSelected.setTag(0);
//                        }
//                    }
//                }
//            });

            holder.rl_selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((int) (holder.vSelected.getTag())) == 0) {//防止快速多次点击
                        holder.vSelected.setTag(1);
                        int pos = holder.getAdapterPosition();
                        boolean isEnable = true;
                        if (onItemCheckListener != null) {
                            isEnable = onItemCheckListener.OnItemCheck(pos, photo, isChecked, getSelectedPhotos().size());
                        }
                        if (isEnable) {

                            toggleSelection(photo);
                            //数据改变跟新控件
//                            notifyItemChanged(pos);
                            notifyDataSetChanged(); // 全部刷新

                        } else {
                            holder.vSelected.setTag(0);
                        }
                    }
                }
            });


            holder.ivPhoto.setBackgroundColor(Color.TRANSPARENT);
        } else {
            holder.tv_pic_number.setVisibility(View.GONE);
            holder.ivPhoto.setImageResource(R.drawable.__picker_camera);
            holder.ivPhoto.setBackgroundColor(Color.BLACK);
            //预览开启
            //  参数配置选泽中
//            holder.texture.setVisibility(View.VISIBLE);  //可见
//            //。。。。。。
//
//            if(surfaceTextureListener==null){
//                surfaceTextureListener = new MySurfaceTextureListener();
//            }
//            holder.texture.setSurfaceTextureListener(surfaceTextureListener);

        }
    }

    @Override
    public int getItemCount() {
        int photosCount = photoDirectories.size() == 0 ? 0 : getCurrentPhotos().size();
        if (showCamera()) {
            return photosCount + 1;
        }
        return photosCount;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;
        private View cover;
        public TextView tv_pic_number;
        public ImageView iv_delete;
//        public TextureView  texture;

        public RelativeLayout rl_selected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            vSelected = itemView.findViewById(R.id.v_selected);
            cover = itemView.findViewById(R.id.cover);
            cover.setVisibility(View.GONE);
            tv_pic_number = (TextView) itemView.findViewById(R.id.tv_pic_number);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            rl_selected = (RelativeLayout) itemView.findViewById(R.id.rl_selected);
            //预览
//            texture= (TextureView) itemView.findViewById(R.id.texture);
        }
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }

    public void setOnCameraClickListener(View.OnClickListener onCameraClickListener) {
        this.onCameraClickListener = onCameraClickListener;
    }

    public ArrayList<String> getSelectedPhotoPaths() {
        ArrayList<String> selectedPhotoPaths = new ArrayList<>(getSelectedItemCount());

        for (Photo photo : selectedPhotos) {
            selectedPhotoPaths.add(photo.getPath());
        }
        return selectedPhotoPaths;
    }

    public void setShowCamera(boolean hasCamera) {
        this.hasCamera = hasCamera;
    }

    public void setPreviewEnable(boolean previewEnable) {
        this.previewEnable = previewEnable;
    }

    public boolean showCamera() {
        return (hasCamera && currentDirectoryIndex == MediaStoreHelper.INDEX_ALL_PHOTOS);
    }

    @Override
    public void onViewRecycled(PhotoViewHolder holder) {
//        Glide.clear(holder.ivPhoto);
        LoadingImgUtil.clearView(holder.ivPhoto);
        super.onViewRecycled(holder);
    }
}
