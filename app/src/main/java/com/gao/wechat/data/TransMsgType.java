package com.gao.wechat.data;

import java.io.Serializable;

public enum TransMsgType implements Serializable {
    // 以下在客户端和服务端传输使用
    MESSAGE,        // 已发送的信息
    REGISTER,		// 用户注册的信息
    CHECK_EXIST,	// 检查账户是否存在
    LOGIN,			// 用户登录信息
    FIND_FRIEND,	// 查找朋友
    FRIEND_REQUEST,	// 好友申请
    FRIEND_LIST,	// 好友列表
    CLOSE,          // 关闭连接
    UPDATE_USER,	// 更新用户信息
    GET_USER,		// 获取用户信息
    // 以下仅在客户端使用
    MESSAGE_DRAFT,  // 未发送的草稿
}
