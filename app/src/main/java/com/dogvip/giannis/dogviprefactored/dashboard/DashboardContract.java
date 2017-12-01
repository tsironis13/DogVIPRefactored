package com.dogvip.giannis.dogviprefactored.dashboard;

import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;

/**
 * Created by giannis on 30/11/2017.
 */

public interface DashboardContract {

    interface View extends Lifecycle.View {
        void onError(int resource);
    }

}
