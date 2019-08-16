package com.gao.wechat.set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.gao.wechat.R;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.UserInfo;
import com.suke.widget.SwitchButton;

public class MsgNotification extends AppCompatActivity {
    public static final String ACTION = "com.gao.wechat.intent.action.set.MsgNotification";

    private UserInfo userInfo;
    private SwitchButton switchNewMsg;  // 新消息提醒
    private SwitchButton switchCall;    // 音视频通知
    private SwitchButton switchDetail;  // 通知显示详情
    private SwitchButton switchSound;   // 声音
    private SwitchButton switchVibrate; // 振动
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_notification);
        setCustomToolbar();

        userInfo = AppData.getInstance().getMyInfo();

        initView();
        initSwichbutton();
    }

    public void initView(){
        switchNewMsg = findViewById(R.id.settings_switch_new_message);
        switchCall = findViewById(R.id.settings_switch_call);
        switchDetail = findViewById(R.id.settings_switch_detail);
        switchSound = findViewById(R.id.settings_switch_sound);
        switchVibrate = findViewById(R.id.settings_switch_vibrate);
    }

    public void setCustomToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_finish);
        toolbar.setTitle(R.string.settings_notification);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void initSwichbutton() {
        switchNewMsg.setChecked(false);
        switchNewMsg.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // TODO: 添加按钮状态改变的事件
            }
        });
        switchCall.setChecked(false);
        switchCall.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // TODO: 添加按钮状态改变的事件
            }
        });
        switchDetail.setChecked(false);
        switchDetail.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // TODO: 添加按钮状态改变的事件
            }
        });
        switchSound.setChecked(false);
        switchSound.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // TODO: 添加按钮状态改变的事件
            }
        });
        switchVibrate.setChecked(false);
        switchVibrate.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // TODO: 添加按钮状态改变的事件
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
