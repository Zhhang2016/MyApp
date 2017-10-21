//package me.iwf.photopicker.widget;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//
//import java.util.ArrayList;
//
//import me.iwf.photopicker.PhotoPickUtils;
//import me.iwf.photopicker.PhotoPreview;
//import me.iwf.photopicker.R;
//import me.iwf.photopicker.utils.CommonUtils;
//import me.iwf.photopicker.utils.ImagePreference;
//import me.iwf.photopicker.utils.LoadingImgUtil;
//
///**
// * Created by donglua on 15/5/31.
// */
//public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
//
//    public ArrayList<String> photoPaths;
//    private LayoutInflater inflater;
//    private int PhotoCount;
//    private Context mContext;
//    private ImagePreference instance;
//    private boolean isCrop = false;
//
//    public void setCrop(boolean crop) {
//        isCrop = crop;
//    }
//
//    public void setAction(@MultiPickResultView.MultiPicAction int action) {
//        this.action = action;
//    }
//
//    public int getPhotoCount() {
//        return PhotoCount;
//    }
//
//    public void setPhotoCount(int photoCount) {
//        PhotoCount = photoCount;
//    }
//
//    private int action;
//
//    public PhotoAdapter(Context mContext, ArrayList<String> photoPaths) {
//        this.photoPaths = photoPaths;
//        this.mContext = mContext;
//        inflater = LayoutInflater.from(mContext);
//        instance = ImagePreference.getInstance(mContext);
//    }
//
//    public void add(ArrayList<String> photoPaths) {
//        if (photoPaths != null && photoPaths.size() > 0) {
//            this.photoPaths.addAll(photoPaths);
//            notifyDataSetChanged();
//        }
//    }
//
//    public void refresh(ArrayList<String> photoPaths) {
//        this.photoPaths.clear();
//        if (photoPaths != null && photoPaths.size() > 0) {
//            this.photoPaths.addAll(photoPaths);
//        }
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = inflater.inflate(R.layout.__picker_item_photo, parent, false);
//        return new PhotoViewHolder(itemView);
//    }
//
//    public interface ClickTakeVideoListener {
//        void ClickAdd();  // 点击
//    }
//
//    public ClickTakeVideoListener getClickTakeVideoListener() {
//        return clickTakeVideoListener;
//    }
//
//    public void setClickTakeVideoListener(ClickTakeVideoListener clickTakeVideoListener) {
//        this.clickTakeVideoListener = clickTakeVideoListener;
//    }
//
//    public ClickTakeVideoListener clickTakeVideoListener = null;
//
//    @Override
//    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
//        if (action == MultiPickResultView.ACTION_SELECT) {
//            if (position == getItemCount() - 1) { //   最后一个始终是+号，点击能够跳去添加图片
//                if (instance.getImagesList(ImagePreference.DRR).size() == instance.getPhotoCount()) {
//                    LoadingImgUtil.displayImage(holder.ivPhoto, photoPaths.get(photoPaths.size() - 1), new LoadingImgUtil.ImageSize(100, 100), 0.5f, true, false);
//                    holder.iv_delete.setVisibility(View.VISIBLE);
////                    holder.iv_delete.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View view) {
////                            // 删除当前图片
////                            photoPaths.remove(position);
////                            ImagePreference.getInstance(mContext).removeOnePath(ImagePreference.UPLOADDIR, position);
////                            ImagePreference.getInstance(mContext).removeOnePath(ImagePreference.DRR, position);
////                            ImagePreference.getInstance(mContext).removeOnePath(ImagePreference.CACHEDIR, position);
////                            PhotoAdapter.this.notifyDataSetChanged();
////                        }
////                    });
//
//                    holder.rl_delete.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (position < photoPaths.size()) {
//                                photoPaths.remove(position);
//                                ImagePreference.getInstance(mContext).removeOnePath(ImagePreference.UPLOADDIR, position);
//                                ImagePreference.getInstance(mContext).removeOnePath(ImagePreference.DRR, position);
//                                ImagePreference.getInstance(mContext).removeOnePath(ImagePreference.CACHEDIR, position);
//                                PhotoAdapter.this.notifyDataSetChanged();
//                            }
//                            if (numberChangeListener != null) {
//                                numberChangeListener.getPicNumberChange(photoPaths.size());
//                            }
//                            PhotoAdapter.this.notifyDataSetChanged();
//                        }
//                    });
//
//
//                } else {
//                    holder.iv_delete.setVisibility(View.GONE);
//                    //预留的添加默认图位置
////                    if (BimpUtils.ADDPICCON.equals("")) {
////                        holder.ivPhoto.setImageResource(R.drawable.__picker_add_pic_select);
////                    } else {
//                        holder.ivPhoto.setImageResource(R.drawable.__picker_add_pic_select);
////                    }
//                }
//                holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //添加点击回调
//                        if (clickTakeVideoListener != null) {
//
//                            if (photoPaths.size() == 0) { // 第一张为加号的时候
//                                clickTakeVideoListener.ClickAdd();
//                            } else {
//                                choosePic(position);
//                            }
//                        } else {
//                            choosePic(position);
//                        }
//                    }
//                });
//            } else {
////                System.out.println("position != getItemCount() - 1");
//                LoadingImgUtil.displayImage(holder.ivPhoto, photoPaths.get(position), new LoadingImgUtil.ImageSize(100, 100), 0.5f, true, false);
//                holder.iv_delete.setVisibility(View.VISIBLE);
////                holder.iv_delete.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        // 删除当前图片
////
////                        photoPaths.remove(position);
////
////                        ImagePreference.getInstance(mContext).removeOnePath(ImagePreference.UPLOADDIR, position);
////                        ImagePreference.getInstance(mContext).removeOnePath(ImagePreference.DRR, position);
////                        ImagePreference.getInstance(mContext).removeOnePath(ImagePreference.CACHEDIR, position);
////                        PhotoAdapter.this.notifyDataSetChanged();
////
////                    }
////                });
//
//                holder.rl_delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if (position < photoPaths.size()) {
//                            photoPaths.remove(position);  //ava.lang.IndexOutOfBoundsException: Invalid index 3, size is 3
//
//                            ImagePreference.getInstance(mContext).removeOnePath(ImagePreference.UPLOADDIR, position);
//                            ImagePreference.getInstance(mContext).removeOnePath(ImagePreference.DRR, position);
//                            ImagePreference.getInstance(mContext).removeOnePath(ImagePreference.CACHEDIR, position);
//
//
//                        }
//
//                        if (numberChangeListener != null) {
//                            numberChangeListener.getPicNumberChange(photoPaths.size());
//                        }
//
//
//                        PhotoAdapter.this.notifyDataSetChanged();
//                    }
//                });
//
//                holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        PhotoPreview.builder()
//                                .setPhotos(photoPaths)
//                                .setAction(action)
//                                .setCurrentItem(position)
//                                .start2((Activity) mContext);
//                    }
//                });
//            }
////            System.out.println("----------------------------------------------------------------------------------------------");
//        } else if (action == MultiPickResultView.ACTION_ONLY_SHOW) {
//            LoadingImgUtil.displayImage(holder.ivPhoto, photoPaths.get(position));
//        }
//    }
//
//
//    //选择图片控件
//    public void choosePic(int position) {
//        if (photoPaths != null && photoPaths.size() == instance.getPhotoCount()) {
//            PhotoPreview.builder()
//                    .setPhotos(photoPaths)
//                    .setAction(action)
//                    .setCurrentItem(position)
//                    .start2((Activity) mContext);
//        } else {
//            if (CommonUtils.checkPermission((Activity) mContext, null, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 30)) {
//                return;
//            }
//            PhotoPickUtils.startPickWithCount((Activity) mContext, instance.getImagesList(ImagePreference.DRR), getPhotoCount(), isCrop);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        if (photoPaths.size() == instance.getPhotoCount()) {
//            return instance.getPhotoCount();
//        } else {
//            return action == MultiPickResultView.ACTION_SELECT ? photoPaths.size() + 1 : photoPaths.size();
//        }
//    }
//
//    public class PhotoViewHolder extends RecyclerView.ViewHolder {
//        public ImageView ivPhoto;
//        public View vSelected;
//        public View cover;
//
//        public ImageView iv_delete;
//
//
//        RelativeLayout rl_delete;
//        public SquareItemLayout outFrame;
//
//        public PhotoViewHolder(View itemView) {
//            super(itemView);
//            outFrame = (SquareItemLayout) itemView.findViewById(R.id.outFrame);
//            outFrame.getLayoutParams().height = (CommonUtils.getScreenWidth(mContext) - CommonUtils.dip2px(mContext, 46)) / 3;
//            outFrame.getLayoutParams().width = (CommonUtils.getScreenWidth(mContext) - CommonUtils.dip2px(mContext, 46)) / 3;
//            rl_delete = (RelativeLayout) itemView.findViewById(R.id.rl_delete);
//            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
//            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
//            iv_delete.setVisibility(View.GONE);
//            vSelected = itemView.findViewById(R.id.v_selected);
//            vSelected.setVisibility(View.GONE);
//            cover = itemView.findViewById(R.id.cover);
//            cover.setVisibility(View.GONE);
//
//        }
//    }
//
//    public NumberChangeListener getNumberChangeListener() {
//        return numberChangeListener;
//    }
//
//    public void setNumberChangeListener(NumberChangeListener numberChangeListener) {
//        this.numberChangeListener = numberChangeListener;
//    }
//
//    public NumberChangeListener numberChangeListener;
//
//    public interface NumberChangeListener {
//
//        void getPicNumberChange(int size);
//
//
//    }
//
//
//}
