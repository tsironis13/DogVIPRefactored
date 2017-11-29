package com.dogvip.giannis.dogviprefactored.pojo.login;

import com.dogvip.giannis.dogviprefactored.pojo.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giannis on 23/10/2017.
 */

public class LoginResponse extends BaseResponse {

    @SerializedName("authtoken")
    @Expose
    private String authtoken;
    @SerializedName("email")
    @Expose
    private String email;

    public String getAuthtoken() { return authtoken; }

    public void setAuthtoken(String authtoken) { this.authtoken = authtoken; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}
