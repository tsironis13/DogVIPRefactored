package com.dogvip.giannis.dogviprefactored.pojo.account;

import java.io.Serializable;

import javax.inject.Inject;

/**
 * Created by giannis on 21/6/2017.
 */

public class UserAccount {

    private String mtoken, email;
    private int userId;

    @Inject
    public UserAccount() {}

    public String getToken() {
        return mtoken;
    }

    public void setToken(String token) {
        this.mtoken = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }
}
