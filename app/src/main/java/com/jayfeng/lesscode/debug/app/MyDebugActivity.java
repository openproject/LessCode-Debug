package com.jayfeng.lesscode.debug.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.jayfeng.lesscode.core.AdapterLess;
import com.jayfeng.lesscode.debug.DebugActivity;
import com.jayfeng.lesscode.debug.DebugApi;
import com.jayfeng.lesscode.debug.DebugApiCallBack;
import com.jayfeng.lesscode.debug.DebugApiState;
import com.jayfeng.lesscode.debug.DebugKV;

import java.util.List;

public class MyDebugActivity extends DebugActivity {

    @Override
    protected void fillKeyValues(List<DebugKV> debugKVList) {
        debugKVList.add(new DebugKV("UserToken", "11111111111111111111"));
        debugKVList.add(new DebugKV("UserToken2", "2222"));
        debugKVList.add(new DebugKV("UserToken3", "333"));
        debugKVList.add(new DebugKV("UserToken4", "444"));
        debugKVList.add(new DebugKV("UserToken5", "11111111111111111111"));
        debugKVList.add(new DebugKV("UserToken6", "11111111111111111111"));
        debugKVList.add(new DebugKV("UserToken7", "11111111111111111111"));
        debugKVList.add(new DebugKV("AndroidId", "next to do"));
    }

    @Override
    protected void fillApiValues(List<DebugApi> debugApiList) {
        debugApiList.add(new DebugApi("通用 - 全局字典", "common/dict", new DebugApiCallBack() {
            @Override
            public void invoke(Context context, DebugApi debugApi, RecyclerView.Adapter adapter) {
                updateDebugApiToFailure(debugApi, adapter);
                Toast.makeText(context, "tttttttttttttttttt", Toast.LENGTH_SHORT).show();
            }
        }));
        debugApiList.add(new DebugApi("通用 - 检查更新", "common/update", new DebugApiCallBack() {
            @Override
            public void invoke(Context context, DebugApi debugApi, RecyclerView.Adapter adapter) {
                updateDebugApiToSuccess(debugApi, adapter);
                Toast.makeText(context, "ooiiiiiii", Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
