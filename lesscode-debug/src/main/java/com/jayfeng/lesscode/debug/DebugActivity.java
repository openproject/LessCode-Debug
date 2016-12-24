package com.jayfeng.lesscode.debug;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jayfeng.lesscode.core.AdapterLess;
import com.jayfeng.lesscode.core.DisplayLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.jayfeng.lesscode.core.other.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class DebugActivity extends AppCompatActivity {

    private List<DebugKV> mListData = new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.Adapter<AdapterLess.RecyclerViewHolder> mAdapter;
    private DividerItemDecoration mDividerItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        fillValues(mListData);

        initView();
    }

    private void initView() {
        mRecyclerView = ViewLess.$(this, R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, new ColorDrawable(Color.parseColor("#66666666")));
        mDividerItemDecoration.setHeight(1);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        mAdapter = AdapterLess.$recycler(this,
                mListData,
                R.layout.activity_debug_item_kv,
                new AdapterLess.RecyclerCallBack<DebugKV>() {

                    @Override
                    public void onBindViewHolder(int i, AdapterLess.RecyclerViewHolder recyclerViewHolder, DebugKV debugKV) {
                        TextView keyView = recyclerViewHolder.$view(R.id.key);
                        TextView valueView = recyclerViewHolder.$view(R.id.value);

                        keyView.setText(debugKV.getKey());
                        valueView.setText(debugKV.getValue());
                    }
                });
        mRecyclerView.setAdapter(mAdapter);
    }

    protected void fillValues(List<DebugKV> debugKVList) {
        debugKVList.add(new DebugKV("dddd", "xxxxx"));
    }
}
