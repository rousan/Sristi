package org.sristi.sristi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.sristi.sristi.utils.EventUpdatesDownloader;
import org.sristi.sristi.utils.FilesData;
import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.SettingsPreferenceKeys;
import org.sristi.sristi.utils.Utils;
import org.sristi.sristi.utils.VLog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

public class AppUpdaterService extends Service {

    private Thread ACTIVE_THREAD = null;

    public AppUpdaterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(final Intent i, int flags, final int startId) {
        try {

            if(ACTIVE_THREAD == null) {
                ACTIVE_THREAD = new Thread(new AppUpdateChecker());
                ACTIVE_THREAD.start();
            } else {
                if (!ACTIVE_THREAD.isAlive()) {
                    try {
                        ACTIVE_THREAD.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ACTIVE_THREAD = new Thread(new AppUpdateChecker());
                    ACTIVE_THREAD.start();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }


    public class AppUpdateChecker implements Runnable {

        public void run() {
            try {

                while (true) {
                    try {

                        if(!RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext())) {
                            continue;//no wait , immediete check
                        }

                        String outs = RemoteServerConstants.checkAppUpdate();
                        if(outs == null || outs.trim().isEmpty()) {
                            Thread.sleep(1*1000*60);
                            continue;
                        }
                       //Utils.showModifiedToast(getApplicationContext(), outs);
                        String parse_str = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + outs.trim();
                        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(parse_str)));
                        String version_code = ((Element)doc.getElementsByTagName("VERSIONCODE").item(0)).getTextContent();
                        String version_name = ((Element)doc.getElementsByTagName("VERSIONNAME").item(0)).getTextContent();
                        FilesData.setup(getApplicationContext());
                        File data_folder = getDir(FilesData.INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE);
                        File version_code_file = new File(data_folder.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_UPDATES + File.separator + FilesData.INTERNAL_FOLDER_APP_UPDATES + File.separator + FilesData.INTERNAL_FILE_APP_UPDATES_APP_UPDATED_VERSION_CODE);
                        File app_updated_details = new File(data_folder.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_UPDATES + File.separator + FilesData.INTERNAL_FOLDER_APP_UPDATES + File.separator + FilesData.INTERNAL_FILE_APP_UPDATES_APP_UPDATED_DETAILS);

                        FilesData.writeFile(version_code_file, version_code.trim(), false);
                        FilesData.writeFile(app_updated_details, outs.trim(), false);

                        String update_ver = FilesData.readFile(version_code_file);
                        try {

                            int update_ver_int = Integer.parseInt(update_ver);
                            if(update_ver_int > Utils.getAppVersionCode()) {

                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication());
                                int notif_id = Utils.AppDaterNotificationId;
                                builder.setContentTitle("New version is available");
                                builder.setContentText("Sristi v" + version_name);
                                builder.setSmallIcon(R.drawable.icon_app_32_32_2);
                                Intent resultIntent = new Intent(getBaseContext(), UpdateCenterActivity.class);
                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                                stackBuilder.addParentStack(UpdateCenterActivity.class);
                                stackBuilder.addNextIntent(resultIntent);
                                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                                builder.setContentIntent(resultPendingIntent);
                                builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                                builder.setAutoCancel(true);
                                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(notif_id, builder.build());
                                Utils.vibrate(getApplicationContext(), 500);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Thread.sleep(60*1000*10);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}

























