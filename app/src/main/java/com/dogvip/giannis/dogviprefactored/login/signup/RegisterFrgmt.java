package com.dogvip.giannis.dogviprefactored.login.signup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
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
import com.dogvip.giannis.dogviprefactored.databinding.RegisterFrgmtBinding;
import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.login.LoginActivity;
import com.dogvip.giannis.dogviprefactored.login.LoginContract;
import com.dogvip.giannis.dogviprefactored.login.signin.SignInFrgmt;
import com.dogvip.giannis.dogviprefactored.pojo.BaseResponse;
import com.dogvip.giannis.dogviprefactored.pojo.login.LoginResponse;
import com.dogvip.giannis.dogviprefactored.pojo.login.SignInUpFbGoogleRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.signup.SignUpEmailRequest;
import com.dogvip.giannis.dogviprefactored.utilities.animation.AnimationListener;
import com.dogvip.giannis.dogviprefactored.utilities.eventbus.RxEventBus;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jakewharton.rxbinding2.view.RxView;

import org.json.JSONException;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by giannis on 15/5/2017.
 */

public class RegisterFrgmt extends BaseFragment implements LoginContract.SignUpView {

    private View mView;
    private RegisterFrgmtBinding mBinding;
    private CallbackManager mCallbackMngr;
    private GoogleApiClient mGoogleApiClient;
    @Inject
    RegistrationViewModel mViewModel;
    @Inject
    RegisterRetainFragment mRegisterRetainFragment;
    @Inject
    SignUpEmailRequest signUpEmailRequest;
    @Inject
    SignInUpFbGoogleRequest signInUpFbGoogleRequest;
    @Inject
    AwesomeValidation mAwesomeValidation;
    @Inject
    AnimationListener animationListener;

