package com.dogvip.giannis.dogviprefactored.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.dogvip.giannis.dogviprefactored.di.DaggerAppComponent;
import com.dogvip.giannis.dogviprefactored.lifecycle.MyLifecycleHandler;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;

/**
 * Created by giannis on 4/11/2017.
 */

public class MyApp extends Application implements HasActivityInjector, HasServiceInjector {

    @Inject
    DispatchingAndroidInjector<Activity> mActivityInjector;

    @Inject
    DispatchingAndroidInjector<Service> mServiceInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent
                .builder()
                .create(this)
                .inject(this);
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return mActivityInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() { return mServiceInjector; }
}
