package com.jayfeng.lesscode.debug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jayfeng.lesscode.core.ViewLess;

public class DebugApiLogActivity extends AppCompatActivity {

    public static final String KEY_LOG = "key_log";

    private String mLogText;

    private TextView mLogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_api_log);

        mLogText = getIntent().getStringExtra(KEY_LOG);

        mLogView = ViewLess.$(this, R.id.log);
        mLogView.setText(mLogText);
    }


}
