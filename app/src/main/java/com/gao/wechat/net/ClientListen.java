package com.gao.wechat.net;

import android.content.Context;
import android.util.Log;

import com.gao.wechat.activity.register.RegisterActivity;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.TransMsg;
import com.gao.wechat.data.TransMsgType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ClientListen extends Thread {
    private Socket socket = null;
    private Context context = null;
    private ObjectInputStream ois;

    private boolean isStart = true;

    public ClientListen(Context context, Socket socket) {
        this.context = context;
        this.socket = socket;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // 客户端不断地接收服务器发来的消息
        try {
            isStart = true;
            while (isStart) {
                Log.d("WeChat:Network:ClientListen", "开始接收信息");
                TransMsg received = (TransMsg) ois.readObject();
                TransMsgType type = received.getObjectType();
                Log.d("WeChat:Network:ClientListen", "成功接收一条消息:" + type.name() + ":" + received.getObject());
                // 接收到消息后，按照消息类型执行相应的操作
                switch (type) {
                    case MESSAGE:
                        AppData.getInstance().messageResult(received);
                        break;
                    case LOGIN:
                        AppData.getInstance().loginResult(received);
                        break;
                    case REGISTER:
                        RegisterActivity.onRegisterArrived(received);
                        break;
                    case FIND_FRIEND:
                        AppData.getInstance().findFriendResult(received);
                        break;
                    case FRIEND_REQUEST:
                        AppData.getInstance().friendRequestResult(received);
                        break;
                    case FRIEND_LIST:
                        break;
                    case GET_USER:
                        break;
                    case UPDATE_USER:
                        AppData.getInstance().updateUserResult(received);
                    default:
                        break;
                }
            }
        } catch (SocketException e) {
            Log.d("WeChat:Network:ClientListen", "Socket 已关闭");
        } catch (SocketTimeoutException e) {
            Log.d("WeChat:Network:ClientListen", "Socket 连接超时");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("WeChat:Network:ClientListen", "Socket I/O 故障");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("WeChat:Network:ClientListen","类 TransMsg 未找到");
        }
    }

    public void close() {
        isStart = false;
    }

}
