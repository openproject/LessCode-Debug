package com.jayfeng.lesscode.debug;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jayfeng.lesscode.core.ToastLess;
import com.jayfeng.lesscode.core.ViewLess;

public class DebugApiLogActivity extends AppCompatActivity {

    public static final String KEY_LOG = "key_log";

    private String mLogText;

    private Button mCopyButton;
    private TextView mLogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_api_log);

        mLogText = getIntent().getStringExtra(KEY_LOG);

        mCopyButton = ViewLess.$(this, R.id.copy);
        mLogView = ViewLess.$(this, R.id.log);
        mLogView.setText(mLogText);

        mCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                clip.setText(mLogText);

                Toast.makeText(DebugApiLogActivity.this, "已复制到粘贴板", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
