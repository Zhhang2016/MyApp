package jzwl.com.comzhmyapp.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import jzwl.com.comzhmyapp.R;
import jzwl.com.comzhmyapp.ui.dialog.CommonDialog;
import jzwl.com.comzhmyapp.ui.dialog.CommonDialogClickListener;
import jzwl.com.comzhmyapp.view.RectImageView;

/**
 * Created by zh on 2017/10/16.
 * describe: 显示圆形图片的显示
 * <p>
 * 可以设置是否带边，带边的颜色等。
 */

public class CircleImageViewActivity extends Activity {

    private RectImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circleimageview_show);
        initView();
    }

    private void initView() {
        iv = (RectImageView) findViewById(R.id.user_icon);
        iv.setOnClickListener(v -> {
            showDialog();
        });

    }

    private void showDialog() {
        CommonDialog dialog = CommonDialog.getInstance();
        dialog.createDiaog(this, "图片显示", "现在就是要展示的图片哦", R.mipmap.icon_common_dial, R.mipmap.icon_common_cancel, new CommonDialogClickListener() {
            @Override
            public void confrim() {

                Toast.makeText(CircleImageViewActivity.this, "拨打电话", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cancle() {
                Toast.makeText(CircleImageViewActivity.this, "取消", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
