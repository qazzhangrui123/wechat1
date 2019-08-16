package com.gao.wechat.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    private static String SHA(String str) throws UnsupportedEncodingException {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        StringBuilder hex = new StringBuilder();
        for (int v : sha.digest(str.getBytes("utf-8"))) {
            int val = v & 0xFF;
            if (val < 16) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(val));
        }
        return hex.toString();
    }

    public static String SHA(String ... strings) {
        StringBuilder sb = new StringBuilder();
        for (String str: strings) {
            sb.append(str);
        }
        try {
            return SHA(sb.toString());
        } catch (UnsupportedEncodingException e) {
            return sb.toString();
        }
    }

}
