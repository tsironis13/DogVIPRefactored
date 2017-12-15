package com.dogvip.giannis.dogviprefactored.owner.form;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.dogvip.giannis.dogviprefactored.pojo.account.UserAccount;
import com.dogvip.giannis.dogviprefactored.pojo.owner.form.SubmitOwnerFormRequest;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserRole;

/**
 * Created by giannis on 9/12/2017.
 */

public interface OwnerFormContract {

    interface View extends Lifecycle.View {
        void onError(int resource);
        void onErrorRetry(int resource);
        void onSuccessOwnerAdded(UserRole userRole);
        void onSuccessOwnerFormDetails(UserRole userRole);
        void onSuccessOwnerEdited();
        UserRole ownerDoesNotExist();
        void ownerExists();
    }

    interface ViewModel extends Lifecycle.ViewModel {
//        void onProcessing();
        void setRequestState(int state);
        void onRetry();
        void checkOwnerExists(boolean exists, UserAccount userAccount);
        void fetchOwnerFormDetails(BaseRequest baseRequest, int userId);
        void submitForm(AwesomeValidation awesomeValidation, SubmitOwnerFormRequest request);
    }

}
