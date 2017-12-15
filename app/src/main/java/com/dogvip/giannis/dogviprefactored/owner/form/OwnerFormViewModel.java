package com.dogvip.giannis.dogviprefactored.owner.form;

import android.accounts.NetworkErrorException;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.dogvip.giannis.dogviprefactored.BR;
import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.owner.profile.OwnerProfileViewModel;
import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.pojo.account.UserAccount;
import com.dogvip.giannis.dogviprefactored.pojo.owner.form.SubmitOwnerFormRequest;
import com.dogvip.giannis.dogviprefactored.pojo.owner.form.SubmitOwnerFormResponse;
import com.dogvip.giannis.dogviprefactored.requestmanager.OwnerFormRequestManager;
import com.dogvip.giannis.dogviprefactored.responsecontroller.ResponseController;
import com.dogvip.giannis.dogviprefactored.responsecontroller.owner.EditOwnerCommand;
import com.dogvip.giannis.dogviprefactored.responsecontroller.owner.OwnerFormDetailsCommand;
import com.dogvip.giannis.dogviprefactored.responsecontroller.owner.SubmitNewOwnerCommand;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.DogVipRoomDatabase;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserRole;
import com.dogvip.giannis.dogviprefactored.utilities.errorhandling.ErrorHandler;
import com.dogvip.giannis.dogviprefactored.utilities.errorhandling.throwables.NoOnwerExistsException;
import com.dogvip.giannis.dogviprefactored.utilities.network.NetworkUtls;
import com.dogvip.giannis.dogviprefactored.utilities.errorhandling.RetryWithDelay;

import java.util.Arrays;
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
    private Disposable mDisp, mTempDisp;
    private int retryCount = 0;
    private int maxRetries = 3;
    @Inject
    ResponseController responseController;
    @Inject
    SubmitNewOwnerCommand submitNewOwnerCommand;
    @Inject
    EditOwnerCommand editOwnerCommand;
    @Inject
    OwnerFormDetailsCommand ownerFormDetailsCommand;
    @Inject
    DogVipRoomDatabase dogVipRoomDatabase;
    @Inject
    RetryWithDelay retryWithDelay;
    @Inject
    NetworkUtls networkUtls;
    @Inject
    Response response;
    @Inject
    SubmitOwnerFormResponse ownerFormResponse;
    @Inject
    ErrorHandler retryHelper;

    @Inject
    public OwnerFormViewModel(OwnerFormRequestManager ownerFormRequestManager) {
        this.mOwnerFormRequestManager = ownerFormRequestManager;
    }

    @Override
    public void onViewAttached(Lifecycle.View viewCallback) {
        this.mViewCallback = (OwnerFormContract.View) viewCallback;
        submitNewOwnerCommand.setViewCallback(mViewCallback);
        editOwnerCommand.setViewCallback(mViewCallback);
        ownerFormDetailsCommand.setViewCallback(mViewCallback);
    }

    @Override
    public void onViewResumed() {
        if (requestState == AppConfig.REQUEST_RUNNING || requestState == AppConfig.REQUEST_FAILED) setProcessing(true);
        if (mDisp != null && requestState == AppConfig.REQUEST_RUNNING) {
            mTempDisp = mProcessor
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new OwnerFormObserver());
        }
    }

    @Override
    public void onViewDetached() {
        setProcessing(false);
        mViewCallback = null;
        if (mDisp != null) mDisp.dispose();
        if (mTempDisp != null) mTempDisp.dispose();
        submitNewOwnerCommand.clearCallback();
        editOwnerCommand.clearCallback();
        ownerFormDetailsCommand.clearCallback();
    }

