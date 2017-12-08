package com.dogvip.giannis.dogviprefactored.base.activity;

import android.app.Activity;
import android.content.Context;

import dagger.Binds;
import dagger.Module;

/**
 * Created by giannis on 4/11/2017.
 */
@Module
public abstract class BaseActivityModule {

    @Binds
    abstract Context provideActivityContext(Activity activity);

}
