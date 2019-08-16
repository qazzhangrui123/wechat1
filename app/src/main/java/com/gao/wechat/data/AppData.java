package com.gao.wechat.data;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.gao.wechat.database.DatabaseUtil;

import java.util.ArrayList;
import java.util.LinkedList;

public class AppData {

    /**
     * 该类的唯一实例
     */
    private static AppData appData;

    private Context mContext;

    /**
     * 私有化构造方法
     */
    private AppData() {

    }

    /**
     * 单例化对象 AppData
     * @return 应用数据对象 AppData
     */
    public static AppData getInstance() {
        if (appData == null) {
            synchronized (AppData.class) {
                if (appData == null) {
                    appData = new AppData();
                }
            }
        }
        return appData;
    }

    /**
     * 当前用户的个人信息
     */
    private UserInfo myInfo;

    /**
     * 当前用户的好友列表
     */
    private ArrayList<FriendInfo> friendList;

    /**
     * 当前用户的聊天室信息
     */
    private LinkedList<ChatRoom> chatRoomList;

    /**
     * 获取个人信息
     * @return 当前登录用户的信息
     */
    public UserInfo getMyInfo() {
        if (myInfo == null) {
            myInfo = new UserInfo();
        }
        return myInfo;
    }

    /**
     * 写入个人信息
     * @param myInfo 个人信息
     */
    public void setMyInfo(UserInfo myInfo) {
        if (this.myInfo == null) {
            this.myInfo = new UserInfo();
        }
        this.myInfo.setAvatar(myInfo.getAvatar());
        this.myInfo.setBirthday(myInfo.getBirthday());
        this.myInfo.setEmail(myInfo.getEmail());
        this.myInfo.setGender(myInfo.getGender());
        this.myInfo.setNickName(myInfo.getNickName());
        this.myInfo.setOnline(myInfo.isOnline());
        this.myInfo.setPassword(myInfo.getPassword());
        this.myInfo.setRegion(myInfo.getRegion());
        this.myInfo.setSignature(myInfo.getSignature());
        this.myInfo.setUserID(myInfo.getUserID());
    }

    /**
     * 根据用户ID从本地获取好友的信息
     * @param friendID 好友ID
     * @return 好友信息
     */
    public FriendInfo getFriendById(Long friendID) {
        for (FriendInfo info : friendList) {
            if (info.getUserID() == friendID) {
                return info;
            }
        }
        return null;
    }

    public boolean containsFriend(Long friendID) {
        return getFriendById(friendID) != null;
    }

    /**
     * 从本地获取好友列表
     * @return 好友列表
     */
    public ArrayList<FriendInfo> getFriendList() {
        return friendList;
    }

    /**
     * 写入多个好友信息
     * @param friendList 多个好友信息
     */
    public void setFriendList(ArrayList<FriendInfo> friendList) {
        if (this.friendList == null) {
            this.friendList = friendList;
        } else {
            this.friendList.clear();
            this.friendList.addAll(friendList);
        }
    }

    /**
     * 写入一个好友信息
     * @param friendInfo 一个好友信息
     */
    public void addFriend(FriendInfo friendInfo) {
        if (this.friendList == null) {
            this.friendList = new ArrayList<>();
        }
        this.friendList.add(friendInfo);
    }

    /**
     * 根据用户ID获取聊天数据
     * @param friendID 好友ID
     * @return 聊天数据
     */
    public ChatRoom getChatRoomById(long friendID) {
        for (ChatRoom chatRoom : chatRoomList) {
            if (chatRoom.getFriend().getUserID() == friendID) {
                return chatRoom;
            }
        }
        return null;
    }

    /**
     * 从本地获取聊天列表
     * @return 聊天列表
     */
    public LinkedList<ChatRoom> getChatRoomList() {
        if (chatRoomList == null) {
            chatRoomList = new LinkedList<>();
        }
        return chatRoomList;
    }

    /**
     * 设置聊天列表内容
     * @param chatRoomList 聊天室表
     */
    public void setChatRoomList(@NonNull LinkedList<ChatRoom> chatRoomList) {
        this.chatRoomList = chatRoomList;
    }

    private LinkedList<FriendInfo> friendRequestList;

    public LinkedList<FriendInfo> getFriendRequestList() {
        if (friendRequestList == null) {
            friendRequestList = new LinkedList<>();
        }
        return friendRequestList;
    }

    public void setFriendRequestList(@NonNull LinkedList<FriendInfo> friendRequestList) {
        if (this.friendRequestList == null) {
            this.friendRequestList = new LinkedList<>();
        }
        this.friendRequestList.clear();
        this.friendRequestList.addAll(friendRequestList);
    }

    public void addFriendRequest(FriendInfo friendInfo) {
        this.friendRequestList.addFirst(friendInfo);
    }

    ////////// 更新用户信息 Start //////////
    private Result updateResult;

