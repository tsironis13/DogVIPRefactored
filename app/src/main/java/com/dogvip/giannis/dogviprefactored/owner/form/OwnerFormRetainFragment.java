package com.dogvip.giannis.dogviprefactored.owner.form;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.dogvip.giannis.dogviprefactored.dashboard.DashboardViewModel;

import javax.inject.Inject;

/**
 * Created by giannis on 9/12/2017.
 */

public class OwnerFormRetainFragment extends Fragment {

    private OwnerFormViewModel mOwnerFormViewModel;

    @Inject
    public OwnerFormRetainFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    public void retainViewModel(OwnerFormViewModel ownerFormViewModel) {
        this.mOwnerFormViewModel = ownerFormViewModel;
    }

    public OwnerFormViewModel getViewModel() {
        return this.mOwnerFormViewModel;
    }

}
