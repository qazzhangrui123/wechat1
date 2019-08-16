package com.gao.wechat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.gao.wechat.BaseActivity;
import com.gao.wechat.MainActivity;
import com.gao.wechat.R;
import com.gao.wechat.action.UserAction;
import com.gao.wechat.adapter.FindFriendAdapter;
import com.gao.wechat.data.AppData;
import com.gao.wechat.data.FriendInfo;
import com.gao.wechat.data.UserInfo;

import java.io.IOException;
import java.util.ArrayList;

public class FindFriendActivity extends BaseActivity {
    public static final String ACTION = "com.gao.wechat.intent.action.FindFriendActivity";

    private SearchView searchView;
    private LinearLayout btnQRScan;
    private RecyclerView recyclerView;
    private FindFriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        initToolbar();
        initView();
        initEvents();
    }

    private void initToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.add_friend_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }
    }

    public void initView() {
        searchView = findViewById(R.id.add_friend_edit);
        btnQRScan = findViewById(R.id.add_friend_scan);
        recyclerView = findViewById(R.id.add_friend_recycler);
    }

    public void initEvents() {
        // 设置 Adapter
        adapter = new FindFriendAdapter();
        adapter.setOnItemClickListener(new FindFriendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ArrayList<FriendInfo> friendList, int position) {
                Intent intent = new Intent(FriendInfoActivity.ACTION);
                intent.putExtra(FriendInfoActivity.FINFO, friendList.get(position));
                startActivity(intent);
            }
        });
        // 设置 RecyclerView
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        // 设置 SearchView
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    searchView.clearFocus();
                    final UserInfo friend = new UserInfo();
                    if (TextUtils.isDigitsOnly(query)) {
                        friend.setUserID(Long.valueOf(query));
                    } else {
                        friend.setNickName(query);
                    }
                    new AsyncTask<Void, Void, Integer>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }
                        @Override
                        protected Integer doInBackground(Void... voids) {
                            try {
                                AppData.getInstance().initFindFriend();
                                UserAction.findFriend(friend);
                                AppData.getInstance().waitForServer();
                                return 1;
                            } catch (NullPointerException e) {
                                return -1;
                            } catch (IOException e) {
                                e.printStackTrace();

                            }
                            return -2;
                        }
                        @Override
                        protected void onPostExecute(Integer result) {
                            super.onPostExecute(result);
                            switch (result) {
                                case -1:
                                    Log.d("WeChat:SearchFriend", "无法连接到服务器！");
                                    toast("无法连接到服务器");
                                    break;
                                case -2:
                                    Log.d("WeChat:SearchFriend","I/O 错误！");
                                    toast("系统出现错误，请稍候重试");
                                    break;
                                case 1:
                                    ArrayList<FriendInfo> list =
                                            AppData.getInstance().getFindFriendResult();
                                    adapter.setFriendList(list);
                                    if (list.size() == 0) {
                                        toast("没有找到相关的用户！");
                                    }
                                    break;
                                default:
                                    toast("参数错误！");
                                    break;
                            }
                        }
                    }.execute();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    btnQRScan.setVisibility(View.VISIBLE);
                    adapter.clearList();
                } else {
                    btnQRScan.setVisibility(View.GONE);
                }
                return true;
            }
        });
        btnQRScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.showQRCodeScanView(FindFriendActivity.this);
            }
        });
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

}
