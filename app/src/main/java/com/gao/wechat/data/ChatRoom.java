package com.gao.wechat.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 聊天室的实体类，用于传递信息
 * 成员有：friend:UserInfo 该房间的好友信息
 */
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    private FriendInfo friend;
    private ArrayList<TransMsg> messageList;
    private int unreadCount = 0;

    public ChatRoom() {
        this(new FriendInfo(), new ArrayList<TransMsg>());
    }

    public ChatRoom(FriendInfo friend, ArrayList<TransMsg> messageList) {
        this.friend = friend;
        this.messageList = messageList;
    }

    public ChatRoom(FriendInfo friend, TransMsg message) {
        this.friend = friend;
        this.messageList = new ArrayList<>();
        messageList.add(message);
    }

    public FriendInfo getFriend() {
        return friend;
    }

    public void setFriend(FriendInfo friend) {
        this.friend = friend;
    }

    public ArrayList<TransMsg> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<TransMsg> messageList) {
        this.messageList = messageList;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getRecentText() {
        if (messageList.size() > 0) {
            return messageList.get(messageList.size() - 1).getObject().toString();
        } else {
            return "";
        }
    }

    public long getRecentTime() {
        if (messageList.size() > 0) {
            return messageList.get(messageList.size() - 1).getSendTime();
        } else {
            return 0L;
        }
    }

    public TransMsg getRecentMessage() {
        if (messageList.size() > 0) {
            return messageList.get(messageList.size() - 1);
        } else {
            return null;
        }
    }

    public void removeRecentMessage() {
        if (messageList.size() > 0) {
            messageList.remove(messageList.size() - 1);
        }
    }

    public void addMessage(TransMsg msg) {
        this.messageList.add(msg);
    }

    public int size() {
        return messageList.size();
    }

}
