package me.iwf.photopicker.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.iwf.photopicker.R;

/**
 * @author dongzheng
 * @version V_5.0.0
 * @date 2016/8/24
 * @description
 */
public class CommonUtils {

    public static void saveBitmap(Bitmap bm, String picName) {
        if (bm != null) {
            try {
//                if (!isFileExist("")) {
//                    File tempf = createSDDir("");
//                }
                File f = new File(BimpUtils.savePath, picName);
                if (!f.exists()) {
                    f.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bm != null && !bm.isRecycled()) {
                    bm.recycle();
                }
                System.gc();
            }
        }
    }

    public static boolean checkPermission(Activity context, Fragment fragment, String[] permission, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(permission[0]) != PackageManager.PERMISSION_GRANTED) {
                if (fragment != null) {
                    fragment.requestPermissions(permission, requestCode);
                } else {
                    context.requestPermissions(permission, requestCode);
                }
                return true;
            }
        }
        return false;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            cursor.close();
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static void deleteImage(Context context, String imagePath) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=?",
                new String[]{imagePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            cursor.close();
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uri = ContentUris.withAppendedId(contentUri, id);
            resolver.delete(uri, null, null);
        } else {
            File file = new File(imagePath);
            if (file.exists()) {
                file.delete();
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(file)));
            }
        }
    }

    private static ThreadLocal<SimpleDateFormat> YYYYMMDDHHMMSS_SSS = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA);
        }
    };

    public static String getCurrentTimeForFileName() {
        return YYYYMMDDHHMMSS_SSS.get().format(new Date());
    }

    private static Toast toast;

    private static void createToast(Context context, String content, int resId) {
        if (context == null) {
            return;
        }
        if (toast == null) {
            Context applicationContext = context.getApplicationContext(); //防止内存泄漏
            toast = new Toast(applicationContext);
            toast.setView(View.inflate(context, R.layout.__picker_layout_toast, null));
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        if (resId > 0) {
            toast.setText(resId);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public static void showToast(final Context context, final String content) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            createToast(context, content, 0);
        }
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) (context.getResources().getDisplayMetrics().density * dpValue + 0.5f);
    }
}
