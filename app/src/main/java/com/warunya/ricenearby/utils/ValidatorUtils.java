package com.warunya.ricenearby.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

public class ValidatorUtils {
    private static final String emailPattern = "(?:[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static final String mobilePattern = "(^06|^08|^09)[0-9]{8}$"; //0xxxxxxxxx

    public static boolean isInValidEmail(String email) {
        //check email Pattern
        return !email.matches(emailPattern);
    }

    public static boolean isInValidMobile(String mobile) {
        //check mobile Pattern
        return !mobile.matches(mobilePattern);
    }

    public static boolean isInValidPassword(String password) {
        //check mobile Pattern
        return !(password.length() >= 6);
    }

    public static void setErrorInput(Context context, EditText editText, int errorMessage) {
        editText.setError(context.getResources().getString(errorMessage));
        editText.requestFocus();
        KeyboardUtils.showKeyboard(context, editText);
    }

    public static void setErrorInput(Context context, TextView textView, int errorMessage) {
        textView.setError(context.getResources().getString(errorMessage));
        textView.requestFocus();
    }
}
