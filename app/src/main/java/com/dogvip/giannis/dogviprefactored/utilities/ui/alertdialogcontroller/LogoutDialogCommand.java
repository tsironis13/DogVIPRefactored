package com.dogvip.giannis.dogviprefactored.utilities.ui.alertdialogcontroller;

import com.dogvip.giannis.dogviprefactored.dashboard.DashboardContract;

import java.io.Serializable;

import javax.inject.Inject;

/**
 * Created by giannis on 14/12/2017.
 */

public class LogoutDialogCommand implements AlertDialogCommand {

    private DashboardContract.View mViewCallback;

    @Inject
    public LogoutDialogCommand() {}

    public void setViewCallback(DashboardContract.View mViewCallback) {
        this.mViewCallback = mViewCallback;
    }

    public void clearCallback() {
        this.mViewCallback = null;
    }

    @Override
    public void onPositiveAction() {
        mViewCallback.onLogoutAction();
    }

    @Override
    public void onNegativeAction() {
        mViewCallback.onLogoutCancelAction();
    }
}
