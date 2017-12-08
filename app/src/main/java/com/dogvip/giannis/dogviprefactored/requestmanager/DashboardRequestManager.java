package com.dogvip.giannis.dogviprefactored.requestmanager;

import com.dogvip.giannis.dogviprefactored.networkservice.DashboardAPIService;

import javax.inject.Inject;

/**
 * Created by giannis on 5/12/2017.
 */

public class DashboardRequestManager {

    private DashboardAPIService mDashboardAPIService;

    @Inject
    public DashboardRequestManager(DashboardAPIService dashboardAPIService) {
        this.mDashboardAPIService = dashboardAPIService;
    }

//    public Flowable<Response> uploadFcmToken(UploadFcmTokenRequest request, DashboardViewModel viewModel) {
//        //in case server response is faster than activity lifecycle callback methods
//        return mDashboardAPIService.uploadFcmToken(request, viewModel);
//    }

}
