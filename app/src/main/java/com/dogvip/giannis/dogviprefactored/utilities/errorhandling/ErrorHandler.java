package com.dogvip.giannis.dogviprefactored.utilities.errorhandling;

import javax.inject.Inject;

/**
 * Created by giannis on 13/12/2017.
 */

public class ErrorHandler {

    private int retryCount;
    private Throwable throwable;

    @Inject
    public ErrorHandler() {}

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

}
