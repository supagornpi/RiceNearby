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
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        try {
            Date newDate = sdf.parse(date);
            sdf = new SimpleDateFormat("dd MMM");
            date = sdf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format(AppInstance.DATE_FORMAT_DEFAULT, cal).toString();
    }
}
