package com.dogvip.giannis.dogviprefactored.base.activity;

import android.app.Activity;
import android.content.Context;

import com.dogvip.giannis.dogviprefactored.accountmanager.AccountManagerModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by giannis on 4/11/2017.
 */
@Module(includes = AccountManagerModule.class)
public abstract class BaseActivityModule {

    @Binds
    abstract Context provideActivityContext(Activity activity);

}