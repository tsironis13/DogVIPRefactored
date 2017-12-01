package com.dogvip.giannis.dogviprefactored.login.forgotpass;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.base.fragment.BaseFragment;
import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.databinding.ForgotpaswrdFrgmtBinding;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.login.LoginActivity;
import com.dogvip.giannis.dogviprefactored.login.LoginContract;
import com.dogvip.giannis.dogviprefactored.login.signin.SignInFrgmt;
import com.dogvip.giannis.dogviprefactored.pojo.BaseResponse;
import com.dogvip.giannis.dogviprefactored.pojo.login.forgotpass.ForgotPassRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.forgotpass.ForgotPassResponse;
import com.dogvip.giannis.dogviprefactored.utilities.animation.AnimationListener;
import com.dogvip.giannis.dogviprefactored.utilities.eventbus.RxEventBus;
import com.dogvip.giannis.dogviprefactored.utilities.notification.NotificationUtls;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by giannis on 17/5/2017.
 */

public class ForgotPaswrdFrgmt extends BaseFragment implements LoginContract.ForgotPassView {
    private static final String debugTAag = ForgotPaswrdFrgmt.class.getSimpleName();
    private View mView;
    private ForgotpaswrdFrgmtBinding mBinding;
    private int fragmentCreatedCode, userId; // login activity: check if fragments are created on button click
    private boolean emailAuthenticated;
    @Inject
    ForgotPaswrdViewModel mViewModel;
    @Inject
    ForgotPaswrdRetainFragment mForgotPaswrdRetainFragment;
    @Inject
    AwesomeValidation mAwesomeValidation;
    @Inject
    ForgotPassRequest forgotPassRequest;
    @Inject
    AnimationListener animationListener;

    public static ForgotPaswrdFrgmt newInstance(int x) {
        Bundle bundle = new Bundle();
        bundle.putInt("frgmnt_created", x);
        ForgotPaswrdFrgmt sign = new ForgotPaswrdFrgmt();
        sign.setArguments(bundle);
        return sign;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCreatedCode = getArguments().getInt(getResources().getString(R.string.frgmnt_created));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentCreatedCode != 100) return null;

