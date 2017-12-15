package com.dogvip.giannis.dogviprefactored.pojo.owner.profile;

import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

/**
 * Created by giannis on 13/12/2017.
 */

public class DeleteOwnerRequest extends BaseRequest {

    @SerializedName("id")
    @Expose
    private int id;

    @Inject
    public DeleteOwnerRequest() {}

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
