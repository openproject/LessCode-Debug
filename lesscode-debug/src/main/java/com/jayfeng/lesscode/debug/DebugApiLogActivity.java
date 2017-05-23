package com.jayfeng.lesscode.debug;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jayfeng.lesscode.core.LogLess;
import com.jayfeng.lesscode.core.ToastLess;
import com.jayfeng.lesscode.core.ViewLess;

public class DebugApiLogActivity extends AppCompatActivity {

    public static final String KEY_TITLE = "key_title";
    public static final String KEY_LOG = "key_log";

    private String mLogTitle;
    private String mLogText;

    private Button mCopyButton;
    private TextView mLogView;

    private StringBuilder mLogStringBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_api_log);

        mLogTitle = getIntent().getStringExtra(KEY_TITLE);
        mLogText = getIntent().getStringExtra(KEY_LOG);

        mLogStringBuilder.append("\n");
        mLogStringBuilder.append("------------------------------------------------------");
        mLogStringBuilder.append("\n");
        mLogStringBuilder.append("// " + mLogTitle);
        mLogStringBuilder.append("\n");
        mLogStringBuilder.append("------------------------------------------------------");
        mLogStringBuilder.append("\n");
        mLogStringBuilder.append(mLogText);
        mLogStringBuilder.append("\n");
        mLogStringBuilder.append("\n");

        mCopyButton = ViewLess.$(this, R.id.copy);
        mLogView = ViewLess.$(this, R.id.log);
        mLogView.setText(mLogStringBuilder.toString());

        mCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                clip.setText(mLogStringBuilder.toString());

                Toast.makeText(DebugApiLogActivity.this, "Copyed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String newTitle = intent.getStringExtra(KEY_TITLE);
        String newLog = intent.getStringExtra(KEY_LOG);

        mLogStringBuilder.append("------------------------------------------------------");
        mLogStringBuilder.append("\n");
        mLogStringBuilder.append("// " + newTitle);
        mLogStringBuilder.append("\n");
        mLogStringBuilder.append("------------------------------------------------------");
        mLogStringBuilder.append("\n");
        mLogStringBuilder.append(newLog);
        mLogStringBuilder.append("\n");
        mLogStringBuilder.append("\n");
        mLogStringBuilder.append("\n");

        mLogView.setText(mLogStringBuilder.toString());
    }
}
