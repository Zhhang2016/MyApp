package jzwl.com.comzhmyapp.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jzwl.com.comzhmyapp.R;
import jzwl.com.comzhmyapp.ui.adapter.MyViewPagerAdapter;

/**
 * 创建日期：2017/10/17
 * 描述: ViewPager的 Demo
 *
 * @author: zhaoh
 */
public class TestViewPagerActivity extends Activity {

    private ViewPager viewPager;
    private List<View> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        initData();
        initView();
    }

    private void initData() {
        for (int i = 0; i < 6; i++) {
            View view = View.inflate(TestViewPagerActivity.this,R.layout.layout_viewpager_item,null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            tvName.setText("第"+i+"页");
             mList.add(view);
        }
    }
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(mList);
        viewPager.setAdapter(adapter);
    }
}
