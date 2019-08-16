package com.gao.wechat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gao.wechat.R;
import com.gao.wechat.action.UserAction;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.FriendInfo;
import com.gao.wechat.data.UserInfo;

import java.io.IOException;
import java.util.ArrayList;

public class FriendInfoActivity extends AppCompatActivity {

    public static final String ACTION = "com.gao.wechat.intent.action.FriendInfoActivity";

    public static final String FID = "friend_id";
    public static final String FINFO = "friend_info";

    private ImageView imgAvatar;
    private TextView tvNickName;
    private TextView tvNickNameRe;
    private TextView tvUserID;
    private TextView tvRegion;
    private TextView tvEmail;
    private TextView tvSignature;
    private TextView btnTvNote;
    private TextView btnTvMore;
    private TextView btnTvSend;

    private FriendInfo friendInfo;

    public FriendInfoActivity() {
        super();
    }


    /**
     * 向该对象传入 intent 信息，信息包含有 用户ID
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        initDataFromIntent();
        initContentView();
        bindDataFromIntent();
        initContentEvent();
        initToolbar();

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.friend_info_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }
    }

    private void initDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            friendInfo = (FriendInfo) intent.getSerializableExtra(FINFO);
            long friendID = intent.getLongExtra(FID, 0);
            setFriendInfoById(friendID);
        }
    }

    private void initContentView() {
        imgAvatar = findViewById(R.id.friend_info_image);
        tvNickName = findViewById(R.id.friend_info_nickname);
        tvNickNameRe = findViewById(R.id.friend_info_nickname_gone);
        tvUserID = findViewById(R.id.friend_info_uid);
        tvRegion = findViewById(R.id.friend_info_region);
        tvEmail = findViewById(R.id.friend_info_email);
        tvSignature = findViewById(R.id.friend_info_signature);
        btnTvNote = findViewById(R.id.friend_info_note);
        btnTvMore = findViewById(R.id.friend_info_more);
        btnTvSend = findViewById(R.id.friend_info_button);
    }

    private void initContentEvent() {
        btnTvNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 添加代码，启动界面
            }
        });
        btnTvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 添加代码，启动界面
            }
        });
        if (AppData.getInstance().containsFriend(friendInfo.getUserID()) ||
                AppData.getInstance().getMyInfo().getUserID() == friendInfo.getUserID()) {
            // 如果是好友(或本人)，则显示发送消息
            btnTvSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ChatActivity.ACTION);
                    intent.putExtra(ChatActivity.FID,friendInfo.getUserID());
                    startActivity(intent);
                    FriendInfoActivity.this.finish();
                }
            });
        } else {
            btnTvNote.setVisibility(View.GONE);
            btnTvSend.setText(R.string.friend_info_btn_apply);
            btnTvSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: 添加好友界面
                    Toast.makeText(FriendInfoActivity.this, "APPLY?", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void bindDataFromIntent() {
        imgAvatar.setImageResource(R.drawable.ic_launcher_background);
        tvUserID.setText("账户：" + friendInfo.getUserID());
        tvRegion.setText("地区：" + friendInfo.getRegion());
        tvEmail.setText("邮箱  " + friendInfo.getEmail());
        tvSignature.setText("个性签名  " + friendInfo.getSignature());

        if (!friendInfo.getNote().equals("")) {
            tvNickName.setText(friendInfo.getNote());
            tvNickNameRe.setText("用户昵称：" + friendInfo.getNickName());
        } else {
            tvNickName.setText(friendInfo.getNickName());
            tvNickNameRe.setVisibility(View.GONE);
        }

        if (friendInfo.getEmail().equals("@")) {
            tvEmail.setVisibility(View.GONE);
        }
        if (friendInfo.getSignature().equals("")) {
            tvSignature.setVisibility(View.GONE);
        }
    }

    private void setFriendInfoById(long friendID) {
        if (friendInfo == null) {
            FriendInfo friend = AppData.getInstance().getFriendById(friendID);
            if (friend != null) {
                friendInfo = friend;
            } else if (friendID != 0) {
                try {
                    // 从服务器中读取消息
                    UserAction.findFriend(new UserInfo(friendID));
                    AppData.getInstance().initFindFriend();
                    AppData.getInstance().waitForServer();
                    ArrayList<FriendInfo> list = AppData.getInstance().getFindFriendResult();
                    if (list != null && list.size() > 0) {
                        friendInfo = list.get(0);
                    }
                } catch (NullPointerException e) {
                    toast("无法连接到服务器");
                    Log.d("WeChat:FriendInfo:FindFriend", "无法连接到服务器");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("WeChat:FriendInfo:FindFriend", "I/O 异常");
                }
            } else {
                friendInfo = new FriendInfo();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_info_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.friend_info_action:
                // TODO: 一个事件
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