    public void initUpdate() {
        isReceived = false;
    }

    public Result getUpdateResult() {
        return updateResult;
    }

    public void updateUserResult(TransMsg message) {
        updateResult = Result.toResult(message.getObject());
        isReceived = true;
    }
    ////////// 更新用户信息 End //////////

    ////////// 查找好友功能 Start //////////
    private ArrayList<FriendInfo> findList;

    public ArrayList<FriendInfo> getFindFriendResult() {
        if (findList == null) {
            findList = new ArrayList<>();
        }
        return findList;
    }

    public void setFindList(ArrayList<FriendInfo> findList) {
        if (this.findList == null) {
            this.findList = new ArrayList<>();
        }
        this.findList.clear();
        this.findList.addAll(findList);
    }

    public void initFindFriend() {
        isReceived = false;
        if (findList != null) {
            findList.clear();
        }
    }

    public void findFriendResult(TransMsg message) {
        ArrayList<FriendInfo> list = (ArrayList<FriendInfo>) JSON.parseArray(message.getObject(), FriendInfo.class);
        setFindList(list);
        isReceived = true;
    }
    ////////// 查找好友功能 End //////////

    // 存储最近接收到的信息
    private TransMsg receivedMessage;
    // 标记最近接收到的信息是否已经接收到
    private boolean isReceived;

    // 登录的结果
    private Result loginResult;

    /**
     * 等待接收消息，死循环
     */
    public void waitForServer() {
        while (!isReceived) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化数据，准备接收数据
     */
    public void initListen(Context context) {
        isReceived = false;
        mContext = context;
        friendList = null;
        myInfo = null;
        receivedMessage = null;
    }

    /**
     * 获取登录结果
     * @return 登录结果
     */
    public Result getLoginResult() {
        return loginResult;
    }

    public TransMsg getReceivedMessage() {
        return receivedMessage;
    }

    public void loginResult(TransMsg message) {
        receivedMessage = message;
        loginResult = Result.toResult(receivedMessage.getObject());
        if (loginResult.typeEquals(Result.LoginSuccess)) {
            myInfo = UserInfo.toUserInfo(loginResult.getMsg());
            // 将个人信息写入到数据库中
            DatabaseUtil.get(mContext).saveMyInfo(myInfo);
        } else {
            myInfo = null;
        }
        isReceived = true;
    }

    public void friendRequestResult(TransMsg message) {
        Result result = Result.toResult(message.getObject());
        if (result.typeEquals(Result.FriendRequestCheck)) {
            Log.d("AppData:FriendRequest","用户申请添加好友");
            FriendInfo friendRequest = FriendInfo.toFriendInfo(result.getMsg());
            addFriendRequest(friendRequest);
            if (friendRequestHandler != null) {
                Message msg = new Message();
                msg.what = 1;
                friendRequestHandler.sendMessage(msg);
            }
        } else if (result.typeEquals(Result.FriendRequestAccept)) {
            Log.d("AppData:FriendRequest","用户同意添加好友");
            FriendInfo friendInfo = FriendInfo.toFriendInfo(result.getMsg());
            addFriend(friendInfo);
            if (friendListHandler != null) {
                Message msg = new Message();
                msg.what = 1;
                friendListHandler.sendMessage(msg);
            }
        } else if (result.typeEquals(Result.FriendRequestRefused)) {
            Log.d("AppData:FriendRequest", "用户拒绝添加好友");
        }
        isReceived = true;
    }

    public void messageResult(TransMsg message) {
        long senderID = message.getSenderID();
        // 向数据库中写入该消息
        DatabaseUtil.get(mContext).saveMessage(message);
        ChatRoom room = getChatRoomById(senderID);
        if (room != null) {
            room.setUnreadCount(room.getUnreadCount() + 1);
            room.addMessage(message);
        } else {
            room = new ChatRoom(getFriendById(senderID), message);
            room.setUnreadCount(1);
            chatRoomList.add(room);
        }
        if (messageHandler != null) {
            Message msg = new Message();
            msg.what = 1;
            messageHandler.sendMessage(msg);
        }
    }

    private Handler chatHandler;
    private Handler messageHandler;
    private Handler friendListHandler;
    private Handler friendRequestHandler;

    public Handler getChatHandler() {
        return chatHandler;
    }

    public Handler getMessageHandler() {
        return messageHandler;
    }

    public Handler getFriendListHandler() {
        return friendListHandler;
    }

    public Handler getFriendRequestHandler() {
        return friendRequestHandler;
    }

    public void setChatHandler(Handler userHandler) {
        this.chatHandler = userHandler;
    }

    public void setMessageHandler(Handler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void setFriendListHandler(Handler friendListHandler) {
        this.friendListHandler = friendListHandler;
    }

    public void setFriendRequestHandler(Handler friendRequestHandler) {
        this.friendRequestHandler = friendRequestHandler;
    }

}
