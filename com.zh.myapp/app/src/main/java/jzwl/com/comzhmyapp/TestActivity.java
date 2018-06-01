package jzwl.com.comzhmyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import jzwl.com.comzhmyapp.ui.activity.TestCardViewActivity;
import jzwl.com.comzhmyapp.ui.activity.TestRecyclerViewPullRefreshActivity;
import jzwl.com.comzhmyapp.ui.activity.TestViewGroupActivity;

/**
 * 创建日期：2017/10/17
 * 描述: 所有测试Activity入口
 *
 * @author: zhaoh
 */
public class TestActivity extends Activity implements View.OnClickListener {
    private TextView tvCardView, tvRefresh, tvViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        tvCardView = (TextView) findViewById(R.id.tv_cardview);
        tvCardView.setOnClickListener(this);
        tvRefresh = (TextView) findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(this);
        tvViewPager = (TextView) findViewById(R.id.tv_viewpager);
        tvViewPager.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cardview:
                startActivity(new Intent(TestActivity.this, TestCardViewActivity.class));
                break;
            case R.id.tv_refresh:
                startActivity(new Intent(TestActivity.this, TestRecyclerViewPullRefreshActivity.class));
                break;
            case R.id.tv_viewpager:
                startActivity(new Intent(TestActivity.this, TestViewGroupActivity.class));
                break;
            default:
        }
    }
}
