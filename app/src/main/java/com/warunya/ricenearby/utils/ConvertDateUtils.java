package com.warunya.ricenearby.utils;

import android.text.format.DateFormat;

import com.warunya.ricenearby.constant.AppInstance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConvertDateUtils {

    public static String getNewDateFormatFOrMealTime(String date) {
        String myFormat = AppInstance.DATE_FORMAT_DEFAULT; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("th"));

        try {
            Date newDate = sdf.parse(date);
            sdf = new SimpleDateFormat("dd MMM");
            date = sdf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Calendar convertToCalendar(String date) {
        String myFormat = AppInstance.DATE_FORMAT_DEFAULT; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("th"));
        Calendar calendar = Calendar.getInstance();
        try {
            Date newDate = sdf.parse(date);
            calendar.setTime(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public static String convertToString(Date date) {
        String myFormat = AppInstance.DATE_FORMAT_DEFAULT; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("th"));
        return sdf.format(date);
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(new Locale("th"));
        cal.setTimeInMillis(time);
        return DateFormat.format(AppInstance.DATE_FORMAT_DEFAULT, cal).toString();
    }

    public static String getDate() {
        Calendar cal = Calendar.getInstance(new Locale("th"));
        cal.setTime(new Date());
        return DateFormat.format(AppInstance.DATE_FORMAT_ORDER, cal).toString();
    }
}
