package com.gao.wechat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.gao.wechat.data.FriendInfo;
import com.gao.wechat.data.TransMsg;
import com.gao.wechat.data.TransMsgType;
import com.gao.wechat.data.UserInfo;
import com.gao.wechat.util.RandomUtil;

import java.util.ArrayList;

public class DatabaseUtil {

    private static final String USER = "UserInfo";
    private static final String MESSAGE = "Message";
    private static final String FRIEND = "Friend";
    private static final String DB_NAME = "chat_local";
    private static final int VERSION = 1;
    private static DatabaseUtil util;
    private SQLiteDatabase db;

    private DatabaseUtil(Context context) {
        IMOpenHelper helper = new IMOpenHelper
                (context, DB_NAME,null,VERSION);
        db = helper.getWritableDatabase();
    }

    public static synchronized DatabaseUtil get(Context context) {
        if (util == null) {
            util = new DatabaseUtil(context);
        }
        return util;
    }

    /**
     * 向数据库中写入(更新)已登录用户的信息
     * @param myInfo 已登录用户的信息
     */
    public void saveMyInfo(@NonNull UserInfo myInfo) {
        ContentValues values = new ContentValues();
        values.put("avatar", myInfo.getAvatar());
        values.put("birthday",myInfo.getBirthday());
        values.put("email",myInfo.getEmail());
        values.put("gender",myInfo.getGender());
        values.put("name",myInfo.getNickName());
        values.put("region",myInfo.getRegion());
        values.put("signature",myInfo.getSignature());
        values.put("uid",myInfo.getUserID());
        db.replace(USER,null,values);
    }

