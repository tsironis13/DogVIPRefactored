package com.dogvip.giannis.dogviprefactored.owner.form;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.dogvip.giannis.dogviprefactored.BR;
import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.dashboard.DashboardContract;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.pojo.account.UserAccount;
import com.dogvip.giannis.dogviprefactored.requestmanager.OwnerFormRequestManager;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.DogVipRoomDatabase;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.entities.UserRole;
import com.dogvip.giannis.dogviprefactored.utilities.network.RetryWithDelay;

import javax.inject.Inject;

import io.reactivex.processors.AsyncProcessor;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by giannis on 9/12/2017.
 */

public class OwnerFormViewModel extends BaseObservable implements OwnerFormContract.ViewModel {
    private static final String debugTag = OwnerFormViewModel.class.getSimpleName();
    private OwnerFormContract.View mViewCallback;
    private OwnerFormRequestManager mOwnerFormRequestManager;
    private AsyncProcessor<Response> mProcessor;
    private boolean isProcessing;
    private int requestState;
    private UserRole userRole;
    @Inject
    DogVipRoomDatabase dogVipRoomDatabase;
    @Inject
    RetryWithDelay retryWithDelay;

    @Inject
    public OwnerFormViewModel(OwnerFormRequestManager ownerFormRequestManager) {
        this.mOwnerFormRequestManager = ownerFormRequestManager;
    }

    @Override
    public void onViewAttached(Lifecycle.View viewCallback) {
        this.mViewCallback = (OwnerFormContract.View) viewCallback;
    }

    @Override
    public void onViewResumed() {
        if (requestState == AppConfig.REQUEST_RUNNING || requestState == AppConfig.REQUEST_FAILED) mViewCallback.onProcessing();
    }

    @Override
    public void onViewDetached() {
        mViewCallback.onStopProcessing();
        mViewCallback = null;
    }

    @Override
    public void onProcessing() {
        mViewCallback.onProcessing();
    }

    @Override
    public void setRequestState(int state) {
        this.requestState = state;
    }

    @Override
    public void checkOwnerExists(boolean exists, UserAccount userAccount) {
        if (!exists) {
//            mViewCallback.ownerDoesNotExist();
//            userRole.setId(1);
//            userRole.setName("name");
//            userRole.setSurname("surname");
//            setProcessing(true);
            setData(mViewCallback.ownerDoesNotExist());
            Log.e(debugTag, "create owner");
        } else {
            mViewCallback.onProcessing();
            mViewCallback.ownerExists();
            Log.e(debugTag, "edit owner");
        }
    }

//    @Override
//    public void fetchOwnerData(int userId) {
//        Log.e(debugTag, userId + " userId");
//        if (requestState != AppConfig.REQUEST_RUNNING && requestState != AppConfig.REQUEST_FAILED) {
////            responseController.setCommand(signInEmailCommand);
////            prepareRequest(getSignInEmailFlowableRequest(request));
//        }
//    }


    @Override
    public void submitForm(AwesomeValidation awesomeValidation, UserRole userRole111) {
        if (awesomeValidation.validate()) {
//            setData(userRole);
            Log.e(debugTag, userRole.getName() + " NAME");
            Log.e(debugTag, userRole.getSurname() + " SURNAME");
            Log.e(debugTag, userRole.getAge() + " AGE");
            Log.e(debugTag, userRole.getCity() + " CITY");
            Log.e(debugTag, userRole.getMobile_number() + " PHONE");
        }
    }

    @Bindable
    public UserRole getData() {
        return userRole;
    }

    @Bindable
    public boolean isProcessing() {
        return isProcessing;
    }

    @Bindable
    public boolean isDeleteImageIconVisible() {
        return userRole != null && userRole.getId() != 0 && !userRole.getImage_url().isEmpty();
    }

    private void setProcessing(boolean processing) {
        this.isProcessing = processing;
        notifyPropertyChanged(BR.processing);
    }

    private void setData(UserRole userRole) {
        this.userRole = userRole;
        notifyPropertyChanged(BR.data);
    }

    private RetryWithDelay configureRetryWithDelayParams(int maxRetries, int retryDelayMillis) {
        retryWithDelay.setMaxRetries(maxRetries);
        retryWithDelay.setRetryDelayMillis(retryDelayMillis);
        return retryWithDelay;
    }

    private class OwnerFormObserver extends DisposableSubscriber<Response> {

        @Override
        public void onNext(Response response) {
//            mLoginDisp = null;
            requestState = AppConfig.REQUEST_SUCCEEDED;
            if (response.getCode() != AppConfig.STATUS_OK) {
//                responseController.executeCommandOnError(AppConfig.getCodes().get(response.getCode()));
            } else {
//                responseController.executeCommandOnSuccess(response);
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
