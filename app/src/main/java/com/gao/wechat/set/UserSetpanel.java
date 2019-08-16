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

import com.gao.wechat.BaseActivity;
import com.gao.wechat.R;
import com.gao.wechat.activity.login.LoginActivity;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.UserInfo;
import com.gao.wechat.net.NetService;
import com.gao.wechat.util.SPUtil;

public class UserSetpanel extends AppCompatActivity {
    public static final String ACTION = "com.gao.wechat.intent.action.set.UserSetpanel";

    private UserInfo userInfo;
    private Toolbar toolbar;

    private LinearLayout menuSafety;
    private LinearLayout menuNotification;
    private LinearLayout menuPrivacy;
    private LinearLayout menuGeneral;
    private LinearLayout menuHelp;
    private LinearLayout menuAbout;
    private LinearLayout menuChange;
    private LinearLayout menuLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setpanel);
        userInfo = AppData.getInstance().getMyInfo();
        setCustomActionBar();
        initView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //Toolbar的事件---返回
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setCustomActionBar() {
        toolbar = findViewById(R.id.settings_toolbar);
        toolbar.setTitle("设置");
        this.setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void initView(){
        menuSafety = findViewById(R.id.settings_safety);
        menuNotification = findViewById(R.id.settings_notification);
        menuPrivacy = findViewById(R.id.settings_privacy);
        menuGeneral = findViewById(R.id.settings_general);
        menuHelp = findViewById(R.id.settings_help);
        menuAbout = findViewById(R.id.settings_about);
        menuChange = findViewById(R.id.settings_change);
        menuLogout = findViewById(R.id.settings_logout);
        menuLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        NetService.getInstance().closeConnection();
                        if (!NetService.getInstance().isConnected()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SPUtil.getInstance().setLogin(UserSetpanel.this, false);
                                    Intent intent = new Intent();
                                    intent.setAction(BaseActivity.FINISH);
                                    sendBroadcast(intent);
                                    startActivity(new Intent(LoginActivity.ACTION));
                                    finish();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
        menuSafety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetpanel.this, UserAndSafety.class);
                startActivity(intent);
            }
        });
        menuNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetpanel.this, MsgNotification.class);
                startActivity(intent);
            }
        });
        menuPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetpanel.this, PrivacySet.class);
                startActivity(intent);
            }
        });
        menuGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSetpanel.this, CurrencySet.class);
                startActivity(intent);
            }
        });
    }


}
