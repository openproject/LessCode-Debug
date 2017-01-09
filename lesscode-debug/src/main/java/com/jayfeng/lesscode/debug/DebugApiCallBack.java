package com.jayfeng.lesscode.debug;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

public interface DebugApiCallBack {
    void invoke(Context context, DebugApi debugApi, RecyclerView.Adapter holderAdapter);
}
