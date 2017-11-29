package com.dogvip.giannis.dogviprefactored.pojo.login.forgotpass;

import com.dogvip.giannis.dogviprefactored.pojo.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giannis on 1/11/2017.
 */

public class ForgotPassResponse extends BaseResponse {

    @SerializedName("user_id")
    @Expose
    private int userId;

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }
}
