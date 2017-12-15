package com.dogvip.giannis.dogviprefactored.roompersistencedata.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import javax.inject.Inject;

/**
 * Created by giannis on 4/12/2017.
 */
@Entity
public class UserDevice {

    @Inject
    public UserDevice() {}

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;
    private int user_id;
    private String device_model;
    @Ignore
    private int logged_in_at;
    private String fcm_registration_token;
    private boolean token_synced;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUser_id() { return user_id; }

    public void setUser_id(int userId) { this.user_id = userId; }

    public String getDevice_model() { return device_model; }

    public void setDevice_model(String deviceModel) { this.device_model = deviceModel; }

    public int getLogged_in_at() { return logged_in_at; }

    public void setLogged_in_at(int loggedInAt) { this.logged_in_at = loggedInAt; }

    public String getFcm_registration_token() { return fcm_registration_token; }

    public void setFcm_registration_token(String fcmRegistrationToken) { this.fcm_registration_token = fcmRegistrationToken; }

    public boolean isToken_synced() { return token_synced; }

    public void setToken_synced(boolean tokenSynced) { this.token_synced = tokenSynced; }

}
