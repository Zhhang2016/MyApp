package jzwl.com.comzhmyapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jzwl.com.comzhmyapp.ui.activity.CircleImageViewActivity;
import jzwl.com.comzhmyapp.ui.activity.TestHandOutEventMechanismActivity;
import jzwl.com.comzhmyapp.util.CommonTools;
import jzwl.com.comzhmyapp.util.CustomSureDialog;
import jzwl.com.comzhmyapp.zxing.CaptureActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.tv_test)
    TextView tvTest;
    @BindView(R.id.tv_open)
    TextView tvOpen;
    @BindView(R.id.bt_test)
    Button btTest;
    private final int SDK_PERMISSION_REQUEST = 127;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_open:
             /*   if (CommonTools.checkPermission(MainActivity.this, null, new String[]{Manifest.permission.CAMERA}, SDK_PERMISSION_REQUEST)) {
                    return;
                }
                callCamera();*/
                break;
            case R.id.bt_test:
//                startActivity(new Intent(MainActivity.this, TestActivity.class));
//                startActivity(new Intent(MainActivity.this, TestCardViewActivity.class));
                break;

            default:
        }
    }

    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception ex) {
        }
        return camera;
    }

    public void callCamera() {
        Camera c = getCameraInstance();
        if (c != null) {
            c.release();
            Intent getImageByCamera = new Intent(MainActivity.this, CaptureActivity.class);
            getImageByCamera.putExtra("wherefrom", "ServiceFragment");
            startActivity(getImageByCamera);
        } else {
            CommonTools.showToast(MainActivity.this, "亲，请检查摄像头是否存在或是否损坏");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SDK_PERMISSION_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callCamera();
                } else {
                    if (!CustomSureDialog.getInstance().isAlertDialog()) {
                        showUpdateDialog(MainActivity.this, getString(R.string.open_camera));
                    }
                }
                break;
            default:
        }
    }

    public void showUpdateDialog(final Context context, String msg) {
        try {
            if (!CustomSureDialog.getInstance().isAlertDialog()) {
                CustomSureDialog.getInstance().createAlertDialog(context, msg, "知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomSureDialog.getInstance().cancelAlertDialog();
                        Uri packageURI = Uri.parse("package:" + context.getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        startActivity(intent);


                    }
                }).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void two(View view) {
        startActivity(new Intent(MainActivity.this, CircleImageViewActivity.class));
    }

    @OnClick({R.id.tv_open, R.id.tv_test, R.id.bt_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_open:
                break;
            case R.id.tv_test:
                startActivity(new Intent(MainActivity.this,TestHandOutEventMechanismActivity.class));
                break;
            case R.id.bt_test:
                break;
                default:
                    break;
        }
    }
}

