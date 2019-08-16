package com.gao.wechat.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.gao.wechat.MainActivity;
import com.gao.wechat.R;
import com.gao.wechat.action.UserAction;
import com.gao.wechat.activity.register.RegisterActivity;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.Result;
import com.gao.wechat.data.UserInfo;
import com.gao.wechat.database.DatabaseUtil;
import com.gao.wechat.net.NetService;
import com.gao.wechat.util.HashUtil;
import com.gao.wechat.util.SPUtil;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class LoginActivity extends AppCompatActivity {

    public static final String ACTION = "com.gao.wechat.intent.activity.action.login.LoginActivity";

    private NetService netService = NetService.getInstance();

    private UserInfo loginInfo;

    private Button btnLogin;
    private Button btnRegister;
    private EditText userID;
    private EditText password;

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        relativeLayout = findViewById(R.id.activity_login_layout);

        btnLogin = findViewById(R.id.Login_button);
        btnRegister = findViewById(R.id.register_button);
        userID = findViewById(R.id.Login_editText);
        password = findViewById(R.id.Login_passwordText);

        btnLogin.setOnClickListener(loginListener);
        btnRegister.setOnClickListener(registerListener);

        loginInfo = new UserInfo();

    }

    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            closeKeyboard(password, LoginActivity.this);
            String uid = userID.getText().toString();
            String pwd = password.getText().toString();
            if (uidVerify(uid) && passwordVerify(pwd)) {
                new LoginTask(LoginActivity.this, loginInfo).execute();
            }
        }
    };

    private boolean uidVerify(@NotNull String uid) {
        if (uid.equals("")) {
            toast("请输入用户名");
        } else if (uid.matches("\\w+\\x40\\w+\\x2e\\w+")) {
            loginInfo.setEmail(uid);
            return true;
        } else if (uid.matches("[0-9]+")) {
            loginInfo.setUserID(Long.parseLong(uid));
            return true;
        } else {
            toast("用户名格式错误！");
        }
        return false;
    }

    private boolean passwordVerify(@NotNull String password) {
        if (password.equals("")) {
            toast("请输入密码");
        } else {
            loginInfo.setPassword(HashUtil.SHA(password));
            return true;
        }
        return false;
    }

    private static class LoginTask extends AsyncTask<Void,Void,Result> {
        private WeakReference<UserInfo> myInfo;
        private WeakReference<LoginActivity> context;
        private LoginTask(LoginActivity context, UserInfo userInfo) {
            this.myInfo = new WeakReference<>(userInfo);
            this.context = new WeakReference<>(context);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            context.get().toastLong("正在登录...");
        }
        @Override
        protected Result doInBackground(Void... voids) {
            try {
                NetService.getInstance().setConnection();
                NetService.getInstance().setContext(context.get());
                if (!NetService.getInstance().isConnected()) {
                    return null;
                }
                UserAction.login(myInfo.get());
                AppData.getInstance().initListen(context.get());
                AppData.getInstance().waitForServer();
                return AppData.getInstance().getLoginResult();
            } catch (IOException e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if (result == null) {
                context.get().toastLong("无法连接服务器，请稍后重试。");
            } else {
                switch (result.getType()) {
                    case Result.LoginSuccess:
                        UserInfo userInfo = UserInfo.toUserInfo(result.getMsg());
                        DatabaseUtil.get(context.get()).saveMyInfo(userInfo);
                        SPUtil.getInstance().setLogin(context.get(), true);
                        SPUtil.getInstance().setLoginUid(context.get(), userInfo.getUserID());
                        Intent intent = new Intent(MainActivity.ACTION);
                        context.get().startActivity(intent);
                        context.get().finish();
                        break;
                    case Result.LoginWrongPwd:
                        context.get().toastLong("用户密码不正确！");
                        break;
                    case Result.LoginNotExist:
                        context.get().toastLong("该用户不存在！");
                        break;
                    default:
                        context.get().toastLong("参数错误！");
                        break;
                }
            }
        }
    }

    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(RegisterActivity.ACTION);
            startActivity(intent);
        }
    };

    public void toast(String text) {
        Snackbar.make(relativeLayout,text,Snackbar.LENGTH_SHORT).show();
    }

    public void toastLong(String text) {
        Snackbar.make(relativeLayout,text,Snackbar.LENGTH_LONG).show();
    }

    public static void closeKeyboard(EditText mEditText, Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
    }


}
