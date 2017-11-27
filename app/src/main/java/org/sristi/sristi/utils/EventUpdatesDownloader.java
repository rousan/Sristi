package org.sristi.sristi.utils;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.sristi.sristi.HomeActivity;
import org.sristi.sristi.R;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * Created by Ariyan Khan on 09-03-2016.
 */
public class EventUpdatesDownloader implements Runnable {

    private Element notificationElement = null;
    private Context context = null;

    public EventUpdatesDownloader(Context context, Element notificationElement) {
        this.notificationElement = notificationElement;
        this.context = context;
    }

    public void run() {
        try {

            String id_elem_value = "0"; //default
            String title_elem_value = "Invalid";
            String shortnote_elem_value = "invalid";
            String imageurl_elem_value = "invalid";
            String htmlurl_elem_value = "invalid";
            String date_elem_value = "00-00-00-00-0000"; //format MINUTE-HOUR-DAY-MONTH-YEAR

            try {
                id_elem_value = ((Element)notificationElement.getElementsByTagName("ID").item(0)).getTextContent();
                title_elem_value = ((Element)notificationElement.getElementsByTagName("TITLE").item(0)).getTextContent();
                shortnote_elem_value = ((Element)notificationElement.getElementsByTagName("SHORTNOTE").item(0)).getTextContent();
                imageurl_elem_value = ((Element)notificationElement.getElementsByTagName("IMAGEURL").item(0)).getTextContent();
                htmlurl_elem_value = ((Element)notificationElement.getElementsByTagName("HTMLURL").item(0)).getTextContent();
                date_elem_value = ((Element)notificationElement.getElementsByTagName("DATE").item(0)).getTextContent();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FilesData.setup(context);
            File notif_folder = new File(context.getDir(FilesData.INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE).getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_UPDATES + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATIONS);
            //notif_folder = new File(Environment.getExternalStorageDirectory(), "notif");
            notif_folder.mkdirs();
            File id_file = new File(notif_folder, id_elem_value);
            if(id_file.isDirectory()) {
                return;//thread stop
            } else {
                id_file.mkdirs();
            }

            FilesData.writeFile(new File(id_file, FilesData.INTERNAL_FILE_NOTIFICATION_TITLE), title_elem_value, false);
            FilesData.writeFile(new File(id_file, FilesData.INTERNAL_FILE_NOTIFICATION_DATE), date_elem_value, false);
            FilesData.writeFile(new File(id_file, FilesData.INTERNAL_FILE_NOTIFICATION_SHORT_NOTE), shortnote_elem_value, false);
            new File(id_file.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATION_IMAGE).mkdirs();//must
            new File(id_file.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATION_HTML).mkdirs();//must
            FilesData.writeFile(new File(id_file.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATION_IMAGE + File.separator + FilesData.INTERNAL_FILE_NOTIFICATION_IMAGE_URL), imageurl_elem_value, false);
            FilesData.writeFile(new File(id_file.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATION_HTML + File.separator + FilesData.INTERNAL_FILE_NOTIFICATION_HTML_URL), htmlurl_elem_value, false);

            try {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                int notif_id = Utils.AppNotificationsNotificationId;
                builder.setContentTitle(title_elem_value);
                builder.setContentText(shortnote_elem_value);
                builder.setSmallIcon(R.drawable.icon_app_32_32_2);
                Intent resultIntent = new Intent(context, HomeActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(HomeActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(resultPendingIntent);
                builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                builder.setAutoCancel(true);
                NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notif_id, builder.build());
                Utils.vibrate(context, 500);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                File image_data_file = new File(id_file.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATION_IMAGE + File.separator + FilesData.INTERNAL_FILE_NOTIFICATION_IMAGE);
                final FileOutputStream fos = new FileOutputStream(image_data_file);
                File downloadsDir = new File(Environment.getExternalStorageDirectory(), "Downloads");
                if (!downloadsDir.isDirectory())
                    downloadsDir.mkdirs();
                final File tempFile = new File(downloadsDir, UUID.randomUUID().toString() + ".tmp");
                tempFile.createNewFile();

                final DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageurl_elem_value));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                request.setAllowedOverRoaming(true);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                request.setTitle(title_elem_value);
                request.setDescription(shortnote_elem_value);
                request.setDestinationUri(Uri.fromFile(tempFile));
                final long download_id = downloadManager.enqueue(request);

                BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        try {

                            Log.d("sss", download_id + " " + downloadManager);

                            long received_id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                            if(received_id != download_id) {
                                return;
                            }

                            ParcelFileDescriptor parcelFileDescriptor = downloadManager.openDownloadedFile(download_id);
                            FileInputStream is = new ParcelFileDescriptor.AutoCloseInputStream(parcelFileDescriptor);

                            byte[] buff = new byte[1024*1024];
                            int r;
                            while ((r=is.read(buff)) != -1) {
                                Log.d("ss", "image " + r);
                                fos.write(buff, 0, r);
                            }
                            fos.flush();
                            fos.close();
                            is.close();

                            tempFile.deleteOnExit();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                context.registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

                /*//downlaod image file
                File image_data_file = new File(id_file.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATION_IMAGE + File.separator + FilesData.INTERNAL_FILE_NOTIFICATION_IMAGE);
                FileOutputStream fos = new FileOutputStream(image_data_file);
                InputStream is = new URL(imageurl_elem_value).openStream();

                Log.d("ss", "image " + imageurl_elem_value);

                byte[] buff = new byte[1024*1024];
                int r;
                while ((r=is.read(buff)) != -1) {
                    Log.d("ss", "image " + r);
                    fos.write(buff, 0, r);
                }
                fos.flush();
                fos.close();
                is.close();*/

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
























