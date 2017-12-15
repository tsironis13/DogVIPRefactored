package com.dogvip.giannis.dogviprefactored.pojo;

import com.dogvip.giannis.dogviprefactored.pojo.login.LoginResponse;
import com.dogvip.giannis.dogviprefactored.pojo.login.forgotpass.ForgotPassResponse;
import com.dogvip.giannis.dogviprefactored.pojo.owner.form.SubmitOwnerFormResponse;
import com.dogvip.giannis.dogviprefactored.pojo.owner.profile.DeleteOwnerResponse;
import com.dogvip.giannis.dogviprefactored.pojo.owner.profile.OwnerProfileDetailsResponse;
import com.dogvip.giannis.dogviprefactored.pojo.sync.SyncUserRolesAndPetsResponse;

import javax.inject.Inject;

/**
 * Created by giannis on 4/11/2017.
 */

public class Response extends BaseResponse {

    private LoginResponse login;
    private ForgotPassResponse forgotPass;
    private SyncUserRolesAndPetsResponse userRolesPets;
    private SubmitOwnerFormResponse ownerFormData;
    private OwnerProfileDetailsResponse ownerProfileData;
    private DeleteOwnerResponse deleteOwner;
//    private GetPetsResponse petdata;
//    private LikeDislikeResponse likedata;

    @Inject
    public Response() {}

    public LoginResponse getLogin() { return login; }

    public void setLogin(LoginResponse login) { this.login = login; }

    public ForgotPassResponse getForgotPass() { return forgotPass; }

    public void setForgotPass(ForgotPassResponse forgotPass) { this.forgotPass = forgotPass; }

    public SyncUserRolesAndPetsResponse getUserRolesPets() { return userRolesPets; }

    public void setUserRolesPets(SyncUserRolesAndPetsResponse userRolesPets) { this.userRolesPets = userRolesPets; }

    public SubmitOwnerFormResponse getOwnerData() { return ownerFormData; }

    public void setOwnerData(SubmitOwnerFormResponse ownerData) { this.ownerFormData = ownerData; }

    public OwnerProfileDetailsResponse getOwnerProfileData() { return ownerProfileData; }

    public void setOwnerProfileData(OwnerProfileDetailsResponse ownerProfileData) { this.ownerProfileData = ownerProfileData; }

    public DeleteOwnerResponse getDeleteOwnerResponse() { return deleteOwner; }

    public void setDeleteOwnerResponse(DeleteOwnerResponse deleteOwnerResponse) { this.deleteOwner = deleteOwnerResponse; }

    //    public GetPetsResponse getPetdata() {
//        return petdata;
//    }
//
//    public void setPetdata(GetPetsResponse petdata) {
//        this.petdata = petdata;
//    }
//
//    public LikeDislikeResponse getLikeDislikeResponse() { return likedata; }
//
//    public void setLikeDislikeResponse(LikeDislikeResponse likedata) { this.likedata = likedata; }

}
