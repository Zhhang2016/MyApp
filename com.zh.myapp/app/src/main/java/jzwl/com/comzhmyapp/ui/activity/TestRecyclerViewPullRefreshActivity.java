package jzwl.com.comzhmyapp.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jzwl.com.comzhmyapp.R;
import jzwl.com.comzhmyapp.ui.adapter.SingleItemAdapter;

/**
 * Created by zh on 2017/10/16.
 * describe: 测试的是 RecyclerView下拉刷新的功能。
 *
 * @author zhaoh
 */

public class TestRecyclerViewPullRefreshActivity extends Activity {
    private RecyclerView mRecycerView;
    private List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        initData();
        initView();
    }

    private void initView() {
        mRecycerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycerView.setLayoutManager(layoutManager);
        SingleItemAdapter adapter = new SingleItemAdapter(TestRecyclerViewPullRefreshActivity.this, list);
        mRecycerView.setAdapter(adapter);
    }

    private void initData() {
        for (int i = 0; i < 60; i++) {
            list.add("String" + i);
        }
    }
}
