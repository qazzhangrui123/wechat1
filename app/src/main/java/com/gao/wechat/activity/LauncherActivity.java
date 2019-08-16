package com.gao.wechat.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gao.wechat.MainActivity;
import com.gao.wechat.activity.login.LoginActivity;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.ChatRoom;
import com.gao.wechat.data.FriendInfo;
import com.gao.wechat.data.TransMsg;
import com.gao.wechat.data.TransMsgType;
import com.gao.wechat.data.UserInfo;
import com.gao.wechat.util.SPUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {

                ////////////////////
                initUserInfo();
                initFriendInfo();
                initChatRoomInfo();
                ////////////////////

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SPUtil util = SPUtil.getInstance();
                       /* boolean firstRun = util.isFirstRun(LauncherActivity.this);
                        boolean login = util.isLogin(LauncherActivity.this);
                        if (firstRun) {
                            // 启动登录界面
                            util.setFirstRun(LauncherActivity.this, false);
                            Intent intent = new Intent(LoginActivity.ACTION);
                            startActivity(intent);
                        } else if (!login) {
                            // 启动登录界面
                            Intent intent = new Intent(LoginActivity.ACTION);
                            startActivity(intent);
                        } else {
                            // 启动主界面*/
                            Intent intent = new Intent(MainActivity.ACTION);
                            intent.putExtra(MainActivity.UID, SPUtil.getInstance().getLoginUid(LauncherActivity.this));
                            startActivity(intent);
                        LauncherActivity.this.finish();
                    }
                });
            }
        }).start();
    }

    // 以下数据仅用于调试界面
    private void initUserInfo() {
        UserInfo info = new UserInfo(100001,"啊","@","password",
                "1999-4-25", 1,"山东|青岛|黄岛区","请多指教喵",
                new byte[]{-1},true);
        AppData.getInstance().setMyInfo(info);
    }

    private void initFriendInfo() {
        ArrayList<FriendInfo> friendMap = new ArrayList<>();
        friendMap.add(new FriendInfo(100002,"章口就莱","@",
                "1999-1-1",0,"|||",
                "改编不是乱编，戏说不是胡说！",new byte[]{-1},false,""));
        friendMap.add(new FriendInfo(100003,"CXK","@",
                "1992-1-1",0,"|||",
                "鸡你太美！",new byte[]{-1},false,""));
        friendMap.add(new FriendInfo(100004,"沙雕","@",
                "1975-1-1",0,"|||",
                "啊我死了！",new byte[]{-1},false,""));
        friendMap.add(new FriendInfo(100005,"对方正在输入...","@",
                "1999-1-1",0,"|||",
                "对方很懒，没有输入任何信息！",new byte[]{-1},false,""));
        friendMap.add(new FriendInfo(100006,"这是测试","@",
                "2008-1-1",0,"|||",
                "test！",new byte[]{-1},false,""));
        friendMap.add(new FriendInfo(100007,"什么鬼玩意","@",
                "2006-1-1",0,"|||",
                "阿西吧！",new byte[]{-1},false,""));
        friendMap.add(new FriendInfo(100008,"测试用户","@",
                "1975-1-1",0,"|||",
                "这是测试！",new byte[]{-1},false,""));
        friendMap.add(new FriendInfo(100004,"用户信息","@",
                "1970-1-1",0,"|||",
                "用户信息！",new byte[]{-1},false,""));
        AppData.getInstance().setFriendList(friendMap);
    }

    private void initChatRoomInfo() {
        UserInfo myself = AppData.getInstance().getMyInfo();
        LinkedList<ChatRoom> chatRoomMap = new LinkedList<>();
        ArrayList<FriendInfo> friendList = AppData.getInstance().getFriendList();

        ArrayList<TransMsg> tm1 = new ArrayList<>();
        tm1.add(new TransMsg(myself, friendList.get(0), TransMsgType.MESSAGE,"你好！"));
        tm1.add(new TransMsg(myself, friendList.get(0), TransMsgType.MESSAGE,"？？？"));
        tm1.add(new TransMsg(myself, friendList.get(0), TransMsgType.MESSAGE,"？？？？？？"));
        chatRoomMap.add(new ChatRoom(friendList.get(0), tm1));

        ArrayList<TransMsg> tm2 = new ArrayList<>();
        tm2.add(new TransMsg(myself, friendList.get(1), TransMsgType.MESSAGE,"阿！"));
        tm2.add(new TransMsg(myself, friendList.get(1), TransMsgType.MESSAGE,"。。。。。。。"));
        tm2.add(new TransMsg(friendList.get(1), myself, TransMsgType.MESSAGE,"！"));
        chatRoomMap.add(new ChatRoom(friendList.get(1), tm2));

        AppData.getInstance().setChatRoomList(chatRoomMap);
    }

}
