package com.dogvip.giannis.dogviprefactored.owner.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.base.activity.BaseActivity;
import com.dogvip.giannis.dogviprefactored.databinding.ActivityOwnerprofileBinding;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;

/**
 * Created by giannis on 8/12/2017.
 */

public class OwnerProfileActivity extends BaseActivity {

    private ActivityOwnerprofileBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ownerprofile);
        setSupportActionBar(mBinding.toolbar);
    }

    @Override
    public Lifecycle.ViewModel getViewModel() {
        return null;
    }
}
