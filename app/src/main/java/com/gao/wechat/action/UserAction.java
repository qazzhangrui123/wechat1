package com.gao.wechat.action;

import com.gao.wechat.data.TransMsg;
import com.gao.wechat.data.TransMsgType;
import com.gao.wechat.data.UserInfo;
import com.gao.wechat.net.NetService;

import java.io.IOException;

public class UserAction {

    private static NetService netService = NetService.getInstance();

    /**
     * 将注册信息发送给服务器
     * @param userInfo 要注册的用户信息
     * @throws IOException I/O出现异常
     */
    public static void register(UserInfo userInfo) throws IOException, NullPointerException {
        TransMsg msg = new TransMsg(TransMsgType.REGISTER, userInfo.toString());
        netService.send(msg);
    }

    /**
     * 将登录信息发送给服务器
     * @param userInfo 要登录的用户信息
     * @throws IOException I/O出现异常
     */
    public static void login(UserInfo userInfo) throws IOException, NullPointerException {
        TransMsg msg = new TransMsg(TransMsgType.LOGIN, userInfo.toString());
        netService.send(msg);
    }

    /**
     * 向服务器更新个人信息
     * @param userInfo 要更新的信息
     * @throws IOException I/O 异常
     */
    public static void updateMyInfo(UserInfo userInfo) throws IOException, NullPointerException {
        TransMsg msg = new TransMsg(TransMsgType.UPDATE_USER, userInfo.toString());
        netService.send(msg);
    }
    /**
     * 向服务器请求自己的信息
     * @throws IOException I/O出现异常
     */
    public static void myInfo() throws IOException, NullPointerException {
        TransMsg msg = new TransMsg(TransMsgType.GET_USER, "");
        netService.send(msg);
    }

    /**
     * 将好友列表申请发送给服务器
     * @param userInfo 要申请的用户
     * @throws IOException I/O出现异常
     */
    public static void friendlist(UserInfo userInfo) throws IOException, NullPointerException {
        if (userInfo == null) {
            TransMsg msg = new TransMsg(TransMsgType.FRIEND_LIST, "");
            netService.send(msg);
        } else {
            TransMsg msg = new TransMsg(TransMsgType.FRIEND_LIST, userInfo.toString());
            netService.send(msg);
        }
    }

    /**
     * 将查找好友的条件发送给服务器
     * @param userInfo 查询的条件
     * @throws IOException I/O出现异常
     */
    public static void findFriend(UserInfo userInfo) throws IOException, NullPointerException {
        TransMsg msg = new TransMsg(TransMsgType.FIND_FRIEND, userInfo.toString());
        netService.send(msg);
    }

    /**
     * 将好友申请发送给服务器
     * @throws IOException
     */
    public static void friendRequest(long userID) throws IOException, NullPointerException {
        TransMsg msg = new TransMsg();
        netService.send(msg);
    }

    /**
     * 向服务器发送消息
     * @param message 要发送的消息
     */
    public static void sendMessage(TransMsg message) {
        message.setObjectType(TransMsgType.MESSAGE);
        try {
            netService.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
