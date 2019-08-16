package com.gao.wechat.util;

import com.gao.wechat.data.UserInfo;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.util.Comparator;

public class PinyinComparator implements Comparator<UserInfo> {

    public int compare(UserInfo o1, UserInfo o2) {
        String str1 = o1.getNickName() != null ? getPinyin(o1.getNickName()) : String.valueOf(o1.getUserID());
        String str2 = o2.getNickName() != null ? getPinyin(o2.getNickName()) : String.valueOf(o2.getUserID());
        return str1.compareTo(str2);
    }

    /**
     * 将汉字转换为拼音，其他字符不变
     * @param string 要转换的字符串
     * @return 拼音
     */
    public static String getPinyin(String string) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char [] input = string.trim().toCharArray();
        StringBuilder result = new StringBuilder();

        try {
            for (char ch : input) {
                // 判断是否为中文
                if (Character.toString(ch).matches("[\\u4E00-\\u9FA5]+")) {
                    String [] temp = PinyinHelper.toHanyuPinyinStringArray(ch, format);
                    for (String s : temp) {
                        result.append(s);
                    }
                // 判断是否为大写字母
                } else if (ch > 'A' && ch < 'Z') {
                    result.append(Character.toString(ch).toUpperCase());
                } else {
                    result.append(ch);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String getPinyinFirst(String string) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char [] input = string.trim().toCharArray();
        StringBuilder result = new StringBuilder();

        try {
            // 判断是否为中文
            if (Character.toString(input[0]).matches("[\\u4E00-\\u9FA5]+")) {
                String [] temp = PinyinHelper.toHanyuPinyinStringArray(input[0], format);
                result.append(temp[0].charAt(0));
                // 判断是否为大写字母
            } else if (input[0] > 'a' && input[0] < 'z') {
                result.append(Character.toString(input[0]).toUpperCase());
            } else {
                result.append(input[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

}
