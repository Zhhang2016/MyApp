package me.iwf.photopicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.fragment.ImagePagerFragment3;
import me.iwf.photopicker.utils.ImagePreference;
import me.iwf.photopicker.utils.StatusBarUtil;
import me.iwf.photopicker.widget.MultiPickResultView;
import me.iwf.photopicker.widget.Titlebar;

import static me.iwf.photopicker.PhotoPicker.KEY_SELECTED_PHOTOS;
import static me.iwf.photopicker.PhotoPreview.EXTRA_ACTION;
import static me.iwf.photopicker.PhotoPreview.EXTRA_CURRENT_ITEM;
import static me.iwf.photopicker.PhotoPreview.EXTRA_PHOTOS;
import static me.iwf.photopicker.PhotoPreview.EXTRA_SHOW_DELETE;

/**
 * Created by donglua on 15/6/24.
 */
public class PhotoPagerActivity2 extends FragmentActivity {

    private ImagePagerFragment3 pagerFragment;

    //private ActionBar actionBar;
    private boolean showDelete;
    private Titlebar titlebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__picker_activity_photo_pager2);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.__picker_new_main_color), 0);
        int currentItem = 0;
        List<String> paths = null;
        int action = MultiPickResultView.ACTION_ONLY_SHOW;
        try {
            currentItem = getIntent().getIntExtra(EXTRA_CURRENT_ITEM, 0);
            paths = getIntent().getStringArrayListExtra(EXTRA_PHOTOS);
            showDelete = getIntent().getBooleanExtra(EXTRA_SHOW_DELETE, true);
            action = getIntent().getIntExtra(EXTRA_ACTION, MultiPickResultView.ACTION_ONLY_SHOW);
        } catch (Exception e) {
            e.printStackTrace();
            if(paths == null){
                paths = new ArrayList<>();
            }
        }

        if (pagerFragment == null) {
            pagerFragment =
                    (ImagePagerFragment3) getSupportFragmentManager().findFragmentById(R.id.photoPagerFragment);
        }
        pagerFragment.setPhotos(paths, currentItem);
        titlebar = (Titlebar) findViewById(R.id.titlebar);
        titlebar.init(this);
        titlebar.setLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, pagerFragment.getPaths());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        if (action == MultiPickResultView.ACTION_SELECT) {
            titlebar.setRitht(getApplicationContext().getResources().getDrawable(R.drawable.__picker_delete), "", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = pagerFragment.getViewPager().getCurrentItem();
                    if (pagerFragment.getPaths().size() > position) {
                        String path = pagerFragment.getPaths().get(position);
                        ImagePreference.getInstance(getApplicationContext()).removeOnePath(ImagePreference.UPLOADDIR, position);
                        ImagePreference.getInstance(getApplicationContext()).removeOnePath(ImagePreference.DRR, position);
                        ImagePreference.getInstance(getApplicationContext()).removeOnePath(ImagePreference.CACHEDIR, position);
                        pagerFragment.getPaths().remove(position);
                    }
//                    if (BimpUtils.uploadDir.size() > position) {
//                        BimpUtils.uploadDir.remove(position);
//                    }
//                    if (BimpUtils.drr.size() > position) {
//                        BimpUtils.drr.remove(position);
//                    }
//                    if (BimpUtils.cacheDir.size() > position) {
//                        BimpUtils.cacheDir.remove(position);
//                    }
                    pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
                    if (pagerFragment.getPaths().size() == 0) {
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, pagerFragment.getPaths());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showDelete) {
            getMenuInflater().inflate(R.menu.__picker_menu_preview, menu);
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, pagerFragment.getPaths());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Intent intent = new Intent();
//        Log.e("==", "=onDestroy=");
//        intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, pagerFragment.getPaths());
//        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

 /* public void updateActionBarTitle() {
    if (actionBar != null) actionBar.setTitle(
        getString(R.string.__picker_image_index, pagerFragment.getViewPager().getCurrentItem() + 1,
            pagerFragment.getPaths().size()));
  }*/
}
