package com.dogvip.giannis.dogviprefactored.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;

import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.accountmanager.MyAccountManager;
import com.dogvip.giannis.dogviprefactored.base.activity.BaseActivity;
import com.dogvip.giannis.dogviprefactored.databinding.ActivityDashboardBinding;
import com.dogvip.giannis.dogviprefactored.databinding.NavigationHeaderBinding;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.login.LoginActivity;
import com.dogvip.giannis.dogviprefactored.owner.form.OwnerFormActivity;
import com.dogvip.giannis.dogviprefactored.owner.profile.OwnerProfileActivity;
import com.dogvip.giannis.dogviprefactored.pojo.sync.UploadFcmTokenRequest;
import com.dogvip.giannis.dogviprefactored.utilities.eventbus.RxEventBus;
import com.dogvip.giannis.dogviprefactored.utilities.ui.MyAlertDialogFragment;
import com.dogvip.giannis.dogviprefactored.utilities.ui.UIUtls;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.Serializable;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by giannis on 30/11/2017.
 */

public class DashboardActivity extends BaseActivity implements DashboardContract.View {

    private static final String debugTag = DashboardActivity.class.getSimpleName();
    private ActivityDashboardBinding mBinding;
    private boolean ownerDeleted;
//    @Inject
//    DispatchingAndroidInjector<Fragment> fragmentInjector;
    @Inject
    MyAccountManager mAccountManager;
    @Inject
    UIUtls uiUtls;
    @Inject
    DashboardViewModel mViewModel;
    @Inject
    DashboardRetainFragment mDashboardRetainFragment;
    @Inject
    UploadFcmTokenRequest mUploadFcmTokenRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        setSupportActionBar(mBinding.incltoolbar.toolbar);
        initializeViewModel();
        setUpNavDrawer(mAccountManager.getAccountDetails().getEmail());
        if (getIntent().getExtras() != null) {
            ownerDeleted = getIntent().getExtras().getBoolean(getResources().getString(R.string.owner_deleted));
//            Log.e(debugTag, ownerDeleted + " ");
        }
        if (savedInstanceState != null) {
            MyAlertDialogFragment dialogFragment = (MyAlertDialogFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.alert_dialog_fgmt));
            if (dialogFragment != null) {
                mViewModel.pickDialogByType(dialogFragment, getSupportFragmentManager(), getResources().getString(R.string.alert_dialog_fgmt), dialogFragment.getArguments().getString("type"));
            } else {
                mViewModel.initializeAlertDialog();
            }
        } else {
            mViewModel.initializeAlertDialog();
            mViewModel.syncFcmToken(mAccountManager.getAccountDetails());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Disposable disp = RxView.clicks(mBinding.petsLlt).subscribe(o -> mViewModel.checkOwnerExists(mAccountManager.getAccountDetails().getUserId()));
        RxEventBus.add(this, disp);
    }

    @Override
    protected void onPause() {
        super.onPause();
        RxEventBus.unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDashboardRetainFragment.retainViewModel(mViewModel);
    }

    @Override
    public void onBackPressed() {
        if (ownerDeleted) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Lifecycle.ViewModel getViewModel() {
        return mViewModel;
    }

//    @Override
//    public AndroidInjector<Fragment> supportFragmentInjector() {
//        return fragmentInjector;
//    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void ownerExists() {
        startActivity(new Intent(this, OwnerProfileActivity.class));
    }

    @Override
    public void ownerDoesNotExist() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(getResources().getString(R.string.owner_exists), false);
        startActivity(new Intent(this, OwnerFormActivity.class).putExtras(bundle));
//        finish();
    }

    @Override
    public void onError(int resource) {
        uiUtls.showSnackBar(mBinding.cntLayt, getResources().getString(resource), getResources().getString(R.string.close), Snackbar.LENGTH_LONG).subscribe();
    }

    @Override
    public void onLogoutAction() {
        mAccountManager.removeAccount().subscribe(
                accountRemoved -> {
                    mViewModel.logoutUser();
                    finish();
                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                },
                throwable -> onError(R.string.error));
    }

    @Override
    public void onLogoutCancelAction() {
        mBinding.drawerLlt.closeDrawers();
    }

    private void setUpNavDrawer(String email) {
        //user 5 parameters constructor to handle toolbar click(hamburger icon, etc)
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mBinding.drawerLlt, mBinding.incltoolbar.toolbar, R.string.common_open_on_phone, R.string.app_logo);
        //Setting the actionbarToggle to drawer layout
        mBinding.drawerLlt.addDrawerListener(mToggle);
        //calling sync state is necessary or else your hamburger icon wont show up
        mToggle.syncState();
        NavigationHeaderBinding _bind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.navigation_header, mBinding.navigationView, false);
        mBinding.navigationView.addHeaderView(_bind.getRoot());
        _bind.setUseremail(email);

        mBinding.navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.logout:
                    mViewModel.setAlertDialogMsgs(getResources().getString(R.string.logout_dialog_title), "", getResources().getString(R.string.no),getResources().getString(R.string.yes));
                    mViewModel.showLogoutDialog(getSupportFragmentManager(), getResources().getString(R.string.alert_dialog_fgmt));
                    break;
            }
            return true;
        });
    }

    private void setUpDialogFragment() {
//        myAlertDialogFragment = MyAlertDialogFragment.newInstance();
//        myAlertDialogFragment.setInvokingActivity(this);
    }

    private void initializeViewModel() {
        if (getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.retained_dashboard_act)) == null) {
            getSupportFragmentManager().beginTransaction().add(mDashboardRetainFragment, getResources().getString(R.string.retained_dashboard_act)).commit();
        } else {
            mDashboardRetainFragment = (DashboardRetainFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.retained_dashboard_act));
        }
        if (mDashboardRetainFragment.getViewModel() != null) mViewModel = mDashboardRetainFragment.getViewModel();
    }

}
