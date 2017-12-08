package com.dogvip.giannis.dogviprefactored.dashboard;

import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.pojo.account.UserAccount;

/**
 * Created by giannis on 30/11/2017.
 */

public interface DashboardContract {

    interface View extends Lifecycle.View {
        void onError(int resource);
        void ownerExists();
        void ownerDoesNotExist();
        void onSuccess();
    }

    interface ViewModel extends Lifecycle.ViewModel {
        void onProcessing();
        void setRequestState(int state);
        void checkOwnerExists(int userId);
        void logoutUser();
        void syncFcmToken(UserAccount userAccount);
    }

}
