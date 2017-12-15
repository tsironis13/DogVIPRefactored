package com.dogvip.giannis.dogviprefactored.pojo.sync;

import com.dogvip.giannis.dogviprefactored.pojo.BaseResponse;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.Pet;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserRole;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by giannis on 8/12/2017.
 */

public class SyncUserRolesAndPetsResponse extends BaseResponse {

    @SerializedName("user_roles")
    @Expose
    private List<UserRole> userRoles;
    @SerializedName("owner_pets")
    @Expose
    private List<Pet> ownerPets;

    public List<UserRole> getUserRoles() { return userRoles; }

    public void setUserRoles(List<UserRole> userRoles) { this.userRoles = userRoles; }

    public List<Pet> getOwnerPets() { return ownerPets; }

    public void setOwnerPets(List<Pet> ownerPets) { this.ownerPets = ownerPets; }
}