//    @Override
//    public void onProcessing() {
//        mViewCallback.onProcessing();
//    }

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
            mViewCallback.ownerExists();
        }
    }

    @Override
    public void submitForm(AwesomeValidation awesomeValidation, SubmitOwnerFormRequest request) {
        if (awesomeValidation.validate()) {
            if (!Arrays.asList(AppConfig.cities).contains(userRole.getCity())) {
                mViewCallback.onError(AppConfig.getCodes().get(AppConfig.ERROR_CITY_NO_MATCH));
            } else {
//                Log.e(debugTag, exists + " exists");
                request.setUserRole(userRole);
                if (userRole.getId() == 0) {
                    if (requestState != AppConfig.REQUEST_RUNNING && requestState != AppConfig.REQUEST_FAILED) {
                        responseController.setCommand(submitNewOwnerCommand);
                        request.setAction("add_owner");
                        prepareRequest(getSubmitNewOwnerFlowableRequest(request));
                    }
                } else {
                    if (requestState != AppConfig.REQUEST_RUNNING && requestState != AppConfig.REQUEST_FAILED) {
                        responseController.setCommand(editOwnerCommand);
                        request.setAction("edit_owner");
                        Log.e(debugTag, "edit owner");
                        prepareRequest(getEditOwnerFlowableRequest(request));
                    }
                }
            }
//            Log.e(debugTag, userRole.getName() + " NAME");
//            Log.e(debugTag, userRole.getSurname() + " SURNAME");
//            Log.e(debugTag, userRole.getAge() + " AGE");
//            Log.e(debugTag, userRole.getCity() + " CITY");
//            Log.e(debugTag, userRole.getMobile_number() + " PHONE");
        }
    }

    @Override
    public void fetchOwnerFormDetails(BaseRequest baseRequest, int userId) {
        if (requestState != AppConfig.REQUEST_RUNNING && requestState != AppConfig.REQUEST_FAILED) {
            Log.e(debugTag, "fetchOwnerFormDetail");
            responseController.setCommand(ownerFormDetailsCommand);
            prepareFetchingOwnerFormDetailsRequest(baseRequest, userId);
        }
    }

    private void prepareRequest(final Flowable<Response> responseFlowable) {
        Log.e(debugTag, "prepare request ");
        mProcessor = AsyncProcessor.create();
        mDisp = mProcessor
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new OwnerFormObserver());

        networkUtls.getNetworkFlowable()
                                .doOnSubscribe(subscription -> setProcessing(true))
                                .flatMap(aBoolean -> responseFlowable
                                                                .subscribeOn(Schedulers.io())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .retryWhen(configureRetryWithDelayParams(3, 2000)))
                                .take(1)
                                .subscribeWith(mProcessor);
    }

    private void prepareFetchingOwnerFormDetailsRequest(BaseRequest request, int userId) {
        mProcessor = AsyncProcessor.create();
        mDisp = mProcessor
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new OwnerFormObserver());

        Flowable.concat(
                    dogVipRoomDatabase
                                .stateEntityDao()
                                .getOwnerData(1).toFlowable()
                                .subscribeOn(Schedulers.io())
                                .filter(stateEntity -> {
//                                    Log.e(debugTag, stateEntity.getUpdated_at() + " ipdate");
                                    return (System.currentTimeMillis()/1000L - stateEntity.getUpdated_at()) < 2700;//45 minutes
                                })
                                .flatMap(stateEntity -> localDBresponseCodeStatusFlowable(userId)),
                    networkUtls.getNetworkFlowable()
                                                .doOnSubscribe(consumer -> requestState = AppConfig.REQUEST_RUNNING)
                                                .flatMap(aBoolean -> mOwnerFormRequestManager
                                                                                        .fetchOwnerFormDetails(request, this)
                                                                                        .subscribeOn(Schedulers.io())
                                                                                        .observeOn(AndroidSchedulers.mainThread())))
                    .doOnSubscribe(subscription -> setProcessing(true))
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
//                            Log.e(debugTag, retryHelper.getRetryCount() + " "+ retryHelper.getThrowable() + " obj");
                            return retryHelper.getRetryCount() == maxRetries ? retryWhen(retryHelper.getThrowable(), false, true) : Flowable.timer(2000, TimeUnit.MILLISECONDS);
                    }))
                    .take(1)
                    .subscribe(mProcessor);
    }

    private Flowable<Response> getSubmitNewOwnerFlowableRequest(SubmitOwnerFormRequest request) {
        return mOwnerFormRequestManager.submitNewOwnerForm(request, this);
    }

    private Flowable<Response> getEditOwnerFlowableRequest(SubmitOwnerFormRequest request) {
        return mOwnerFormRequestManager.editOwnerForm(request, this);
    }

    @Override
    public void onRetry() {
        retryWithDelay.getRetrySubject().onNext(new Object());
    }

    @Bindable
    public boolean isImageUploadViewComponentsVisible() {
//        if (userRole != null) Log.e(debugTag, "HERE: "+userRole + " "+userRole.getId());
        return userRole != null && userRole.getId() != 0;
    }

    public int isDeleteImageIconVisible() {
        if (userRole != null && userRole.getId() != 0 && userRole.getImage_url() != null) {
            return View.VISIBLE;
        } else {
            return View.GONE;
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

    private void setProcessing(boolean isProcessing) {
        this.isProcessing = isProcessing;
        notifyPropertyChanged(BR.processing);
    }

    public void setData(UserRole userRole) {
        this.userRole = userRole;
        notifyPropertyChanged(BR._all);
    }

    private RetryWithDelay configureRetryWithDelayParams(int maxRetries, int retryDelayMillis) {
        retryWithDelay.setMaxRetries(maxRetries);
        retryWithDelay.setRetryDelayMillis(retryDelayMillis);
        return retryWithDelay;
    }

    private Flowable<Response> localDBresponseCodeStatusFlowable(int userId) {
        return dogVipRoomDatabase.userRoleDao().fetchOwnerDetails(userId, 1).toFlowable()
                                                                                    .flatMap(userRole -> {
                                                                                                    ownerFormResponse.setData(userRole);
                                                                                                    response.setOwnerData(ownerFormResponse);
                                                                                                    response.setCode(AppConfig.STATUS_OK);
                                                                                                    return Flowable.just(response);
                                                                                    });
    }

    private Flowable<Response> responseCodeStatusFlowable(final Response response) {
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

    private void handleError(Throwable throwable) {
        Log.e(debugTag, "handleError");
        setProcessing(false);
        if (mViewCallback != null) {
            //no network connection error
            if (throwable instanceof NetworkErrorException) {
                mViewCallback.onError(R.string.no_internet_connection);
            } else {
                mViewCallback.onError(R.string.error);
            }
        }
    }

    private void handleErrorRetry(Throwable throwable) {
        Log.e(debugTag, "handleErrorRetry: " + throwable);
        retryCount = 0;
        mDisp = null;
        setProcessing(false);
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

    private Flowable<Object> retryWhen(Throwable throwable, boolean processing, boolean hasError) {
        Log.e(debugTag, "retry WHEN: ");
        handleErrorRetry(throwable);
        retryWithDelay.setRetryButtonClicked();
        return retryWithDelay.getRetrySubject().toFlowable(BackpressureStrategy.LATEST);
    }

    private class OwnerFormObserver extends DisposableSubscriber<Response> {

        @Override
        public void onNext(Response response) {
            mDisp = null;
            requestState = AppConfig.REQUEST_SUCCEEDED;
            setProcessing(false);
            if (response.getCode() != AppConfig.STATUS_OK) {
                responseController.executeCommandOnError(AppConfig.getCodes().get(response.getCode()));
            } else {
                responseController.executeCommandOnSuccess(response);
            }
        }

        @Override
        public void onError(Throwable t) {
            Log.e(debugTag, " ON ERROR DOWN HERE");
            mDisp = null;
            if (mViewCallback != null) {
                handleError(t);
                requestState = AppConfig.REQUEST_NONE;
            }
        }

        @Override
        public void onComplete() {}
    }
}
