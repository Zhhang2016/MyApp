package me.iwf.photopicker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Army
 * @version V_1.0.0
 * @date 2016/9/23
 * @description 存储选择的图片
 */
public class ImagePreference {
    public static final String DRR = "drr";
    public static final String UPLOADDIR = "uploadDir";
    public static final String CACHEDIR = "cacheDir";
    public static final String PHOTOCOUNT = "photocount";

    private static ImagePreference instance = null;
    private SharedPreferences preferences;

    private ImagePreference(Context context) {
        if (context != null) {
            preferences = context.getSharedPreferences("images", Context.MODE_PRIVATE);
        }
    }

    public static ImagePreference getInstance(Context context) {
        if (instance == null) {
            instance = new ImagePreference(context.getApplicationContext());
        }
        return instance;
    }

    public void storeImagesList(String key, ArrayList<String> drr) {
        SharedPreferences.Editor editor = preferences.edit();
        if (drr.isEmpty()) {
            editor.putString(key, "");
        } else {
            String[] arrays = new String[drr.size()];
            drr.toArray(arrays);
            String images = TextUtils.join(";", arrays);
            editor.putString(key, images);
        }
        editor.commit();
    }

    public void storeImagesList(String key, String path) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, path);
        editor.commit();
    }

    public ArrayList<String> getImagesList(String key) {
        ArrayList<String> images = new ArrayList<>(8);
        String image = preferences.getString(key, "");
        if (!TextUtils.isEmpty(image)) {
            String[] strings = image.split(";");
            if (strings != null) {
                images.addAll(Arrays.asList(strings));
            }
        }
        return images;
    }

    public void addImagesList(String key, String path) {
        StringBuilder sb = new StringBuilder(200);
        String paths = preferences.getString(key, "");
        if (TextUtils.isEmpty(paths)) {
            sb.append(path);
        } else {
            sb.append(paths).append(";").append(path);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, sb.toString());
        editor.commit();
    }

    public void clearImagesList(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, "");
        editor.commit();
    }

    public void clearAllImagesList() {
        new Thread(){
            @Override
            public void run() {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(DRR, "");
                editor.putString(CACHEDIR, "");
                editor.putString(UPLOADDIR, "");
                editor.commit();
            }
        }.start();
    }

    public void storePhotoCount(int count) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PHOTOCOUNT, count);
        editor.commit();
    }

    public int getPhotoCount() {
        int count = preferences.getInt(PHOTOCOUNT, 0);
        return count;
    }

    public void removeOnePath(String key, String path) {
        String imageLists = preferences.getString(key, "");
        if (!TextUtils.isEmpty(imageLists)) {
            String[] images = imageLists.split(";");
            if (images != null) {
                StringBuilder sb = new StringBuilder(200);
                for (String flagPath : images) {
                    if (!path.equals(flagPath)) {
                        sb.append(";").append(flagPath);
                    }
                }
                SharedPreferences.Editor editor = preferences.edit();
                if (sb.length() > 0) {
                    editor.putString(key, sb.substring(1));
                } else {
                    editor.putString(key, "");
                }
                editor.commit();
            }
        }
    }

    public void removeOnePath(String key, int position) {
        String imageLists = preferences.getString(key, "");
        if (!TextUtils.isEmpty(imageLists)) {
            String[] images = imageLists.split(";");
            if (images != null) {
                StringBuilder sb = new StringBuilder(200);
                for (int i = 0; i < images.length; i++) {
                    if (i != position) {
                        sb.append(";").append(images[i]);
                    }
                }
                SharedPreferences.Editor editor = preferences.edit();
                if (sb.length() > 0) {
                    editor.putString(key, sb.substring(1));
                } else {
                    editor.putString(key, "");
                }
                editor.commit();
            }
        }
    }
}
