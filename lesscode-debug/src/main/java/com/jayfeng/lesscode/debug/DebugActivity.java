package com.jayfeng.lesscode.debug;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.jayfeng.lesscode.core.AdapterLess;
import com.jayfeng.lesscode.core.ViewLess;

import java.util.ArrayList;
import java.util.List;

public class DebugActivity extends AppCompatActivity {

    public List<DebugKV> mKVListData = new ArrayList<>();
    public List<DebugApi> mApiListData = new ArrayList<>();

    private ViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        fillKeyValues(mKVListData);
        fillApiValues(mApiListData);

        mViewPager = ViewLess.$(this, R.id.viewpager);
        mViewPager.setOffscreenPageLimit(10);
        mFragmentPagerAdapter = AdapterLess.$pager(getSupportFragmentManager(), 2, new AdapterLess.FullFragmentPagerCallBack() {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment;
                if (position == 0) {
                    fragment = new KVFragment();
                } else {
                    fragment = new ApiFragment();
                }
                return fragment;
            }

            @Override
            public String getPageTitle(int position) {
                return "";
            }
        });
        mViewPager.setAdapter(mFragmentPagerAdapter);
    }

    protected void fillKeyValues(List<DebugKV> debugKVList) {
        debugKVList.add(new DebugKV("dddd", "xxxxx"));
    }

    protected void fillApiValues(List<DebugApi> debugApiList) {
        debugApiList.add(new DebugApi("用户模块 - 获取用户信息", "user/getUserInfo/4", new DebugApiCallBack() {
            @Override
            public void invoke(Context context, DebugApi debugApi, RecyclerView.Adapter adapter) {
                // DEMO
            }
        }));
    }

    protected void showDebugApiLog(String debugApiLog, String title) {
        Intent intent = new Intent(this, DebugApiLogActivity.class);
        intent.putExtra(DebugApiLogActivity.KEY_LOG, debugApiLog);
        intent.putExtra(DebugApiLogActivity.KEY_TITLE, title);
        startActivity(intent);
    }

    protected void updateDebugApiToSuccess(DebugApi debugApi, RecyclerView.Adapter adapter) {
        debugApi.setDebugApiState(DebugApiState.STATE_SUCCESS);
        adapter.notifyDataSetChanged();
    }

    protected void updateDebugApiToFailure(DebugApi debugApi, RecyclerView.Adapter adapter) {
        debugApi.setDebugApiState(DebugApiState.STATE_FAILURE);
        adapter.notifyDataSetChanged();
    }

    public List<DebugKV> getKVListData() {
        return mKVListData;
    }

    public List<DebugApi> getApiListData() {
        return mApiListData;
    }
}
