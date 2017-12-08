package com.dogvip.giannis.dogviprefactored.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giannis on 4/11/2017.
 */

public class BaseResponse {

    @SerializedName("code")
    @Expose
    private int code;

    public int getCode() { return code; }

    public void setCode(int code) { this.code = code; }

}
