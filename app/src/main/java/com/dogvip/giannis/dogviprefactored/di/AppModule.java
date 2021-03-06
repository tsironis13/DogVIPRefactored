package com.dogvip.giannis.dogviprefactored.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.dogvip.giannis.dogviprefactored.app.MyApp;
import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.di.qualifiers.ApplicationContext;
import com.dogvip.giannis.dogviprefactored.retrofit.ServiceAPI;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.DogVipRoomDatabase;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by giannis on 4/11/2017.
 */
@Module(includes = AndroidInjectionModule.class)
public class AppModule {

    @Provides
    Application application(MyApp myApp) {
        return myApp;
    }

    @Provides
    @ApplicationContext
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(httpLoggingInterceptor)
//                .addInterceptor(new MOck())
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    ServiceAPI provideServiceAPI(Retrofit retrofit) {
        return retrofit.create(ServiceAPI.class);
    }

    @Provides
    @Singleton
    DogVipRoomDatabase provideDogVipRoomDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, DogVipRoomDatabase.class, "dogVIP_database").build();
    }

}
