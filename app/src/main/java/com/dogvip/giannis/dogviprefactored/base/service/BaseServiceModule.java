package com.dogvip.giannis.dogviprefactored.base.service;

import android.app.Service;
import android.content.Context;

import dagger.Binds;
import dagger.Module;

/**
 * Created by giannis on 7/12/2017.
 */
@Module
public abstract class BaseServiceModule {

    @Binds
    abstract Context provideServiceContext(Service service);

}
