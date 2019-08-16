package com.gao.wechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gao.wechat.R;
import com.gao.wechat.data.TransMsg;
import com.gao.wechat.data.TransMsgType;
import com.gao.wechat.data.UserInfo;
import com.gao.wechat.util.TimeUtil;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TransMsg> messageList;
    private UserInfo myself;
    private static final int VIEW_LEFT = 1;
    private static final int VIEW_RIGHT = 2;

    public ChatAdapter(ArrayList<TransMsg> messageList, UserInfo myself) {
        this.messageList = messageList;
        this.myself = myself;
    }

    public void addMessage(TransMsg message) {
        messageList.add(message);
        notifyDataSetChanged();
    }

    public void removeMessage(TransMsg message) {
        messageList.remove(message);
        notifyDataSetChanged();
    }

    public void removeMessage(int position) {
        messageList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        long senderID = messageList.get(position).getSenderID();
        if (senderID == myself.getUserID()) {
            return VIEW_RIGHT;
        } else {
            return VIEW_LEFT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == VIEW_RIGHT) {
            view = inflater.inflate(R.layout.activity_chat_recycler_right, parent, false);
        } else {
            view = inflater.inflate(R.layout.activity_chat_recycler_left, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        TransMsg msg = messageList.get(i);
        long preTime = messageList.get(i-(i>0?1:0)).getSendTime();
        TextView tvTime = ((ViewHolder)holder).chatViewTime;
        tvTime.setText(TimeUtil.getDetailTime(msg.getSendTime()));
        ((ViewHolder)holder).chatViewUserMessage.setText(msg.getObject().toString());
        // ((ViewHolder)holder).chatViewUserImage.setImageResource(0);
        if (msg.getSendTime() - preTime < (1000*240) && i>0) {
            tvTime.setVisibility(TextView.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView chatViewTime;
        private TextView chatViewUserMessage;
        private ImageView chatViewUserImage;

        public ViewHolder(View view) {
            super(view);
            chatViewTime = view.findViewById(R.id.chat_activity_recycler_time);
            chatViewUserImage = view.findViewById(R.id.chat_activity_recycler_image);
            chatViewUserMessage = view.findViewById(R.id.chat_activity_recycler_text);
        }
    }

}
