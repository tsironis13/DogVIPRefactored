package com.dogvip.giannis.dogviprefactored.owner.profile;

import android.accounts.NetworkErrorException;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.dogvip.giannis.dogviprefactored.BR;
import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.login.signin.SignInViewModel;
import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.pojo.owner.profile.DeleteOwnerRequest;
import com.dogvip.giannis.dogviprefactored.pojo.owner.profile.OwnerProfileDetailsResponse;
import com.dogvip.giannis.dogviprefactored.requestmanager.OwnerProfileRequestManager;
import com.dogvip.giannis.dogviprefactored.responsecontroller.ResponseController;
import com.dogvip.giannis.dogviprefactored.responsecontroller.owner.DeleteOwnerCommand;
import com.dogvip.giannis.dogviprefactored.responsecontroller.owner.OwnerProfileDetailsCommand;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.DogVipRoomDatabase;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserData;
import com.dogvip.giannis.dogviprefactored.utilities.errorhandling.ErrorHandler;
import com.dogvip.giannis.dogviprefactored.utilities.errorhandling.throwables.NoOnwerExistsException;
import com.dogvip.giannis.dogviprefactored.utilities.network.NetworkUtls;
import com.dogvip.giannis.dogviprefactored.utilities.errorhandling.RetryWithDelay;
import com.dogvip.giannis.dogviprefactored.utilities.ui.MyAlertDialogFragment;
import com.dogvip.giannis.dogviprefactored.utilities.ui.alertdialogcontroller.DeleteOwnerDialogCommand;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.AsyncProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by giannis on 11/12/2017.
 */

public class OwnerProfileViewModel extends BaseObservable implements OwnerProfileContract.ViewModel {
    private static final String debugTag = OwnerProfileViewModel.class.getSimpleName();
    private OwnerProfileContract.View mViewCallback;
    private OwnerProfileRequestManager mOwnerProfileRequestManager;
    private int requestState, processingType;
    private boolean isProcessing, isOnLayoutProcessing, hasError;
    private AsyncProcessor<Response> mProcessor;
    private Disposable mDisp, mTempDisp;
    private int retryCount = 0;
    private int maxRetries = 3;
    private MyAlertDialogFragment myAlertDialogFragment;
    @Inject
    ResponseController responseController;
    @Inject
    DogVipRoomDatabase dogVipRoomDatabase;
    @Inject
    OwnerProfileDetailsCommand ownerProfileDetailsCommand;
    @Inject
    DeleteOwnerCommand deleteOwnerCommand;
    @Inject
    NetworkUtls networkUtls;
    @Inject
    RetryWithDelay retryWithDelay;
    @Inject
    Response response;
    @Inject
    OwnerProfileDetailsResponse ownerProfileDetailsResponse;
    @Inject
    ErrorHandler retryHelper;
    @Inject
    DeleteOwnerDialogCommand deleteOwnerDialogCommand;

    @Inject
    public OwnerProfileViewModel(OwnerProfileRequestManager ownerProfileRequestManager) {
        this.mOwnerProfileRequestManager = ownerProfileRequestManager;
    }

    @Override
    public void onViewAttached(Lifecycle.View viewCallback) {
        this.mViewCallback = (OwnerProfileContract.View) viewCallback;
        deleteOwnerDialogCommand.setViewCallback(mViewCallback);
        deleteOwnerCommand.setViewCallback(mViewCallback);
        ownerProfileDetailsCommand.setViewCallback(mViewCallback);
    }

