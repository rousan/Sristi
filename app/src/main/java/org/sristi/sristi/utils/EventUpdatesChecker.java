package org.sristi.sristi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Ariyan Khan on 09-03-2016.
 */
public class EventUpdatesChecker implements Runnable {

    private Context context = null;

    public EventUpdatesChecker(Context context) {
        this.context = context;
    }

    public void run() {
        try {

            while(true) {
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
                    //checking network
                    if(!RemoteServerConstants.isNetworkOrInternetConnected(context)) {
                        continue;//no wait , immediete check
                    }
                    Log.d("s", "procesing");
                    String outs_xml = RemoteServerConstants.processingURL(RemoteServerConstants.getFullAddressOfRemoteServerEventUpdatesChecker(), "POST", data_will_be_sent);
                    if(outs_xml == null || outs_xml.isEmpty()) {
                        try {
                            Thread.sleep(1000*60*1); //1 minutes
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        continue;
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

                    SharedPreferences sharedPreferences = context.getSharedPreferences(SettingsPreferenceKeys.settings_shared_pref_file, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if(!sharedPreferences.contains(SettingsPreferenceKeys.settings_panel_syncing_frequency_pref_key)) {
                        editor.putInt(SettingsPreferenceKeys.settings_panel_syncing_frequency_pref_key, 0);
                        editor.commit();
                    }

                    int pos = sharedPreferences.getInt(SettingsPreferenceKeys.settings_panel_syncing_frequency_pref_key, 0);
                    int wait = 10;//in minutes
                    switch (pos) {
                        case 0:
                            wait = 10;
                            break;
                        case 1:
                            wait = 20;
                            break;
                        case 2:
                            wait = 30;
                            break;
                        case 3:
                            wait = 40;
                            break;
                        case 4:
                            wait = 50;
                            break;
                        case 5:
                            wait = 60;
                            break;
                        case 6:
                            wait = 70;
                            break;
                        case 7:
                            wait = 80;
                            break;
                        default:
                            wait = 10;
                    }

                    VLog.d("key", wait + " key");

                    try {
                        Thread.sleep(1000*60*10); //important put here, not up
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
