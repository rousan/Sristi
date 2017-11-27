package org.sristi.sristi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            Intent intent_event_updater_service = new Intent();
            intent_event_updater_service.setClassName("org.sristi.sristi", "org.sristi.sristi.EventUpdaterService");
            context.startService(intent_event_updater_service);

            Intent intent_app_updater_service = new Intent();
            intent_app_updater_service.setClassName("org.sristi.sristi", "org.sristi.sristi.AppUpdaterService");
            context.startService(intent_app_updater_service);

            Intent intent_app_starter_notification_service = new Intent();
            intent_app_updater_service.setClassName("org.sristi.sristi", "org.sristi.sristi.SristiService");
            context.startService(intent_app_updater_service);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
