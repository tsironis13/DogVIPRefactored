package com.dogvip.giannis.dogviprefactored.responsecontroller.login.signin;

import com.dogvip.giannis.dogviprefactored.login.LoginContract;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.responsecontroller.Command;

import javax.inject.Inject;

/**
 * Created by giannis on 23/10/2017.
 */

public class SignInUpGoogleCommand implements Command {

    private LoginContract.SignInUpFbGoogleView mViewCallback;

    @Inject
    public SignInUpGoogleCommand() {}

    public void setViewCallback(LoginContract.SignInUpFbGoogleView mViewCallback) {
        this.mViewCallback = mViewCallback;
    }

    public void clearCallback() {
        this.mViewCallback = null;
    }

    @Override
    public void executeOnSuccess(Response response) {
        mViewCallback.onSuccessGoogleLogin(response.getLogin());
    }

    @Override
    public void executeOnError(int resource) {
        mViewCallback.onErrorGoogleLogin(resource);
    }

}
