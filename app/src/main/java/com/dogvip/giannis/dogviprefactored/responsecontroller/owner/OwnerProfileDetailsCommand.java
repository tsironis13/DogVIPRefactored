package com.dogvip.giannis.dogviprefactored.responsecontroller.owner;

import com.dogvip.giannis.dogviprefactored.owner.profile.OwnerProfileContract;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.responsecontroller.Command;

import javax.inject.Inject;

/**
 * Created by giannis on 11/12/2017.
 */

public class OwnerProfileDetailsCommand implements Command {

    private OwnerProfileContract.View mViewCallback;

    @Inject
    public OwnerProfileDetailsCommand() {}

    @Override
    public void executeOnSuccess(Response response) {
        mViewCallback.onSuccessFetchOwnerDetails(response.getOwnerProfileData());
    }

    @Override
    public void executeOnError(int resource) {
        mViewCallback.onErrorRetry(resource);
    }

    public void setViewCallback(OwnerProfileContract.View viewCallback) {
        this.mViewCallback = viewCallback;
    }

    public void clearCallback() {
        this.mViewCallback = null;
    }
}
