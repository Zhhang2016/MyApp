package me.iwf.photopicker.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.iwf.photopicker.PhotoPicker;

/**
 * Created by donglua on 15/6/23.
 * <p/>
 * <p/>
 * http://developer.android.com/training/camera/photobasics.html
 */
public class ImageCaptureManager {

    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final String TAKEPHOTOPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/orange/takephotoCache/";

    private String mCurrentPhotoPath;
    private Context mContext;

    public ImageCaptureManager(Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = new File(TAKEPHOTOPATH);
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                throw new IOException();
            }
        }
        File image = new File(storageDir, imageFileName);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public Intent dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    uri = FileProvider.getUriForFile(mContext, PhotoPicker.MainPackageId + ".fileProvider", photoFile);
                } else {
                    uri = Uri.fromFile(photoFile);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            }
        }
        return takePictureIntent;
    }

    public void galleryAddPic() {
        try {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            if (TextUtils.isEmpty(mCurrentPhotoPath)) {
                return;
            }
            File f = new File(mCurrentPhotoPath);
            Bitmap bm = BimpUtils.revitionImageSize(mCurrentPhotoPath);
            int degree = BimpUtils.readPictureDegree(mCurrentPhotoPath);
            if (degree != 0) {// 旋转照片角度
                bm = BimpUtils.rotateBitmap(bm, degree);
            }
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
            }
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            mContext.sendBroadcast(mediaScanIntent);
//            MediaStore.Images.Media.insertImage(application.getContentResolver(), mCurrentPhotoPath,
//                    mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("/") + 1), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && mCurrentPhotoPath != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoPath);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
        }
    }

}
