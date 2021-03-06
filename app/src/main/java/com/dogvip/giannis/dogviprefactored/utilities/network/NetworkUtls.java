package com.dogvip.giannis.dogviprefactored.utilities.network;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by giannis on 6/10/2017.
 */

public class NetworkUtls {

    private Context mContext;

    @Inject
    public NetworkUtls(Context context) {
        this.mContext = context;
    }

    public Flowable<Boolean> getNetworkFlowable() {
        return Flowable.create(e -> {
            if (isNetworkAvailable()) {
                e.onNext(true);
            } else {
                e.onError(new NetworkErrorException());
            }
        }, BackpressureStrategy.LATEST);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null ? cm.getActiveNetworkInfo() : null) != null && cm.getActiveNetworkInfo().isConnected();
    }

//    public Flowable<Boolean> isNetworkAvailable1() {
//        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//        return Flowable.just((cm != null ? cm.getActiveNetworkInfo() : null) != null && cm.getActiveNetworkInfo().isConnected());
//    }

}
