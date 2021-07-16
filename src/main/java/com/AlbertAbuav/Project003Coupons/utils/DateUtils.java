package com.AlbertAbuav.Project003Coupons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * The method insert the parameters of a date and time in the local
     * format by Integers and return a java util Date
     * @param dd
     * @param MM
     * @param yyyy
     * @return Date util object
     * @throws ParseException
     */
    public static Date getDate(int dd, int MM, int yyyy) {
        String str = String.format("%2d/%2d/%4d", dd, MM, yyyy);
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * beautify the string of a java util date
     * @param date
     * @return formatted String
     */
    public static String beautifyDate(Date date) {
        return simpleDateFormat.format(date);
    }

    /**
     * Create a contemporary system time
     * @return java util Date
     */
    public static Date contemporaryDate() {
        return new Date();
    }

    /**
     * The method convert a Java "Date" to SQL "Date".
     * @return
     */

    public static java.sql.Date convertJavaDateToSqlDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    /**
     * The method convert a "LocalDate" to Java "Date".
     * @return
     */
    public static Date javaDateFromLocalDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }

    public static Date addOneDayToUtilDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    public static Date reduceOneDayToUtilDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
}
