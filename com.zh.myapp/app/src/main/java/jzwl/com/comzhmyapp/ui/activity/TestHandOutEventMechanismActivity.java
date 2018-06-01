package jzwl.com.comzhmyapp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jzwl.com.comzhmyapp.R;

/**
 * 创建日期：2018/6/1
 * 描述: 测试事件的分发机制
 *
 * @author: zhaoh
 */
public class TestHandOutEventMechanismActivity extends AppCompatActivity {

    @BindView(R.id.bt_view)
    Button btView;
    @BindView(R.id.rl_viewgroup)
    RelativeLayout rlViewgroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_handout_event);
        ButterKnife.bind(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("TestHandOutEvent","dispatchTouchEvent==="+ev.getAction()+";getX"+ev.getX()+";getY=="+ev.getY());
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TestHandOutEvent","MotionEvent=="+event.getAction());
        return super.onTouchEvent(event);
    }

    @OnClick({R.id.bt_view, R.id.rl_viewgroup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_view:
                break;
            case R.id.rl_viewgroup:
                break;
                default:
                    break;
        }
    }
}
