package com.gao.wechat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 好友列表
    private ArrayList<FriendInfo> friendList;

    private ArrayList<FriendInfo> headerList;
    private ArrayList<OnItemClickListener> headerListener;
    private ArrayList<FriendInfo> footerList;
    private ArrayList<OnItemClickListener> footerListener;

    private Context mContext;

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_NORMAL = 2;
    private static final int TYPE_FOOTER = 3;

    public ContactAdapter(Context context, ArrayList<FriendInfo> friendList){
        this.friendList = friendList;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerList != null) {
            if (position < headerList.size()) {
                return TYPE_HEADER;
            }
        }
        if (footerList != null) {
            if (position > getHeaderSize() + getFooterSize() - 1 && position < getItemCount()) {
                return TYPE_FOOTER;
            }
        }
        return TYPE_NORMAL;
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
        switch (getItemViewType(position)) {
            case TYPE_FOOTER:
                FriendInfo footer = footerList.get(getFriendPosition(position));
                viewHolder.nickName.setText(footer.getNickName());
                viewHolder.nickName.setGravity(View.TEXT_ALIGNMENT_CENTER);
                viewHolder.userImage.setVisibility(View.GONE);
                break;
            case TYPE_HEADER:
                FriendInfo header = headerList.get(position);
                viewHolder.nickName.setText(header.getNickName());
                viewHolder.userImage.setImageBitmap(BitmapUtil.getBitmap(header.getAvatar()));
                break;
            case TYPE_NORMAL:
                FriendInfo friend = friendList.get(getFriendPosition(position));
                viewHolder.nickName.setText(friend.getNickName());
                viewHolder.userImage.setImageBitmap(BitmapUtil.getBitmap(friend.getAvatar()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return friendList.size() + getHeaderSize() + getFooterSize();
    }

    /**
     * 设置 Header和 Listener
     * @param text Header显示的文字
     * @param listener Listener
     */
    public void addHeader(String text, OnItemClickListener listener) {
        if (headerList == null) {
            headerList = new ArrayList<>();
        }
        if (headerListener == null) {
            headerListener = new ArrayList<>();
        }
        headerList.add(new FriendInfo(text));
        headerListener.add(listener);
    }

    public void addHeader(int id, OnItemClickListener listener) {
        addHeader(mContext.getResources().getString(id), listener);
    }

    public void addHeader(int id, int pic, OnItemClickListener listener) {
        FriendInfo info = new FriendInfo();
        info.setNickName(mContext.getResources().getString(id));
        info.setAvatar(BitmapUtil.getBitmapByte(mContext,pic,Bitmap.CompressFormat.PNG));
        if (headerList == null) {
            headerList = new ArrayList<>();
        }
        if (headerListener == null) {
            headerListener = new ArrayList<>();
        }
        headerList.add(info);
        headerListener.add(listener);
    }

    public int getHeaderSize() {
        return headerList != null ? headerList.size() : 0;
    }

    private int getFriendPosition(int position) {
        return position - getHeaderSize();
    }

    /**
     * 添加 Header和 Listener
     * @param text Header显示的信息
     * @param listener Listener
     */
    public void addFooter(String text, OnItemClickListener listener) {
        if (footerList == null) {
            footerList = new ArrayList<>();
        }
        if (footerListener == null) {
            footerListener = new ArrayList<>();
        }
        footerList.add(new FriendInfo(text));
        footerListener.add(listener);
    }

    private int getFooterPosition(int position) {
        return position - getHeaderSize() - friendList.size();
    }

    private int getFooterSize() {
        return footerList != null ? footerList.size() : 0;
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
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
                    int p = getAdapterPosition();
                    switch (ContactAdapter.this.getItemViewType(p)) {
                        case TYPE_NORMAL:
                            if (mListener != null) {
                                mListener.onItemClick(v, getFriendPosition(p));
                            }
                            break;
                        case TYPE_HEADER:
                            headerListener.get(p).onItemClick(v, p);
                            break;
                        case TYPE_FOOTER:
                            int fp = getFooterPosition(p);
                            footerListener.get(fp).onItemClick(v, fp);
                            break;
                    }

                }
            });
        }
    }

}
