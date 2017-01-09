package com.jayfeng.lesscode.debug;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jayfeng.lesscode.core.AdapterLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.jayfeng.lesscode.core.other.DividerItemDecoration;

import java.util.List;

public class KVFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.Adapter<AdapterLess.RecyclerViewHolder> mAdapter;
    private DividerItemDecoration mDividerItemDecoration;


    public KVFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kv, container, false);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {
        mRecyclerView = ViewLess.$(rootView, R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, new ColorDrawable(Color.parseColor("#66666666")));
        mDividerItemDecoration.setHeight(1);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        List<DebugKV> listData = ((DebugActivity)getActivity()).getKVListData();
        listData.add(0, new DebugKV("KEY", "值"));

        mAdapter = AdapterLess.$recycler(getContext(),
                listData,
                R.layout.fragment_kv_item,
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
}
