package com.dogvip.giannis.dogviprefactored.pojo.owner.form;

import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserRole;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

/**
 * Created by giannis on 11/12/2017.
 */

public class SubmitOwnerFormRequest extends BaseRequest {

    @SerializedName("user_role")
    @Expose
    private UserRole data;

    @Inject
    public SubmitOwnerFormRequest() {}

    public UserRole getUserRole() { return data; }

    public void setUserRole(UserRole userRole) { this.data = userRole; }
}
