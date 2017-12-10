package com.dogvip.giannis.dogviprefactored.requestmanager;

import com.dogvip.giannis.dogviprefactored.networkservice.DashboardAPIService;
import com.dogvip.giannis.dogviprefactored.networkservice.OwnerFormAPIService;

import javax.inject.Inject;

/**
 * Created by giannis on 9/12/2017.
 */

public class OwnerFormRequestManager {

    private OwnerFormAPIService mOwnerFormAPIService;

    @Inject
    public OwnerFormRequestManager(OwnerFormAPIService ownerFormAPIService) {
        this.mOwnerFormAPIService = ownerFormAPIService;
    }

}
