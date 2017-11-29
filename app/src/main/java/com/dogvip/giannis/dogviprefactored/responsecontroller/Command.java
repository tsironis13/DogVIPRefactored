package com.dogvip.giannis.dogviprefactored.responsecontroller;


import com.dogvip.giannis.dogviprefactored.pojo.Response;

/**
 * Created by giannis on 9/10/2017.
 */

public interface Command {

    void executeOnSuccess(Response response);
    void executeOnError(int resource);

}
