package com.dogvip.giannis.dogviprefactored.owner.form;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Menu;

import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.base.activity.BaseActivity;
import com.dogvip.giannis.dogviprefactored.databinding.ActivityOwnerformBinding;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;

/**
 * Created by giannis on 8/12/2017.
 */

public class OwnerFormActivity extends BaseActivity {

    private ActivityOwnerformBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ownerform);
        setSupportActionBar(mBinding.incltoolbar.toolbar);
        if (getIntent().getExtras() != null) {
            setOwnerFormState(getIntent().getExtras().getBoolean(getResources().getString(R.string.owner_exists)));
        }

    }

    @Override
    public Lifecycle.ViewModel getViewModel() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_menu, menu);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                menu.findItem(R.id.submit).setVisible(false);
                menu.findItem(R.id.loading).setVisible(true);
            }
        },1500);


        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                menu.findItem(R.id.submit).setVisible(true);
                menu.findItem(R.id.loading).setVisible(false);
            }
        },3000);

        return true;
    }

    private void setOwnerFormState(boolean state) {

    }
}
