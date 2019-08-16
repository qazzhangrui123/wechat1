package com.gao.wechat.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {

    private static final String SP_NAME = "CHAT_SP";
    private static final String LOGIN = "isLogin";
    private static final String LOGIN_ID = "loginUid";
    private static final String FIRST_RUN = "firstRun";
    private static SPUtil instance;

    private SPUtil() {

    }

    public static SPUtil getInstance() {
        if (instance == null) {
            instance = new SPUtil();
        }
        return instance;
    }

    /**
     * 获取 Context 的 SharedPreference
     * @param context context
     * @return SharedPreference
     */
    public SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取是否为第一次运行程序
     * @param context Context
     * @return 是否为第一次运行
     */
    public boolean isFirstRun(Context context) {
        return getSharedPreference(context).getBoolean(FIRST_RUN, true);
    }

    public void setFirstRun(Context context, boolean firstRun) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(FIRST_RUN, firstRun);
        editor.apply();
    }

    /**
     * 获取是否已经登录
     * @param context Context
     * @return 是否已经登录的结果
     */
    public boolean isLogin(Context context) {
        return getSharedPreference(context).getBoolean(LOGIN, false);
    }

    public void setLogin(Context context, boolean login) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(LOGIN, login);
        editor.apply();
    }

    /**
     * 获取最近登录的账户
     * @param context Context
     * @return 最近登录的账户名
     */

    public long getLoginUid(Context context) {
        return getSharedPreference(context).getLong(LOGIN_ID, 0L);
    }

    public void setLoginUid(Context context, long userID) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putLong(LOGIN_ID, userID);
        editor.apply();
    }

}
