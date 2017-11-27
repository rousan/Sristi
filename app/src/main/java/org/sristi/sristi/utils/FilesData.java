package org.sristi.sristi.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.sristi.sristi.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;

/**
 * Created by Ariyan Khan on 09-03-2016.
 */
public abstract class FilesData {

    public static final String INTERNAL_FOLDER_DATA = "data";
    public static final String INTERNAL_FOLDER_UPDATES = "updates";

    //notifications folder-files
    public static final String INTERNAL_FOLDER_NOTIFICATIONS = "notifications";
    public static final String INTERNAL_FOLDER_APP_UPDATES = "app_updates";
    public static final String INTERNAL_FILE_APP_UPDATES_APP_UPDATED_DETAILS = "app_updated_details.xml";
    public static final String INTERNAL_FILE_APP_UPDATES_APP_UPDATED_VERSION_CODE = "app_updated_version_code.xml";
    public static final String INTERNAL_FILE_NOTIFICATION_TITLE = "notification_title.txt";
    public static final String INTERNAL_FILE_NOTIFICATION_SHORT_NOTE = "notification_short_note.txt";
    public static final String INTERNAL_FILE_NOTIFICATION_DATE = "notification_date.txt";
    public static final String INTERNAL_FOLDER_NOTIFICATION_IMAGE = "image";
    public static final String INTERNAL_FILE_NOTIFICATION_IMAGE_URL = "image_url.txt";
    public static final String INTERNAL_FILE_NOTIFICATION_IMAGE = "image.data";
    public static final String INTERNAL_FOLDER_NOTIFICATION_HTML = "html";
    public static final String INTERNAL_FILE_NOTIFICATION_HTML_URL = "html_url.txt";

    public static void writeFile(File file, String data, boolean append) {
        try {

            FileOutputStream fos = new FileOutputStream(file, append);
            fos.write(data.getBytes("UTF-8"));
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFile(File file) {
        String read = "";
        try {

            FileInputStream fis = new FileInputStream(file);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            read = "";
            while ((line=bfr.readLine()) != null) {
                read += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return read;
    }

    public static void setup(Context context) {
        try {

            File data_folder = context.getDir(FilesData.INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE);
            if(!data_folder.isDirectory()) {
                data_folder.mkdirs();
            }

            File updates_folder = new File(data_folder, FilesData.INTERNAL_FOLDER_UPDATES);
            if(!updates_folder.isDirectory()) {
                updates_folder.mkdirs();
            }

            File notifications_folder = new File(updates_folder, FilesData.INTERNAL_FOLDER_NOTIFICATIONS);
            if(!notifications_folder.isDirectory()) {
                notifications_folder.mkdirs();
            }

            File app_updates_folder = new File(updates_folder, FilesData.INTERNAL_FOLDER_APP_UPDATES);
            if(!app_updates_folder.isDirectory()) {
                app_updates_folder.mkdirs();
            }

            File app_updated_version_code = new File(app_updates_folder, FilesData.INTERNAL_FILE_APP_UPDATES_APP_UPDATED_VERSION_CODE);
            if(!app_updated_version_code.isFile()) {
                app_updated_version_code.createNewFile();
                writeFile(app_updated_version_code, Utils.getAppVersionCode() + "", false);
            }

            File app_updated_details = new File(app_updates_folder, FilesData.INTERNAL_FILE_APP_UPDATES_APP_UPDATED_DETAILS);
            if(!app_updated_details.isFile()) {
                String data = "<APPUPDATE>\n" +
                        "    <STATUS>1</STATUS>\n" +
                        "    <RESTRICTION>false</RESTRICTION>\n" +
                        "    <VERSIONCODE>" + Utils.getAppVersionCode() + "</VERSIONCODE>\n" +
                        "    <VERSIONNAME>1.0.1-Atom</VERSIONNAME>\n" +
                        "    <SIZE>4.34MB</SIZE>\n" +
                        "    <DOWNLOADLINK>https://google.com</DOWNLOADLINK>\n" +
                        "    <EXTRADATAHTMLURL>https://google.com</EXTRADATAHTMLURL>\n" +
                        "</APPUPDATE>";
                app_updated_details.createNewFile();
                writeFile(app_updated_details, data, false);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



















