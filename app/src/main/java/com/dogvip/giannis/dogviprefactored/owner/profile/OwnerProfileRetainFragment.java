package com.dogvip.giannis.dogviprefactored.owner.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

/**
 * Created by giannis on 11/12/2017.
 */

public class OwnerProfileRetainFragment extends Fragment {

    private OwnerProfileViewModel mOwnerProfileViewModel;

    @Inject
    public OwnerProfileRetainFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    public void retainViewModel(OwnerProfileViewModel ownerProfileViewModel) {
        this.mOwnerProfileViewModel = ownerProfileViewModel;
    }

    public OwnerProfileViewModel getViewModel() {
        return this.mOwnerProfileViewModel;
    }

}
