package com.gao.wechat.data;

import java.io.Serializable;

public class TransMsg implements Serializable {

    private static final long serialVersionUID = 1L;
    // 发送方ID
    private long senderID;
    // 接收方ID
    private long receiverID;
    // 传输的信息
    private String object;
    // 传输的信息的类型
    private TransMsgType objectType;
    // 发送时间
    private long sendTime;

    public TransMsg() {
        this(TransMsgType.MESSAGE,"");
    }

    public TransMsg(TransMsgType objectType, String object) {
        this(0L,0L,objectType,object,System.currentTimeMillis());
    }

    public TransMsg(UserInfo myself, UserInfo friend, TransMsgType objectType, String object) {
        this(myself,friend,objectType,object,System.currentTimeMillis());
    }

    public TransMsg(UserInfo myself, UserInfo friend, TransMsgType objectType, String object, long sendTime) {
        this(myself.getUserID(),friend.getUserID(),objectType,object,sendTime);
    }

    public TransMsg(long senderID, long receiverID, TransMsgType objectType, String object, long sendTime) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.object = object;
        this.objectType = objectType;
        this.sendTime = sendTime;
    }

    public long getSenderID() {
        return senderID;
    }

    public void setSenderID(long senderID) {
        this.senderID = senderID;
    }

    public long getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(long receiverID) {
        this.receiverID = receiverID;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public TransMsgType getObjectType() {
        return objectType;
    }

    public void setObjectType(TransMsgType objectType) {
        this.objectType = objectType;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
