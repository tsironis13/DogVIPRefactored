package com.dogvip.giannis.dogviprefactored.owner.form;

import android.arch.persistence.room.Insert;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.accountmanager.MyAccountManager;
import com.dogvip.giannis.dogviprefactored.base.activity.BaseActivity;
import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.databinding.ActivityOwnerformBinding;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.owner.profile.OwnerProfileActivity;
import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.dogvip.giannis.dogviprefactored.pojo.owner.form.SubmitOwnerFormRequest;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserRole;
import com.dogvip.giannis.dogviprefactored.upload.ImageUploadRetainFragment;
import com.dogvip.giannis.dogviprefactored.upload.ImageUploadViewModel;
import com.dogvip.giannis.dogviprefactored.utilities.eventbus.RxEventBus;
import com.dogvip.giannis.dogviprefactored.utilities.ui.MyAlertDialogFragment;
import com.dogvip.giannis.dogviprefactored.utilities.ui.MyDateDialogFragment;
import com.dogvip.giannis.dogviprefactored.utilities.ui.UIUtls;
import com.jakewharton.rxbinding2.view.RxView;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by giannis on 8/12/2017.
 */

public class OwnerFormActivity extends BaseActivity implements OwnerFormContract.View, Lifecycle.MyDateDialogCallbackContract {
    private static final String debugTag = OwnerFormActivity.class.getSimpleName();
    private ActivityOwnerformBinding mBinding;
    private MyDateDialogFragment myDateDialogFragment;
    private boolean ownerExists;
//    private Bundle savedInstanceState;
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
    @Inject
    SubmitOwnerFormRequest submitOwnerFormRequest;
    @Inject
    BaseRequest baseRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ownerform);
        setSupportActionBar(mBinding.incltoolbar.toolbar);
        if (getSupportActionBar()!= null)getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeViewModel();
        initializeImageUploadViewModel();
//        this.savedInstanceState = savedInstanceState;
        if (savedInstanceState != null) {
            MyAlertDialogFragment dialogFragment = (MyAlertDialogFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.alert_dialog_fgmt));
            if (dialogFragment != null) {
                mViewModel.pickDialogByType(dialogFragment, dialogFragment.getArguments().getString("type"));
            } else {
                mViewModel.initializeAlertDialog();
            }
            ownerExists = savedInstanceState.getBoolean(getResources().getString(R.string.owner_exists));
            MyDateDialogFragment dateDialogFragment = (MyDateDialogFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.date_dialog_fgmt));
            if (dateDialogFragment != null) {
                myDateDialogFragment = dateDialogFragment;
                dateDialogFragment.setInvokingActivity(this);
            } else {
                setUpDialogFragment();
            }
        } else {
            setUpDialogFragment();
            if (getIntent().getExtras() != null) {
                ownerExists = getIntent().getExtras().getBoolean(getResources().getString(R.string.owner_exists));
            }
        }
        mBinding.setViewmodel(mViewModel);
        mBinding.executePendingBindings();
        mBinding.cityEdt.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, AppConfig.cities));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.checkOwnerExists(ownerExists, mAccountManager.getAccountDetails());
