package com.dogvip.giannis.dogviprefactored.owner.profile;

import android.app.Activity;

import com.dogvip.giannis.dogviprefactored.base.activity.BaseActivityModule;
import com.dogvip.giannis.dogviprefactored.di.scope.PerActivity;
import com.dogvip.giannis.dogviprefactored.owner.form.OwnerFormActivity;

import dagger.Binds;
import dagger.Module;

/**
 * Created by giannis on 11/12/2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class OwnerProfileActivityModule {

    @Binds
    @PerActivity
    abstract Activity activity(OwnerProfileActivity ownerProfileActivity);

}
