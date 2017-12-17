package com.dogvip.giannis.dogviprefactored.owner.profile;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.accountmanager.MyAccountManager;
import com.dogvip.giannis.dogviprefactored.base.activity.BaseActivity;
import com.dogvip.giannis.dogviprefactored.dashboard.DashboardActivity;
import com.dogvip.giannis.dogviprefactored.databinding.ActivityOwnerprofileBinding;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.owner.form.OwnerFormActivity;
import com.dogvip.giannis.dogviprefactored.owner.form.OwnerFormRetainFragment;
import com.dogvip.giannis.dogviprefactored.pet.PetFormActivity;
import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.dogvip.giannis.dogviprefactored.pojo.owner.profile.DeleteOwnerRequest;
import com.dogvip.giannis.dogviprefactored.pojo.owner.profile.OwnerProfileDetailsResponse;
import com.dogvip.giannis.dogviprefactored.utilities.eventbus.RxEventBus;
import com.dogvip.giannis.dogviprefactored.utilities.ui.MyAlertDialogFragment;
import com.dogvip.giannis.dogviprefactored.utilities.ui.UIUtls;
import com.jakewharton.rxbinding2.view.RxView;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by giannis on 8/12/2017.
 */

public class OwnerProfileActivity extends BaseActivity implements OwnerProfileContract.View {
    private static final String debugTag = OwnerProfileActivity.class.getSimpleName();
    private ActivityOwnerprofileBinding mBinding;
    @Inject
    OwnerProfileViewModel mViewModel;
    @Inject
    OwnerProfileRetainFragment mOwnerProfileRetainFragment;
    @Inject
    UIUtls uiUtls;
    @Inject
    MyAccountManager mAccountManager;
    @Inject
    BaseRequest baseRequest;
    @Inject
    DeleteOwnerRequest deleteOwnerRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ownerprofile);
        setSupportActionBar(mBinding.toolbar);
        mBinding.colTlbrLyt.setExpandedTitleColor(Color.parseColor("#00FFFFFF"));
        if (getSupportActionBar()!= null)getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeViewModel();
        if (savedInstanceState != null) {
            Log.e(debugTag, "savedInstanceState is not null");
            MyAlertDialogFragment dialogFragment = (MyAlertDialogFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.alert_dialog_fgmt));
            if (dialogFragment != null) {
                mViewModel.pickDialogByType(dialogFragment, dialogFragment.getArguments().getString("type"));
            } else {
                mViewModel.initializeAlertDialog();
            }
        } else {
            Log.e(debugTag, "savedInstanceState is null");
            mViewModel.initializeAlertDialog();
        }
        mBinding.setViewmodel(mViewModel);
        mBinding.executePendingBindings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        baseRequest.setAuthtoken(mAccountManager.getAccountDetails().getToken());
        baseRequest.setAction(getResources().getString(R.string.get_my_owner_details));
        mViewModel.fetchOwnerDetails(baseRequest, mAccountManager.getAccountDetails().getUserId());
        Disposable disp = RxView.clicks(mBinding.addPetFlbtn).subscribe(o -> {
            startActivity(new Intent(this, PetFormActivity.class));
        });
        RxEventBus.add(this, disp);
    }

    @Override
    protected void onPause() {
        super.onPause();
        RxEventBus.unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOwnerProfileRetainFragment.retainViewModel(mViewModel);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.owner_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                editProfile();
                return true;
            case R.id.delete:
                mViewModel.setAlertDialogMsgs(getResources().getString(R.string.delete_owner_dialog_title), getResources().getString(R.string.delete_owner_dialog_msg), getResources().getString(R.string.no),getResources().getString(R.string.yes));
                mViewModel.showDeleteOwnerDialog(getSupportFragmentManager(), getResources().getString(R.string.alert_dialog_fgmt));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Lifecycle.ViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void onDeleteOwnerAction() {
        deleteOwnerRequest.setAuthtoken(mAccountManager.getAccountDetails().getToken());
        deleteOwnerRequest.setAction(getResources().getString(R.string.delete_owner));
        mViewModel.deleteOwner(deleteOwnerRequest);
    }

    @Override
    public void onErrorRetry(int resource) {
        uiUtls.showSnackBar(mBinding.coordlt, getResources().getString(resource), getResources().getString(R.string.retry), Snackbar.LENGTH_INDEFINITE).subscribe(aBoolean -> mViewModel.onRetry());
    }

    @Override
    public void onSuccessFetchOwnerDetails(OwnerProfileDetailsResponse data) {
//        Log.e(debugTag, data.getOwnerPets().size() +  "size");
        mViewModel.setData(data);
    }

    @Override
    public void onSuccessDeleteOwner() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(getResources().getString(R.string.owner_deleted), true);
        startActivity(new Intent(this, DashboardActivity.class).putExtras(bundle));
        finish();
    }

    private void editProfile() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(getResources().getString(R.string.owner_exists), true);
        startActivity(new Intent(this, OwnerFormActivity.class).putExtras(bundle));
    }

    private void initializeViewModel() {
        if (getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.retained_owner_profile_act)) == null) {
            getSupportFragmentManager().beginTransaction().add(mOwnerProfileRetainFragment, getResources().getString(R.string.retained_owner_profile_act)).commit();
        } else {
            mOwnerProfileRetainFragment = (OwnerProfileRetainFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.retained_owner_profile_act));
        }
        if (mOwnerProfileRetainFragment.getViewModel() != null) mViewModel = mOwnerProfileRetainFragment.getViewModel();
//        Log.e(debugTag, "view model: "+mViewModel);
    }
}
