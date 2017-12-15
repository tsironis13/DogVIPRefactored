package com.dogvip.giannis.dogviprefactored.di;

import com.dogvip.giannis.dogviprefactored.accountmanager.AccountManagerModule;
import com.dogvip.giannis.dogviprefactored.base.activity.BaseActivityModule;
import com.dogvip.giannis.dogviprefactored.dashboard.DashboardActivity;
import com.dogvip.giannis.dogviprefactored.dashboard.DashboardActivityModule;
import com.dogvip.giannis.dogviprefactored.di.scope.PerActivity;
import com.dogvip.giannis.dogviprefactored.login.LoginActivity;
import com.dogvip.giannis.dogviprefactored.login.LoginActivityModule;
import com.dogvip.giannis.dogviprefactored.owner.form.OwnerFormActivity;
import com.dogvip.giannis.dogviprefactored.owner.form.OwnerFormActivityModule;
import com.dogvip.giannis.dogviprefactored.owner.profile.OwnerProfileActivity;
import com.dogvip.giannis.dogviprefactored.owner.profile.OwnerProfileActivityModule;
import com.dogvip.giannis.dogviprefactored.utilities.notification.NotificationUtlsModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by giannis on 4/11/2017.
 */
@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = {LoginActivityModule.class, AccountManagerModule.class, NotificationUtlsModule.class})
    abstract LoginActivity bindLoginActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = {DashboardActivityModule.class, AccountManagerModule.class})
    abstract DashboardActivity bindDashboardActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = {OwnerFormActivityModule.class, AccountManagerModule.class})
    abstract OwnerFormActivity bindOwnerFormActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = {OwnerProfileActivityModule.class, AccountManagerModule.class})
    abstract OwnerProfileActivity bindOwnerProfileActivity();

}
