package com.jayfeng.lesscode.debug.app;

import android.app.Application;

import com.jayfeng.lesscode.core.$;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        $.getInstance()
                .context(this)
                .log(true, "LessCode-Debug")
                .build();
    }
}
