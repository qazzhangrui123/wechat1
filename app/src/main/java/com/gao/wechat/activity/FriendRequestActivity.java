package com.gao.wechat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.Toast;

import com.gao.wechat.R;
import com.gao.wechat.adapter.FriendRequestAdapter;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.FriendInfo;

import java.util.LinkedList;

public class FriendRequestActivity extends AppCompatActivity {

    public static final String ACTION = "com.gao.wechat.intent.action.activity.FriendRequestActivity";

    public static final String UID = "user_id";

    private RecyclerView recyclerView;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        initIntent();
        initToolbar();
        initView();
    }

    private void initToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.friend_request_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }
    }

    private void initIntent() {

    }

    private void initView() {
        recyclerView = findViewById(R.id.friend_request_recycler);
        final FriendRequestAdapter adapter =
                new FriendRequestAdapter(AppData.getInstance().getFriendRequestList());
        adapter.setOnSelectListener(new FriendRequestAdapter.OnSelectListener() {
            @Override
            public void onSelect(LinkedList<FriendInfo> friendRequestList, int position, boolean isAccept) {
                if (isAccept) {
                    toast("TRUE");
                } else {
                    toast("FALSE");
                }
            }
        });
        adapter.setOnItemClickListener(new FriendRequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LinkedList<FriendInfo> friendRequestList, int position) {
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(ChatActivity.ACTION);
                intent.putExtra(FriendInfoActivity.FINFO,friendRequestList.get(position));
                startActivity(intent);
                FriendRequestActivity.this.finish();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration
                (FriendRequestActivity.this, DividerItemDecoration.VERTICAL));
        handler = new FriendRequestHandler(adapter);
        AppData.getInstance().setFriendRequestHandler(handler);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    static class FriendRequestHandler extends Handler {
        private FriendRequestAdapter adapter;
        public FriendRequestHandler(FriendRequestAdapter adapter) {
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
