package com.dogvip.giannis.dogviprefactored.owner.profile;

import android.support.v4.app.FragmentManager;

import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.dogvip.giannis.dogviprefactored.pojo.owner.profile.DeleteOwnerRequest;
import com.dogvip.giannis.dogviprefactored.pojo.owner.profile.OwnerProfileDetailsResponse;
import com.dogvip.giannis.dogviprefactored.utilities.ui.MyAlertDialogFragment;

/**
 * Created by giannis on 11/12/2017.
 */

public interface OwnerProfileContract {

    interface View extends Lifecycle.View {
        void onErrorRetry(int resource);
        void onSuccessFetchOwnerDetails(OwnerProfileDetailsResponse data);
        void onSuccessDeleteOwner();
        void onDeleteOwnerAction();
//        void onDeleteOwnerCancelAction();
    }

    interface ViewModel extends Lifecycle.ViewModel {
//        void onProcessing();
        void onRetry();
        void setRequestState(int state);
        void initializeAlertDialog();
        void pickDialogByType(MyAlertDialogFragment dialogFragment, FragmentManager fragmentManager, String tag, String type);
        void showDeleteOwnerDialog(FragmentManager fragmentManager, String tag);
        void setAlertDialogMsgs(String dialogTtl, String dialogMsg, String dialogNegativeText, String dialogPositiveText);
        void fetchOwnerDetails(BaseRequest request, int userId);
        void deleteOwner(DeleteOwnerRequest request);
    }

}
