package com.gao.wechat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.gao.wechat.BaseActivity;
import com.gao.wechat.R;
import com.gao.wechat.action.UserAction;
import com.gao.wechat.adapter.ChatAdapter;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.ChatRoom;
import com.gao.wechat.data.FriendInfo;
import com.gao.wechat.data.TransMsg;
import com.gao.wechat.data.TransMsgType;
import com.gao.wechat.data.UserInfo;
import com.gao.wechat.database.DatabaseUtil;

import java.util.ArrayList;

public class ChatActivity extends BaseActivity {

    public static final String ACTION = "com.gao.wechat.intent.action.ChatActivity";

    public static final String FID = "FriendID";

    private RecyclerView recyclerView;
    private EditText editText;
    private Button btnSend;

    private LinearLayoutManager llManager;
    private ChatAdapter chatAdapter;

    private Handler handler;

    // 好友信息
    private ChatRoom chatRoom;
    private long friendID;
    // 个人信息
    private UserInfo myself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initIntentData();
        initView();
        initEvents();
        initToolbar(chatRoom.getFriend().getNickName());
    }

    private void initIntentData() {
        Intent intent = getIntent();
        friendID = intent.getLongExtra(FID, 0);
    }

    private void initView() {
        editText = findViewById(R.id.chat_activity_edit);
        recyclerView = findViewById(R.id.chat_activity_recycler);
        btnSend = findViewById(R.id.chat_activity_send);
    }

    private void initToolbar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setElevation(0);
            actionBar.setTitle(title);
        }
    }

    private void initEvents() {
        // 获取我的信息
        myself = AppData.getInstance().getMyInfo();
        // 获取 ChatRoom 数据
        chatRoom = AppData.getInstance().getChatRoomById(friendID);
        if (chatRoom == null) {
            ArrayList<TransMsg> msgList =
                    DatabaseUtil.get(ChatActivity.this)
                            .getMessageByFriend(myself.getUserID(), friendID);
            FriendInfo friendInfo =
                    DatabaseUtil.get(ChatActivity.this)
                            .getFriendInfo(myself.getUserID(), friendID);
            chatRoom = new ChatRoom(friendInfo, msgList);
            AppData.getInstance().getChatRoomList().add(chatRoom);
        }

        // 设置 Adapter
        chatAdapter = new ChatAdapter(chatRoom.getMessageList(), myself);
        recyclerView.setAdapter(chatAdapter);
        // 设置 LinearLayoutManager
        llManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(llManager);
        // 设置 Handler
        handler = new ChatHandler(chatAdapter);
        AppData.getInstance().setChatHandler(handler);

        // 设置按钮的事件
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Editable,Void,TransMsg>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }
                    @Override
                    protected TransMsg doInBackground(Editable... voids) {
                        TransMsg msg = new TransMsg();
                        try {
                            Editable message = voids[0];
                            if (message.length() > 0) {
                                msg.setSenderID(myself.getUserID());
                                msg.setReceiverID(chatRoom.getFriend().getUserID());
                                msg.setObjectType(TransMsgType.MESSAGE);
                                msg.setObject(message.toString());
                                UserAction.sendMessage(msg);
                                DatabaseUtil.get(ChatActivity.this).saveMessage(msg);
                            }
                            return msg;
                        } catch (NullPointerException e) {
                            return null;
                        }
                    }
                    @Override
                    protected void onPostExecute(TransMsg message) {
                        super.onPostExecute(message);
                        if (message != null) {
                            if (message.getSenderID() != 0) {
                                chatAdapter.addMessage(message);
                                chatAdapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(chatRoom.size() - 1);
                            }
                        } else {
                            toast("无法连接到服务器");
                            Log.d("WeChat:ChatActivity", "无法连接到服务器");
                        }
                    }
                }.execute(editText.getText());
            }
        });

    }

    @Override
    public void onBackPressed() {
        // 按下返回按钮时执行的操作
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // 按下ActionBar上的返回按钮，执行一些操作
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void closeKeyboard(EditText mEditText, Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    static class ChatHandler extends Handler {
        private ChatAdapter adapter;
        public ChatHandler(ChatAdapter adapter) {
            this.adapter = adapter;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

}
