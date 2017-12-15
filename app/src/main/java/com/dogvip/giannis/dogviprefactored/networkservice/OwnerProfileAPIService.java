package com.dogvip.giannis.dogviprefactored.networkservice;

import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.owner.form.OwnerFormViewModel;
import com.dogvip.giannis.dogviprefactored.owner.profile.OwnerProfileViewModel;
import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.pojo.owner.form.SubmitOwnerFormRequest;
import com.dogvip.giannis.dogviprefactored.pojo.owner.profile.DeleteOwnerRequest;
import com.dogvip.giannis.dogviprefactored.retrofit.ServiceAPI;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by giannis on 11/12/2017.
 */

public class OwnerProfileAPIService {

    private ServiceAPI mServiceAPI;

    @Inject
    public OwnerProfileAPIService(ServiceAPI serviceAPI) {
        this.mServiceAPI = serviceAPI;
    }

    public Flowable<Response> fetchOwnerProfileDetails(final BaseRequest request, final OwnerProfileViewModel viewModel) {
        return mServiceAPI.fetchOwnerProfileDetails(request)
                                    .doOnSubscribe(subscription -> viewModel.setRequestState(AppConfig.REQUEST_RUNNING))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Response> deleteOwner(final DeleteOwnerRequest request, final OwnerProfileViewModel viewModel) {
        return mServiceAPI.deleteOwner(request)
                                    .doOnSubscribe(subscription -> viewModel.setRequestState(AppConfig.REQUEST_RUNNING))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());
    }

}
