package com.dogvip.giannis.dogviprefactored.pojo.sync;

import android.os.Parcel;
import android.os.Parcelable;

import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import javax.inject.Inject;

/**
 * Created by giannis on 5/12/2017.
 */

public class UploadFcmTokenRequest extends BaseRequest {

    @SerializedName("deviceid")
    @Expose
    private String deviceid;
    @SerializedName("fcmtoken")
    @Expose
    private String fcmtoken;

    @Inject
    public UploadFcmTokenRequest() {}

    public String getDeviceid() { return deviceid; }

    public void setDeviceid(String deviceId) { this.deviceid = deviceId; }

    public String getFcmtoken() { return fcmtoken; }

    public void setFcmtoken(String fcmToken) { this.fcmtoken = fcmToken; }

}
