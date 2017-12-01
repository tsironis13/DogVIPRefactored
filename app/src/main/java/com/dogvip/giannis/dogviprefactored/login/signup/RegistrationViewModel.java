package com.dogvip.giannis.dogviprefactored.login.signup;

import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.login.LoginContract;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.pojo.login.SignInUpFbGoogleRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.signup.SignUpEmailRequest;
import com.dogvip.giannis.dogviprefactored.requestmanager.LoginRequestManager;
import com.dogvip.giannis.dogviprefactored.responsecontroller.ResponseController;
import com.dogvip.giannis.dogviprefactored.responsecontroller.login.signin.SignInUpFbCommand;
import com.dogvip.giannis.dogviprefactored.responsecontroller.login.signin.SignInUpGoogleCommand;
import com.dogvip.giannis.dogviprefactored.responsecontroller.login.signup.SignUpEmailCommand;
import com.dogvip.giannis.dogviprefactored.utilities.network.NetworkUtls;
import com.dogvip.giannis.dogviprefactored.utilities.network.RetryWithDelay;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.AsyncProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by giannis on 22/5/2017.
 */

public class RegistrationViewModel implements LoginContract.SignUpViewModel {

    private static final String debugTag = RegistrationViewModel.class.getSimpleName();
    private LoginRequestManager mLoginRequestManager;
    private LoginContract.SignUpView mViewCallback;
    private AsyncProcessor<Response> mProcessor;
    private int requestState;
    private Disposable mLoginDisp, mTempDisp;
    @Inject
    ResponseController responseController;
    @Inject
    SignUpEmailCommand signUpEmailCommand;
    @Inject
    SignInUpFbCommand signInUpFbCommand;
    @Inject
    SignInUpGoogleCommand signInUpGoogleCommand;
    @Inject
    NetworkUtls networkUtls;
    @Inject
    RetryWithDelay retryWithDelay;

    @Inject
    public RegistrationViewModel(LoginRequestManager loginRequestManager) {
        this.mLoginRequestManager = loginRequestManager;
    }

    @Override
    public void onViewAttached(Lifecycle.View viewCallback) {
        this.mViewCallback = (LoginContract.SignUpView) viewCallback;
        signUpEmailCommand.setViewCallback(mViewCallback);
        signInUpFbCommand.setViewCallback(mViewCallback);
        signInUpGoogleCommand.setViewCallback(mViewCallback);
    }

    @Override
    public void onViewResumed() {
//        Log.e(debugTag, "onResume: "+mViewCallback + " request state "+requestState + " login disp: "+ mLoginDisp);
        if (requestState == AppConfig.REQUEST_RUNNING || requestState == AppConfig.REQUEST_FAILED) {
//            Log.e(debugTag, "HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE PROCESSING");
            mViewCallback.onProcessing();
        }
        if (mLoginDisp != null && requestState == AppConfig.REQUEST_RUNNING) {
//            Log.e(debugTag, "HERE: "+ requestState);
            mTempDisp = mProcessor
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new RegistrationObserver());
        }
    }

    @Override
    public void onViewDetached() {
        mViewCallback.onStopProcessing();
        mViewCallback = null;
        if (mLoginDisp != null) mLoginDisp.dispose();
        if (mTempDisp != null) mTempDisp.dispose();
        signUpEmailCommand.clearCallback();
        signInUpFbCommand.clearCallback();
        signInUpGoogleCommand.clearCallback();
    }

    @Override
    public void onProcessing() {
        mViewCallback.onProcessing();
    }

    @Override
    public void handleGoogleSignUpResult(GoogleSignInResult result, SignInUpFbGoogleRequest request) {
        if (result.isSuccess() && result.getSignInAccount() != null) {
            request.setEmail(result.getSignInAccount().getEmail());
            Completable.complete()
                    .delay(500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(() -> signUpGoogle(request))
                    .subscribe();
        } else {
            mViewCallback.onError(R.string.error);
        }
    }

    @Override
    public void handleFbSignUpResult(String email, SignInUpFbGoogleRequest request) {
        request.setEmail(email);
        Completable.complete()
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> signUpFb(request))
                .subscribe();
    }

    @Override
    public void signUpEmail(SignUpEmailRequest request) {
        if (requestState != AppConfig.REQUEST_RUNNING && requestState != AppConfig.REQUEST_FAILED) {
            responseController.setCommand(signUpEmailCommand);
            prepareRequest(getSignUpEmailFlowableRequest(request));
        }
    }

    private void signUpFb(SignInUpFbGoogleRequest request) {
        if (requestState != AppConfig.REQUEST_RUNNING && requestState != AppConfig.REQUEST_FAILED) {
            responseController.setCommand(signInUpFbCommand);
            prepareRequest(getSignUpFbGoogleRequest(request));
        }
    }

    private void signUpGoogle(SignInUpFbGoogleRequest request) {
        if (requestState != AppConfig.REQUEST_RUNNING && requestState != AppConfig.REQUEST_FAILED) {
            responseController.setCommand(signInUpGoogleCommand);
            prepareRequest(getSignUpFbGoogleRequest(request));
        }
    }

    private void prepareRequest(final Flowable<Response> responseFlowable) {
//        Log.e(debugTag, "prepare request " + mViewCallback);
        mProcessor = AsyncProcessor.create();
        mLoginDisp = mProcessor
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RegistrationObserver());

        networkUtls.getNetworkFlowable()
                .doOnSubscribe(subscription -> onProcessing())
                .flatMap(aBoolean -> responseFlowable
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .retryWhen(configureRetryWithDelayParams(3, 2000)))
                .take(1)
                .subscribeWith(mProcessor);
    }

    private Flowable<Response> getSignUpEmailFlowableRequest(SignUpEmailRequest request) {
        return mLoginRequestManager.signUpEmail(request, this);
    }

    private Flowable<Response> getSignUpFbGoogleRequest(SignInUpFbGoogleRequest request) {
        return mLoginRequestManager.signUpFbGoogle(request, this);
    }

    private RetryWithDelay configureRetryWithDelayParams(int maxRetries, int retryDelayMillis) {
        retryWithDelay.setMaxRetries(maxRetries);
        retryWithDelay.setRetryDelayMillis(retryDelayMillis);
        return retryWithDelay;
    }

    private void handleError(Throwable throwable) {
//        Log.e("ddd", throwable + " throwable "+ mViewCallback + " callbacl");
        if (throwable instanceof IllegalStateException) { //server error
            mViewCallback.onError(R.string.error);
        } else if (throwable instanceof InvalidPropertiesFormatException) {
//            mViewCallback.onError(R.string.please_fill_out_search_filters);
        } else {//no network connection error
//            Log.e(debugTag, " on error");
            mViewCallback.onError(R.string.no_internet_connection);
        }
    }

    @Override
    public void setRequestState(int requestState) {
        this.requestState = requestState;
    }

    private class RegistrationObserver extends DisposableSubscriber<Response> {

        @Override
        public void onNext(Response response) {
            mLoginDisp = null;
            requestState = AppConfig.REQUEST_SUCCEEDED;
            if (response.getCode() != AppConfig.STATUS_OK) {
                responseController.executeCommandOnError(AppConfig.getCodes().get(response.getCode()));
            } else {
                responseController.executeCommandOnSuccess(response);
            }
        }

        @Override
        public void onError(Throwable t) {
            mLoginDisp = null;
            if (mViewCallback != null) {
                handleError(t);
                requestState = AppConfig.REQUEST_NONE;
            }
        }

        @Override
        public void onComplete() {}
    }
}
