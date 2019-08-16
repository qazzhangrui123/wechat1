package com.gao.wechat.view;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gao.wechat.activity.FriendInfoActivity;
import com.gao.wechat.R;
import com.gao.wechat.activity.FriendRequestActivity;
import com.gao.wechat.adapter.ContactAdapter;
import com.gao.wechat.adapter.ContactItemDecoration;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.FriendInfo;
import com.gao.wechat.data.UserInfo;
import com.gao.wechat.util.PinyinComparator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class ContactFragment extends Fragment {

    private UserInfo myself;
    private ArrayList<FriendInfo> friendList;

    private RecyclerView recyclerView;
    private LetterView letterView;
    private TextView letterText;

    private Context mContext;
    private LinearLayoutManager llManager;
    private ContactAdapter adapter;
    private ContactItemDecoration itemDecoration;
    private Handler handler;

    private ArrayList<String> tags;

    public ContactFragment() {
        // 保留默认构造方法
    }

    /**
     * 获取一个 ContactFragment 实例
     * @return ContactFragment
     */
    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        mContext = getActivity();
        initData();
        initView(view);
        return view;
    }

    private void initView(View view) {
        // 设置字母索引提示文字
        letterText = view.findViewById(R.id.contact_letter_text);
        letterText.setVisibility(View.GONE);
        // 设置右侧字母索引
        letterView = view.findViewById(R.id.contact_letter_view);
        letterView.setLetterDialog(letterText);
        letterView.setOnLetterTouchListener(new LetterView.OnLetterTouchListener() {
            @Override
            public void onTouch(String tag, int position) {
                int pos = getPosition(tags, tag) + adapter.getHeaderSize();
                if (position == 0) {
                    recyclerView.scrollToPosition(0);
                } else if (pos < tags.size() + adapter.getHeaderSize()) {
                    recyclerView.scrollToPosition(pos);
                }
            }
            @Override
            public void onTouchResult() {
                // 滑动结束时的动作，暂时不使用
            }
        });
        // 设置 RecyclerView
        recyclerView = view.findViewById(R.id.contact_recycler);
        // 设置 LayoutManager
        llManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(llManager);
        // 设置 Adapter
        adapter = new ContactAdapter(getActivity(),friendList);
        adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FriendInfo friend = friendList.get(position);
                Intent intent = new Intent(FriendInfoActivity.ACTION);
                intent.putExtra(FriendInfoActivity.FID, friend.getUserID());
                startActivity(intent);
            }
        });
        adapter.addHeader(R.string.friend_request_title,
                new ContactAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(FriendRequestActivity.ACTION);
                        intent.putExtra(FriendRequestActivity.UID, myself.getUserID());
                        startActivity(intent);
                    }
                });
        adapter.addHeader(R.string.group_title,
                new ContactAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getActivity(), "GROUP", Toast.LENGTH_SHORT).show();
                    }
                });
//        adapter.addFooter(friendList.size() + " 位好友", null);
        recyclerView.setAdapter(adapter);
        // 设置 ItemDecoration
        itemDecoration = new
                ContactItemDecoration(mContext, adapter.getHeaderSize());
        itemDecoration.setTags(tags);
        itemDecoration.setPadding(15);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(mContext.getApplicationContext(),
                        DividerItemDecoration.VERTICAL));
        // 设置 Handler
        handler = new ContactHandler(adapter);
        AppData.getInstance().setFriendListHandler(handler);
    }

    private void initData() {
        // 从 AppData 中获取数据
        myself = AppData.getInstance().getMyInfo();
        friendList = AppData.getInstance().getFriendList();
        // 对好友列表进行排序
        if (friendList != null) {
            PinyinComparator comparator = new PinyinComparator();
            Collections.sort(friendList, comparator);
            tags = getTags(friendList);
        } else {
            friendList = new ArrayList<>();
            tags = new ArrayList<>();
        }
    }

    private int getPosition(ArrayList<String> tags, String tag) {
        if (tags.size() == 0) {
            return 0;
        }
        int position = 0;
        for (; position < tags.size(); position++) {
            if (tags.get(position).trim().equals(tag)) {
                break;
            }
        }
        if (tag.equals(tags.get(tags.size() - 1))) {
            position = tags.size() - 1;
        }
        return position;
    }

    private ArrayList<String> getTags(@NotNull ArrayList<FriendInfo> list) {
        ArrayList<String> tags = new ArrayList<>();
        for (UserInfo info : list) {
            tags.add(PinyinComparator.getPinyinFirst(info.getNickName()));
        }
        return tags;
    }

    static class ContactHandler extends Handler {
        private ContactAdapter adapter;
        public ContactHandler(ContactAdapter adapter) {
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
