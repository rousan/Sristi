package org.sristi.sristi;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.sristi.sristi.utils.EventUpdatesChecker;
import org.sristi.sristi.utils.EventUpdatesDownloader;
import org.sristi.sristi.utils.FilesData;
import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.SettingsPreferenceKeys;
import org.sristi.sristi.utils.Utils;
import org.sristi.sristi.utils.VLog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

public class EventUpdaterService extends Service {

    private Thread ACTIVE_THREAD = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int  onStartCommand(final Intent i, int flags, final int startId) {

        try {

            if(i != null) {
                if(i.getBooleanExtra("EVENT_CHECK_EXTRA", false)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                checkEventUpdate(getApplicationContext());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }

            setDefaultSettingsPrefs();
            FilesData.setup(getApplicationContext());

            if(ACTIVE_THREAD == null) {
                ACTIVE_THREAD = new Thread(new EventUpdatesChecker(getApplicationContext()));
                ACTIVE_THREAD.start();
            } else {
                if(!ACTIVE_THREAD.isAlive()) {
                    try {
                        ACTIVE_THREAD.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ACTIVE_THREAD = new Thread(new EventUpdatesChecker(getApplicationContext()));
                    ACTIVE_THREAD.start();
                }
            }

        } catch (Exception e) {
            postSmallErros(e);
        }

        return START_STICKY;
    }

    private void setDefaultSettingsPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences(SettingsPreferenceKeys.settings_shared_pref_file, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!sharedPreferences.contains(SettingsPreferenceKeys.settings_panel_syncing_frequency_pref_key)) {
            editor.putInt(SettingsPreferenceKeys.settings_panel_syncing_frequency_pref_key, 0);
        }
        if(!sharedPreferences.contains(SettingsPreferenceKeys.settings_panel_download_over_wifi_pref_key)) {
            editor.putInt(SettingsPreferenceKeys.settings_panel_download_over_wifi_pref_key, 0);
        }
        if(!sharedPreferences.contains(SettingsPreferenceKeys.settings_panel_home_slide_animation_speed_pref_key)) {
            editor.putInt(SettingsPreferenceKeys.settings_panel_home_slide_animation_speed_pref_key, 4);
        }
        if(!sharedPreferences.contains(SettingsPreferenceKeys.settings_panel_home_slide_animation_on_off_pref_key)) {
            editor.putInt(SettingsPreferenceKeys.settings_panel_home_slide_animation_on_off_pref_key, 1);
        }
        editor.apply();
    }

    private void postSmallErros( Object error ) {
        error = (error == null) ? ("null") : (error);
        VLog.d("service", error + "");
    }

    public static void checkEventUpdate(Context context) {
        try {
            Log.d("s", "init");
            FilesData.setup(context);
            File notif_folder = new File(context.getDir(FilesData.INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE).getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_UPDATES + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATIONS);
            //notif_folder = new File(Environment.getExternalStorageDirectory(), "notif");
            String data_will_be_sent = "";
            for (File dir : notif_folder.listFiles()) {
                if(!dir.isDirectory()) {
                    continue;
                }
                data_will_be_sent += dir.getName() + "x"; //separator is x
            }
            data_will_be_sent = "NOTIF_IDS=" + data_will_be_sent;
            Log.d("s", data_will_be_sent);
            Log.d("s", "net checking");
            Log.d("s", "procesing");
            String outs_xml = RemoteServerConstants.processingURL(RemoteServerConstants.getFullAddressOfRemoteServerEventUpdatesChecker(), "POST", data_will_be_sent);
            if(outs_xml == null || outs_xml.isEmpty()) {
                return;
            }

            Log.d("s", outs_xml);
            //remove all extra text sent from server
            //<UPDATES>.....</UPDATES>
            String regex = Pattern.quote("<UPDATES>") + ".*" + Pattern.quote("</UPDATES>");
            Matcher matcher = Pattern.compile(regex).matcher(outs_xml);
            if(matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                outs_xml = outs_xml.substring(start, end);
            }

            outs_xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + outs_xml;

            Document docs = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outs_xml)));
            NodeList nl = docs.getElementsByTagName("NOTIFICATION");
            for(int i=0; i<nl.getLength(); i++) {
                Element notif_elem = (Element)nl.item(i);
                new Thread(new EventUpdatesDownloader(context, notif_elem)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
















