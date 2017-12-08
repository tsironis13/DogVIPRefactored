package com.dogvip.giannis.dogviprefactored.di;

import com.dogvip.giannis.dogviprefactored.accountmanager.AccountManagerModule;
import com.dogvip.giannis.dogviprefactored.services.ServicesModule;
import com.dogvip.giannis.dogviprefactored.services.jobs.SyncLocalUserRolesAndPetsJob;
import com.dogvip.giannis.dogviprefactored.services.service.MyFirebaseInstanceIDService;
import com.dogvip.giannis.dogviprefactored.services.jobs.SyncFcmTokenJob;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by giannis on 5/12/2017.
 */

@Module
public abstract class ServiceModule {

    @ContributesAndroidInjector(modules = {ServicesModule.class, AccountManagerModule.class})
    abstract MyFirebaseInstanceIDService bindMyFirebaseInstanceIDService();

    @ContributesAndroidInjector
    abstract SyncFcmTokenJob bindSyncFcmTokenJob();

    @ContributesAndroidInjector
    abstract SyncLocalUserRolesAndPetsJob bindSyncLocalUserRolesAndPetsJob();
}
