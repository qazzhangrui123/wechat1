package com.gao.wechat.set;

import androidx.annotation.NonNull;
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
import com.suke.widget.SwitchButton;

public class FriendSeeSet extends AppCompatActivity {
    public static final String ACTION = "com.gao.wechat.intent.action.set.FriendSeeSet";

    private UserInfo userInfo;

    private LinearLayout llFriendWatch;     // 谁能看我的动态
    private LinearLayout llFriendWatchDate; // 允许查看的范围
    private LinearLayout llFriendNoWatch;   // 不看他（她）的动态
    private SwitchButton switchSoundEffect;
    private SwitchButton switchWifiVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_see_set);
        setCustomToolbar();

        userInfo = AppData.getInstance().getMyInfo();

        initSwitchButton();
        initView();
    }

    private void setCustomToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_finish);
        toolbar.setTitle(R.string.settings_privacy_friend);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void initSwitchButton(){
        switchSoundEffect = findViewById(R.id.settings_switch_sound_effect);
        switchSoundEffect.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // TODO: 添加事件
            }
        });
        switchWifiVideo = findViewById(R.id.settings_switch_wifi_video);
        switchWifiVideo.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // TODO: 添加事件
            }
        });
    }

    public void initView(){
        llFriendWatch = findViewById(R.id.settings_privacy_friend_watch);
        llFriendWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO：添加点击事件
            }
        });
        llFriendWatchDate = findViewById(R.id.settings_privacy_friend_watch_date);
        llFriendWatchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO：添加点击事件
            }
        });
        llFriendNoWatch = findViewById(R.id.settings_privacy_friend_nowatch);
        llFriendNoWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO：添加点击事件
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
