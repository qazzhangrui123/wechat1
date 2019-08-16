package com.gao.wechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gao.wechat.R;
import com.gao.wechat.data.FriendInfo;
import com.gao.wechat.util.BitmapUtil;

import java.util.ArrayList;

public class FindFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<FriendInfo> friendList;

    public FindFriendAdapter(ArrayList<FriendInfo> friendList) {
        this.friendList = friendList;
    }

    public FindFriendAdapter() {

    }

    public void setFriendList(ArrayList<FriendInfo> friendList) {
        if (this.friendList == null) {
            this.friendList = new ArrayList<>();
        }
        this.friendList.clear();
        this.friendList.addAll(friendList);
        notifyDataSetChanged();
    }

    public void clearList() {
        if (this.friendList != null) {
            this.friendList.clear();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_contact_recycler_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        FriendInfo friend = friendList.get(position);
        viewHolder.nickName.setText(friend.getNickName() + "(" + friend.getUserID() + ")");
        viewHolder.userImage.setImageBitmap(BitmapUtil.getBitmap(friend.getAvatar()));
    }

    @Override
    public int getItemCount() {
        return friendList == null ? 0 : friendList.size();
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ArrayList<FriendInfo> friendList, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView nickName;

        public ViewHolder(View view) {
            super(view);
            userImage = view.findViewById(R.id.contact_recycler_image);
            nickName = view.findViewById(R.id.contact_recycler_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(friendList,getAdapterPosition());
                    }
                }
            });
        }
    }

}
