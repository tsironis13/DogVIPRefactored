package com.dogvip.giannis.dogviprefactored.services.jobs;

import android.util.Log;

import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.pojo.sync.UploadFcmTokenRequest;
import com.dogvip.giannis.dogviprefactored.retrofit.ServiceAPI;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.DogVipRoomDatabase;
import com.dogvip.giannis.dogviprefactored.utilities.errorhandling.RetryWithDelay;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by giannis on 6/12/2017.
 */

public class SyncFcmTokenJob extends JobService {
    private static final String debugTag = SyncFcmTokenJob.class.getSimpleName();
    @Inject
    ServiceAPI mServiceAPI;
    @Inject
    UploadFcmTokenRequest request;
    @Inject
    RetryWithDelay retryWithDelay;
    @Inject
    DogVipRoomDatabase dogVipRoomDatabase;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.e(debugTag, "SYNC FCM TOKEN JOB ON START JOB: ");
        request.setAction("save_registration_token");
        request.setAuthtoken(job.getExtras().getString("auth_token"));
        request.setFcmtoken(job.getExtras().getString("fcm_token"));
        request.setDeviceid(android.os.Build.SERIAL);
        mServiceAPI
                .uploadFcmToken(request)
                .subscribeOn(Schedulers.io())
                .retryWhen(configureRetryWithDelayParams(3, 2000))
                .subscribe(baseResponse -> {
                    if (baseResponse.getCode() == AppConfig.STATUS_OK) {
                        updateLclFcmToken()
                                .retryWhen(configureRetryWithDelayParams(3, 2000))
                                .subscribeOn(Schedulers.io())
                                .subscribe(
                                        () -> {
                                            Log.e(debugTag, "SyncFcmTokenJob successfully finished");
                                            jobFinished(job, false);
//                                        dogVipRoomDatabase.userDeviceDao().getDeviceDetails(android.os.Build.SERIAL).subscribe(new Consumer<UserDevice>() {
//                                            @Override
//                                            public void accept(UserDevice userDevice) throws Exception {
//                                                Log.e(debugTag, "userid: "+userDevice.getUser_id() + " synced: "+userDevice.isToken_synced() + " fcm token: "+
//                                                userDevice.getFcm_registration_token() + " devoce: "+userDevice.getDevice_model());
//                                            }
//                                        });
                                        },
                                        onError -> {
                                            Log.e(debugTag, "job finished with error, reschedule it");
                                            jobFinished(job, true);
                                        });
                    } else {
                        jobFinished(job, true);
                    }
                }, onError -> {
                    Log.e(debugTag, "onerr: "+onError);
                    jobFinished(job, true);
                });
        return true;
    }
   /* Called when the scheduling engine has decided to interrupt the execution of a running job, most
   * likely because the runtime constraints associated with the job are no longer satisfied. The job
   * must stop execution
   * */
    @Override
    public boolean onStopJob(JobParameters job) {
        Log.e(debugTag, "onEndJob");
        return true;
    }

    private Completable updateLclFcmToken() {
        return Completable.fromAction(() -> {
            dogVipRoomDatabase
                            .userDeviceDao()
                            .updateTokenSynced(true, android.os.Build.SERIAL);
//            Log.e(debugTag, "updated in job: "+ x);
        });
    }

    private RetryWithDelay configureRetryWithDelayParams(int maxRetries, int retryDelayMillis) {
        retryWithDelay.setMaxRetries(maxRetries);
        retryWithDelay.setRetryDelayMillis(retryDelayMillis);
        return retryWithDelay;
    }
}
