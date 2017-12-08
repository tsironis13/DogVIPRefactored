package com.dogvip.giannis.dogviprefactored.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import javax.inject.Inject;

/**
 * Created by giannis on 4/12/2017.
 */

public class DashboardRetainFragment extends Fragment {

    private DashboardViewModel mDashboardViewModel;

    @Inject
    public DashboardRetainFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    public void retainViewModel(DashboardViewModel dashboardViewModel) {
        this.mDashboardViewModel = dashboardViewModel;
    }

    public DashboardViewModel getViewModel() {
        return this.mDashboardViewModel;
    }

}
