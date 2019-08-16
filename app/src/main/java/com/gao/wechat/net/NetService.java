package com.gao.wechat.net;

import android.content.Context;
import android.util.Log;

import com.gao.wechat.data.TransMsg;

import java.io.IOException;
import java.net.Socket;

public class NetService {
    private static NetService instance = null;

    private ClientListen clientListen = null;
    private ClientSend clientSend = null;

    private Connect connect = null;
    private Socket clientSocket = null;
    private boolean isConnected = false;
    private Context context = null;

    private NetService() {

    }

    public static NetService getInstance() {
        if (instance == null) {
            instance = new NetService();
        }
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setConnection() {
        connect = new Connect();
        connect.beginConnect();
        if (connect.isConnected()) {
            isConnected = true;
            clientSocket = connect.getSocket();
            beginListen(clientSocket);
        } else {
            isConnected = false;
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void beginListen(Socket socket) {
        clientListen = new ClientListen(context, socket);
        clientSend = new ClientSend(socket);
        clientListen.start();
    }

    public void send(TransMsg message) throws IOException, NullPointerException {
        clientSend.sendMessage(message);
    }

    public void closeConnection() {
        if (clientListen != null) {
            clientListen.close();
        }
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("NetService:IOException", "关闭连接出错！");
        }
        isConnected = false;
        clientSocket = null;
    }

}
