package com.jayfeng.lesscode.debug;

public class DebugApi {

    private String title;
    private String url;
    private int debugApiState = DebugApiState.STATE_NONE;
    private DebugApiCallBack debugApiCallBack;

    public DebugApi(String title, String url, DebugApiCallBack debugApiCallBack) {
        this.title = title;
        this.url = url;
        this.debugApiCallBack = debugApiCallBack;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDebugApiState() {
        return debugApiState;
    }

    public void setDebugApiState(int debugApiState) {
        this.debugApiState = debugApiState;
    }

    public DebugApiCallBack getDebugApiCallBack() {
        return debugApiCallBack;
    }

    public void setDebugApiCallBack(DebugApiCallBack debugApiCallBack) {
        this.debugApiCallBack = debugApiCallBack;
    }
}