    @Override
    public void onViewResumed() {
        if (requestState == AppConfig.REQUEST_RUNNING || requestState == AppConfig.REQUEST_FAILED) setProcessing(true, true, processingType);
        if (mDisp != null && requestState == AppConfig.REQUEST_RUNNING) {
            mTempDisp = mProcessor
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new OwnerProfileObserver());
        }
    }

    @Override
    public void onViewDetached() {
        if (myAlertDialogFragment != null) myAlertDialogFragment = null;
        deleteOwnerDialogCommand.clearCallback();
        setProcessing(false, true, processingType);
        mViewCallback = null;
        if (mDisp != null) mDisp.dispose();
        if (mTempDisp != null) mTempDisp.dispose();
        ownerProfileDetailsCommand.clearCallback();
        deleteOwnerCommand.clearCallback();
    }

    @Override
    public void setRequestState(int state) {
        this.requestState = state;
    }

    @Override
    public void initializeAlertDialog() {
        myAlertDialogFragment = MyAlertDialogFragment.newInstance();
    }

    @Override
    public void showDeleteOwnerDialog(FragmentManager fragmentManager, String tag) {
        if (myAlertDialogFragment == null) initializeAlertDialog();
        myAlertDialogFragment.setCommand(deleteOwnerDialogCommand);
        Bundle bundle = new Bundle();
        bundle.putString("type", "delete_owner");
        myAlertDialogFragment.setArguments(bundle);
        myAlertDialogFragment.show(fragmentManager, tag);
    }

    @Override
    public void pickDialogByType(MyAlertDialogFragment dialogFragment, FragmentManager fragmentManager, String tag, String type) {
        if (type.equals("delete_owner")) {
            dialogFragment.setCommand(deleteOwnerDialogCommand);
        }
    }

    @Override
    public void setAlertDialogMsgs(String dialogTtl, String dialogMsg, String dialogNegativeText, String dialogPositiveText) {
        MyAlertDialogFragment.setDialogMsgs(dialogTtl, dialogMsg, dialogNegativeText, dialogPositiveText);
    }

    @Override
    public void deleteOwner(DeleteOwnerRequest request) {
//        Log.e(debugTag, ownerProfileDetailsResponse.getData().getId() + " id");
        if (requestState != AppConfig.REQUEST_RUNNING && requestState != AppConfig.REQUEST_FAILED) {
            responseController.setCommand(deleteOwnerCommand);
            request.setId(ownerProfileDetailsResponse.getData().getId());
            prepareDeleteOwnerRequest(request);
        }
    }

    @Override
    public void fetchOwnerDetails(BaseRequest request, int userId) {
        if (requestState != AppConfig.REQUEST_RUNNING && requestState != AppConfig.REQUEST_FAILED) {
            responseController.setCommand(ownerProfileDetailsCommand);
            prepareFetchingOwnerDetailsRequest(request, userId);
        }
    }

    private void prepareDeleteOwnerRequest(DeleteOwnerRequest request) {
        mProcessor = AsyncProcessor.create();
        mDisp = mProcessor
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new OwnerProfileObserver());

        networkUtls.getNetworkFlowable()
                                .doOnSubscribe(consumer -> requestState = AppConfig.REQUEST_RUNNING)
                                .flatMap(aBoolean -> mOwnerProfileRequestManager
                                                            .deleteOwner(request, this)
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread()))
                                .doOnSubscribe(subscription -> {
                                    processingType = AppConfig.PROCESSING_ON_LAYOUT_TYPE;
                                    setProcessing(true, false, processingType);
                                })
                                .flatMap(this::responseCodeStatusFlowable)
                                .retryWhen(repeatHandler -> repeatHandler.flatMap(throwable -> {
                                    retryHelper.setThrowable(throwable);
                                    if (++retryCount <= maxRetries) {
                                        Log.e(debugTag, "retry: "+ retryCount);
                                        retryHelper.setRetryCount(retryCount);
                                        return Flowable.just(retryHelper);
                                    }
                                    return Flowable.just(retryHelper);
                                }).flatMap(retryHelper -> retryHelper.getRetryCount() == maxRetries ? retryWhen(retryHelper.getThrowable(), false, false) : Flowable.timer(2000, TimeUnit.MILLISECONDS)))
                                .take(1)
                                .subscribeWith(mProcessor);
    }

    private void prepareFetchingOwnerDetailsRequest(BaseRequest request, int userId) {
        mProcessor = AsyncProcessor.create();
        mDisp = mProcessor
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new OwnerProfileObserver());

        Flowable.concat(
                    dogVipRoomDatabase
                                .stateEntityDao()
                                .getOwnerData(1).toFlowable()
                                .subscribeOn(Schedulers.io())
                                .filter(stateEntity -> {
                                    Log.e(debugTag, stateEntity.getUpdated_at() + " ipdate");
                                    return (System.currentTimeMillis()/1000L - stateEntity.getUpdated_at()) < 2700;//45 minutes
                                })
                                .flatMap(stateEntity -> localDBresponseCodeStatusFlowable(userId)),
                    networkUtls.getNetworkFlowable()
                                                .doOnSubscribe(consumer -> requestState = AppConfig.REQUEST_RUNNING)
                                                .flatMap(aBoolean -> mOwnerProfileRequestManager
                                                                                        .fetchOwnerProfileDetails(request, this)
                                                                                        .subscribeOn(Schedulers.io())
                                                                                        .observeOn(AndroidSchedulers.mainThread())))
                    .doOnSubscribe(subscription -> {
                        processingType = AppConfig.PROCESSING_BASE_TYPE;
                        setProcessing(true, true, processingType);
                    })
                    .flatMap(this::responseCodeStatusFlowable)
                    .retryWhen(repeatHandler -> repeatHandler.flatMap(throwable -> {
                        retryHelper.setThrowable(throwable);
                        if (++retryCount <= maxRetries) {
                            Log.e(debugTag, "retry here: "+ retryCount);
                            retryHelper.setRetryCount(retryCount);
                            return Flowable.just(retryHelper);
                        }
                        return Flowable.just(retryHelper);
                    }).flatMap(retryHelper -> {
//                        Log.e(debugTag, retryHelper.getRetryCount() + " "+ retryHelper.getThrowable() + " obj");
                        return retryHelper.getRetryCount() == maxRetries ? retryWhen(retryHelper.getThrowable(), false, true) : Flowable.timer(2000, TimeUnit.MILLISECONDS);
                    }))
                    .take(1)
                    .subscribe(mProcessor);
    }

    @Override
    public void onRetry() {
        retryWithDelay.getRetrySubject().onNext(new Object());
    }

    @Bindable
    public OwnerProfileDetailsResponse getOwnerDetails() {
        return ownerProfileDetailsResponse;
    }

    //on layout processing
    @Bindable
    public boolean isOnProcessing() {
        return isOnLayoutProcessing;
    }

    @Bindable
    public boolean isProcessing() {
        return isProcessing;
    }

    @Bindable
    public boolean isViewVisible() {
        return !isProcessing && !hasError;
    }

    public void setData(OwnerProfileDetailsResponse data) {
        this.ownerProfileDetailsResponse = data;
        notifyPropertyChanged(BR._all);
    }

    public void setProcessing(boolean processing, boolean hasError, int processingType) {
        if (processingType == AppConfig.PROCESSING_BASE_TYPE) {
            this.isProcessing = processing;
            this.hasError = hasError;
            notifyPropertyChanged(BR._all);
        } else {
            this.isOnLayoutProcessing = processing;
            notifyPropertyChanged(BR.onProcessing);
        }

    }

    private Flowable<Object> retryWhen(Throwable throwable, boolean processing, boolean hasError) {
        handleError(throwable, processing, hasError);
        retryWithDelay.setRetryButtonClicked();
        return retryWithDelay.getRetrySubject().toFlowable(BackpressureStrategy.LATEST);
    }

    private Flowable<Response> localDBresponseCodeStatusFlowable(int userId) {
        return dogVipRoomDatabase.userRoleDao().fetchOwnerDetails(userId, 1).toFlowable()
                .flatMap(userRole -> {
                    ownerProfileDetailsResponse.setData(userRole);
                    return dogVipRoomDatabase.petDao().fetchOwnerPets(userRole.getId()).toFlowable().flatMap(pets -> {
                        ownerProfileDetailsResponse.setOwnerPets(pets);
                        response.setOwnerProfileData(ownerProfileDetailsResponse);
                        response.setCode(AppConfig.STATUS_OK);
                        return Flowable.just(response);
                    });
                });
    }

    private Flowable<Response> responseCodeStatusFlowable(final Response response) {
        Log.e(debugTag, "responseCodeStatusFlowable");
        return Flowable.create(e -> {
            if (response.getCode() != AppConfig.STATUS_OK) {
//                Log.e(debugTag, response.getCode() +  " r code");
                e.onError(AppConfig.getErrorThrowables().get(response.getCode()));
            } else {
                retryCount = 0;
//                Log.e(debugTag, response +  " response data");
                e.onNext(response);

            }
        }, BackpressureStrategy.LATEST);
    }

