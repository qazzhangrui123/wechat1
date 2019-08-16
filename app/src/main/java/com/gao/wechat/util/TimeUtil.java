package com.gao.wechat.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    private static SimpleDateFormat timeWithDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat timeWithNoWeek = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat timeWithWeek = new SimpleDateFormat("E HH:mm");
    private static SimpleDateFormat week = new SimpleDateFormat("E");

    public static String getDetailTime(long timeMills) {
        Calendar calendar = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        current.setTime(new Date());
        calendar.setTime(new Date(timeMills));
        int dayMinus = current.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
        int yearMinus = current.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        if (dayMinus > 7 || (dayMinus <= 0 && yearMinus > 0)) {
            return timeWithDate.format(calendar.getTime());
        } else if (dayMinus == 0) {
            return timeWithNoWeek.format(calendar.getTime());
        } else {
            return timeWithWeek.format(calendar.getTime());
        }
    }

    public static String getShortTime(long timeMills) {
        Calendar calendar = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        current.setTime(new Date());
        calendar.setTime(new Date(timeMills));
        int dayMinus = current.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
        int yearMinus = current.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        if (dayMinus > 7 || (dayMinus <= 0 && yearMinus > 0)) {
            return date.format(calendar.getTime());
        } else if (dayMinus == 0) {
            return timeWithNoWeek.format(calendar.getTime());
        } else {
            return week.format(calendar.getTime());
        }
    }

}
