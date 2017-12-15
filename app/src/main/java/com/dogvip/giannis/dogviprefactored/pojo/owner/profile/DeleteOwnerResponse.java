package com.dogvip.giannis.dogviprefactored.pojo.owner.profile;

import com.dogvip.giannis.dogviprefactored.pojo.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

/**
 * Created by giannis on 14/12/2017.
 */

public class DeleteOwnerResponse extends BaseResponse {

    @SerializedName("id")
    @Expose
    private int id;

    @Inject
    public DeleteOwnerResponse() {}

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

}
