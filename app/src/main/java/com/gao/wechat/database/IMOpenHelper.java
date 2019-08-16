package com.gao.wechat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IMOpenHelper extends SQLiteOpenHelper {

    private static final String USER_INFO = "CREATE TABLE IF NOT EXISTS UserInfo(" +
            "uid INTEGER PRIMARY KEY," +    // 用户ID
            "name TEXT," +                  // 名字
            "email TEXT," +                 // 邮箱
            "birthday TEXT," +              // 生日
            "gender INTEGER," +             // 性别
            "region TEXT," +                // 地区
            "signature TEXT," +             // 签名
            "avatar BLOB," +                // 头像
            "online INTEGER)";              // 在线情况

    private static final String MESSAGE = "CREATE TABLE IF NOT EXISTS Message(" +
            "msgid TEXT PRIMARY KEY," + // 消息ID
            "uid INTEGER," +            // 接收方
            "sid INTEGER," +            // 发送方
            "msg TEXT," +               // 接收的消息
            "msgtype INTEGER," +        // 消息类型
            "time INTEGER," +           // 消息发送时间
            "read INTEGER)";            // 标记该消息已读

    private static final String FRIEND_LIST = "CREATE TABLE IF NOT EXISTS Friend(" +
            "id TEXT PRIMARY KEY," +    // 好友信息ID
            "uid INTEGER," +            // 用户ID
            "fid INTEGER," +            // 好友ID
            "name TEXT," +              // 名字
            "email TEXT," +             // 邮箱
            "birthday TEXT," +          // 生日
            "gender INTEGER," +         // 性别
            "region TEXT," +            // 地区
            "signature TEXT," +         // 签名
            "avatar BLOB," +            // 头像
            "online INTEGER," +         // 在线情况
            "note TEXT)";               // 好友备注

    public IMOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(USER_INFO);
        sqLiteDatabase.execSQL(MESSAGE);
        sqLiteDatabase.execSQL(FRIEND_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
