package com.jayfeng.lesscode.debug.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jayfeng.lesscode.core.ViewLess;

public class MainActivity extends AppCompatActivity {

    private Button mToDebugButton;
    private Button mToDebugBySevenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToDebugButton = ViewLess.$(this, R.id.to_debug);
        mToDebugBySevenButton = ViewLess.$(this, R.id.to_debug_by_seven);

        mToDebugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDebugPage();
            }
        });
    }

    private void gotoDebugPage() {
        Intent intent = new Intent(this, MyDebugActivity.class);
        startActivity(intent);
    }
}
