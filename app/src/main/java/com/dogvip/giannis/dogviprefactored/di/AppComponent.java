package com.dogvip.giannis.dogviprefactored.di;

import android.app.Application;

import com.dogvip.giannis.dogviprefactored.app.MyApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;

/**
 * Created by giannis on 4/11/2017.
 */
@Singleton
@Component(modules = {AppModule.class, ServiceModule.class, JobSchedulerModule.class, ActivityBuilder.class})
public interface AppComponent extends AndroidInjector<MyApp> {
    //OLD WAY INJECTING APPLICATION CONTEXT
//    @Component.Builder
//    interface Builder {
//        @BindsInstance
//        Builder application(Application application);
//        AppComponent build();
//    }
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<MyApp> {}

//    void inject(MyApp application);

}
