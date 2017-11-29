package com.dogvip.giannis.dogviprefactored.responsecontroller;

import com.dogvip.giannis.dogviprefactored.pojo.Response;

import javax.inject.Inject;

/**
 * Created by giannis on 9/10/2017.
 */

public class ResponseController {

    private Command command;

    @Inject
    public ResponseController() {}

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommandOnSuccess(Response response) {
//        Log.e("command execute", loveMatch +  "sdksd");
        command.executeOnSuccess(response);
    }

    public void executeCommandOnError(int resource) {
        command.executeOnError(resource);
    }
}
