package com.dogvip.giannis.dogviprefactored.responsecontroller.login.forgotpass;



import com.dogvip.giannis.dogviprefactored.login.LoginContract;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.responsecontroller.Command;

import javax.inject.Inject;

/**
 * Created by giannis on 1/11/2017.
 */

public class SubmitNewPasswordCommand implements Command {

    private LoginContract.ForgotPassView mViewCallback;

    @Inject
    public SubmitNewPasswordCommand() {}

    public void setViewCallback(LoginContract.ForgotPassView mViewCallback) {
        this.mViewCallback = mViewCallback;
    }

    public void clearCallback() {
        this.mViewCallback = null;
    }

    @Override
    public void executeOnSuccess(Response response) {
        mViewCallback.onSuccessNewPassChange(response);
    }

    @Override
    public void executeOnError(int resource) {
        mViewCallback.onError(resource);
    }
}
