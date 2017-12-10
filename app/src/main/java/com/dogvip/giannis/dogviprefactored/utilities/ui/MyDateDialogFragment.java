package com.dogvip.giannis.dogviprefactored.utilities.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by giannis on 10/12/2017.
 */

public class MyDateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private Activity invokingActivity;
    private static int dialogType;

    public static MyDateDialogFragment newInstance() {
//        MyDateDialogFragment dialogFragment =
//        Bundle args = new Bundle();
//        args.putInt("dialog_type", dialogType);
//        dialogFragment.setArguments(args);
        return new MyDateDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        int dialogType = getArguments().getInt("dialog_type");
        Calendar c = Calendar.getInstance();
        if (dialogType == 1) {
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        } else {

        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.MONTH, month);
        long epoch = cal.getTimeInMillis();
        if (epoch/1000L > System.currentTimeMillis()/1000L) {
            ((Lifecycle.MyDateDialogCallbackContract)invokingActivity).onInvalidDateSet();
        } else {
            SimpleDateFormat datefrmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            ((Lifecycle.MyDateDialogCallbackContract)invokingActivity).onDisplayDateSet(datefrmt.format(new Date(epoch)));
            ((Lifecycle.MyDateDialogCallbackContract)invokingActivity).onDateSet(epoch/1000L);
        }
    }

    public static void setDialogType(int dlgType) {
        dialogType = dlgType;
    }

    public void setInvokingActivity(Activity activity) {
        this.invokingActivity = activity;
    }

    public void clearInvokingActivity() {
        this.invokingActivity = null;
    }
}
