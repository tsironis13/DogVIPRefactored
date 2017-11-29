package com.dogvip.giannis.dogviprefactored.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;

import dagger.android.AndroidInjection;

/**
 * Created by giannis on 4/11/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements Lifecycle.View {

    public abstract Lifecycle.ViewModel getViewModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getViewModel() != null) getViewModel().onViewAttached(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getViewModel() != null) getViewModel().onViewResumed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getViewModel() != null) getViewModel().onViewDetached();
    }

}
