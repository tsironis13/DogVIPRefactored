package com.dogvip.giannis.dogviprefactored.pojo.login.forgotpass;

import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

/**
 * Created by giannis on 1/11/2017.
 */

public class ForgotPassRequest extends BaseRequest {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("newpassword")
    @Expose
    private String newpassword;
    @SerializedName("conf_newpassword")
    @Expose
    private String confNewpassword;
    @SerializedName("user_id")
    @Expose
    private int userId;
    transient private boolean emailAuthenticated;

    @Inject
    public ForgotPassRequest() {}

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getNewpassword() { return newpassword; }

    public void setNewpassword(String newpassword) { this.newpassword = newpassword; }

    public String getConfNewpassword() { return confNewpassword; }

    public void setConfNewpassword(String confNewpassword) { this.confNewpassword = confNewpassword; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public boolean isEmailAuthenticated() { return emailAuthenticated; }

    public void setEmailAuthenticated(boolean emailAuthenticated) { this.emailAuthenticated = emailAuthenticated; }
}
