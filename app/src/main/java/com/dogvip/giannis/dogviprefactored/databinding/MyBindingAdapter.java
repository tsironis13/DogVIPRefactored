package com.dogvip.giannis.dogviprefactored.databinding;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by giannis on 16/12/2017.
 */

public class MyBindingAdapter {
    private static final String debugTag = MyBindingAdapter.class.getSimpleName();

    @BindingAdapter("bind:textDate")
    public static void setDateText(TextView textView, String text) {
        if (text != null) textView.setText(convertEpochToDate(Long.parseLong(text)));
    }

    @BindingAdapter("bind:textAge")
    public static void setAgeText(TextView textView, String text) {
        if (text != null) {
            String displayAge;
            String[] holder = convertEpochToDate(Long.parseLong(text)).split("/");
            int current_year = Calendar.getInstance().get(Calendar.YEAR);
            int current_month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int age = current_year - Integer.valueOf(holder[2]);
            if (age > 0) {
                displayAge = String.valueOf(age);
                if (age == 1) {
                    displayAge += " έτους";
                } else {
                    displayAge += " ετών";
                }
            } else {
                int month = current_month - Integer.valueOf(holder[1]);
                if (month == 0) {
                    month = 1;
                } else {
                    month++;
                }
                displayAge = String.valueOf(month);
                displayAge += " μηνών";
            }
            textView.setText(displayAge);
        }
    }

    private static String convertEpochToDate(long epoch) {
        SimpleDateFormat datefrmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return datefrmt.format(new Date(epoch*1000L));
    }
}
