package org.sristi.sristi;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.sristi.sristi.utils.Utils;
import org.sristi.sristi.utils.VLog;

import java.io.File;
import java.io.FileInputStream;

public class SristiService extends Service {

    private Thread appStarterNotificationHandlerThread = null;

    public SristiService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int  onStartCommand(final Intent i, int flags, final int startId) {

        try {

            if(appStarterNotificationHandlerThread == null) {
                appStarterNotificationHandlerThread = new Thread(new AppStarterNotificationHandler());
                appStarterNotificationHandlerThread.start();
            } else {
                if (!appStarterNotificationHandlerThread.isAlive()) {
                    try {
                        appStarterNotificationHandlerThread.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    appStarterNotificationHandlerThread = new Thread(new AppStarterNotificationHandler());
                    appStarterNotificationHandlerThread.start();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    public class AppStarterNotificationHandler implements  Runnable {

        public void run() {
            try {

                while (true) {
                    try {

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                        int notif_id = Utils.AppStarterNotificationId;
                        builder.setContentTitle("Sristi 2k16 : Largest Tech Fest in North Bengal");
                        builder.setContentText("Tap to start");
                        builder.setSmallIcon(R.drawable.icon_app_32_32_2);
                        Intent resultIntent = new Intent(getApplicationContext(), AppLoadingActivity.class);
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                        stackBuilder.addParentStack(AppLoadingActivity.class);
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(resultPendingIntent);
                        builder.setAutoCancel(false);
                        builder.setOngoing(true);
                        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(notif_id, builder.build());

                        Thread.sleep(1000*60*300);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                Utils.showModifiedToast(getApplication(), e);
            }
        }

    }



}


















