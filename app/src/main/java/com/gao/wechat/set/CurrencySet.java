package com.gao.wechat.set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.gao.wechat.R;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.UserInfo;

public class CurrencySet extends AppCompatActivity {
    public static final String ACTION = "com.gao.wechat.intent.action.set.CurrencySet";

    private UserInfo userInfo;
    private Toolbar toolbar;
    //private SwitchButton switchButton1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_set);
        toolbar = (Toolbar) findViewById(R.id.toolbar_finish);
        userInfo = AppData.getInstance().getMyInfo();
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        init();
    }

    public void init(){
        //switchButton1 = (SwitchButton) findViewById(R.id.switch_button1);


    }
    public void initSwichbutton(){
        /*switchButton1.setChecked(false);
        switchButton1.isChecked();
        switchButton1.toggle();     //switch state
        switchButton1.toggle(false);//switch without animation
        switchButton1.setShadowEffect(true);//disable shadow effect
        switchButton1.setEnabled(true);//disable button
        switchButton1.setEnableEffect(false);//disable the switch animation
        switchButton1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
            }
        });*/
    }
    //Toolbar的事件---返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
