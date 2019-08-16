package com.gao.wechat.set;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.gao.wechat.R;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.UserInfo;

public class Changepwd extends AppCompatActivity {

    public static final String ACTION = "com.gao.wechat.intent.action.set.Changepwd";

    private UserInfo userInfo;
    private EditText oldpwd;
    private EditText firstpwd;
    private EditText secondpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepwd);
        setCustomToolbar();

        userInfo = AppData.getInstance().getMyInfo();

        oldpwd = findViewById(R.id.settings_safety_pwd_old);
        firstpwd = findViewById(R.id.settings_safety_pwd_new);
        secondpwd = findViewById(R.id.settings_safety_pwd_new2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.myname_toolbar,menu);
        return true;
    }

    private void setCustomToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_finish);
        toolbar.setTitle(R.string.settings_safety_pwd);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //Toolbar的事件---返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.finish:
                finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
