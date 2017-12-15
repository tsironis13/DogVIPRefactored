package com.dogvip.giannis.dogviprefactored.pojo.login;

import com.dogvip.giannis.dogviprefactored.pojo.BaseResponse;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.Pet;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserRole;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by giannis on 23/10/2017.
 */

public class LoginResponse extends BaseResponse {

    @SerializedName("authtoken")
    @Expose
    private String authtoken;
    @SerializedName("user_id")
    @Expose
    private int user_id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("user_roles")
    @Expose
    private List<UserRole> userRoles;
    @SerializedName("owner_pets")
    @Expose
    private List<Pet> ownerPets;

    public String getAuthtoken() { return authtoken; }

    public void setAuthtoken(String authtoken) { this.authtoken = authtoken; }

    public int getUser_id() { return user_id; }

    public void setUser_id(int user_id) { this.user_id = user_id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public List<UserRole> getUserRoles() { return userRoles; }

    public void setUserRoles(List<UserRole> userRoles) { this.userRoles = userRoles; }

    public List<Pet> getOwnerPets() { return ownerPets; }

    public void setOwnerPets(List<Pet> ownerPets) { this.ownerPets = ownerPets; }
}
