package com.dogvip.giannis.dogviprefactored.services.service;

import android.arch.persistence.room.EmptyResultSetException;
import android.util.Log;

import com.dogvip.giannis.dogviprefactored.accountmanager.MyAccountManager;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.DogVipRoomDatabase;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.entities.UserDevice;
import com.dogvip.giannis.dogviprefactored.services.JobConfiguration;
import com.dogvip.giannis.dogviprefactored.utilities.network.RetryWithDelay;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by giannis on 4/12/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String debugTag = MyFirebaseInstanceIDService.class.getSimpleName();

    @Inject
    DogVipRoomDatabase dogVipRoomDatabase;
    @Inject
    UserDevice userDevice;
    @Inject
    RetryWithDelay retryWithDelay;
    @Inject
    MyAccountManager mAccountManager;
    @Inject
    JobConfiguration mJobConfiguration;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public void onTokenRefresh() {
        Log.e(debugTag, "onTOkenRefresh: "+FirebaseInstanceId.getInstance().getToken() + "\n"+ dogVipRoomDatabase);
        dogVipRoomDatabase
                .userDeviceDao()
                .getDeviceDetails(android.os.Build.SERIAL)
                .subscribeOn(Schedulers.io())
                .retryWhen(configureRetryWithDelayParams(3, 2000))
                .subscribe(userDevice -> {
                    Log.e(debugTag, "token is not synced and user exist");
                    updateLclFcmToken(FirebaseInstanceId.getInstance().getToken())
                                .retry(3)
                                .subscribeOn(Schedulers.io())
                                .subscribe(() -> {
                                        if (mAccountManager.getAccountDetails() != null)
                                            mJobConfiguration.syncFcmTokenJob(mAccountManager.getAccountDetails().getToken(), FirebaseInstanceId.getInstance().getToken());
                                            Log.e(debugTag, "update success");
                                        },
                                        throwable -> {
                                            Log.e(debugTag, "Unable to update ..."+ throwable);
                                        });
                }, throwable -> {
                    if (throwable instanceof EmptyResultSetException) {
                        Log.e(debugTag, "empty set");
                        userDevice.setDevice_model(android.os.Build.SERIAL);
                        userDevice.setFcm_registration_token(FirebaseInstanceId.getInstance().getToken());
                        userDevice.setToken_synced(false);
                        insertLocalFcmToken(userDevice)
                                .retryWhen(configureRetryWithDelayParams(3, 2000))
                                .subscribe(
                                        () -> Log.e(debugTag, "insert success: "),
                                        onError -> Log.e(debugTag, "Unable to insert ...", onError));
                    }
                });
    }

    private Completable insertLocalFcmToken(UserDevice userDevice) {
        return Completable.fromAction(() -> {
            dogVipRoomDatabase
                    .userDeviceDao()
                    .insertUserDevice(userDevice);
//            Log.e(debugTag, x + " longggggg");
        });
    }

    private Completable updateLclFcmToken(String fcmToken) {
        return Completable.fromAction(() ->
                dogVipRoomDatabase
                        .userDeviceDao()
                        .updateFcmToken(false, fcmToken, android.os.Build.SERIAL));
    }

    private RetryWithDelay configureRetryWithDelayParams(int maxRetries, int retryDelayMillis) {
        retryWithDelay.setMaxRetries(maxRetries);
        retryWithDelay.setRetryDelayMillis(retryDelayMillis);
        return retryWithDelay;
    }
}
