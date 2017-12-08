package com.dogvip.giannis.dogviprefactored.services;

import android.os.Bundle;

import com.dogvip.giannis.dogviprefactored.services.jobs.SyncFcmTokenJob;
import com.dogvip.giannis.dogviprefactored.services.jobs.SyncLocalUserRolesAndPetsJob;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobTrigger;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import javax.inject.Inject;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * Created by giannis on 6/12/2017.
 */

public class JobConfiguration {

    @Inject
    FirebaseJobDispatcher mFirebaseJobDispatcher;

    @Inject
    public JobConfiguration() {}

    public void syncFcmTokenJob(String authToken, String fcmToken) {
        Bundle bundle = new Bundle();
        bundle.putString("auth_token", authToken);
        bundle.putString("fcm_token", fcmToken);
        scheduleJob(mFirebaseJobDispatcher.newJobBuilder()
                                                .setService(SyncFcmTokenJob.class)
                                                .setTag("sync_fcm_job")
                                                .setTrigger(Trigger.executionWindow(0, 0))
                                                .setConstraints(Constraint.ON_ANY_NETWORK)
                                                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                                                .setExtras(bundle)
                                                .build());
    }

    public void syncLocalUserRolesAndPets(String authToken) {
        Bundle bundle = new Bundle();
        bundle.putString("auth_token", authToken);
        int periodicity = (int) MINUTES.toSeconds(2);
//        int periodicity = 30;
        scheduleJob(mFirebaseJobDispatcher.newJobBuilder()
                                                .setLifetime(Lifetime.FOREVER)
                                                .setService(SyncLocalUserRolesAndPetsJob.class)
                                                .setTag("sync_local_user_roles_pets")
                                                .setReplaceCurrent(false)
                                                .setRecurring(true)
                                                .setTrigger(Trigger.executionWindow(periodicity, periodicity + 60))
                                                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                                                .setConstraints(Constraint.ON_ANY_NETWORK)
                                                .setExtras(bundle)
                                                .build());
    }

    public void cancelJobByTag(String tag) {
        mFirebaseJobDispatcher.cancel(tag);
    }

    private void scheduleJob(Job job) {
//        mFirebaseJobDispatcher.cancelAll();
        mFirebaseJobDispatcher.mustSchedule(job);
    }

}
