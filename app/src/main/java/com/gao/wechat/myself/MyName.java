package com.gao.wechat.myself;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.gao.wechat.BaseActivity;
import com.gao.wechat.R;
import com.gao.wechat.action.UserAction;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.Result;
import com.gao.wechat.data.UserInfo;
import com.gao.wechat.database.DatabaseUtil;
import com.gao.wechat.net.NetService;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MyName extends AppCompatActivity {

    public static final String ACTION = "com.gao.wechat.intent.action.myself.MyName";

    private EditText editNickName;
    private UserInfo userInfo;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_name);
        setCustomToolBar();

        userInfo = AppData.getInstance().getMyInfo();
        editNickName = findViewById(R.id.my_Name_edit);
        editNickName.setText(userInfo.getNickName());
        editNickName.setFocusable(true);
        editNickName.requestFocus();
        showKeyboard(editNickName);
    }

    private void setCustomToolBar() {
        toolbar = findViewById(R.id.settings_toolbar);
        toolbar.setTitle("修改昵称");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
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
                submitResult(editNickName.getText().toString());
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitResult(String message) {
        final UserInfo myInfo = AppData.getInstance().getMyInfo();
        myInfo.setNickName(message);
        new AsyncTask<Void,Void,Integer>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(MyName.this, "正在提交...", Toast.LENGTH_SHORT).show();
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
                            DatabaseUtil.get(MyName.this).saveMyInfo(myInfo);
                            toast("修改成功");
                            MyName.this.finish();
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

    /**
     * 弹出软键盘
     * @param editText 要弹出的位置
     */
    private void showKeyboard(final EditText editText){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(editText, 0);
                }
            }
        }, 200);
    }

    protected void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void toastLong(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}
