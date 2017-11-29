package com.dogvip.giannis.dogviprefactored.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by giannis on 4/11/2017.
 */

public abstract class BaseFragment extends Fragment implements HasSupportFragmentInjector, Lifecycle.View {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;
    public abstract Lifecycle.ViewModel getViewModel();

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        // Perform injection here before M, L (API 22) and below because onAttach(Context)
        // is not yet available at L.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) AndroidSupportInjection.inject(this);
        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        // Perform injection here for M (API 23) due to deprecation of onAttach(Activity).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
//        Log.e(debugTag, "onStart");
        getViewModel().onViewAttached(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getViewModel().onViewResumed();
    }

    @Override
    public void onStop() {
        super.onStop();
        getViewModel().onViewDetached();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

}
