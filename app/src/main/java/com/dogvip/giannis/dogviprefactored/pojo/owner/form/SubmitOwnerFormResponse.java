package com.dogvip.giannis.dogviprefactored.pojo.owner.form;

import com.dogvip.giannis.dogviprefactored.pojo.BaseResponse;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserRole;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

/**
 * Created by giannis on 11/12/2017.
 */

public class SubmitOwnerFormResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private UserRole data;

    @Inject
    public SubmitOwnerFormResponse() {}

    public UserRole getData() { return data; }

    public void setData(UserRole data) { this.data = data; }
}