//        if (savedInstanceState == null) {
////            mViewModel.checkOwnerExists(ownerExists, mAccountManager.getAccountDetails());
//        } else {
////            mViewModel.setData(userRole);
//        }
        Disposable disp = RxView.clicks(mBinding.ageEdt).subscribe(o -> {
            MyDateDialogFragment.setDialogType(1);
            myDateDialogFragment.show(getSupportFragmentManager(), getResources().getString(R.string.date_dialog_fgmt));
        });
        RxEventBus.add(this, disp);
        Disposable disp1 = RxView.clicks(mBinding.profileImgv).subscribe(o -> {
            mViewModel.setAlertDialogMsgs(getResources().getString(R.string.upload_image_dialog_title), getResources().getString(R.string.upload_image_dialog_msg), getResources().getString(R.string.gallery),getResources().getString(R.string.camera));
            mViewModel.showUploadImageDialog(getSupportFragmentManager(), getResources().getString(R.string.alert_dialog_fgmt));
        });
        RxEventBus.add(this, disp1);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConfig.EXTERNAL_CONTENT_URI) {
                Log.e(debugTag, "external content uri");
            } else {

            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
    public void onDeleteOwnerImageAction() {
        Log.e(debugTag, "onDeleteOwnerImageAction");
    }

    @Override
    public void onGalleryUploadAction() {
        Log.e(debugTag, "onGalleryUploadAction");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.image_choose_label)), AppConfig.EXTERNAL_CONTENT_URI);
    }

    @Override
    public void onCameraUploadAction() {
        Log.e(debugTag, "onCameraUploadAction");
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
        onError(AppConfig.getCodes().get(AppConfig.ERROR_INVALID_DATE));
    }

    @Override
    public void onError(int resource) {
        uiUtls.showSnackBar(mBinding.cntLayt, getResources().getString(resource), getResources().getString(R.string.close), Snackbar.LENGTH_LONG).subscribe();
    }

    @Override
    public void onErrorRetry(int resource) {
        Log.e(debugTag, "onErrorRetry");
        uiUtls.showSnackBar(mBinding.cntLayt, getResources().getString(resource), getResources().getString(R.string.retry), Snackbar.LENGTH_INDEFINITE).subscribe(aBoolean -> mViewModel.onRetry());

    }

    @Override
    public void onSuccessOwnerAdded(UserRole userRole) {
        Log.e(debugTag, userRole.getImage_url() + " IMAGE URL");
        ownerExists = true;
        setTitle(getResources().getString(R.string.edit_owner));
        uiUtls.showSnackBar(mBinding.cntLayt, getResources().getString(R.string.owner_added), getResources().getString(R.string.close), Snackbar.LENGTH_LONG).subscribe();
        mViewModel.setData(userRole);
//        mBinding.scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
////                mBinding.scrollView.getViewTreeObserver().removeOnGlobalLayoutListene‌​r(this);
//                mBinding.scrollView.fullScroll(View.FOCUS_UP);
//            }
//        });
    }

    @Override
    public void onSuccessOwnerFormDetails(UserRole userRole) {
        Log.e(debugTag, "onSuccessOwnerFormDetails: "+ userRole.getImage_url());
        ownerExists = true;
        setTitle(getResources().getString(R.string.edit_owner));
        mViewModel.setData(userRole);
//        mBinding.scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
////                mBinding.scrollView.getViewTreeObserver().removeOnGlobalLayoutListene‌​r(this);
//                mBinding.scrollView.fullScroll(View.FOCUS_UP);
//            }
//        });
    }

    @Override
    public void onSuccessOwnerEdited() {
        startActivity(new Intent(this, OwnerProfileActivity.class));
    }

    @Override
    public UserRole ownerDoesNotExist() {
        setTitle(getResources().getString(R.string.add_owner));
        userRole.setRole(1);
        userRole.setName(mBinding.nameEdt.getText().toString());
        userRole.setSurname(mBinding.surnameEdt.getText().toString());
        userRole.setAge(mBinding.hiddenAgeEdt.getText().toString());
        userRole.setCity(mBinding.cityEdt.getText().toString());
        userRole.setMobile_number(mBinding.phoneEdt.getText().toString());
        return userRole;
    }

    @Override
    public void ownerExists() {
        setTitle(getResources().getString(R.string.edit_owner));
        baseRequest.setAction(getResources().getString(R.string.get_owner_form_details));
        baseRequest.setAuthtoken(mAccountManager.getAccountDetails().getToken());
        mViewModel.fetchOwnerFormDetails(baseRequest, mAccountManager.getAccountDetails().getUserId());
    }

    private void submitForm() {
        hideSoftKeyboard();
        mAwesomeValidation.addValidation(mBinding.nameEdt, ".*\\S.*", getResources().getString(R.string.required_field));
        mAwesomeValidation.addValidation(mBinding.surnameEdt, ".*\\S.*", getResources().getString(R.string.required_field));
        mAwesomeValidation.addValidation(mBinding.ageEdt, ".*\\S.*", getResources().getString(R.string.required_field));
        submitOwnerFormRequest.setAuthtoken(mAccountManager.getAccountDetails().getToken());
        mViewModel.submitForm(mAwesomeValidation, submitOwnerFormRequest);
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
