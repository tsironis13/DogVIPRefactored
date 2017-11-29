package com.dogvip.giannis.dogviprefactored.utilities.animation;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation;

import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.utilities.eventbus.RxEventBus;

import javax.inject.Inject;

/**
 * Created by giannis on 4/11/2017.
 */

public class AnimationListener extends Animation implements Animation.AnimationListener {

    @Inject
    public AnimationListener() {}

    @Override
    public void onAnimationStart(Animation animation) {
        RxEventBus.createSubject(AppConfig.FRAGMENT_ANIMATION, AppConfig.PUBLISH_SUBJ).post(true);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        RxEventBus.createSubject(AppConfig.FRAGMENT_ANIMATION, AppConfig.PUBLISH_SUBJ).post(false);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

}
