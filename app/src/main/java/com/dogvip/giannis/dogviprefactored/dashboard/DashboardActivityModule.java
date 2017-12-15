package com.dogvip.giannis.dogviprefactored.dashboard;

import android.app.Activity;

import com.dogvip.giannis.dogviprefactored.base.activity.BaseActivityModule;
import com.dogvip.giannis.dogviprefactored.di.scope.PerActivity;
import com.dogvip.giannis.dogviprefactored.di.scope.PerFragment;
import com.dogvip.giannis.dogviprefactored.login.LoginActivity;
import com.dogvip.giannis.dogviprefactored.utilities.ui.MyAlertDialogFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by giannis on 1/12/2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class DashboardActivityModule {

//    @PerFragment
//    @ContributesAndroidInjector
//    abstract MyAlertDialogFragment myAlertDialogFrgmtInjector();

    @Binds
    @PerActivity
    abstract Activity activity(DashboardActivity dashboardActivity);

}
