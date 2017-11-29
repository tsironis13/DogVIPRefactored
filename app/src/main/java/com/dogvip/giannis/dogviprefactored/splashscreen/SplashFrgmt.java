package com.dogvip.giannis.dogviprefactored.splashscreen;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.base.fragment.BaseFragment;
import com.dogvip.giannis.dogviprefactored.databinding.SplashFrgmtBinding;
import com.dogvip.giannis.dogviprefactored.login.signin.SignInFrgmt;
import com.dogvip.giannis.dogviprefactored.login.signup.RegisterFrgmt;
import com.dogvip.giannis.dogviprefactored.utilities.animation.AnimationListener;
import com.dogvip.giannis.dogviprefactored.utilities.eventbus.RxEventBus;
import com.jakewharton.rxbinding2.view.RxView;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by giannis on 15/5/2017.
 */

public class SplashFrgmt extends Fragment implements HasSupportFragmentInjector {

    private View view;
    private SplashFrgmtBinding mBinding;
    private FragmentManager mFragmentManager;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;
    @Inject
    AnimationListener animationListener;

    public static SplashFrgmt newInstance() {
        return new SplashFrgmt();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        // Perform injection here before M, L (API 22) and below because onAttach(Context)
        // is not yet available at L.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) AndroidSupportInjection.inject(this);
        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        // Perform injection here for M (API 23) due to deprecation of onAttach(Activity).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.splash_frgmt, container, false);
            view = mBinding.getRoot();
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public void onResume() {
        super.onResume();
        Disposable disp1 = RxView.clicks(mBinding.registerBtn).subscribe(
                o -> mFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.loginContainer, RegisterFrgmt.newInstance(), getResources().getString(R.string.regstr_fgmt))
                        .addToBackStack(getResources().getString(R.string.regstr_fgmt))
                        .commit()
        );
        RxEventBus.add(this, disp1);

        Disposable disp2 = RxView.clicks(mBinding.signInBtn).subscribe(
                o -> mFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.loginContainer, SignInFrgmt.newInstance(100), getResources().getString(R.string.signin_fgmt))
                        .addToBackStack(getResources().getString(R.string.signin_fgmt))
                        .commit());
        RxEventBus.add(this, disp2);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxEventBus.unregister(this);
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
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

//    private static void addToDisposables(CompositeDisposable compDisp, Disposable disp) {
//        if (compDisp == null) compDisp = new CompositeDisposable();
//        compDisp.add(disp);
//    }

}
