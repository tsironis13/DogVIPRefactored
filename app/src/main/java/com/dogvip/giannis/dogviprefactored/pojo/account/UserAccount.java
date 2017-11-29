package com.dogvip.giannis.dogviprefactored.pojo.account;

import javax.inject.Inject;

/**
 * Created by giannis on 21/6/2017.
 */

public class UserAccount {

    private String mtoken, email;

    @Inject
    public UserAccount() {}

    public String getToken() {
        return mtoken;
    }

    public void setToken(String mtoken) {
        this.mtoken = mtoken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
