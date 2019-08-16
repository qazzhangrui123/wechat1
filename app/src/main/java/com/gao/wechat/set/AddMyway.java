package com.gao.wechat.set;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.gao.wechat.R;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.UserInfo;
import com.suke.widget.SwitchButton;

public class AddMyway extends AppCompatActivity {
    public static final String ACTION = "com.gao.wechat.intent.action.set.AddMyway";

    private UserInfo userInfo;

    private SwitchButton switchUID;     // 账号
    private SwitchButton switchEmail;   // 邮箱
    private SwitchButton switchGroup;   // 群聊
    private SwitchButton switchQRCode;  // 二维码
    private SwitchButton switchProfile; // 名片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_myway);
        setCustomToolbar();

        userInfo = AppData.getInstance().getMyInfo();

        initSwitchButton();
    }

    private void setCustomToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_finish);
        toolbar.setTitle(R.string.settings_privacy_add);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void initSwitchButton(){
        switchUID = findViewById(R.id.settings_switch_add_uid);
        switchUID.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // TODO: 添加事件
            }
        });
        switchEmail = findViewById(R.id.settings_switch_add_email);
        switchEmail.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // TODO: 添加事件
            }
        });
        switchGroup = findViewById(R.id.settings_switch_add_group);
        switchGroup.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // TODO: 添加事件
            }
        });
        switchQRCode = findViewById(R.id.settings_switch_add_qrcode);
        switchQRCode.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // TODO: 添加事件
            }
        });
        switchProfile = findViewById(R.id.settings_switch_add_profile);
        switchProfile.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // TODO: 添加事件
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
