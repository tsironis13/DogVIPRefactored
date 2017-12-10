package com.dogvip.giannis.dogviprefactored.owner.form;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.pojo.account.UserAccount;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.entities.UserRole;

/**
 * Created by giannis on 9/12/2017.
 */

public interface OwnerFormContract {

    interface View extends Lifecycle.View {
        void onError(int resource);
        void onSuccessOwnerAdded();
        void onSuccessOwnerEdited();
        void onProcessing();
        void onStopProcessing();
        UserRole ownerDoesNotExist();
        void ownerExists();
    }

    interface ViewModel extends Lifecycle.ViewModel {
        void onProcessing();
        void setRequestState(int state);
        void checkOwnerExists(boolean exists, UserAccount userAccount);
        void submitForm(AwesomeValidation awesomeValidation, UserRole userRole);
    }

}
