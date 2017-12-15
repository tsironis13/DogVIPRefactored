package com.dogvip.giannis.dogviprefactored.retrofit;

import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.pojo.owner.form.SubmitOwnerFormRequest;
import com.dogvip.giannis.dogviprefactored.pojo.owner.profile.DeleteOwnerRequest;
import com.dogvip.giannis.dogviprefactored.pojo.sync.UploadFcmTokenRequest;
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

    @POST("actions.php")
    Flowable<Response> uploadFcmToken(@Body UploadFcmTokenRequest request);

    @POST("actions.php")
    Flowable<Response> syncUserRolesAndPets(@Body BaseRequest request);

    @POST("actions.php")
    Flowable<Response> submitOwnerForm(@Body SubmitOwnerFormRequest request);

    @POST("actions.php")
    Flowable<Response> fetchOwnerFormDetails(@Body BaseRequest request);

    @POST("actions.php")
    Flowable<Response> fetchOwnerProfileDetails(@Body BaseRequest request);

    @POST("actions.php")
    Flowable<Response> deleteOwner(@Body DeleteOwnerRequest request);

}
