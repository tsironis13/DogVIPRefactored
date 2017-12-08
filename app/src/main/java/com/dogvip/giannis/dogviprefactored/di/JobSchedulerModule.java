package com.dogvip.giannis.dogviprefactored.di;

import android.content.Context;
import android.os.Build;
import com.dogvip.giannis.dogviprefactored.di.qualifiers.ApplicationContext;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by giannis on 5/12/2017.
 */
@Module
public class JobSchedulerModule {

    @Provides
    @Singleton
    GooglePlayDriver provideGooglePlayDriver(@ApplicationContext Context context) {
        return new GooglePlayDriver(context);
    }

    @Provides
    @Singleton
    FirebaseJobDispatcher provideFirebaseJobDispatcher(GooglePlayDriver googlePlayDriver) {
        return new FirebaseJobDispatcher(googlePlayDriver);
    }

}
