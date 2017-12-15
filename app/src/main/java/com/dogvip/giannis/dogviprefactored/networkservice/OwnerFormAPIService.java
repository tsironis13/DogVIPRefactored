package com.dogvip.giannis.dogviprefactored.networkservice;

import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.login.signin.SignInViewModel;
import com.dogvip.giannis.dogviprefactored.owner.form.OwnerFormViewModel;
import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.dogvip.giannis.dogviprefactored.pojo.Response;
import com.dogvip.giannis.dogviprefactored.pojo.login.SignInEmailRequest;
import com.dogvip.giannis.dogviprefactored.pojo.owner.form.SubmitOwnerFormRequest;
import com.dogvip.giannis.dogviprefactored.retrofit.ServiceAPI;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by giannis on 9/12/2017.
 */

public class OwnerFormAPIService {

    private ServiceAPI mServiceAPI;

    @Inject
    public OwnerFormAPIService(ServiceAPI serviceAPI) {
        this.mServiceAPI = serviceAPI;
    }

    public Flowable<Response> submitOwnerForm(final SubmitOwnerFormRequest request, final OwnerFormViewModel viewModel) {
        return mServiceAPI.submitOwnerForm(request)
                .doOnSubscribe(subscription -> viewModel.setRequestState(AppConfig.REQUEST_RUNNING))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Response> fetchOwnerFormDetails(final BaseRequest request, final OwnerFormViewModel viewModel) {
        return mServiceAPI.fetchOwnerFormDetails(request)
                .doOnSubscribe(subscription -> viewModel.setRequestState(AppConfig.REQUEST_RUNNING))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
