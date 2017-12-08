package com.dogvip.giannis.dogviprefactored.dashboard;

import android.arch.persistence.room.EmptyResultSetException;
import android.util.Log;
import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.pojo.account.UserAccount;
import com.dogvip.giannis.dogviprefactored.requestmanager.DashboardRequestManager;
import com.dogvip.giannis.dogviprefactored.responsecontroller.ResponseController;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.DogVipRoomDatabase;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.entities.UserDevice;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.entities.UserRole;
import com.dogvip.giannis.dogviprefactored.services.JobConfiguration;
import com.dogvip.giannis.dogviprefactored.utilities.network.RetryWithDelay;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by giannis on 4/12/2017.
 */

public class DashboardViewModel implements DashboardContract.ViewModel {
    private static final String debugTag = DashboardViewModel.class.getSimpleName();
    private DashboardRequestManager mDashboardRequestManager;
    private DashboardContract.View mViewCallback;
    private int requestState;
    @Inject
    DogVipRoomDatabase dogVipRoomDatabase;
    @Inject
    UserDevice userDevice;
    @Inject
    ResponseController responseController;
    @Inject
    RetryWithDelay retryWithDelay;
    @Inject
    JobConfiguration mJobConfiguration;


    @Inject
    public DashboardViewModel(DashboardRequestManager dashboardRequestManager) {
        this.mDashboardRequestManager = dashboardRequestManager;
    }

    @Override
    public void onViewAttached(Lifecycle.View viewCallback) {
        this.mViewCallback = (DashboardContract.View) viewCallback;
    }

    @Override
    public void onViewResumed() {}

    @Override
    public void onViewDetached() {
        mViewCallback = null;
    }

    @Override
    public void onProcessing() {

    }

    @Override
    public void setRequestState(int state) {
        this.requestState = state;
    }

    @Override
    public void checkOwnerExists(int userId) {
        dogVipRoomDatabase
                    .userRoleDao()
                    .checkOwnerExists(userId)
                    .subscribeOn(Schedulers.io())
                    .retryWhen(configureRetryWithDelayParams(3, 250))
                    .subscribe(userRole -> {
                        mViewCallback.ownerExists();
                    }, throwable -> {
                        Log.e(debugTag, "no owner ");
                        if (throwable instanceof EmptyResultSetException) mViewCallback.ownerDoesNotExist();
                    });
    }

    @Override
    public void logoutUser() {
        mJobConfiguration.cancelJobByTag("sync_local_user_roles_pets");
        clearLocalUserData().subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void syncFcmToken(UserAccount userAccount) {
        dogVipRoomDatabase
                    .userDeviceDao()
                    .getDeviceDetails(android.os.Build.SERIAL)
                    .subscribeOn(Schedulers.io())
                    .retryWhen(configureRetryWithDelayParams(3, 2000))
                    .subscribe(userDevice -> {
                        if (userDevice.getFcm_registration_token() != null && !userDevice.isToken_synced() && userAccount.getUserId() != 0) {
                            Log.e(debugTag, "token is not synced and user exists: " + userDevice.getFcm_registration_token());
                            Log.e(debugTag, "PRIMARY KEY: " + userDevice.getId());

//                            userDevice.setToken_synced(false);
//                            userDevice.setUser_id(userAccount.getUserId());
                            updateLclFcmToken(userAccount.getUserId())
                                        .subscribeOn(Schedulers.io())
                                        .retryWhen(configureRetryWithDelayParams(3, 2000))
                                        .subscribe(() -> {
                                                    Log.e(debugTag, "update success");
                                                    mJobConfiguration.syncFcmTokenJob(userAccount.getToken(), userDevice.getFcm_registration_token());
                                                },
                                                onError -> Log.e(debugTag, "Unable to update ...", onError));
                        } else {
                            Log.e(debugTag, "eventually do nothing: ");
                        }
                    }, throwable -> {
                        if (throwable instanceof EmptyResultSetException) {
                            userDevice.setUser_id(userAccount.getUserId());
                            userDevice.setDevice_model(android.os.Build.SERIAL);
                            userDevice.setToken_synced(false);
                            insertLocalFcmToken(userDevice)
                                    .retryWhen(configureRetryWithDelayParams(3, 2000))
                                    .subscribe(() -> {
                                        Log.e(debugTag, "insert success: ");
                                    },
                                    onError -> Log.e(debugTag, "Unable to insert ...", onError));
                        }
                    });
    }

    private Completable insertLocalFcmToken(UserDevice userDevice) {
        return Completable.fromAction(() -> {
            dogVipRoomDatabase
                            .userDeviceDao()
                            .insertUserDevice(userDevice);
        });
    }

    private Completable updateLclFcmToken(int userId) {
        Log.e(debugTag, "userIDDDD: "+userId);
        return Completable.fromAction(() -> {
            dogVipRoomDatabase
                            .userDeviceDao()
                            .updateUserId(false, userId, android.os.Build.SERIAL);
//            Log.e(debugTag, "updated in view model: "+ x);
        });
    }

    private Completable clearLocalUserData() {
        return Completable.fromAction(() -> {
                        dogVipRoomDatabase.stateEntityDao().deleteUserState(1);
                        dogVipRoomDatabase.userRoleDao().deleteUserRoleData();
//            Log.e(debugTag, x + " longggggg");
        });
    }

    private RetryWithDelay configureRetryWithDelayParams(int maxRetries, int retryDelayMillis) {
        retryWithDelay.setMaxRetries(maxRetries);
        retryWithDelay.setRetryDelayMillis(retryDelayMillis);
        return retryWithDelay;
    }

    private class DashboardObserver extends DisposableSubscriber<Response> {

        @Override
        public void onNext(Response response) {
//            mLoginDisp = null;
            requestState = AppConfig.REQUEST_SUCCEEDED;
            if (response.getCode() != AppConfig.STATUS_OK) {
                responseController.executeCommandOnError(AppConfig.getCodes().get(response.getCode()));
            } else {
                responseController.executeCommandOnSuccess(response);
            }
        }

        @Override
        public void onError(Throwable t) {
//            mLoginDisp = null;
            if (mViewCallback != null) {
//                handleError(t);
                requestState = AppConfig.REQUEST_NONE;
            }
        }

        @Override
        public void onComplete() {}
    }
}
