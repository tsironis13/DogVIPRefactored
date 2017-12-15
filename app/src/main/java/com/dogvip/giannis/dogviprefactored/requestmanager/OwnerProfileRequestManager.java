package com.dogvip.giannis.dogviprefactored.requestmanager;

import android.util.Log;

import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.networkservice.OwnerProfileAPIService;
import com.dogvip.giannis.dogviprefactored.owner.profile.OwnerProfileViewModel;
import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.pojo.owner.profile.DeleteOwnerRequest;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.DogVipRoomDatabase;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by giannis on 11/12/2017.
 */

public class OwnerProfileRequestManager {
    private static final String debugTag = OwnerProfileRequestManager.class.getSimpleName();
    private OwnerProfileAPIService mOwnerProfileAPIService;
    @Inject
    DogVipRoomDatabase dogVipRoomDatabase;

    @Inject
    public OwnerProfileRequestManager(OwnerProfileAPIService ownerProfileAPIService) {
        this.mOwnerProfileAPIService = ownerProfileAPIService;
    }

    public Flowable<Response> fetchOwnerProfileDetails(BaseRequest request, OwnerProfileViewModel viewModel) {
        //in case server response is faster than activity lifecycle callback methods
        return mOwnerProfileAPIService
                                .fetchOwnerProfileDetails(request, viewModel)
                                .delay(300, TimeUnit.MILLISECONDS)
                                .doAfterNext(this::updateOwnerRolesPets);
    }

    public Flowable<Response> deleteOwner(DeleteOwnerRequest request, OwnerProfileViewModel viewModel) {
        //in case server response is faster than activity lifecycle callback methods
        return mOwnerProfileAPIService
                                .deleteOwner(request, viewModel)
                                .delay(300, TimeUnit.MILLISECONDS)
                                .doAfterNext(this::deleteLocalOwner);
    }

    private void updateOwnerRolesPets(Response response) {
        if (response.getCode() == AppConfig.STATUS_OK) {
            Log.e(debugTag, "updateOwnerRolesPets");
            int id = response.getOwnerProfileData().getData().getId();
            String name = response.getOwnerProfileData().getData().getName();
            String surname = response.getOwnerProfileData().getData().getSurname();
            String city = response.getOwnerProfileData().getData().getCity();
            String age = response.getOwnerProfileData().getData().getAge();
            String phone = response.getOwnerProfileData().getData().getMobile_number();
            String image_url = response.getOwnerProfileData().getData().getImage_url();
            int kal = dogVipRoomDatabase.userRoleDao().updateUserRole(id, name, surname, city, age, phone, image_url, 1);
            Log.e(debugTag, kal +  "USER ROLE");

            int stateENtity = dogVipRoomDatabase.stateEntityDao().updateUserStateEntity(System.currentTimeMillis()/1000L, new int[]{1});
            Log.e(debugTag, stateENtity +  "STATE ENTITY");

                if (!response.getOwnerProfileData().getOwnerPets().isEmpty()) {
                    dogVipRoomDatabase.petDao().deleteOwnerPet(response.getOwnerProfileData().getData().getId());
                    dogVipRoomDatabase.petDao().insertPet(response.getOwnerProfileData().getOwnerPets());
                } else {
                    Log.e(debugTag, "isEMpty pets");
                }
        }
    }

    private void deleteLocalOwner(Response response) {
        if (response.getCode() == AppConfig.STATUS_OK) {
            dogVipRoomDatabase.stateEntityDao().updateUserStateEntity(System.currentTimeMillis()/1000L, new int[]{1});
            dogVipRoomDatabase.userRoleDao().deleteUserRole(response.getDeleteOwnerResponse().getId(), 1);
        }
    }
}
