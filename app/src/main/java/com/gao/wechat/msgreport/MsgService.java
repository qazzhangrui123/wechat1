package com.gao.wechat.msgreport;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;

import androidx.core.app.NotificationCompat;

import com.gao.wechat.MainActivity;
import com.gao.wechat.R;
import com.hss01248.notifyutil.NotifyUtil;

import java.util.ArrayList;

public class MsgService extends Service {
    private static final String TAG = "MyService";
    private ToastUtils toastUtils;
    public MsgService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(TAG, "onBind: ");
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        sendMessage();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }


    /**
     * 模仿推送，发消息
     */
    private void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                toastUtils = new ToastUtils();
                toastUtils.showFullScreen(MsgService.this);
                //sendPop();

                Intent intent = new Intent();
                intent.setAction("com.gao.wechat.intent.action.msgreport.LockScreenMsgReceiver");
                sendBroadcast(intent); //发送广播
            }
        }).start();
    }
    /*private  void sendPop(){
        popUtil = new PopUtil(MsgService.this, "我是POP测试");
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                popUtil.showAtLocation(MsgService.findViewById(R.id.pop),
                        Gravity.TOP, 0, 0);
            }
            @Override
            public void onFinish() {
                popUtil.dismiss();
            }
    }*/



}
