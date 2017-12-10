package com.dogvip.giannis.dogviprefactored.networkservice;

import com.dogvip.giannis.dogviprefactored.retrofit.ServiceAPI;

import javax.inject.Inject;

/**
 * Created by giannis on 9/12/2017.
 */

public class OwnerFormAPIService {

    private ServiceAPI mServiceAPI;

    @Inject
    public OwnerFormAPIService(ServiceAPI serviceAPI) {
        this.mServiceAPI = serviceAPI;
    }

}
