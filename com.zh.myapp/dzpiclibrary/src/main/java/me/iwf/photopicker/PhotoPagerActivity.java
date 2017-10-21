package me.iwf.photopicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.adapter.SelectableAdapter;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.fragment.ImagePagerFragment2;
import me.iwf.photopicker.utils.StatusBarUtil;
import me.iwf.photopicker.widget.MultiPickResultView;
import me.iwf.photopicker.widget.Titlebar;

import static me.iwf.photopicker.PhotoPreview.EXTRA_ACTION;
import static me.iwf.photopicker.PhotoPreview.EXTRA_CURRENT_ITEM;
import static me.iwf.photopicker.PhotoPreview.EXTRA_PHOTOS;

/**
 * Created by donglua on 15/6/24.
 */
public class PhotoPagerActivity extends FragmentActivity {

    private ImagePagerFragment2 pagerFragment;

    private Titlebar titlebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__picker_activity_photo_pager);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.__picker_new_main_color), 0);
        int currentItem = 0;
        List<String> paths = null;
        int action = MultiPickResultView.ACTION_ONLY_SHOW;
        try {
            Intent intent = getIntent();
            currentItem = intent.getIntExtra(EXTRA_CURRENT_ITEM, 0);
            paths = intent.getStringArrayListExtra(EXTRA_PHOTOS);
            action = intent.getIntExtra(EXTRA_ACTION, MultiPickResultView.ACTION_ONLY_SHOW);
        } catch (Exception e) {
            e.printStackTrace();
            if(paths == null){
                paths = new ArrayList<>();
            }
        }
        List<Photo> photos = SelectableAdapter.selectedPhotos;

        if (pagerFragment == null) {
            pagerFragment =
                    (ImagePagerFragment2) getSupportFragmentManager().findFragmentById(R.id.photoPagerFragment);
        }
        pagerFragment.setPhotoPath(paths, currentItem);
        pagerFragment.setPhotos(photos);
        pagerFragment.setOnCheckBoxClickListener2(new ImagePagerFragment2.OnCheckBoxClickListener2() {
            @Override
            public boolean OnCheckBoxClick2(int position, Photo photo) {
//                if (SelectableAdapter.selection(photo)) {
//
//                } else {
//
//                }
                return false;
            }
        });
        titlebar = (Titlebar) findViewById(R.id.titlebar);
        titlebar.init(this);
        if (action == MultiPickResultView.ACTION_SELECT) {
            titlebar.setRitht(getApplicationContext().getResources().getDrawable(R.drawable.__picker_delete), "", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = pagerFragment.getViewPager().getCurrentItem();
                    pagerFragment.getPaths().remove(position);
                    pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();

                }
            });
        }
        titlebar.setTitle(getString(R.string.__picker_preview));
        pagerFragment.getViewPager().addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                titlebar.setTitle(getString(R.string.__picker_preview) + " " + getString(R.string.__picker_image_index, pagerFragment.getViewPager().getCurrentItem() + 1,
                        pagerFragment.getPaths().size()));
                // updateActionBarTitle();
            }
        });
    }

    //把actionBar的文字标题居中
    public static void centerActionBarTitle(Activity activity) {
        int titleId = activity.getResources().getIdentifier("action_bar_title", "id", "android");
        if (titleId <= 0) return;
        TextView titleTextView = (TextView) activity.findViewById(titleId);
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) titleTextView.getLayoutParams();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        titleTextView.setLayoutParams(txvPars);
        titleTextView.setGravity(Gravity.CENTER);
    }
}
