package com.dogvip.giannis.dogviprefactored.requestmanager;


import android.util.Log;

import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.login.forgotpass.ForgotPaswrdViewModel;
import com.dogvip.giannis.dogviprefactored.login.signin.SignInViewModel;
import com.dogvip.giannis.dogviprefactored.login.signup.RegistrationViewModel;
import com.dogvip.giannis.dogviprefactored.networkservice.LoginAPIService;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.pojo.login.SignInEmailRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.SignInUpFbGoogleRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.forgotpass.ForgotPassRequest;
import com.dogvip.giannis.dogviprefactored.pojo.login.signup.SignUpEmailRequest;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.DogVipRoomDatabase;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserData;
import com.dogvip.giannis.dogviprefactored.services.JobConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by giannis on 23/5/2017.
 */

public class LoginRequestManager {
    private static final String debugTag = LoginRequestManager.class.getSimpleName();
    private LoginAPIService mLoginAPIService;
    @Inject
    DogVipRoomDatabase dogVipRoomDatabase;
    @Inject
    JobConfiguration mJobConfiguration;

    @Inject
    public LoginRequestManager(LoginAPIService loginAPIService) {
        this.mLoginAPIService = loginAPIService;
    }

    public Flowable<Response> signUpEmail(SignUpEmailRequest request, RegistrationViewModel viewModel) {
        //in case server response is faster than activity lifecycle callback methods
        return mLoginAPIService.signUpEmail(request, viewModel).delay(600, TimeUnit.MILLISECONDS);
    }

    public Flowable<Response> signInEmail(SignInEmailRequest request, SignInViewModel viewModel) {
        //in case server response is faster than activity lifecycle callback methods
        return mLoginAPIService
                        .signInEmail(request, viewModel)
                        .delay(300, TimeUnit.MILLISECONDS)
                        .doAfterNext(this::insertUserRolesPets);
    }

    public Flowable<Response> signInFbGoogle(SignInUpFbGoogleRequest request, SignInViewModel viewModel) {
        return mLoginAPIService
                        .signInFbGoogle(request, viewModel)
                        .delay(300, TimeUnit.MILLISECONDS)
                        .doAfterNext(this::insertUserRolesPets);
    }

    public Flowable<Response> signUpFbGoogle(SignInUpFbGoogleRequest request, RegistrationViewModel viewModel) {
        return mLoginAPIService
                        .signUpFbGoogle(request, viewModel)
                        .delay(300, TimeUnit.MILLISECONDS)
                        .doAfterNext(this::insertUserRolesPets);
    }

    public Flowable<Response> forgotPass(ForgotPassRequest request, ForgotPaswrdViewModel viewModel) {
        //in case server response is faster than activity lifecycle callback methods
        return mLoginAPIService.forgotPass(request, viewModel).delay(500, TimeUnit.MILLISECONDS);
    }

    private void insertUserRolesPets(Response response) {
        if (response.getCode() == AppConfig.STATUS_OK) {
//            stateEntity.setId(1);
//            stateEntity.setUpdated_at(System.currentTimeMillis()/1000);
            dogVipRoomDatabase.stateEntityDao().insertData(userRolesEntities());
//            Log.e(debugTag, a + "ABD");
            mJobConfiguration.syncLocalUserRolesAndPets(response.getLogin().getAuthtoken());
            Log.e(debugTag, "execute commnad to start job: "+ System.currentTimeMillis()/1000L);
            if (!response.getLogin().getUserRoles().isEmpty()) {
                List<Long> rows = dogVipRoomDatabase
                                                .userRoleDao()
                                                .insertUserRoles(response.getLogin().getUserRoles());
                if (!rows.isEmpty()) {
                    dogVipRoomDatabase
                            .petDao()
                            .insertPet(response.getLogin().getOwnerPets());
                }
            }
//                Log.e(debugTag, petrows + " PET ROWS");
//                dogVipRoomDatabase.stateEntityDao().get().subscribe(stateEntities -> {
//                    for (UserData stateEntity: stateEntities) {
//                        Log.e(debugTag, "ID: "+stateEntity.getEntity_ID() + " UPDATE TIME: "+stateEntity.getUpdated_at());
//                    }
//                }, onError -> Log.e(debugTag, " onerror KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK"));
        }
    }

    private List<UserData> userRolesEntities() {
        List<UserData> entities = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            UserData stateEntity = new UserData();
            stateEntity.setData_type(i);
            stateEntity.setUpdated_at(System.currentTimeMillis()/1000L);
            entities.add(stateEntity);
        }
        return entities;
    }

}
