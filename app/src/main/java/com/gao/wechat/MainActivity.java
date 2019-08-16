package com.gao.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gao.wechat.activity.FindFriendActivity;
import com.gao.wechat.activity.QRCodeScanActivity;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.ChatRoom;
import com.gao.wechat.data.FriendInfo;
import com.gao.wechat.data.TransMsg;
import com.gao.wechat.database.DatabaseUtil;
import com.gao.wechat.net.NetService;
import com.gao.wechat.view.ChatListFragment;
import com.gao.wechat.view.ContactFragment;
import com.gao.wechat.view.ExploreFragment;
import com.gao.wechat.view.MyselfFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    public static final String ACTION = "com.gao.wechat.intent.action.MainActivity";

    public static final String UID = "my_uid";
    private long userID;

    private List<Fragment> fragmentList;
    private int lastFragmentPosition;
    private Toolbar toolbar;

    private TextView textView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.main_bottom_nav_chat_list:
                    setFragmentPosition(0);
                    toolbar.setTitle(R.string.app_name);
                    return true;
                case R.id.main_bottom_nav_contact:
                    setFragmentPosition(1);
                    toolbar.setTitle(R.string.main_bottom_nav_contact);
                    return true;
                case R.id.main_bottom_nav_discover:
                    setFragmentPosition(2);
                    toolbar.setTitle(R.string.main_bottom_nav_discover);
                    return true;
                case R.id.main_bottom_nav_myself:
                    setFragmentPosition(3);
                    toolbar.setTitle(R.string.main_bottom_nav_myself);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.main_hint_text);
        textView.setVisibility(View.GONE);

        // 从 Intent 和 数据库 中获取数据
        initIntentData();
        initDbData();
        initNetwork();

        // 初始化 Toolbar 和 Fragment 界面
        initToolbar();
        initFragments();

        BottomNavigationView mBottomNavigationView = findViewById(R.id.main_bottom_nav);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        Intent startIntent = new Intent(MainActivity.this, MsgService.class);
//        startService(startIntent);

    }

    private void initNetwork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!NetService.getInstance().isConnected()) {
                    NetService.getInstance().setContext(MainActivity.this);
                    NetService.getInstance().setConnection();
                    if (!NetService.getInstance().isConnected()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText("无法连接到服务器，请确认网络是否正常！");
                                textView.setVisibility(View.GONE);
                            }
                        });
                        try {
                            // 暂停 2s
                            Thread.sleep(2 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                }
            }
        }).start();
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getLongExtra(UID, 0L);
        }
    }

    private void initDbData() {
        // 从数据库中读取一些数据
        if (userID != 0) {
            AppData.getInstance().setMyInfo(DatabaseUtil.get(this).getMyInfo(userID));
            AppData.getInstance().setFriendList(DatabaseUtil.get(this).getFriendList(userID));
            for (FriendInfo info : AppData.getInstance().getFriendList()) {
                ArrayList<TransMsg> list = DatabaseUtil.get(this).getMessageByFriend(userID, info.getUserID());
                if (list.size() > 0) {
                    AppData.getInstance().getChatRoomList().add(new ChatRoom(info, list));
                }
            }
        }
    }

    protected void initToolbar() {
        toolbar = findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolbar);
    }

    protected void initFragments() {
        ChatListFragment f1 = ChatListFragment.newInstance();
        ContactFragment f2 = ContactFragment.newInstance();
        ExploreFragment f3 = new ExploreFragment();
        MyselfFragment f4 = MyselfFragment.newInstance();

        fragmentList = new ArrayList<>();
        fragmentList.add(f1);
        fragmentList.add(f2);
        fragmentList.add(f3);
        fragmentList.add(f4);
        setFragmentPosition(0);
    }

    protected void setFragmentPosition(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = fragmentList.get(position);
        Fragment lastFragment = fragmentList.get(lastFragmentPosition);
        lastFragmentPosition = position;
        transaction.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            transaction.add(R.id.main_frame_layout, currentFragment);
        }
        transaction.show(currentFragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_group:
                //创建群聊
                return true;
            case R.id.toolbar_addFriends:
                //添加朋友
                Intent intent = new Intent(MainActivity.this, FindFriendActivity.class);
                startActivity(intent);
                return true;
            case R.id.toolbar_scan:
                //扫描二维码
                showQRCodeScanView(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void showQRCodeScanView(Activity activity) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setCaptureActivity(QRCodeScanActivity.class);
        integrator.setBeepEnabled(false);
        integrator.setPrompt("将二维码放入框内，即可自动扫描");
        integrator.initiateScan();
    }

    public void toast(String text) {
        Snackbar.make(MainActivity.this.findViewById(R.id.main_frame_layout), text, Snackbar.LENGTH_SHORT).show();
    }

}
