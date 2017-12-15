package com.dogvip.giannis.dogviprefactored.requestmanager;

import android.util.Log;

import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.login.signup.RegistrationViewModel;
import com.dogvip.giannis.dogviprefactored.networkservice.DashboardAPIService;
import com.dogvip.giannis.dogviprefactored.networkservice.OwnerFormAPIService;
import com.dogvip.giannis.dogviprefactored.owner.form.OwnerFormViewModel;
import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.pojo.login.signup.SignUpEmailRequest;
import com.dogvip.giannis.dogviprefactored.pojo.owner.form.SubmitOwnerFormRequest;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.DogVipRoomDatabase;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserRole;

import java.lang.ref.PhantomReference;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by giannis on 9/12/2017.
 */

public class OwnerFormRequestManager {
    private static final String debugTag = OwnerFormRequestManager.class.getSimpleName();
    private OwnerFormAPIService mOwnerFormAPIService;
    @Inject
    DogVipRoomDatabase dogVipRoomDatabase;

    @Inject
    public OwnerFormRequestManager(OwnerFormAPIService ownerFormAPIService) {
        this.mOwnerFormAPIService = ownerFormAPIService;
    }

    public Flowable<Response> submitNewOwnerForm(SubmitOwnerFormRequest request, OwnerFormViewModel viewModel) {
        //in case server response is faster than activity lifecycle callback methods
        return mOwnerFormAPIService
                                .submitOwnerForm(request, viewModel)
                                .delay(300, TimeUnit.MILLISECONDS)
                                .doAfterNext(this::insertUserRole);
    }

    public Flowable<Response> editOwnerForm(SubmitOwnerFormRequest request, OwnerFormViewModel viewModel) {
        //in case server response is faster than activity lifecycle callback methods
        return mOwnerFormAPIService
                                .submitOwnerForm(request, viewModel)
                                .delay(300, TimeUnit.MILLISECONDS)
                                .doAfterNext(this::updateUserRole);
    }

    public Flowable<Response> fetchOwnerFormDetails(BaseRequest request, OwnerFormViewModel viewModel) {
        return mOwnerFormAPIService
                                .fetchOwnerFormDetails(request, viewModel)
                                .delay(300, TimeUnit.MILLISECONDS)
                                .doAfterNext(this::updateUserRole);
    }

    private void insertUserRole(Response response) {
        if (response.getCode() == AppConfig.STATUS_OK) {
            dogVipRoomDatabase.userRoleDao().insertUserRole(response.getOwnerData().getData());
//            dogVipRoomDatabase.userRoleDao().fetchOwnerDetails(66, 1).subscribe(new Consumer<UserRole>() {
//                @Override
//                public void accept(UserRole userRole) throws Exception {
//                    Log.e(debugTag, userRole.getId() + " "+  userRole.getUser_id() + " "+ userRole.getName() + " "+ userRole.getSurname());
//                }
//            });
            dogVipRoomDatabase.stateEntityDao().updateUserStateEntity(System.currentTimeMillis()/1000L, new int[]{1});
        }
    }

    private void updateUserRole(Response response) {
        if (response.getCode() == AppConfig.STATUS_OK) {
            int id = response.getOwnerData().getData().getId();
//            int userId = response.getOwnerData().getData().getUser_id();
            String name = response.getOwnerData().getData().getName();
            String surname = response.getOwnerData().getData().getSurname();
            String city = response.getOwnerData().getData().getCity();
            String age = response.getOwnerData().getData().getAge();
            String phone = response.getOwnerData().getData().getMobile_number();
            String image_url = response.getOwnerData().getData().getImage_url();
//            Log.e(debugTag, image_url + "UPDATE imageURL");
            dogVipRoomDatabase.userRoleDao().updateUserRole(id, name, surname, city, age, phone, image_url, 1);
            dogVipRoomDatabase.stateEntityDao().updateUserStateEntity(System.currentTimeMillis()/1000L, new int[]{1});
        }
    }

}
