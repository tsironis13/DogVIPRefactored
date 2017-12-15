package com.dogvip.giannis.dogviprefactored.dashboard;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.pojo.account.UserAccount;
import com.dogvip.giannis.dogviprefactored.utilities.ui.MyAlertDialogFragment;

/**
 * Created by giannis on 30/11/2017.
 */

public interface DashboardContract {

    interface View extends Lifecycle.View {
        void onError(int resource);
        void ownerExists();
        void ownerDoesNotExist();
        void onSuccess();
        void onLogoutAction();
        void onLogoutCancelAction();
    }

    interface ViewModel extends Lifecycle.ViewModel {
        void onProcessing();
        void setRequestState(int state);
        void initializeAlertDialog();
        void pickDialogByType(MyAlertDialogFragment dialogFragment, FragmentManager fragmentManager, String tag, String type);
        void showLogoutDialog(FragmentManager fragmentManager, String tag);
        void setAlertDialogMsgs(String dialogTtl, String dialogMsg, String dialogNegativeText, String dialogPositiveText);
        void checkOwnerExists(int userId);
        void logoutUser();
        void syncFcmToken(UserAccount userAccount);
    }

}
