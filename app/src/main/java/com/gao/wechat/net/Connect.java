package com.gao.wechat.net;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Connect {

    private Socket mClientSocket = null;
//    public static final String SERVER_IP = "192.168.56.1"; // LOCAL, TEST ONLY
    public static final String SERVER_IP = "47.102.158.49";    // REMOTE
    public static final int SERVER_PORT = 9425;
    private boolean mIsConnected = false;

    public Connect() {

    }

    public void beginConnect() {
        try {
            mClientSocket = new Socket();
            mClientSocket.connect(new InetSocketAddress(SERVER_IP,SERVER_PORT),3000);
            Log.d("WeChat:Network:Connect", "已连接服务器");
            if (mClientSocket.isConnected()) {
                mIsConnected = true;
            } else {
                mIsConnected = false;
            }
        } catch (UnknownHostException e) {
            Log.d("WeChat:Network:Connect", "Socket 地址错误！");
        } catch (SocketTimeoutException e) {
            Log.d("WeChat:Network:Connect", "Socket 连接超时");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("WeChat:Network:Connect", "Socket I/O 出现异常！");
        }
    }

    public boolean isConnected() {
        return mIsConnected;
    }

    public Socket getSocket() {
        return mClientSocket;
    }

}
