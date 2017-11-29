package com.dogvip.giannis.dogviprefactored.pojo.login.signup;

import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

/**
 * Created by giannis on 4/11/2017.
 */

public class SignUpEmailRequest extends BaseRequest {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("confpassword")
    @Expose
    private String confpassword;
    @SerializedName("deviceid")
    @Expose
    private String deviceid;
    @SerializedName("regtype")
    @Expose
    private int regtype;

    @Inject
    public SignUpEmailRequest() {}

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getConfpassword() { return confpassword; }

    public void setConfpassword(String confpassword) { this.confpassword = confpassword; }

    public String getDeviceid() { return deviceid; }

    public void setDeviceid(String deviceid) { this.deviceid = deviceid; }

    public int getRegtype() { return regtype; }

    public void setRegtype(int regtype) { this.regtype = regtype; }

}
