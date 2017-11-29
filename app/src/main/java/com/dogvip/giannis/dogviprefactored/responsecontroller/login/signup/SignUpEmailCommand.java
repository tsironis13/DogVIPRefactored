package com.dogvip.giannis.dogviprefactored.responsecontroller.login.signup;

import com.dogvip.giannis.dogviprefactored.login.LoginContract;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.responsecontroller.Command;

import javax.inject.Inject;

/**
 * Created by giannis on 23/10/2017.
 */

public class SignUpEmailCommand implements Command {

    private LoginContract.SignUpView mViewCallback;

    @Inject
    public SignUpEmailCommand() {}

    public void setViewCallback(LoginContract.SignUpView mViewCallback) {
        this.mViewCallback = mViewCallback;
    }

    public void clearCallback() {
        this.mViewCallback = null;
    }

    @Override
    public void executeOnSuccess(Response response) {
        mViewCallback.onSuccessEmailSignUp(response.getLogin());
    }

    @Override
    public void executeOnError(int resource) {
        mViewCallback.onError(resource);
    }

}
