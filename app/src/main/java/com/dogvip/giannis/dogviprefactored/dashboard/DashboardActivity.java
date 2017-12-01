package com.dogvip.giannis.dogviprefactored.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.accountmanager.MyAccountManager;
import com.dogvip.giannis.dogviprefactored.base.activity.BaseActivity;
import com.dogvip.giannis.dogviprefactored.databinding.ActivityDashboardBinding;
import com.dogvip.giannis.dogviprefactored.databinding.NavigationHeaderBinding;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.login.LoginActivity;
import com.dogvip.giannis.dogviprefactored.utilities.ui.MyAlertDialogFragment;
import com.dogvip.giannis.dogviprefactored.utilities.ui.UIUtls;
import javax.inject.Inject;
import dagger.android.AndroidInjection;

/**
 * Created by giannis on 30/11/2017.
 */

public class DashboardActivity extends BaseActivity implements DashboardContract.View, Lifecycle.MyAlertDialogCallbackContract {

    private static final String debugTag = DashboardActivity.class.getSimpleName();
    private ActivityDashboardBinding mBinding;
    private MyAlertDialogFragment myAlertDialogFragment;
    @Inject
    MyAccountManager mAccountManager;
    @Inject
    UIUtls uiUtls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        setSupportActionBar(mBinding.incltoolbar.toolbar);
        setUpNavDrawer();
        if (savedInstanceState != null) {
            MyAlertDialogFragment dialogFragment = (MyAlertDialogFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.alert_dialog_fgmt));
            if (dialogFragment != null) {
                myAlertDialogFragment = dialogFragment;
                dialogFragment.setInvokingActivity(this);
            } else {
                setUpDialogFragment();
            }
        } else {
            setUpDialogFragment();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myAlertDialogFragment != null)myAlertDialogFragment.clearInvokingActivity();
    }

    @Override
    public Lifecycle.ViewModel getViewModel() {
        return null;
    }

    @Override
    public void onError(int resource) {
        uiUtls.showSnackBar(mBinding.cntLayt, getResources().getString(resource), getResources().getString(R.string.close), Snackbar.LENGTH_LONG).subscribe();
    }

    @Override
    public void onPositiveAction() {
        mAccountManager.removeAccount().subscribe(
                accountRemoved -> {
                    finish();
                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                },
                throwable -> onError(R.string.error));
    }

    @Override
    public void onNegativeAction() {
        mBinding.drawerLlt.closeDrawers();
    }

    private void setUpNavDrawer() {
        //user 5 parameters constructor to handle toolbar click(hamburger icon, etc)
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mBinding.drawerLlt, mBinding.incltoolbar.toolbar, R.string.common_open_on_phone, R.string.app_logo);
        //Setting the actionbarToggle to drawer layout
        mBinding.drawerLlt.addDrawerListener(mToggle);
        //calling sync state is necessary or else your hamburger icon wont show up
        mToggle.syncState();
        NavigationHeaderBinding _bind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.navigation_header, mBinding.navigationView, false);
        mBinding.navigationView.addHeaderView(_bind.getRoot());

        mBinding.navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.logout:
                    myAlertDialogFragment.show(getSupportFragmentManager(), getResources().getString(R.string.alert_dialog_fgmt));
//                        logout = true;
//                        mBinding.drawerLlt.closeDrawers();
                    break;
            }
            return true;
        });
    }

    private void setUpDialogFragment() {
        myAlertDialogFragment = MyAlertDialogFragment.newInstance(getResources().getString(R.string.logout_dialog_title), getResources().getString(R.string.no), getResources().getString(R.string.yes));
        myAlertDialogFragment.setInvokingActivity(this);
    }
}
