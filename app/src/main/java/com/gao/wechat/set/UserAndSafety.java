package com.gao.wechat.set;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.gao.wechat.R;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.UserInfo;

public class UserAndSafety extends AppCompatActivity {

    public static final String ACTION = "com.gao.wechat.intent.action.set.UserAndSafety";

    private UserInfo userInfo;
    private LinearLayout changepwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_and_safety);
        setCustomToolbar();

        userInfo = AppData.getInstance().getMyInfo();
        changepwd = findViewById(R.id.settings_safety_pwd);
        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAndSafety.this,Changepwd.class);
                startActivity(intent);
            }
        });
    }

    private void setCustomToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_finish);
        toolbar.setTitle(R.string.settings_safety);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //Toolbar的事件---返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
