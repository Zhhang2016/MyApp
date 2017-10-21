package me.iwf.photopicker.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPickUtils;
import me.iwf.photopicker.PhotoPreview;
import me.iwf.photopicker.R;
import me.iwf.photopicker.utils.BimpUtils;
import me.iwf.photopicker.utils.CommonUtils;
import me.iwf.photopicker.utils.ImagePreference;
import me.iwf.photopicker.utils.LoadingImgUtil;
import me.iwf.photopicker.widget.MultiPickResultView;

/**
 * Created by liuyuan on 2016/9/12 0012.
 */
public class PhotoHoriAdapter extends RecyclerView.Adapter<PhotoHoriAdapter.PhotoHoriViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<String> photoPaths;
    private int PhotoCount;
    private Context mContext;
    private int action;
    private ImagePreference instance;
    private boolean isCrop = false;

    public void setCrop(boolean crop) {
        isCrop = crop;
    }

    public PhotoHoriAdapter(Context mContext, ArrayList<String> photoPaths) {
        this.photoPaths = photoPaths;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        instance = ImagePreference.getInstance(mContext);
    }

    public int getPhotoCount() {
        return PhotoCount;
    }

    public void add(ArrayList<String> photoPaths) {
        if (photoPaths != null && photoPaths.size() > 0) {
            this.photoPaths.addAll(photoPaths);
            notifyDataSetChanged();
        }

    }

    public void refresh(ArrayList<String> photoPaths) {
        this.photoPaths.clear();
        if (photoPaths != null && photoPaths.size() > 0) {
            this.photoPaths.addAll(photoPaths);
        }
        notifyDataSetChanged();
    }

    public void setPhotoCount(int photoCount) {
        PhotoCount = photoCount;
    }

    @Override
    public PhotoHoriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.__picker_horiitem, parent, false);
        return new PhotoHoriViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoHoriViewHolder holder, final int position) {
        if (action == MultiPickResultView.ACTION_SELECT) {
            if (position == getItemCount() - 1) {//最后一个始终是+号，点击能够跳去添加图片

                if (instance.getImagesList(ImagePreference.DRR).size() == instance.getPhotoCount()) {
//                    Uri uri = Uri.fromFile(new File(photoPaths.get(position)));
//                    Glide.with(application)
//                            .load(uri)
//                            .centerCrop()
//                            .thumbnail(0.1f)
//                            .override(150, 150)
//                            .placeholder(R.drawable.__picker_default_weixin)
//                            .error(R.drawable.__picker_ic_broken_image_black_48dp)
//                            .into(holder.ivPhoto);
                    LoadingImgUtil.displayImage(holder.ivPhoto, photoPaths.get(position), new LoadingImgUtil.ImageSize(150, 150), 0.1f, false, false);
                } else {
                    //预留的添加默认图位置
                    if (BimpUtils.ADDPICCON.equals("")) {
//                        Glide.with(application)
//                                .load("")
//                                .centerCrop()
//                                .thumbnail(0.1f)
////                                .override(100, 100)
//                                .placeholder(R.drawable.addphoto)
//                                .error(R.drawable.addphoto)
//                                .into(holder.ivPhoto);
                        holder.ivPhoto.setImageResource(R.drawable.addphoto);
//                        LoadingImgUtil.displayImage(holder.ivPhoto, "", 0.1f, R.drawable.addphoto);
                    } else {
//                        Glide.with(application)
//                                .load("")
//                                .centerCrop()
//                                .thumbnail(0.1f)
////                                .override(100, 100)
//                                .placeholder(R.drawable.addphoto)
//                                .error(R.drawable.addphoto)
//                                .into(holder.ivPhoto);
                        holder.ivPhoto.setImageResource(R.drawable.addphoto);
//                        LoadingImgUtil.displayImage(holder.ivPhoto, "", 0.1f, R.drawable.addphoto);
                    }
                }


                holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (photoPaths != null && photoPaths.size() == instance.getPhotoCount()) {
                            PhotoPreview.builder()
                                    .setPhotos(photoPaths)
                                    .setAction(action)
                                    .setCurrentItem(position)
                                    .start2((Activity) mContext);
                        } else {
                            // PhotoPickUtils.startPick((Activity) application, BimpUtils.drr);
                            if (CommonUtils.checkPermission((Activity) mContext, null, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 30)) {
                                return;
                            }
                            PhotoPickUtils.startPickWithCount((Activity) mContext, instance.getImagesList(ImagePreference.DRR), getPhotoCount(), isCrop);
                        }
                    }
                });
            } else {
                //String str = photoPaths.get(position);
//                Uri uri = Uri.fromFile(new File(photoPaths.get(position)));
//                Glide.with(application)
//                        .load(uri)
//                        .centerCrop()
//                        .thumbnail(0.1f)
//                        .override(150, 150)
//                        .placeholder(R.drawable.__picker_default_weixin)
//                        .error(R.drawable.__picker_ic_broken_image_black_48dp)
//                        .into(holder.ivPhoto);
                LoadingImgUtil.displayImage(holder.ivPhoto, photoPaths.get(position), new LoadingImgUtil.ImageSize(150, 150), 0.1f);
                holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PhotoPreview.builder()
                                .setPhotos(photoPaths)
                                .setAction(action)
                                .setCurrentItem(position)
                                .start2((Activity) mContext);
                    }
                });
            }
        } else if (action == MultiPickResultView.ACTION_ONLY_SHOW) {
//            Uri uri = Uri.fromFile(new File(photoPaths.get(position)));
//            Uri uri = Uri.parse(photoPaths.get(position));
            try {
//                LoadingImgUtil.loadingLocalImage(ImageDownloader.Scheme.FILE.wrap(photoPaths.get(position)), new ImageSize(50, 50), holder.ivPhoto);
//                Glide.with(application)
//                        .load(uri)
//                        .placeholder(R.drawable.__picker_default_weixin)
//                        .error(R.drawable.__picker_ic_broken_image_black_48dp)
//                        .into(holder.ivPhoto);
                LoadingImgUtil.displayImage(holder.ivPhoto, photoPaths.get(position));
            } catch (Exception e) {
            }


//            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    PhotoPreview.builder()
//                            .setPhotos(photoPaths)
//                            .setAction(action)
//                            .setCurrentItem(position)
//                            .start((Activity) application);
//                }
//            });
        }

    }

    @Override
    public int getItemCount() {
        if (photoPaths.size() == instance.getPhotoCount()) {
            return instance.getPhotoCount();
        } else {
            return action == MultiPickResultView.ACTION_SELECT ? photoPaths.size() + 1 : photoPaths.size();
        }
    }

    public void setAction(@MultiPickResultView.MultiPicAction int action) {
        this.action = action;
    }

    public static class PhotoHoriViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;

        public PhotoHoriViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.img);
        }
    }


}
