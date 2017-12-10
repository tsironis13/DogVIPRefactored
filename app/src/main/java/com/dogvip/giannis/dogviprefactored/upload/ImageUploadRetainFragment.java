package com.dogvip.giannis.dogviprefactored.upload;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.dogvip.giannis.dogviprefactored.owner.form.OwnerFormViewModel;

import javax.inject.Inject;

/**
 * Created by giannis on 9/12/2017.
 */

public class ImageUploadRetainFragment extends Fragment {

    private ImageUploadViewModel mImageUploadViewModel;

    @Inject
    public ImageUploadRetainFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    public void retainViewModel(ImageUploadViewModel imageUploadViewModel) {
        this.mImageUploadViewModel = imageUploadViewModel;
    }

    public ImageUploadViewModel getViewModel() {
        return this.mImageUploadViewModel;
    }

}