    public static RegisterFrgmt newInstance() {
        return new RegisterFrgmt();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null ) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.register_frgmt, container, false);
            mView = mBinding.getRoot();
        }
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeViewModel();
        mGoogleApiClient = ((LoginActivity)getActivity()).getmGoogleApiClient();

        mAwesomeValidation.addValidation(mBinding.emailEdt, Patterns.EMAIL_ADDRESS, getResources().getString(R.string.not_valid_email));
        mAwesomeValidation.addValidation(mBinding.passEdt, "^(?=.*\\D)[a-zA-Z\\d]{8}$", getResources().getString(R.string.not_valid_pass));
        mAwesomeValidation.addValidation(mBinding.confpassEdt, mBinding.passEdt, getResources().getString(R.string.passwrds_not_match));

        initializeFBSignUp();
    }

    @Override
    public void onResume() {
        super.onResume();
        Disposable disp = RxView.clicks(mBinding.registerBtn).subscribe(o -> {
            if (mAwesomeValidation.validate()) {
                ((LoginActivity)getActivity()).hideSoftKeyboard();
                registerEmailUser();
            }
        });
        RxEventBus.add(this, disp);
        Disposable disp1 = RxView.clicks(mBinding.facebookBtn).subscribe(o -> mBinding.hiddenFbBtn.performClick());
        RxEventBus.add(this, disp1);
        Disposable disp2 = RxView.clicks(mBinding.googleBtn).subscribe(o -> {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, AppConfig.GOOGLE_REQ_CODE);
        });
        RxEventBus.add(this, disp2);
        Disposable disp3 = RxView.clicks(mBinding.termsBtn).subscribe(o -> {
            Uri uri = Uri.parse(getResources().getString(R.string.terms_url));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        RxEventBus.add(this, disp3);
        Disposable disp4 = RxView.clicks(mBinding.backBtn).filter(o -> !mBinding.getProcessing()).subscribe(o -> ((LoginActivity)getActivity()).onCustomNavigationUpButtonClick());
        RxEventBus.add(this, disp4);
        mBinding.confpassEdt.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE && mAwesomeValidation.validate()) {
                ((LoginActivity)getActivity()).hideSoftKeyboard();
                Completable.timer(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(this::registerEmailUser);
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
    public void onDestroy() {
        super.onDestroy();
        mRegisterRetainFragment.retainViewModel(mViewModel);
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
        if (mBinding.getProcessing()) setProcessing(false);
    }

    @Override
    public void onSuccessFbLogin(LoginResponse response) {
        setProcessing(false);
        logoutFBUser();
        ((LoginActivity)getActivity()).addAccount(response);
    }

    @Override
    public void onSuccessGoogleLogin(LoginResponse response) {
        setProcessing(false);
        logoutGoogleUser();
        ((LoginActivity)getActivity()).addAccount(response);
    }

    @Override
    public void onSuccessEmailSignUp() {
        setProcessing(false);
        ((LoginActivity)getActivity()).showNotification(getResources().getString(R.string.welcome), getResources().getString(R.string.confirm_account));
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

    @Override
    public void onErrorFbLogin(int resource) {
        logoutFBUser();
        onError(resource);
    }

    @Override
    public void onErrorGoogleLogin(int resource) {
        logoutGoogleUser();
        onError(resource);
    }

    @Override
    public Lifecycle.ViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConfig.GOOGLE_REQ_CODE) {
            signInUpFbGoogleRequest.setAction(getResources().getString(R.string.register_user));
            signInUpFbGoogleRequest.setDeviceid(android.os.Build.SERIAL);
            signInUpFbGoogleRequest.setRegtype(2);
            mViewModel.handleGoogleSignUpResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data), signInUpFbGoogleRequest);
        } else {
            mCallbackMngr.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setProcessing(boolean processing) {
        mBinding.setProcessing(processing);
    }

    private void registerEmailUser() {
        signUpEmailRequest.setAction(getResources().getString(R.string.register_user));
        signUpEmailRequest.setEmail(mBinding.emailEdt.getText().toString());
        signUpEmailRequest.setPassword(mBinding.passEdt.getText().toString());
        signUpEmailRequest.setConfpassword(mBinding.confpassEdt.getText().toString());
        signUpEmailRequest.setRegtype(0); //0 -> email registr
        mViewModel.signUpEmail(signUpEmailRequest);
    }

    private void initializeFBSignUp() {
        mCallbackMngr = CallbackManager.Factory.create();

        mBinding.hiddenFbBtn.setReadPermissions(getResources().getString(R.string.email));
        mBinding.hiddenFbBtn.setFragment(this);
        mBinding.hiddenFbBtn.registerCallback(mCallbackMngr, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                    try {
                        signInUpFbGoogleRequest.setAction(getResources().getString(R.string.register_user));
                        signInUpFbGoogleRequest.setDeviceid(android.os.Build.SERIAL);
                        signInUpFbGoogleRequest.setRegtype(1);
                        mViewModel.handleFbSignUpResult(object.getString(getResources().getString(R.string.email)), signInUpFbGoogleRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                ((LoginActivity)getActivity()).onError(AppConfig.getCodes().get(AppConfig.ERROR_SIGNUP_CANCELED));
            }

            @Override
            public void onError(FacebookException error) {
                ((LoginActivity)getActivity()).onError(AppConfig.getCodes().get(AppConfig.STATUS_ERROR));
            }
        });
    }

    private void logoutFBUser() {
        LoginManager.getInstance().logOut();
    }

    private void logoutGoogleUser() {
        if (mGoogleApiClient.isConnected())
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(status -> {});
    }

    private void initializeViewModel() {
        if (getFragmentManager().findFragmentByTag(getResources().getString(R.string.retained_signup_fgmt)) == null) {
            getFragmentManager().beginTransaction().add(mRegisterRetainFragment, getResources().getString(R.string.retained_signup_fgmt)).commit();
        } else {
            mRegisterRetainFragment = (RegisterRetainFragment) getFragmentManager().findFragmentByTag(getResources().getString(R.string.retained_signup_fgmt));
        }
        if (mRegisterRetainFragment.getViewModel() != null) mViewModel = mRegisterRetainFragment.getViewModel();
    }
}
