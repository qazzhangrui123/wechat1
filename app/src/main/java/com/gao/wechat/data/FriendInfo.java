package com.gao.wechat.data;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class FriendInfo extends UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    // 备注信息
    private String note;

    public FriendInfo() {
        super(); this.note = "";
    }

    public FriendInfo(String nickName) {
        super();
        setNickName(nickName);
    }

    public FriendInfo(long userID, String nickName, String email, String birthday, int gender,
                      String region, String signature, byte[] avatar, boolean online, String note) {
        super(userID,nickName,email,"",birthday,gender,region,signature,avatar,online);
        this.note = note;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public void setPassword(String password) {
        super.setPassword("");
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static FriendInfo toFriendInfo(String jsonText) {
        return JSON.parseObject(jsonText, FriendInfo.class);
    }

}
