package com.dogvip.giannis.dogviprefactored.utilities.ui.alertdialogcontroller;

import com.dogvip.giannis.dogviprefactored.owner.profile.OwnerProfileContract;

import javax.inject.Inject;

/**
 * Created by giannis on 14/12/2017.
 */

public class DeleteOwnerDialogCommand implements AlertDialogCommand {

    private OwnerProfileContract.View mViewCallback;

    @Inject
    public DeleteOwnerDialogCommand() {}

    public void setViewCallback(OwnerProfileContract.View mViewCallback) {
        this.mViewCallback = mViewCallback;
    }

    public void clearCallback() {
        this.mViewCallback = null;
    }

    @Override
    public void onPositiveAction() {
        mViewCallback.onDeleteOwnerAction();
    }

    @Override
    public void onNegativeAction() {
//        mViewCallback.onDeleteOwnerCancelAction();
    }

}
