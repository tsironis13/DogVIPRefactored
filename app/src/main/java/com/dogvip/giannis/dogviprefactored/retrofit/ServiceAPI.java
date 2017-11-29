package com.dogvip.giannis.dogviprefactored.retrofit;

import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.pojo.login.SignInEmailRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.SignInUpFbGoogleRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.forgotpass.ForgotPassRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.signup.SignUpEmailRequest;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by giannis on 4/11/2017.
 */

public interface ServiceAPI {

    @POST("actions.php")
    Flowable<Response> signInEmail(@Body SignInEmailRequest request);

    @POST("actions.php")
    Flowable<Response> signUpEmail(@Body SignUpEmailRequest request);

    //register user fb, google
    @POST("actions.php")
    Flowable<Response> signInUpFbGoogle(@Body SignInUpFbGoogleRequest request);

    @POST("actions.php")
    Flowable<Response> forgotPass(@Body ForgotPassRequest request);

}