    /**
     * 从数据库中获取用户的信息
     * @param userID 用户ID
     */
    public UserInfo getMyInfo(long userID) {
        UserInfo myInfo = new UserInfo();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM UserInfo WHERE uid=?",
                new String[] {"" + userID});
        if (cursor != null) {
            if (cursor.moveToNext()) {
                myInfo.setAvatar(cursor.getBlob(cursor.getColumnIndex("avatar")));
                myInfo.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
                myInfo.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                myInfo.setGender(cursor.getInt(cursor.getColumnIndex("gender")));
                myInfo.setNickName(cursor.getString(cursor.getColumnIndex("name")));
                myInfo.setRegion(cursor.getString(cursor.getColumnIndex("region")));
                myInfo.setSignature(cursor.getString(cursor.getColumnIndex("signature")));
                myInfo.setUserID(cursor.getLong(cursor.getColumnIndex("uid")));
            }
            cursor.close();
        }
        return myInfo;
    }

    /**
     * 添加好友信息
     * @param userID 用户ID
     * @param friendInfo 好友信息
     */
    public void saveFriend(long userID, FriendInfo friendInfo) {
        ContentValues values = new ContentValues();
        values.put("id", RandomUtil.uuid());
        values.put("uid",userID);
        values.put("fid",friendInfo.getUserID());
        values.put("avatar", friendInfo.getAvatar());
        values.put("birthday",friendInfo.getBirthday());
        values.put("email",friendInfo.getEmail());
        values.put("gender",friendInfo.getGender());
        values.put("name",friendInfo.getNickName());
        values.put("region",friendInfo.getRegion());
        values.put("signature",friendInfo.getSignature());
        values.put("note",friendInfo.getNote());
        db.insert(FRIEND,null,values);
    }

    /**
     * 获取某个好友的详细信息
     * @param userID 用户ID
     * @param friendID 好友ID
     * @return 好友的信息
     */
    public FriendInfo getFriendInfo(long userID, long friendID) {
        FriendInfo friend = new FriendInfo();
        Cursor cursor = db.rawQuery("SELECT * FROM Friend WHERE uid=? AND fid=?",
                new String[] {"" + userID, "" + friendID});
        if (cursor != null) {
            if (cursor.moveToNext()) {
                friend.setUserID(cursor.getLong(cursor.getColumnIndex("fid")));
                friend.setNote(cursor.getString(cursor.getColumnIndex("note")));
                friend.setAvatar(cursor.getBlob(cursor.getColumnIndex("avatar")));
                friend.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
                friend.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                friend.setGender(cursor.getInt(cursor.getColumnIndex("gender")));
                friend.setNickName(cursor.getString(cursor.getColumnIndex("name")));
                friend.setRegion(cursor.getString(cursor.getColumnIndex("region")));
                friend.setSignature(cursor.getString(cursor.getColumnIndex("signature")));
            }
            cursor.close();
        }
        return friend;
    }

    /**
     * 获取好友列表信息
     * @param userID 用户ID
     */
    public ArrayList<FriendInfo> getFriendList(long userID) {
        ArrayList<FriendInfo> friends = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Friend WHERE uid=?",
                new String[] {"" + userID});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                FriendInfo friend = new FriendInfo();
                friend.setUserID(cursor.getLong(cursor.getColumnIndex("fid")));
                friend.setNote(cursor.getString(cursor.getColumnIndex("note")));
                friend.setAvatar(cursor.getBlob(cursor.getColumnIndex("avatar")));
                friend.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
                friend.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                friend.setGender(cursor.getInt(cursor.getColumnIndex("gender")));
                friend.setNickName(cursor.getString(cursor.getColumnIndex("name")));
                friend.setRegion(cursor.getString(cursor.getColumnIndex("region")));
                friend.setSignature(cursor.getString(cursor.getColumnIndex("signature")));
                friends.add(friend);
            }
            cursor.close();
        }
        return friends;
    }

    /**
     * 更新好友信息
     * @param userID 用户ID
     * @param friendInfo 好友信息
     */
    public void updateFriend(long userID, FriendInfo friendInfo) {
        removeFriend(userID, friendInfo);
        saveFriend(userID, friendInfo);
    }

    /**
     * 删除好友信息
     * @param userID 用户ID
     * @param friendInfo 好友信息
     */
    public void removeFriend(long userID, FriendInfo friendInfo) {
        String sql = "DELETE FROM Friend" +
                " WHERE uid=" + userID +
                " AND fid=" + friendInfo.getUserID();
        db.execSQL(sql);
    }

    /**
     * 保存信息
     * @param message 要保存的信息
     */
    public void saveMessage(TransMsg message) {
        ContentValues values = new ContentValues();
        values.put("msgid", RandomUtil.uuid());
        values.put("uid", message.getReceiverID());
        values.put("sid", message.getSenderID());
        values.put("msg", message.getObject());
        values.put("msgtype", message.getObjectType().ordinal());
        values.put("time", message.getSendTime());
        values.put("read", 0);
        db.insert(MESSAGE, null, values);
    }

    /**
     * 根据好友ID获取信息
     * @param userID 用户ID
     * @param friendID 好友ID
     */
    public ArrayList<TransMsg> getMessageByFriend(long userID, long friendID) {
        ArrayList<TransMsg> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Message" +
                " WHERE sid=? AND uid=?",
                new String[] {"" + friendID, "" + userID});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TransMsg message = new TransMsg();
                message.setSenderID(cursor.getLong(cursor.getColumnIndex("fid")));
                message.setReceiverID(cursor.getLong(cursor.getColumnIndex("uid")));
                message.setObjectType(TransMsgType.values()[
                        cursor.getInt(cursor.getColumnIndex("msgtype"))]);
                message.setObject(cursor.getString(cursor.getColumnIndex("msg")));
                message.setSendTime(cursor.getLong(cursor.getColumnIndex("time")));
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 根据发送方ID删除信息
     * @param message 发送方ID
     */
    public void removeMessageBySenderID(@NonNull TransMsg message) {
        String sql = "DELETE FROM Message" +
                " WHERE fid=" + message.getSenderID()  +
                " AND type=" + message.getObjectType().ordinal() +
                " AND uid=" + message.getReceiverID();
        db.execSQL(sql);
    }

    /**
     * 根据内容删除信息
     * @param message 信息
     */
    public void removeMessageByContent(@NonNull TransMsg message) {
        String sql = "DELETE FROM Message" +
                " WHERE fid=" + message.getSenderID() +
                " AND type=" + message.getObjectType().ordinal() +
                " AND uid=" + message.getReceiverID() +
                " AND msg=" + message.getObject();
        db.execSQL(sql);
    }


}
