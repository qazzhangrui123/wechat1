package com.gao.wechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gao.wechat.R;
import com.gao.wechat.data.ChatRoom;
import com.gao.wechat.data.TransMsg;
import com.gao.wechat.data.TransMsgType;
import com.gao.wechat.data.UserInfo;
import com.gao.wechat.util.BitmapUtil;
import com.gao.wechat.util.TimeUtil;

import java.util.LinkedList;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LinkedList<ChatRoom> chatRoomList;
    private OnItemClickListener onItemClickListener;

    public ChatListAdapter(LinkedList<ChatRoom> chatRoomList) {
        this.chatRoomList = chatRoomList;
    }

    public void addRoom(ChatRoom chatRoom) {
        chatRoomList.add(chatRoom);
        notifyDataSetChanged();
    }

    public void removeRoom(ChatRoom chatRoom) {
        chatRoomList.remove(chatRoom);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_chat_list_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ChatRoom chatRoom = chatRoomList.get(position);
        UserInfo userInfo = chatRoom.getFriend();
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.nickName.setText(userInfo.getNickName());
        holder.userImage.setImageBitmap(BitmapUtil.getBitmap(userInfo.getAvatar()));
        TransMsg recent = chatRoom.getRecentMessage();
        if (recent != null) {
            String msg = recent.getObjectType() == TransMsgType.MESSAGE_DRAFT ? "[草稿]":"";
            holder.recentMessage.setText(msg + recent.getObject());
            holder.recentTime.setText(TimeUtil.getShortTime(chatRoom.getRecentTime()));
        } else {
            holder.recentMessage.setText("");
            holder.recentTime.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    /**
     * 定义{@link OnItemClickListener}接口，用于触发点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(int position, LinkedList<ChatRoom> chatRoomList);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView nickName;
        TextView recentMessage;
        TextView recentTime;

        public ViewHolder(View view) {
            super(view);
            userImage = view.findViewById(R.id.chat_list_recycler_image);
            nickName = view.findViewById(R.id.chat_list_recycler_user);
            recentMessage = view.findViewById(R.id.chat_list_recycler_text);
            recentTime = view.findViewById(R.id.chat_list_recycler_time);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        // 调用 OnItemClickListener 接口
                        onItemClickListener.onItemClick(getAdapterPosition(), chatRoomList);
                    }
                }
            });
        }
    }

}
