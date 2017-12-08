package com.dogvip.giannis.dogviprefactored.networkservice;

import com.dogvip.giannis.dogviprefactored.retrofit.ServiceAPI;

import javax.inject.Inject;

/**
 * Created by giannis on 5/12/2017.
 */

public class DashboardAPIService {

    private ServiceAPI mServiceAPI;

    @Inject
    public DashboardAPIService(ServiceAPI serviceAPI) {
        this.mServiceAPI = serviceAPI;
    }

}
