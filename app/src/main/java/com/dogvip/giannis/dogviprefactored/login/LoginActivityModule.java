package com.dogvip.giannis.dogviprefactored.login;

import android.app.Activity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.dogvip.giannis.dogviprefactored.base.activity.BaseActivityModule;
import com.dogvip.giannis.dogviprefactored.di.scope.PerActivity;
import com.dogvip.giannis.dogviprefactored.di.scope.PerFragment;
import com.dogvip.giannis.dogviprefactored.login.forgotpass.ForgotPaswrdFrgmt;
import com.dogvip.giannis.dogviprefactored.login.signin.SignInFrgmt;
import com.dogvip.giannis.dogviprefactored.login.signup.RegisterFrgmt;
import com.dogvip.giannis.dogviprefactored.splashscreen.SplashFrgmt;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by giannis on 4/11/2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class LoginActivityModule {

    @PerFragment
    @ContributesAndroidInjector
    abstract SplashFrgmt splashFrgmtInjector();

    //SignIn Fragment injector
    @PerFragment
    @ContributesAndroidInjector
    abstract SignInFrgmt signInFrgmtInjector();

    //Register Fragment injector
    @PerFragment
    @ContributesAndroidInjector
    abstract RegisterFrgmt registerFrgmtInjector();

    //ForgotPassword Fragment injector
    @PerFragment
    @ContributesAndroidInjector
    abstract ForgotPaswrdFrgmt forgotPaswrdFrgmtInjector();

    @Binds
    @PerActivity
    abstract Activity activity(LoginActivity loginActivity);

    @Provides
    static GoogleSignInOptions provideGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }

    @Provides
    static GoogleApiClient provideGoogleApiClient(GoogleSignInOptions googleSignInOptions, LoginActivity loginActivity) {
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        return new GoogleApiClient.Builder(loginActivity)
//                .enableAutoManage(loginActivity /* FragmentActivity */, loginActivity /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    @Provides
    static AwesomeValidation provideAwesomeValidation() {
        return new AwesomeValidation(ValidationStyle.BASIC);
    }

}
