package com.gao.wechat.data;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    // 账户ID
    private long userID;
    // 昵称
    private String nickName;
    // 密码
    private String password;
    // 生日
    private String birthday;
    // 性别
    private int gender;
    // 地区
    private String region;
    // 签名
    private String signature;
    // 头像
    private byte[] avatar;
    // 在线状态
    private boolean online;
    // 邮箱
    private String email;

    public UserInfo() {
        this(0);
    }

    public UserInfo(long userID) {
        this(userID, "", "");
    }

    public UserInfo(long userID, String nickName, String password) {
        this(userID,nickName,"@",password,"1970-1-1",1,"","",new byte[] {},false);
    }


    public UserInfo(long userID, String nickName, String password, int gender, String email) {
        this(userID,nickName,email,password,"1970-1-1",gender,"","",new byte[]{},false);
    }

    public UserInfo(long userID, String nickName, String email, String password, String birthday, int gender,
                    String region, String signature, byte[] avatar, boolean online) {
        this.userID = userID;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.region = region;
        this.signature = signature;
        this.avatar = avatar;
        this.online = online;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static UserInfo toUserInfo(String jsonText) {
        return JSON.parseObject(jsonText, UserInfo.class);
    }

}
