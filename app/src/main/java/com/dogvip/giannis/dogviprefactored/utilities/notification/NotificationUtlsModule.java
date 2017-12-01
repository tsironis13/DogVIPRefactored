package com.dogvip.giannis.dogviprefactored.utilities.notification;

import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.dogvip.giannis.dogviprefactored.R;

import dagger.Module;
import dagger.Provides;

/**
 * Created by giannis on 1/12/2017.
 */
@Module
public abstract class NotificationUtlsModule {

    @Provides
    static NotificationCompat.Builder provideNotificationBuilder(Context context) {
        return new NotificationCompat.Builder(context, context.getResources().getString(R.string.default_notification_channel_id));
    }

    @Provides
    static NotificationManager provideNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

}
