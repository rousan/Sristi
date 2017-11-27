package org.sristi.sristi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.sristi.sristi.utils.EventUpdatesDownloader;
import org.sristi.sristi.utils.FilesData;
import org.sristi.sristi.utils.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Ariyan Khan on 08-03-2016.
 */
public abstract class RemoteServerConstants {

    public static final String REMOTE_SERVER_PROTOCOL = "http";
    public static final String REMOTE_SERVER_HOST = "app.sristi.org.in";
    public static final int REMOTE_SERVER_PORT = 80;

    public static final String REMOTE_SERVER_FOLDER_APP = "app";
        public static final String REMOTE_SERVER_FOLDER_UPDATES = "updates";
            public static final String REMOTE_SERVER_FOLDER_EVENTS = "events";
                public static final String REMOTE_SERVER_FILE_EVENTS = "events.html";
            public static final String REMOTE_SERVER_FOLDER_SCHEDULE = "schedule";
                public static final String REMOTE_SERVER_FILE_SCHEDULE = "schedule.html";
        public static final String REMOTE_SERVER_FOLDER_CONTROLLERS = "controllers";
            public static final String REMOTE_SERVER_FILE_EVENT_UPDATE_CHECKER = "event_update_checker.php";
            public static final String REMOTE_SERVER_FILE_LOGIN_HANDLER = "login_handler.php";
            public static final String REMOTE_SERVER_FILE_APP_UPDATE_HANDLER = "app_update_handler.php";
            public static final String REMOTE_SERVER_FILE_REGISTER_HANDLER = "register_handler.php";
            public static final String REMOTE_SERVER_FILE_USER_DATA_UPDATE_HANDLER = "update_user_data.php";
            public static final String REMOTE_SERVER_FILE_HELP_SUGGESTIONS_SUBMIT_HANDLER = "help_suggestions_handler.php";

