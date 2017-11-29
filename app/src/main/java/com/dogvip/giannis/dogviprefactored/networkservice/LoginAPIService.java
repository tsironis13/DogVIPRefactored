package com.dogvip.giannis.dogviprefactored.networkservice;

import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.login.forgotpass.ForgotPaswrdViewModel;
import com.dogvip.giannis.dogviprefactored.login.signin.SignInViewModel;
import com.dogvip.giannis.dogviprefactored.login.signup.RegistrationViewModel;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.pojo.login.SignInEmailRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.SignInUpFbGoogleRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.forgotpass.ForgotPassRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.signup.SignUpEmailRequest;
import com.dogvip.giannis.dogviprefactored.retrofit.ServiceAPI;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by giannis on 28/5/2017.
 */

public class LoginAPIService {

    private ServiceAPI mServiceAPI;

    @Inject
    public LoginAPIService(ServiceAPI serviceAPI) {
        this.mServiceAPI = serviceAPI;
    }

    public Flowable<Response> signInEmail(final SignInEmailRequest request, final SignInViewModel viewModel) {
        return mServiceAPI.signInEmail(request)
                .doOnSubscribe(subscription -> viewModel.setRequestState(AppConfig.REQUEST_RUNNING))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Response> signUpEmail(SignUpEmailRequest request, final RegistrationViewModel loginViewModel) {
        return mServiceAPI.signUpEmail(request)
                .doOnSubscribe(subscription -> loginViewModel.setRequestState(AppConfig.REQUEST_RUNNING))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Response> signInFbGoogle(SignInUpFbGoogleRequest request, final SignInViewModel viewModel) {
        return mServiceAPI.signInUpFbGoogle(request)
                .doOnSubscribe(subscription -> {
                    viewModel.setRequestState(AppConfig.REQUEST_RUNNING);
//                    Log.e("REQUEST RUNNING", "RUNNING");
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Response> signUpFbGoogle(SignInUpFbGoogleRequest request, final RegistrationViewModel viewModel) {
        return mServiceAPI.signInUpFbGoogle(request)
                .doOnSubscribe(subscription -> viewModel.setRequestState(AppConfig.REQUEST_RUNNING))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Response> forgotPass(ForgotPassRequest request, final ForgotPaswrdViewModel viewModel) {
        return mServiceAPI.forgotPass(request)
                .doOnSubscribe(subscription -> viewModel.setRequestState(AppConfig.REQUEST_RUNNING))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