        if (mView == null ) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.forgotpaswrd_frgmt, container, false);
            mView = mBinding.getRoot();
            fragmentCreatedCode = 300;
        }
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeViewModel();

        if (savedInstanceState != null) {
            mBinding.setEmailisvalid(savedInstanceState.getBoolean(getResources().getString(R.string.is_forgot_pass_email_valid)));
            userId = savedInstanceState.getInt(getResources().getString(R.string.user_id));
            emailAuthenticated = savedInstanceState.getBoolean(getResources().getString(R.string.email_authenticated));
        }
    }

    @Override
    public Lifecycle.ViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void onResume() {
        super.onResume();
        Disposable disp = RxView.clicks(mBinding.submitBtn).subscribe(o -> submit());
        RxEventBus.add(this, disp);

        Disposable disp1 = RxView.clicks(mBinding.signInBtn).subscribe(
                o -> {
                    ((LoginActivity)getActivity()).hideSoftKeyboard();
                    getFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                    .replace(R.id.loginContainer, SignInFrgmt.newInstance(200), getResources().getString(R.string.signin_fgmt))
                                    .addToBackStack(getResources().getString(R.string.signin_fgmt))
                                    .commit();
                }
        );
        RxEventBus.add(this, disp1);
        Disposable disp2 = RxTextView.textChanges(mBinding.emailEdt)
                .skip(1)
                .filter(charSequence -> mBinding.getEmailisvalid())
                .subscribe(charSequence -> {
                    mBinding.setEmailisvalid(false);
                    emailAuthenticated = false;
                    userId = 0;
                });
        RxEventBus.add(this, disp2);
        Disposable disp3 = RxView.clicks(mBinding.backBtn).filter(o -> !mBinding.getProcessing()).subscribe(o -> ((LoginActivity)getActivity()).onCustomNavigationUpButtonClick());
        RxEventBus.add(this, disp3);

        mBinding.emailEdt.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE && mAwesomeValidation.validate()) {
                ((LoginActivity)getActivity()).hideSoftKeyboard();
                Completable.timer(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(this::submit);
            }
            return true;
        });
        mBinding.confpassEdt.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE && mAwesomeValidation.validate()) {
                ((LoginActivity)getActivity()).hideSoftKeyboard();
                Completable.timer(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(this::submit);
            }
            return true;
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        RxEventBus.unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(getResources().getString(R.string.is_forgot_pass_email_valid), mBinding.getEmailisvalid());
        outState.putInt(getResources().getString(R.string.user_id), userId);
        outState.putBoolean(getResources().getString(R.string.email_authenticated), emailAuthenticated);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mForgotPaswrdRetainFragment.retainViewModel(mViewModel);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim != 0) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
            if (enter) return super.onCreateAnimation(transit, true, nextAnim);
            animation.setAnimationListener(animationListener);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(animation);

            return animationSet;
        } else {
            return null;
        }
    }

    @Override
    public void onProcessing() {
        setProcessing(true);
        ((LoginActivity)getActivity()).onProcessing(mBinding.loadingInd);
    }

    @Override
    public void onStopProcessing() {
        if (mBinding.getProcessing()) mBinding.setProcessing(false);
    }

    @Override
    public void onSuccessIsEmailValid(ForgotPassResponse response) {
        setProcessing(false);
        mBinding.passEdt.getText().clear();
        mBinding.confpassEdt.getText().clear();
        mBinding.setEmailisvalid(true);
        userId = response.getUserId();
        emailAuthenticated = true;
    }

    @Override
    public void onSuccessNewPassChange(BaseResponse response) {
        setProcessing(false);
        ((LoginActivity)getActivity()).showNotification(getResources().getString(R.string.reset_passwrd_notfctn_title), getResources().getString(R.string.reset_passwrd_success));
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.loginContainer, SignInFrgmt.newInstance(100), getResources().getString(R.string.signin_fgmt))
                .addToBackStack(getResources().getString(R.string.signin_fgmt))
                .commit();
    }

    @Override
    public void onError(final int resource) {
        setProcessing(false);
        ((LoginActivity)getActivity()).onError(resource);
    }

    private void submit() {
        //            mAwesomeValidation.clear();
        mAwesomeValidation.addValidation(mBinding.emailEdt, Patterns.EMAIL_ADDRESS, getResources().getString(R.string.not_valid_email));
//            if (mBinding.getEmailisvalid()) {
//                mAwesomeValidation.addValidation(mBinding.passEdt, "^(?=.*\\D)[a-zA-Z\\d]{8}$", getResources().getString(R.string.not_valid_pass));
//                mAwesomeValidation.addValidation(mBinding.confpassEdt, mBinding.passEdt, getResources().getString(R.string.passwrds_not_match));
//            }
        forgotPassRequest.setEmailAuthenticated(emailAuthenticated);
        forgotPassRequest.setEmail(mBinding.emailEdt.getText().toString());
        forgotPassRequest.setConfNewpassword(mBinding.passEdt.getText().toString());
        forgotPassRequest.setNewpassword(mBinding.confpassEdt.getText().toString());
        forgotPassRequest.setUserId(userId);
        if (mAwesomeValidation.validate()) {
            ((LoginActivity)getActivity()).hideSoftKeyboard();
            mViewModel.handleUserInputAction(forgotPassRequest);
        }
    }

    private void setProcessing(boolean processing) {
        mBinding.setProcessing(processing);
    }

    private void initializeViewModel() {
        if (getFragmentManager().findFragmentByTag(getResources().getString(R.string.retained_forgotpass_fgmt)) == null) {
            getFragmentManager().beginTransaction().add(mForgotPaswrdRetainFragment, getResources().getString(R.string.retained_forgotpass_fgmt)).commit();
        } else {
            mForgotPaswrdRetainFragment = (ForgotPaswrdRetainFragment) getFragmentManager().findFragmentByTag(getResources().getString(R.string.retained_forgotpass_fgmt));
        }
        if (mForgotPaswrdRetainFragment.getViewModel() != null) mViewModel = mForgotPaswrdRetainFragment.getViewModel();
    }

}
