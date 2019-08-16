package com.gao.wechat.myself;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gao.wechat.R;
import com.gao.wechat.action.UserAction;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.Result;
import com.gao.wechat.data.UserInfo;
import com.gao.wechat.database.DatabaseUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyBirthday extends AppCompatActivity {
    public static final String ACTION = "com.gao.wechat.intent.action.myself.MyBirthday";
    private TextView textView;
    private LinearLayout llBirthday;
    private String birthday;
    private UserInfo userInfo;
    private Toolbar toolbar;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_birthday);
        setCustomToolbar();

        userInfo = AppData.getInstance().getMyInfo();
        birthday = userInfo.getBirthday();
        textView = findViewById(R.id.settings_birthday_edit_text);
        textView.setText("生日：" + birthday);
        llBirthday = findViewById(R.id.settings_birthday_edit);
        llBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerView time = new TimePickerBuilder(MyBirthday.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        birthday = getTime(date);
                        textView.setText("生日 " + birthday);
                    }
                }).build();
                Calendar date = parseCalendar(birthday);
                if (date != null) { time.setDate(date); }
                time.show();
            }
        });
    }

    /**
     * 将String转换为Calendar类对象
     * @param dateStr 要转换的字符串
     * @return 转换结果，如果格式不正确，返回空值
     */
    private Calendar parseCalendar(String dateStr) {
        try {
            Date date = dateFormat.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            Log.e("MyBirthday", "日期格式错误！");
            return null;
        }

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        return dateFormat.format(date);
    }

    private void setCustomToolbar() {
        toolbar = findViewById(R.id.settings_toolbar);
        toolbar.setTitle("修改生日");
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
                submitResult(birthday);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitResult(String message) {
        final UserInfo myInfo = AppData.getInstance().getMyInfo();
        myInfo.setBirthday(message);
        new AsyncTask<Void,Void,Integer>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                toast("正在提交...");
            }
            @Override
            protected Integer doInBackground(Void... voids) {
                try {
                    UserAction.updateMyInfo(myInfo);
                    AppData.getInstance().initUpdate();
                    AppData.getInstance().waitForServer();
                    return 1;
                } catch (NullPointerException e) {
                    return -1;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return -2;
            }
            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                switch (result) {
                    case -1:
                        Log.d("WeChat:UpdateUserInfo:NickName", "无法连接到服务器！");
                        toast("无法连接到服务器");
                        break;
                    case -2:
                        Log.d("WeChat:UpdateUserInfo:NickName","I/O 错误！");
                        toast("系统出现错误，请稍候重试");
                        break;
                    case 1:
                        Result r = AppData.getInstance().getUpdateResult();
                        if (r.typeEquals(Result.UpdateSuccess)) {
                            AppData.getInstance().setMyInfo(myInfo);
                            DatabaseUtil.get(MyBirthday.this).saveMyInfo(myInfo);
                            toast("修改成功");
                            MyBirthday.this.finish();
                        } else {
                            toast("修改失败");
                        }
                        break;
                    default:
                        toast("参数错误！");
                        break;
                }
            }
        }.execute();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.myname_toolbar,menu);
        return true;
    }

    protected void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void toastLong(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
