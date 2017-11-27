package org.sristi.sristi.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.sristi.sristi.MainActivity;

/**
 * Created by Ariyan Khan on 02-03-2016.
 */
public final class SettingsPreferenceKeys {

    public static String settings_panel_download_over_wifi_pref_key = "PREF_1";
    public static String settings_panel_home_slide_animation_on_off_pref_key = "PREF_2";
    public static String settings_panel_home_slide_animation_speed_pref_key = "PREF_3";
    public static String settings_panel_syncing_frequency_pref_key = "PREF_5";

    public static String settings_shared_pref_file = "SETTINGS_PREF";
    public static String user_account_shared_pref_file = "USER_ACCOUNT_PREF";
    public static String user_account_shared_pref_signed_in_emails = "SIGNED_EMAILS";



    public static void setupUserAccountUserPrefs(Context context) {
        try {

            SharedPreferences sp = context.getSharedPreferences(user_account_shared_pref_file, 0);
            SharedPreferences.Editor editor = sp.edit();

            if(!sp.contains("FIRSTNAME")) {
                editor.putString("FIRSTNAME", "");
            }

            if(!sp.contains("LASTNAME")) {
                editor.putString("LASTNAME", "");
            }

            if(!sp.contains("PASSWORD")) {
                editor.putString("PASSWORD", "");
            }

            if(!sp.contains("EMAIL")) {
                editor.putString("EMAIL", "");
            }

            if(!sp.contains("COLLEGE")) {
                editor.putString("COLLEGE", "");
            }

            if(!sp.contains("YEAR")) {
                editor.putString("YEAR", "");
            }

            if(!sp.contains("LEVEL")) {
                editor.putString("LEVEL", "0");
            }

            if(!sp.contains(user_account_shared_pref_signed_in_emails)) {
                editor.putString(user_account_shared_pref_signed_in_emails, "");
            }

            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
