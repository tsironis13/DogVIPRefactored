package com.dogvip.giannis.dogviprefactored.login.forgotpass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

/**
 * Created by giannis on 30/10/2017.
 */

public class ForgotPaswrdRetainFragment extends Fragment {

    private ForgotPaswrdViewModel mForgotPaswrdViewModel;

    @Inject
    public ForgotPaswrdRetainFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    public void retainViewModel(ForgotPaswrdViewModel forgotPaswrdViewModel) {
        this.mForgotPaswrdViewModel = forgotPaswrdViewModel;
    }

    public ForgotPaswrdViewModel getViewModel() {
        return this.mForgotPaswrdViewModel;
    }

}
