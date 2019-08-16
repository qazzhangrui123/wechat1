package com.gao.wechat.data;

import com.alibaba.fastjson.JSON;

public class Result {
    public static final String LoginSuccess = "Login_Successful";
    public static final String LoginWrongPwd = "Login_Wrong_Password";
    public static final String LoginNotExist = "Login_Not_Exist";
    public static final String RegisterSuccess = "Register_Successful";
    public static final String RegisterFailed = "Register_Failed";
    public static final String CheckExist = "Check_Exist";
    public static final String CheckNotExist = "Check_Not_Exist";
    public static final String FriendRequestCheck = "Friend_Request_Check";
    public static final String FriendRequestAccept = "Friend_Request_Accept";
    public static final String FriendRequestRefused = "Friend_Request_Refused";
    public static final String UpdateSuccess = "Update_Successful";
    public static final String UpdateFailed = "Update_Failed";

    private String type;
    private String msg;

    public Result() {
        this(Result.LoginNotExist);
    }

    public Result(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public Result(String type) {
        this(type, "");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean typeEquals(String resultType) {
        return this.type.equals(resultType);
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

    public static Result toResult(String jsonText) {
        return JSON.parseObject(jsonText, Result.class);
    }
}