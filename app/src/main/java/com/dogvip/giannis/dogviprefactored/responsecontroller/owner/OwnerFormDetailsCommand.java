package com.dogvip.giannis.dogviprefactored.responsecontroller.owner;

import android.util.Log;

import com.dogvip.giannis.dogviprefactored.owner.form.OwnerFormContract;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.responsecontroller.Command;

import javax.inject.Inject;

/**
 * Created by giannis on 15/12/2017.
 */

public class OwnerFormDetailsCommand implements Command {

    private OwnerFormContract.View mViewCallback;

    @Inject
    public OwnerFormDetailsCommand() {}

    @Override
    public void executeOnSuccess(Response response) {
        mViewCallback.onSuccessOwnerFormDetails(response.getOwnerData().getData());
    }

    @Override
    public void executeOnError(int resource) {
        mViewCallback.onErrorRetry(resource);
    }

    public void setViewCallback(OwnerFormContract.View viewCallback) {
        this.mViewCallback = viewCallback;
    }

    public void clearCallback() {
        this.mViewCallback = null;
    }

}
