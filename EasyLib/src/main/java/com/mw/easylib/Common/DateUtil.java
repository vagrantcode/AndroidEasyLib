package com.mw.easylib.Common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static String defaultFormat = DateStringFormat.DateTimeHorizontal.value();

    public static Date convertToDate(String strDate) throws Exception {
        DateFormat df = new SimpleDateFormat(defaultFormat);
        return df.parse(strDate);
    }

    public static Date convertToDate(String strDate, String format) throws Exception {
        DateFormat df = new SimpleDateFormat(format);
        return df.parse(strDate);
    }

    public static String convertToString(Date date) {
        DateFormat df = new SimpleDateFormat(defaultFormat);
        return df.format(date);
    }

    public static String convertToString(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static long difTime(Date startDate, Date endDate) throws Exception {
        long d1 = startDate.getTime();
        long d2 = endDate.getTime();
        return d1 - d2;
    }

    public static void setDefaultFormat(String formatString) {
        defaultFormat = formatString;
    }

    public enum DateStringFormat {
        DateHorizontal("yyyy-MM-dd"), DateTimeHorizontal("yyyy-MM-dd HH:mm:ss");
        private String format = "yyyy-MM-dd";

        DateStringFormat(String format) {
            this.format = format;
        }

        public static DateStringFormat getValueOf(String val) {
            switch (val) {
                case "yyyy-MM-dd":
                    return DateHorizontal;
                case "yyyy-MM-dd HH:mm:ss":
                    return DateTimeHorizontal;
                default:
                    return null;
            }
        }

        public String value() {
            return this.format;
        }
    }
}
