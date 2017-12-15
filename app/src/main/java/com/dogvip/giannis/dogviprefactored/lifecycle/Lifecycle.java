package com.dogvip.giannis.dogviprefactored.lifecycle;

/**
 * Created by giannis on 4/11/2017.
 */

public interface Lifecycle {

    interface View {}

    interface ViewModel {
        void onViewAttached(View viewCallback);
        void onViewResumed();
        void onViewDetached();
    }

    interface MyDateDialogCallbackContract {
        void onDisplayDateSet(String displayDate);
        void onDateSet(long date);
        void onInvalidDateSet();
    }

}
