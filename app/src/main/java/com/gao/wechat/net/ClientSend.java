package com.gao.wechat.net;

import android.util.Log;

import com.gao.wechat.data.TransMsg;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSend {
    private Socket socket = null;
    private ObjectOutputStream oos = null;

    public ClientSend(Socket socket) {
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ClientSend:IOException","I/O异常");
        }
    }

    public void sendMessage(TransMsg message) throws IOException {
        oos.writeObject(message);
        oos.flush();
    }
}
