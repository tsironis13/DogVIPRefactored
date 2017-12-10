package com.dogvip.giannis.dogviprefactored.owner.form;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.accountmanager.MyAccountManager;
import com.dogvip.giannis.dogviprefactored.base.activity.BaseActivity;
import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.dashboard.DashboardRetainFragment;
import com.dogvip.giannis.dogviprefactored.databinding.ActivityOwnerformBinding;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.room_persistence_data.entities.UserRole;
import com.dogvip.giannis.dogviprefactored.upload.ImageUploadRetainFragment;
import com.dogvip.giannis.dogviprefactored.upload.ImageUploadViewModel;
import com.dogvip.giannis.dogviprefactored.utilities.eventbus.RxEventBus;
import com.dogvip.giannis.dogviprefactored.utilities.ui.MyAlertDialogFragment;
import com.dogvip.giannis.dogviprefactored.utilities.ui.MyDateDialogFragment;
import com.dogvip.giannis.dogviprefactored.utilities.ui.UIUtls;
import com.jakewharton.rxbinding2.view.RxView;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by giannis on 8/12/2017.
 */

public class OwnerFormActivity extends BaseActivity implements OwnerFormContract.View, Lifecycle.MyDateDialogCallbackContract {
    private static final String debugTag = OwnerFormActivity.class.getSimpleName();
    private ActivityOwnerformBinding mBinding;
    private MyDateDialogFragment myDateDialogFragment;
    private boolean ownerExists;
    @Inject
    MyAccountManager mAccountManager;
    @Inject
    OwnerFormViewModel mViewModel;
    @Inject
    ImageUploadViewModel mImageUploadViewModel;
    @Inject
    OwnerFormRetainFragment mOwnerFormRetainFragment;
    @Inject
    ImageUploadRetainFragment mImageUploadRetainFragment;
    @Inject
    UIUtls uiUtls;
    @Inject
    AwesomeValidation mAwesomeValidation;
    @Inject
    UserRole userRole;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ownerform);
        setSupportActionBar(mBinding.incltoolbar.toolbar);
        mBinding.setViewmodel(mViewModel);
        initializeViewModel();
        initializeImageUploadViewModel();
        if (savedInstanceState != null) {
            ownerExists = savedInstanceState.getBoolean(getResources().getString(R.string.owner_exists));
            MyDateDialogFragment dialogFragment = (MyDateDialogFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.date_dialog_fgmt));
            if (dialogFragment != null) {
                myDateDialogFragment = dialogFragment;
                dialogFragment.setInvokingActivity(this);
            } else {
                setUpDialogFragment();
            }
        } else {
            setUpDialogFragment();
            if (getIntent().getExtras() != null) {
                ownerExists = getIntent().getExtras().getBoolean(getResources().getString(R.string.owner_exists));
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, AppConfig.cities);
        mBinding.cityEdt.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.checkOwnerExists(ownerExists, mAccountManager.getAccountDetails());
        Disposable disp = RxView.clicks(mBinding.ageEdt).subscribe(o -> {
            MyDateDialogFragment.setDialogType(1);
            myDateDialogFragment.show(getSupportFragmentManager(), getResources().getString(R.string.date_dialog_fgmt));
        });
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
        outState.putBoolean(getResources().getString(R.string.owner_exists), ownerExists);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOwnerFormRetainFragment.retainViewModel(mViewModel);
        mImageUploadRetainFragment.retainViewModel(mImageUploadViewModel);
        if (myDateDialogFragment != null)myDateDialogFragment.clearInvokingActivity();
    }

    @Override
    public Lifecycle.ViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit:
                submitForm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDisplayDateSet(String displayDate) {
        mBinding.ageEdt.setText(displayDate);
    }

    @Override
    public void onDateSet(long date) {
        mBinding.hiddenAgeEdt.setText(String.valueOf(date));
        Log.e(debugTag, date + " Date");
    }

    @Override
    public void onInvalidDateSet() {
//        mBinding.ageEdt.setText("");
    }

    @Override
    public void onError(int resource) {

    }

    @Override
    public void onSuccessOwnerAdded() {

    }

    @Override
    public void onSuccessOwnerEdited() {

    }

    @Override
    public UserRole ownerDoesNotExist() {
        userRole.setName(mBinding.nameEdt.getText().toString());
        userRole.setSurname(mBinding.surnameEdt.getText().toString());
        userRole.setAge(mBinding.hiddenAgeEdt.getText().toString());
        userRole.setCity(mBinding.cityEdt.getText().toString());
        userRole.setMobile_number(mBinding.phoneEdt.getText().toString());
        return userRole;
    }

    @Override
    public void ownerExists() {

    }

    @Override
    public void onProcessing() {

    }

    @Override
    public void onStopProcessing() {

    }

    private void submitForm() {
        hideSoftKeyboard();
        mAwesomeValidation.addValidation(mBinding.nameEdt, ".*\\S.*", getResources().getString(R.string.required_field));
        mAwesomeValidation.addValidation(mBinding.surnameEdt, ".*\\S.*", getResources().getString(R.string.required_field));
        mAwesomeValidation.addValidation(mBinding.ageEdt, ".*\\S.*", getResources().getString(R.string.required_field));
        mViewModel.submitForm(mAwesomeValidation, userRole);
    }

    public void hideSoftKeyboard() {
        uiUtls.hideSoftKeyboard(this.getCurrentFocus());
    }

    private void initializeViewModel() {
        if (getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.retained_owner_form_act)) == null) {
            getSupportFragmentManager().beginTransaction().add(mOwnerFormRetainFragment, getResources().getString(R.string.retained_owner_form_act)).commit();
        } else {
            mOwnerFormRetainFragment = (OwnerFormRetainFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.retained_owner_form_act));
        }
        if (mOwnerFormRetainFragment.getViewModel() != null) mViewModel = mOwnerFormRetainFragment.getViewModel();
//        Log.e(debugTag, "view model: "+mViewModel);
    }

    private void initializeImageUploadViewModel() {
        if (getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.retained_image_upload_fgmt)) == null) {
            getSupportFragmentManager().beginTransaction().add(mImageUploadRetainFragment, getResources().getString(R.string.retained_image_upload_fgmt)).commit();
        } else {
            mImageUploadRetainFragment = (ImageUploadRetainFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.retained_image_upload_fgmt));
        }
        if (mImageUploadRetainFragment.getViewModel() != null) mImageUploadViewModel = mImageUploadRetainFragment.getViewModel();
//        Log.e(debugTag, "upload view model: "+mImageUploadViewModel);
    }

    private void setUpDialogFragment() {
        myDateDialogFragment = MyDateDialogFragment.newInstance();
        myDateDialogFragment.setInvokingActivity(this);
    }

}
