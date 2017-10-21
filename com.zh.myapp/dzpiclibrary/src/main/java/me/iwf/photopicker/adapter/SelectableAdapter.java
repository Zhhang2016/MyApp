package me.iwf.photopicker.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.event.Selectable;
import me.iwf.photopicker.utils.ImagePreference;

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements Selectable {

    private static final String TAG = SelectableAdapter.class.getSimpleName();

    public static List<PhotoDirectory> photoDirectories;
    public static List<Photo> selectedPhotos;
    //初始进入时已选的照片 original selected photos
    public static ArrayList<String> originalPhotos = null;

    public int currentDirectoryIndex = 0;
    protected Context context;


    public SelectableAdapter() {
        photoDirectories = new ArrayList<>();
        selectedPhotos = new ArrayList<>();
    }


    /**
     * Indicates if the item at position where is selected
     *
     * @param photo Photo of the item to check
     * @return true if the item is selected, false otherwise
     */
    @Override
    public boolean isSelected(Photo photo) {
//        if (originalPhotos != null && originalPhotos.contains(photo.getPath()) && !selectedPhotos.contains(photo)) {
//            selectedPhotos.add(photo);
//        }
        return selectedPhotos.contains(photo);
    }


    /**
     * Toggle the selection status of the item at a given position
     *
     * @param photo Photo of the item to toggle the selection status for
     */
    @Override
    public void toggleSelection(Photo photo) {
        ImagePreference instance = ImagePreference.getInstance(context);
        if (selectedPhotos.contains(photo)) {
            instance.removeOnePath(ImagePreference.DRR, photo.getPath());
//            BimpUtils.drr.remove(photo.getPath());
            selectedPhotos.remove(photo);
            if (originalPhotos != null && originalPhotos.contains(photo.getPath())) {
                originalPhotos.remove(photo.getPath());
            }
        } else {
            if (instance.getPhotoCount() == 1) {
//                BimpUtils.drr.clear();
                instance.clearImagesList(ImagePreference.DRR);
                selectedPhotos.clear();
            }
            instance.addImagesList(ImagePreference.DRR, photo.getPath());
//            BimpUtils.drr.add(photo.getPath());
            selectedPhotos.add(photo);
            if (originalPhotos != null && !originalPhotos.contains(photo.getPath())) {
                originalPhotos.add(photo.getPath());
            }
        }
    }

    public static boolean selection(Photo photo,Context context) {
        if (selectedPhotos.contains(photo)) {
            selectedPhotos.remove(photo);
            if (originalPhotos != null && originalPhotos.contains(photo.getPath())) {
                originalPhotos.remove(photo.getPath());
            }
            return true;
        } else {
            if (ImagePreference.getInstance(context).getPhotoCount() == 1) {
                selectedPhotos.clear();
            }
            selectedPhotos.add(photo);
            if (originalPhotos != null && !originalPhotos.contains(photo.getPath())) {
                originalPhotos.add(photo.getPath());
            }
            return false;
        }
    }

    /**
     * Clear the selection status for all items
     */
    @Override
    public void clearSelection() {
        selectedPhotos.clear();
    }


    /**
     * Count the selected items
     *
     * @return Selected items count
     */
    @Override
    public int getSelectedItemCount() {
        return selectedPhotos.size();
    }


    public void setCurrentDirectoryIndex(int currentDirectoryIndex) {
        this.currentDirectoryIndex = currentDirectoryIndex;
    }


    public List<Photo> getCurrentPhotos() {
        return photoDirectories.get(currentDirectoryIndex).getPhotos();
    }


    public List<String> getCurrentPhotoPaths() {
        List<String> currentPhotoPaths = new ArrayList<>(getCurrentPhotos().size());
        for (Photo photo : getCurrentPhotos()) {
            currentPhotoPaths.add(photo.getPath());
        }
        return currentPhotoPaths;
    }


    public List<Photo> getSelectedPhotos() {
        return selectedPhotos;
    }

}