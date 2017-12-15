package com.dogvip.giannis.dogviprefactored.responsecontroller.owner;

import com.dogvip.giannis.dogviprefactored.login.LoginContract;
import com.dogvip.giannis.dogviprefactored.owner.form.OwnerFormContract;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.responsecontroller.Command;

import javax.inject.Inject;

/**
 * Created by giannis on 11/12/2017.
 */

public class SubmitNewOwnerCommand implements Command {

    private OwnerFormContract.View mViewCallback;

    @Inject
    public SubmitNewOwnerCommand() {}

    @Override
    public void executeOnSuccess(Response response) {
        mViewCallback.onSuccessOwnerAdded(response.getOwnerData().getData());
    }

    @Override
    public void executeOnError(int resource) {
        mViewCallback.onError(resource);
    }

    public void setViewCallback(OwnerFormContract.View viewCallback) {
        this.mViewCallback = viewCallback;
    }

    public void clearCallback() {
        this.mViewCallback = null;
    }
}
