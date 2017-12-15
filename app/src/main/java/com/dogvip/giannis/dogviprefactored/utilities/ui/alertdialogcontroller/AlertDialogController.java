package com.dogvip.giannis.dogviprefactored.utilities.ui.alertdialogcontroller;

import java.io.Serializable;

import javax.inject.Inject;

/**
 * Created by giannis on 14/12/2017.
 */

public class AlertDialogController {

    private AlertDialogCommand command;

    @Inject
    public AlertDialogController() {}

    public AlertDialogCommand getCommand() {
        return command;
    }

    public void setCommand(AlertDialogCommand command) {
        this.command = command;
    }

    public void executeCommandOnPositiveAction() {
//        Log.e("command execute", loveMatch +  "sdksd");
        command.onPositiveAction();
    }

    public void executeCommandOnNegativeAction() {
        command.onNegativeAction();
    }

}
