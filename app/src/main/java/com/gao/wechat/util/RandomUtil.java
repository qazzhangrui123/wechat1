package com.gao.wechat.util;

import java.util.Random;
import java.util.UUID;

/**
 * 2019-07-30 Gao
 */
public class RandomUtil {

    /**
     * 生成唯一ID
     * @return 唯一ID
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成随机字符
     * @return 随机字符
     */
    public static String salt() {
        try {
            UUID uuid = UUID.randomUUID();
            return HashUtil.SHA(uuid.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成用户ID
     * @return 用户ID
     */
    public static long uid() {
        return new Random().nextInt(MAX_UID) + MIN_UID;
    }

    private static final int MAX_UID = (1 << 29);
    private static final int MIN_UID = (1 << 14);

}
