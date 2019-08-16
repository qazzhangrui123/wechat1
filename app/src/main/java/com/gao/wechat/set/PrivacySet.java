package com.gao.wechat.set;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gao.wechat.R;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.UserInfo;
import com.suke.widget.SwitchButton;

public class PrivacySet extends AppCompatActivity {
    public static final String ACTION = "com.gao.wechat.intent.action.set.PrivacySet";

    private UserInfo userInfo;
    private LinearLayout addmw;
    private LinearLayout friendset;
    private SwitchButton switchConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_set);
        setCustomToolbar();

        userInfo = AppData.getInstance().getMyInfo();

        initView();
        initClickListener();
        initSwitchButton();
    }

    private void setCustomToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_finish);
        toolbar.setTitle(R.string.settings_privacy);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void initView(){
        switchConfirm = findViewById(R.id.settings_switch_confirm);
        addmw = findViewById(R.id.settings_privacy_add);
        friendset = findViewById(R.id.settings_privacy_friend);
    }

    public void initClickListener(){
        addmw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrivacySet.this,AddMyway.class);
                startActivity(intent);
            }
        });
        friendset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrivacySet.this,FriendSeeSet.class);
                startActivity(intent);
            }
        });
    }

    public void initSwitchButton() {
        switchConfirm.setChecked(false);
        switchConfirm.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // TODO: 添加按钮发生改变的事件
            }
        });
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
