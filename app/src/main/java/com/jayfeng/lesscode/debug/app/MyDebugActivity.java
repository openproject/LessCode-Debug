package com.jayfeng.lesscode.debug.app;

import com.jayfeng.lesscode.debug.DebugActivity;
import com.jayfeng.lesscode.debug.DebugApi;
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
        debugApiList.add(new DebugApi("通用 - 全局字典", "common/dict"));
        debugApiList.add(new DebugApi("通用 - 检查更新", "common/update"));
    }
}
