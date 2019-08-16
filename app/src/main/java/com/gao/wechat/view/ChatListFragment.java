package com.gao.wechat.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gao.wechat.activity.ChatActivity;
import com.gao.wechat.R;
import com.gao.wechat.adapter.ChatListAdapter;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.ChatRoom;

import java.util.LinkedList;

public class ChatListFragment extends Fragment {

    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private Handler handler;

    public ChatListFragment() {
        // 保留默认构造方法
    }

    /**
     * 获取一个ChatListFragment实例
     * @return ChatListFragment
     */
    public static ChatListFragment newInstance() {
        return new ChatListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        mContext = getActivity();
        initView(view);
        initData();
        initRefreshAction();
        return view;
    }

    private void initData() {
        // 获取数据
        LinkedList<ChatRoom> chatRoomList = AppData.getInstance().getChatRoomList();
        // 设置 Adapter
        adapter = new ChatListAdapter(chatRoomList);
        adapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, LinkedList<ChatRoom> chatRoomList) {
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(ChatActivity.ACTION);
                intent.putExtra(ChatActivity.FID,
                        chatRoomList.get(position).getFriend().getUserID());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        // 设置 LayoutManager
        LinearLayoutManager llManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(llManager);
        // 设置 ItemDecoration
        recyclerView.addItemDecoration(
                new DividerItemDecoration(mContext.getApplicationContext(),
                        DividerItemDecoration.VERTICAL));
        // 设置 Handler
        handler = new ChatListHandler(adapter);
        AppData.getInstance().setMessageHandler(handler);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.chat_list_recycler);
        swipeRefreshLayout = view.findViewById(R.id.chat_list_layout);
    }

    private void initRefreshAction() {
        // 设置 下拉刷新操作
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        // TODO: 添加刷新操作
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });
    }

    static class ChatListHandler extends Handler {
        private ChatListAdapter adapter;
        public ChatListHandler(ChatListAdapter adapter) {
            this.adapter = adapter;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();
                    // 更新 RecyclerView
                    break;
                default:
                    break;
            }
        }
    }

}
