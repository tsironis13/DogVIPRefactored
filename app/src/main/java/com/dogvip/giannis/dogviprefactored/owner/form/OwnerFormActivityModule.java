package com.dogvip.giannis.dogviprefactored.owner.form;

import android.app.Activity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.dogvip.giannis.dogviprefactored.base.activity.BaseActivityModule;
import com.dogvip.giannis.dogviprefactored.dashboard.DashboardActivity;
import com.dogvip.giannis.dogviprefactored.di.scope.PerActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by giannis on 9/12/2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class OwnerFormActivityModule {

    @Binds
    @PerActivity
    abstract Activity activity(OwnerFormActivity ownerFormActivity);

    @Provides
    static AwesomeValidation provideAwesomeValidation() {
        return new AwesomeValidation(ValidationStyle.BASIC);
    }

}