    public static final URL getFullAddressOfRemoteServer() {
        URL url = null;
        try {

            url = new URL(REMOTE_SERVER_PROTOCOL + "://" + REMOTE_SERVER_HOST + ":" + REMOTE_SERVER_PORT);

        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
        return url;
    }

    public static final URL getFullAddressOfRemoteServerLoginHandler() {
        URL url = null;
        try {
            url = new URL(REMOTE_SERVER_PROTOCOL + "://" + REMOTE_SERVER_HOST + ":" + REMOTE_SERVER_PORT + "/" + REMOTE_SERVER_FOLDER_APP + "/" + appSpecificCorrespondingFolder() + "/" + REMOTE_SERVER_FOLDER_CONTROLLERS + "/" + REMOTE_SERVER_FILE_LOGIN_HANDLER);
        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
        return url;
    }

    public static final URL getFullAddressOfRemoteServerRegisterHandler() {
        URL url = null;
        try {
            url = new URL(REMOTE_SERVER_PROTOCOL + "://" + REMOTE_SERVER_HOST + ":" + REMOTE_SERVER_PORT + "/" + REMOTE_SERVER_FOLDER_APP + "/" + appSpecificCorrespondingFolder() + "/" + REMOTE_SERVER_FOLDER_CONTROLLERS + "/" + REMOTE_SERVER_FILE_REGISTER_HANDLER);
        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
        return url;
    }

    public static final URL getFullAddressOfRemoteServerUserDataUpdateHandler() {
        URL url = null;
        try {
            url = new URL(REMOTE_SERVER_PROTOCOL + "://" + REMOTE_SERVER_HOST + ":" + REMOTE_SERVER_PORT + "/" + REMOTE_SERVER_FOLDER_APP + "/" + appSpecificCorrespondingFolder() + "/" + REMOTE_SERVER_FOLDER_CONTROLLERS + "/" + REMOTE_SERVER_FILE_USER_DATA_UPDATE_HANDLER);
        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
        return url;
    }

    public static final URL getFullAddressOfRemoteServerHelpSuggestionsSubmitHandler() {
        URL url = null;
        try {
            url = new URL(REMOTE_SERVER_PROTOCOL + "://" + REMOTE_SERVER_HOST + ":" + REMOTE_SERVER_PORT + "/" + REMOTE_SERVER_FOLDER_APP + "/" + appSpecificCorrespondingFolder() + "/" + REMOTE_SERVER_FOLDER_CONTROLLERS + "/" + REMOTE_SERVER_FILE_HELP_SUGGESTIONS_SUBMIT_HANDLER);
        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
        return url;
    }

    public static final URL getFullAddressOfRemoteServerAppUpdatesHandler() {
        URL url = null;
        try {
            url = new URL(REMOTE_SERVER_PROTOCOL + "://" + REMOTE_SERVER_HOST + ":" + REMOTE_SERVER_PORT + "/" + REMOTE_SERVER_FOLDER_APP + "/" + appSpecificCorrespondingFolder() + "/" + REMOTE_SERVER_FOLDER_CONTROLLERS + "/" + REMOTE_SERVER_FILE_APP_UPDATE_HANDLER);
        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
        return url;
    }


    public static final URL getFullAddressOfRemoteServerEventUpdatesChecker() {
        URL url = null;

        try {

            url = new URL(REMOTE_SERVER_PROTOCOL + "://" + REMOTE_SERVER_HOST + ":" + REMOTE_SERVER_PORT + "/" + REMOTE_SERVER_FOLDER_APP + "/" + appSpecificCorrespondingFolder() + "/" + REMOTE_SERVER_FOLDER_CONTROLLERS + "/" + REMOTE_SERVER_FILE_EVENT_UPDATE_CHECKER);

        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
        return url;
    }

    public static final URL getFullAddressOfRemoteServerEventsPage() {
        URL url = null;

        try {

            url = new URL(REMOTE_SERVER_PROTOCOL + "://" + REMOTE_SERVER_HOST + ":" + REMOTE_SERVER_PORT + "/" + REMOTE_SERVER_FOLDER_APP + "/" + appSpecificCorrespondingFolder() + "/" + REMOTE_SERVER_FOLDER_UPDATES + "/" + REMOTE_SERVER_FOLDER_EVENTS + "/" + REMOTE_SERVER_FILE_EVENTS);

        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
        return url;
    }

    public static final URL getFullAddressOfRemoteServerSchedulePage() {
        URL url = null;

        try {

            url = new URL(REMOTE_SERVER_PROTOCOL + "://" + REMOTE_SERVER_HOST + ":" + REMOTE_SERVER_PORT + "/" + REMOTE_SERVER_FOLDER_APP + "/" + appSpecificCorrespondingFolder() + "/" + REMOTE_SERVER_FOLDER_UPDATES + "/" + REMOTE_SERVER_FOLDER_SCHEDULE + "/" + REMOTE_SERVER_FILE_SCHEDULE);

        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
        return url;
    }



    public static boolean isNetworkOrInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo == null) {
            return false;
        } else {
            return networkInfo.isConnected();
        }
    }

    public static String processingURL(URL url, String req_type, String data) {
        String outputs = null;
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

            httpURLConnection.setRequestMethod(req_type.trim().toUpperCase());
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes("UTF-8").length));
            httpURLConnection.setRequestProperty("Connection", "keep-alive");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream os = httpURLConnection.getOutputStream();
            os.write(data.getBytes("UTF-8"));
            os.flush();
            try {
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            InputStream is = httpURLConnection.getInputStream();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
            outputs = "";//important
            String line_data = "";
            while ((line_data=bfr.readLine()) != null) {
                outputs += line_data + "\n";
            }
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return outputs;
        } catch (Exception e) {
            outputs = null;//important
            VLog.d("jk", "kl " + e.getMessage());
            e.printStackTrace();
        }
        return outputs;
    }

    public static void checkEventUpdates(Context context, Runnable runnable) {
        try {

            Log.d("s", "init");
            FilesData.setup(context);
            File notif_folder = new File(context.getDir(FilesData.INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE).getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_UPDATES + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATIONS);
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
                runnable.run();
                return;
            }
            Log.d("s", "procesing");
            String outs_xml = RemoteServerConstants.processingURL(RemoteServerConstants.getFullAddressOfRemoteServerEventUpdatesChecker(), "POST", data_will_be_sent);
            if(outs_xml == null || outs_xml.isEmpty()) {

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

            //boolean k = 90 <= 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String checkAppUpdate() {
        String outs = null;

        try {
            String outs_server = processingURL(getFullAddressOfRemoteServerAppUpdatesHandler(), "POST", "version_code=" + Utils.getAppVersionCode());
            if (outs_server == null || outs_server.trim().isEmpty())
                return null; //null

            outs = outs_server;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return outs;
    }

    public static HashMap<String, String> userLogIn(String email, String password) {
        HashMap<String, String> outs = null;
        try {

            String data = "user=" + URLEncoder.encode(email, "utf-8") + "&pass=" +  URLEncoder.encode(password, "utf-8");
            outs = new HashMap<>();
            String outs_server = processingURL(getFullAddressOfRemoteServerLoginHandler(), "POST", data);
            if(outs_server == null || outs_server.trim().isEmpty()) {
                return null;
            }

            outs_server = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + outs_server.trim();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outs_server)));
            String status = doc.getElementsByTagName("STATUS").item(0).getTextContent().trim();
            if(status.equals("1")) {
                outs.put("STATUS", "1");
                outs.put("FIRSTNAME", doc.getElementsByTagName("FIRSTNAME").item(0).getTextContent().trim());
                outs.put("LASTNAME", doc.getElementsByTagName("LASTNAME").item(0).getTextContent().trim());
                outs.put("PASSWORD", doc.getElementsByTagName("PASSWORD").item(0).getTextContent().trim());
                outs.put("EMAIL", doc.getElementsByTagName("EMAIL").item(0).getTextContent().trim());
                outs.put("COLLEGE", doc.getElementsByTagName("COLLEGE").item(0).getTextContent().trim());
                outs.put("YEAR", doc.getElementsByTagName("YEAR").item(0).getTextContent().trim());
                outs.put("LEVEL", doc.getElementsByTagName("LEVEL").item(0).getTextContent().trim());
            } else {
                String cause = doc.getElementsByTagName("CAUSE").item(0).getTextContent().trim();
                outs.put("STATUS", "0");
                outs.put("CAUSE", cause);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return outs;
    }


    public static HashMap<String, String> getUserSherlockedGameInfo(String email, String password) {
        HashMap<String, String> outs = null;
        try {

            String data = "email=" + URLEncoder.encode(email, "utf-8") + "&pass=" +  URLEncoder.encode(password, "utf-8");
            outs = new HashMap<>();
            String outs_server = processingURL(new URL("http://app.sristi.org.in/app/online_games/sherlocked/controllers/get_user_question_info.php"), "POST", data);
            if(outs_server == null || outs_server.trim().isEmpty()) {
                return null;
            }

            VLog.d("l", outs_server);

            outs_server = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + outs_server.trim();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outs_server)));
            String status = doc.getElementsByTagName("STATUS").item(0).getTextContent().trim();
            if(status.equals("1")) {
                outs.put("STATUS", "1");
                outs.put("CURRENTLEVEL", doc.getElementsByTagName("CURRENTLEVEL").item(0).getTextContent().trim());
                outs.put("FULLNAME", doc.getElementsByTagName("FULLNAME").item(0).getTextContent().trim());
                outs.put("NEXTLEVELQUESTION", doc.getElementsByTagName("NEXTLEVELQUESTION").item(0).getTextContent().trim());
                outs.put("NEXTLEVELANSWER", doc.getElementsByTagName("NEXTLEVELANSWER").item(0).getTextContent().trim());
            } else {
                String cause = doc.getElementsByTagName("CAUSE").item(0).getTextContent().trim();
                outs.put("STATUS", "0");
                outs.put("CAUSE", cause);
            }

        } catch (Exception e) {;
            VLog.d("jk", "kl " + e.getMessage());
            return null;
        }
        return outs;
    }


    public static HashMap<String, String> submitSherlockedAnswer(String email, String password, String level, String ans) {
        HashMap<String, String> outs = null;
        try {

            String data = "email=" + URLEncoder.encode(email, "utf-8") + "&pass=" +  URLEncoder.encode(password, "utf-8") + "&level=" + URLEncoder.encode(level, "utf-8") + "&ans=" + URLEncoder.encode(ans, "utf-8");
            outs = new HashMap<>();
            String outs_server = processingURL(new URL("http://app.sristi.org.in/app/online_games/sherlocked/controllers/answer_submit.php"), "POST", data);
            if(outs_server == null || outs_server.trim().isEmpty()) {
                return null;
            }

            outs_server = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + outs_server.trim();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outs_server)));
            String status = doc.getElementsByTagName("STATUS").item(0).getTextContent().trim();
            if(status.equals("1")) {
                outs.put("STATUS", "1");
            } else {
                String cause = doc.getElementsByTagName("CAUSE").item(0).getTextContent().trim();
                outs.put("STATUS", "0");
                outs.put("CAUSE", cause);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return outs;
    }

    public static HashMap<String, String> userRegister(String fname, String lname, String email, String pass, String college, String year) {
        HashMap<String, String> outs = null;
        try {

            String data = "" +
                    "fname=" + URLEncoder.encode(fname, "UTF-8") + "&" +
                    "lname=" + URLEncoder.encode(lname, "UTF-8") + "&" +
                    "email=" + URLEncoder.encode(email, "UTF-8") + "&" +
                    "pass=" + URLEncoder.encode(pass, "UTF-8") + "&" +
                    "college=" + URLEncoder.encode(college, "UTF-8") + "&" +
                    "year=" + URLEncoder.encode(year, "UTF-8");

            outs = new HashMap<>();
            String outs_server = processingURL(getFullAddressOfRemoteServerRegisterHandler(), "POST", data);
            if(outs_server == null || outs_server.trim().isEmpty()) {
                return null;
            }

            outs_server = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + outs_server.trim();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outs_server)));
            String status = doc.getElementsByTagName("STATUS").item(0).getTextContent().trim();
            if(status.equals("1")) {
                outs.put("STATUS", "1");
            } else {
                String cause = doc.getElementsByTagName("CAUSE").item(0).getTextContent().trim();
                outs.put("STATUS", "0");
                outs.put("CAUSE", cause);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return outs;
    }

    public static HashMap<String, String> updateUserData(String email, String pass, String data_column, String data_value) {
        HashMap<String, String> outs = null;
        try {
            String data = "" +
                    "email=" + URLEncoder.encode(email, "UTF-8") + "&" +
                    "pass=" + URLEncoder.encode(pass, "UTF-8") + "&" +
                    "datacolumn=" + URLEncoder.encode(data_column, "UTF-8") + "&" +
                    "datavalue=" + URLEncoder.encode(data_value, "UTF-8");

            outs = new HashMap<>();
            String outs_server = processingURL(getFullAddressOfRemoteServerUserDataUpdateHandler(), "POST", data);
            if(outs_server == null || outs_server.trim().isEmpty()) {
                return null;
            }

            outs_server = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + outs_server.trim();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outs_server)));
            String status = doc.getElementsByTagName("STATUS").item(0).getTextContent().trim();
            if(status.equals("1")) {
                outs.put("STATUS", "1");
            } else {
                String cause = doc.getElementsByTagName("CAUSE").item(0).getTextContent().trim();
                outs.put("STATUS", "0");
                outs.put("CAUSE", cause);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return outs;
    }

    public static HashMap<String, String> postHelpSuggestions(String email, String messages) {
        HashMap<String, String> outs = null;
        try {

            String data = "" +
                    "email=" + URLEncoder.encode(email, "UTF-8") + "&" +
                    "messages=" + URLEncoder.encode(messages, "UTF-8");

            outs = new HashMap<>();
            String outs_server = processingURL(getFullAddressOfRemoteServerHelpSuggestionsSubmitHandler(), "POST", data);
            if(outs_server == null || outs_server.trim().isEmpty()) {
                return null;
            }

            outs_server = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + outs_server.trim();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outs_server)));
            String status = doc.getElementsByTagName("STATUS").item(0).getTextContent().trim();
            if(status.equals("1")) {
                outs.put("STATUS", "1");
            } else {
                String cause = doc.getElementsByTagName("CAUSE").item(0).getTextContent().trim();
                outs.put("STATUS", "0");
                outs.put("CAUSE", cause);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return outs;
    }

    public static String appSpecificCorrespondingFolder() {
        return "20161";
    } //its very important
}

