//    private RetryWithDelay configureRetryWithDelayParams(int maxRetries, int retryDelayMillis) {
////        retryWithDelay.setRetryButtonClicked();
//        retryWithDelay.setMaxRetries(maxRetries);
//        retryWithDelay.setRetryDelayMillis(retryDelayMillis);
//        return retryWithDelay;
//    }

    private void handleError(Throwable throwable, boolean processing, boolean hasError) {
        Log.e(debugTag, throwable + " EXCEPTION");
        retryCount = 0;
        mDisp = null;
        setProcessing(processing, hasError, processingType);
        requestState = AppConfig.REQUEST_NONE;
        if (mViewCallback != null) {
            //no network connection error
            if (throwable instanceof NetworkErrorException) {
                mViewCallback.onErrorRetry(R.string.no_internet_connection);
            } else if (throwable instanceof NoOnwerExistsException){
                mViewCallback.onErrorRetry(R.string.no_owner_exists);
            } else {
                mViewCallback.onErrorRetry(R.string.error);
            }
        }
    }

    private class OwnerProfileObserver extends DisposableSubscriber<Response> {

        @Override
        public void onNext(Response response) {
            Log.e(debugTag, "ON NEXT OBSERVER: "+response.getCode() +  " CODESS <-.>");
            mDisp = null;
            requestState = AppConfig.REQUEST_SUCCEEDED;
            setProcessing(false, false, processingType);
            responseController.executeCommandOnSuccess(response);
//            if (response.getCode() != AppConfig.STATUS_OK) {
//                setProcessing(false, true);
//                responseController.executeCommandOnError(AppConfig.getCodes().get(response.getCode()));
//            } else {
//                setProcessing(false, false);
//                responseController.executeCommandOnSuccess(response);
//            }
        }

        @Override
        public void onError(Throwable t) {
            Log.e(debugTag, "ON ERROR OBSERVER");
//            mDisp = null;
//            setProcessing(false, true);
//            if (mViewCallback != null) {
//                handleError(t);
//                requestState = AppConfig.REQUEST_NONE;
//            }
        }

        @Override
        public void onComplete() {}
    }
}
