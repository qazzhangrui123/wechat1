package com.gao.wechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gao.wechat.R;
import com.gao.wechat.data.FriendInfo;

import java.util.LinkedList;

public class FriendRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LinkedList<FriendInfo> friendRequestList;

    public FriendRequestAdapter(@NonNull LinkedList<FriendInfo> friendRequestList) {
       this.friendRequestList = friendRequestList;
    }

    public void addRequest(FriendInfo friendInfo) {
        friendRequestList.addFirst(friendInfo);
        notifyDataSetChanged();
    }

    public void removeRequest(FriendInfo friendInfo) {
        friendRequestList.remove(friendInfo);
        notifyDataSetChanged();
    }

    public void removeRequest(int position) {
        friendRequestList.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_friend_request_recycler, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FriendInfo friendInfo = friendRequestList.get(position);
        if (friendInfo != null) {
            ((ViewHolder) holder).userName.setText(friendInfo.getNickName());
            //((ViewHolder) holder).userImage.setImageBitmap();
        }
    }

    @Override
    public int getItemCount() {
        return friendRequestList.size();
    }

    // 按下某一个ViewItem的监听器
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(LinkedList<FriendInfo> friendRequestList, int position);
    }

    // 按下按钮的事件监听器
    private OnSelectListener onSelectListener;
    public void setOnSelectListener(OnSelectListener listener) {
        this.onSelectListener = listener;
    }
    public interface OnSelectListener {
        /**
         * 当按下按钮时会触发该监听器
         * @param friendRequestList 申请人列表
         * @param position 该 Item 在列表中的位置
         * @param isAccept 是否接受
         */
        void onSelect(LinkedList<FriendInfo> friendRequestList, int position, boolean isAccept);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView userName;
        Button btnAccept;
        Button btnRefuse;
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(friendRequestList,
                                getAdapterPosition());
                    }
                }
            });
            userImage = view.findViewById(R.id.friend_request_recycler_image);
            userName = view.findViewById(R.id.friend_request_recycler_name);
            btnAccept = view.findViewById(R.id.friend_request_recycler_accept);
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onSelectListener != null) {
                        onSelectListener.onSelect(friendRequestList,
                                getAdapterPosition(), true);
                    }
                }
            });
            btnRefuse = view.findViewById(R.id.friend_request_recycler_refuse);
            btnRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onSelectListener != null) {
                        onSelectListener.onSelect(friendRequestList,
                                getAdapterPosition(), false);
                    }
                }
            });
        }
    }

}
