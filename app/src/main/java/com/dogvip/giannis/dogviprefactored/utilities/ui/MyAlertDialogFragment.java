package com.dogvip.giannis.dogviprefactored.utilities.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.responsecontroller.login.signin.SignInEmailCommand;
import com.dogvip.giannis.dogviprefactored.utilities.ui.alertdialogcontroller.AlertDialogCommand;
import com.dogvip.giannis.dogviprefactored.utilities.ui.alertdialogcontroller.AlertDialogController;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by giannis on 30/11/2017.
 */

public class MyAlertDialogFragment extends DialogFragment {
    private static final String debugTag = MyAlertDialogFragment.class.getSimpleName();
    private static String title, message, negativeText, positiveText;
    private AlertDialogController mAlertDialogController;
//    @Inject
//    DispatchingAndroidInjector<Fragment> fragmentInjector;

    public static MyAlertDialogFragment newInstance() {
        return new MyAlertDialogFragment();
    }

    public static void setDialogMsgs(String dialogTtl, String dialogMsg, String dialogNegativeText, String dialogPositiveText) {
        title = dialogTtl;
        message = dialogMsg;
        negativeText = dialogNegativeText;
        positiveText = dialogPositiveText;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        // Perform injection here before M, L (API 22) and below because onAttach(Context)
        // is not yet available at L.
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) AndroidSupportInjection.inject(this);
        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        // Perform injection here for M (API 23) due to deprecation of onAttach(Activity).
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putSerializable("ser", mAlertDialogController);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (message.isEmpty()) {
            return new AlertDialog.Builder(getActivity())
                                                .setTitle(title)
                                                .setPositiveButton(positiveText,
                                                                    (dialog, whichButton) -> mAlertDialogController.executeCommandOnPositiveAction())
                                                .setNegativeButton(negativeText,
                                                                    (dialog, whichButton) -> mAlertDialogController.executeCommandOnNegativeAction())
                                                .create();
        } else {
            return new AlertDialog.Builder(getActivity())
                                                .setTitle(title)
                                                .setMessage(message)
                                                .setPositiveButton(positiveText,
                                                                    (dialog, whichButton) -> mAlertDialogController.executeCommandOnPositiveAction())
                                                .setNegativeButton(negativeText,
                                                                    (dialog, whichButton) -> mAlertDialogController.executeCommandOnNegativeAction())
                                                .create();
        }

    }

//    @Override
//    public AndroidInjector<Fragment> supportFragmentInjector() {
//        return fragmentInjector;
//    }

    public void setCommand(AlertDialogCommand command) {
        mAlertDialogController = new AlertDialogController();
        mAlertDialogController.setCommand(command);
    }

}
