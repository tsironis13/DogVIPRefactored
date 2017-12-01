package com.dogvip.giannis.dogviprefactored.utilities.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.dogvip.giannis.dogviprefactored.R;

import javax.inject.Inject;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

/**
 * Created by giannis on 1/12/2017.
 */

public class NotificationUtls {

    private Context mContext;
    @Inject
    NotificationCompat.Builder mBuilder;
    @Inject
    NotificationManager mNotificationManager;

    @Inject
    public NotificationUtls(Context context) {
        this.mContext = context;
    }

    public void buildNotification(String title, String msg) {
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title);
        mBuilder.setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE);
        mBuilder.setContentText(msg);

        mNotificationManager.notify(100, mBuilder.build());
    }

}
