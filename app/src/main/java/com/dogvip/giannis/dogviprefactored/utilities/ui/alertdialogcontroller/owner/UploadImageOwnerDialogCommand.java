package com.dogvip.giannis.dogviprefactored.utilities.ui.alertdialogcontroller.owner;

import com.dogvip.giannis.dogviprefactored.owner.form.OwnerFormContract;
import com.dogvip.giannis.dogviprefactored.utilities.ui.alertdialogcontroller.AlertDialogCommand;

import javax.inject.Inject;

/**
 * Created by giannis on 17/12/2017.
 */

public class UploadImageOwnerDialogCommand implements AlertDialogCommand {

    private OwnerFormContract.View mViewCallback;

    @Inject
    public UploadImageOwnerDialogCommand() {}

    public void setViewCallback(OwnerFormContract.View mViewCallback) {
        this.mViewCallback = mViewCallback;
    }

    public void clearCallback() {
        this.mViewCallback = null;
    }

    @Override
    public void onPositiveAction() {
        mViewCallback.onCameraUploadAction();
    }

    @Override
    public void onNegativeAction() { mViewCallback.onGalleryUploadAction(); }
    
}
