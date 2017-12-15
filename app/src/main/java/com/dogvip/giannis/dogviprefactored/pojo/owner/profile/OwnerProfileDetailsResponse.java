package com.dogvip.giannis.dogviprefactored.pojo.owner.profile;

import com.dogvip.giannis.dogviprefactored.pojo.BaseResponse;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.Pet;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserRole;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by giannis on 11/12/2017.
 */

public class OwnerProfileDetailsResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private UserRole data;

    @SerializedName("owner_pets")
    @Expose
    private List<Pet> ownerPets;

    @Inject
    public OwnerProfileDetailsResponse() {}

    public UserRole getData() { return data; }

    public void setData(UserRole data) { this.data = data; }

    public List<Pet> getOwnerPets() { return ownerPets; }

    public void setOwnerPets(List<Pet> ownerPets) { this.ownerPets = ownerPets; }
}
