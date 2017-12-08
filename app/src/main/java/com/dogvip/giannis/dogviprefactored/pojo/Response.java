package com.dogvip.giannis.dogviprefactored.pojo;

import com.dogvip.giannis.dogviprefactored.pojo.login.LoginResponse;
import com.dogvip.giannis.dogviprefactored.pojo.login.forgotpass.ForgotPassResponse;
import com.dogvip.giannis.dogviprefactored.pojo.sync.SyncUserRolesAndPetsResponse;

/**
 * Created by giannis on 4/11/2017.
 */

public class Response extends BaseResponse {

    private LoginResponse login;
    private ForgotPassResponse forgotPass;
    private SyncUserRolesAndPetsResponse userRolesPets;
//    private GetPetsResponse petdata;
//    private LikeDislikeResponse likedata;

    public LoginResponse getLogin() { return login; }

    public void setLogin(LoginResponse login) { this.login = login; }

    public ForgotPassResponse getForgotPass() { return forgotPass; }

    public void setForgotPass(ForgotPassResponse forgotPass) { this.forgotPass = forgotPass; }

    public SyncUserRolesAndPetsResponse getUserRolesPets() { return userRolesPets; }

    public void setUserRolesPets(SyncUserRolesAndPetsResponse userRolesPets) { this.userRolesPets = userRolesPets; }

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
