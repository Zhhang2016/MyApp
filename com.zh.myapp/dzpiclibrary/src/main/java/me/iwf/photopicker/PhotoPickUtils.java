package me.iwf.photopicker;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/5 0005.
 */
public class PhotoPickUtils {
    public static ArrayList<String> photosPath = new ArrayList<>();

    public static void onActivityResult(int requestCode, int resultCode, Intent data, PickHandler pickHandler) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PhotoPicker.REQUEST_CODE) {//第一次，选择图片后返回
                if (data != null) {
                    photosPath = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    pickHandler.onPickSuccess(photosPath);
                   /* if (photos != null){
                        if (photos.size() >0){

                        }else {
                            pickHandler.onPickFail("未选择图片1");
                        }
                    }else {
                        pickHandler.onPickFail("未选择图片2");
                    }*/
                } else {
                    pickHandler.onPickFail("选择图片失败");
                }
            } else if (requestCode == PhotoPreview.REQUEST_CODE) {//如果是预览与删除后返回
                if (data != null) {
                    photosPath = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    pickHandler.onPreviewBack(photosPath);
                } else {
                    // pickHandler.onPickFail("选择图片失败");
                }

            }
        } else {

            if (requestCode == PhotoPicker.REQUEST_CODE) {
                pickHandler.onPickCancle();
            }
        }
    }

    public static void startPick(Activity context, ArrayList<String> photos) {
        PhotoPicker.builder()
                .setPhotoCount(8, context)
                .setShowCamera(true)
                .setShowGif(true)
                .setSelected(photos)
                .setPreviewEnabled(true)
                .start(context, PhotoPicker.REQUEST_CODE);
    }

    public static void startPickWithCount(Activity context, ArrayList<String> photos, int count, boolean... isCrop) {
        PhotoPicker.builder()
                .setPhotoCount(count, context)
                .setShowCamera(true)
                .setShowGif(true)
                .setSelected(photos)
                .setPreviewEnabled(true)
                .setWhereFrom(isCrop != null && isCrop.length != 0 && isCrop[0] ? PhotoPicker.MULTICHOOSEONECROP : "")
                .start(context, PhotoPicker.REQUEST_CODE);
    }

    public interface PickHandler {
        void onPickSuccess(ArrayList<String> photos);

        void onPreviewBack(ArrayList<String> photos);

        void onPickFail(String error);

        void onPickCancle();
    }
}
