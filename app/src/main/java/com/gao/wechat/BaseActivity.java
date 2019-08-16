package com.gao.wechat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    public static final String FINISH = "com.gao.wechat.intent.action.FINISH";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcast();
    }

    private void registerBroadcast() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(FINISH)) {
                    finish();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(FINISH);
        registerReceiver(receiver, filter);
    }

    protected void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void toastLong(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}
