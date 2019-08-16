package com.gao.wechat.activity.register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gao.wechat.R;
import com.gao.wechat.action.UserAction;
import com.gao.wechat.data.Result;
import com.gao.wechat.data.TransMsg;
import com.gao.wechat.data.UserInfo;
import com.gao.wechat.net.NetService;
import com.gao.wechat.util.HashUtil;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    public static final String ACTION = "com.gao.wechat.intent.action.activity.register.RegisterActivity";

    private EditText editNickName;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editRePassword;
    private LinearLayout btnRegister;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolbar();

        initView();
        setListeners();
    }

    private void initView() {
        linearLayout = findViewById(R.id.register_layout);
        editNickName = findViewById(R.id.register_nickname);
        editEmail = findViewById(R.id.register_email);
        editPassword = findViewById(R.id.register_pwd);
        editRePassword = findViewById(R.id.register_repwd);
        btnRegister = findViewById(R.id.register_button);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_finish);
        toolbar.setTitle(R.string.register_title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(editRePassword, RegisterActivity.this);
                String name = editNickName.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String pwd = editPassword.getText().toString().trim();
                String repwd = editRePassword.getText().toString().trim();
                if (name.equals("")) {
                    toast("请输入昵称！");
                } else if (email.equals("")) {
                    toast("请输入邮箱");
                } else if (!email.contains("@")) {
                    toast("请确认邮箱格式是否正确！");
                } else if (pwd.equals("")) {
                    toast("请输入密码");
                } else if (repwd.equals("")) {
                    toast("请再次输入密码");
                } else if (!pwd.equals(repwd)) {
                    toast("请确认两次输入的密码是否相同！");
                } else {
                    tryRegister(name,email, HashUtil.SHA(pwd));
                }
            }
        });
    }

    public void toast(String text) {
        Snackbar.make(linearLayout,text,Snackbar.LENGTH_SHORT).show();
    }

    public void toastLong(String text) {
        Snackbar.make(linearLayout,text,Snackbar.LENGTH_LONG).show();
    }

    public void toastIndefinite(String text) {
        Snackbar.make(linearLayout,text,Snackbar.LENGTH_INDEFINITE).show();
    }

    private static TransMsg registerMessage = null;
    private static Result registerResult = null;
    private static boolean isReceived = false;
    private NetService netService = NetService.getInstance();

    private void tryRegister(final String nickname, final String email, final String encrypted) {
        new AsyncTask<Void,Void,Integer>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                toastLong("正在提交注册申请......");
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                UserInfo info = new UserInfo();
                info.setNickName(nickname);
                info.setEmail(email);
                info.setPassword(encrypted);
                try {
                    isReceived = false;
                    netService.setConnection();
                    if (!netService.isConnected()) {
                        return -1;  // 网络连接错误
                    }
                    UserAction.register(info);
                    while (!isReceived); // 等待服务器的结果
                    netService.closeConnection();
                    if (registerMessage.getObject().equals("")) {
                        return -1;  // 网络连接错误
                    }
                    registerResult = Result.toResult(registerMessage.getObject());
                    if (registerResult.typeEquals(Result.RegisterSuccess)) {
                        return 2;
                    } else {
                        return 1;
                    }
                } catch (IOException e) {
                    return -2;  // I/O 错误
                }
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                switch (integer) {
                    case 1:
                        toast("注册失败，原因：服务器出现问题");
                        break;
                    case 2:
                        UserInfo info = new UserInfo();
                        if (!registerResult.getMsg().trim().equals("")) {
                            info = UserInfo.toUserInfo(registerResult.getMsg());
                        }
                        toastIndefinite("注册成功！账号为" + info.getUserID());
                        break;
                    case -1:
                        toast("无法连接服务器！");
                        break;
                    default:
                        toast("参数错误，错误代码：" + integer);
                        break;
                }
            }
        }.execute();
    }

    public static void onRegisterArrived(TransMsg message) {
        registerMessage = message;
        isReceived = true;
    }

    public static void closeKeyboard(EditText mEditText, Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}

