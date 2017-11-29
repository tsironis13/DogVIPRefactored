package com.dogvip.giannis.dogviprefactored.login;

import com.dogvip.giannis.dogviprefactored.lifecycle.Lifecycle;
import com.dogvip.giannis.dogviprefactored.pojo.BaseResponse;
import com.dogvip.giannis.dogviprefactored.pojo.login.LoginResponse;
import com.dogvip.giannis.dogviprefactored.pojo.login.SignInEmailRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.SignInUpFbGoogleRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.forgotpass.ForgotPassRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.forgotpass.ForgotPassResponse;
import com.dogvip.giannis.dogviprefactored.pojo.login.signup.SignUpEmailRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

/**
 * Created by giannis on 4/11/2017.
 */

public interface LoginContract {

    interface View extends Lifecycle.View {
        void onError(int resource);
        void onProcessing();
        void onStopProcessing();
    }

    interface SignInUpFbGoogleView extends View {
        void onSuccessFbLogin(LoginResponse response);
        void onSuccessGoogleLogin(LoginResponse response);
        void onErrorFbLogin(int resource);
        void onErrorGoogleLogin(int resource);
    }

    interface SignInView extends SignInUpFbGoogleView {
        void onSuccessEmailSignIn(LoginResponse response);
    }

    interface SignUpView extends SignInUpFbGoogleView {
        void onSuccessEmailSignUp(BaseResponse response);
    }

    interface ForgotPassView extends View {
        void onSuccessIsEmailValid(ForgotPassResponse response);
        void onSuccessNewPassChange(BaseResponse response);
        void onError(int resource);
    }

    interface ViewModel extends Lifecycle.ViewModel {
        void onProcessing();
        void setRequestState(int state);
    }

    interface SignInViewModel extends ViewModel {
        void handleGoogleSignInResult(GoogleSignInResult result, SignInUpFbGoogleRequest request);
        void handleFbSignInResult(String email, SignInUpFbGoogleRequest request);
        void signInEmail(SignInEmailRequest request);
    }

    interface SignUpViewModel extends ViewModel {
        void handleGoogleSignUpResult(GoogleSignInResult result, SignInUpFbGoogleRequest request);
        void handleFbSignUpResult(String email, SignInUpFbGoogleRequest request);
        void signUpEmail(SignUpEmailRequest request);
    }

    interface ForgotPassViewModel extends ViewModel {
        void handleUserInputAction(ForgotPassRequest request);
    }

}
