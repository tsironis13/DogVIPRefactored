package com.dogvip.giannis.dogviprefactored.di;

import android.app.Application;

import com.dogvip.giannis.dogviprefactored.app.MyApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Created by giannis on 4/11/2017.
 */
@Singleton
@Component(modules = {AppModule.class, ActivityBuilder.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(MyApp application);

}
