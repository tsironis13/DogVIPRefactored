package com.dogvip.giannis.dogviprefactored.utilities.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;

/**
 * Created by giannis on 30/11/2017.
 */

public class MyAlertDialogFragment extends DialogFragment {
    private static final String debugTag = MyAlertDialogFragment.class.getSimpleName();
    private Activity invokingActivity;

    public static MyAlertDialogFragment newInstance(String title, String negativeText, String positiveText) {
        MyAlertDialogFragment dialogFragment = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("positive_text", positiveText);
        args.putString("negative_text", negativeText);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                                    .setTitle(getArguments().getString("title"))
                                    .setPositiveButton(getArguments().getString("positive_text"),
                                            (dialog, whichButton) -> ((Lifecycle.MyAlertDialogCallbackContract)invokingActivity).onPositiveAction())
                                    .setNegativeButton(getArguments().getString("negative_text"),
                                            (dialog, whichButton) -> ((Lifecycle.MyAlertDialogCallbackContract)invokingActivity).onNegativeAction())
                                    .create();
    }

    public void setInvokingActivity(Activity activity) {
        this.invokingActivity = activity;
    }

    public void clearInvokingActivity() {
        this.invokingActivity = null;
    }
}
