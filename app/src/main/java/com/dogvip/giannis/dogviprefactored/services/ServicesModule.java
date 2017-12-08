package com.dogvip.giannis.dogviprefactored.services;

import android.app.Service;
import android.content.Context;

import com.dogvip.giannis.dogviprefactored.base.service.BaseServiceModule;
import com.dogvip.giannis.dogviprefactored.services.service.MyFirebaseInstanceIDService;

import dagger.Binds;
import dagger.Module;

/**
 * Created by giannis on 7/12/2017.
 */
@Module(includes = BaseServiceModule.class)
public abstract class ServicesModule {

    @Binds
    abstract Service myFirebaseInstanceIDService(MyFirebaseInstanceIDService service);

}
