package org.sristi.sristi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.sristi.sristi.utils.SettingsPreferenceKeys;
import org.sristi.sristi.utils.Utils;

import java.util.HashMap;
import java.util.Vector;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);
            findViewById(R.id.back_imageeview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            final SharedPreferences sharedPreferences = getSharedPreferences(SettingsPreferenceKeys.settings_shared_pref_file, MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPreferences.edit();

            Spinner spinner_home_slide_anim_rate = (Spinner) findViewById(R.id.spinner_home_slide_anim_rate);
            ArrayAdapter<CharSequence> spinner_home_slide_anim_rate_adapter = ArrayAdapter.createFromResource(this,
                    R.array.home_slide_animation_rate_seconds, R.layout.spinner_item);
            spinner_home_slide_anim_rate_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_home_slide_anim_rate.setAdapter(spinner_home_slide_anim_rate_adapter);
            spinner_home_slide_anim_rate.setSelection(sharedPreferences.getInt(SettingsPreferenceKeys.settings_panel_home_slide_animation_speed_pref_key, 4));
            spinner_home_slide_anim_rate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    editor.putInt(SettingsPreferenceKeys.settings_panel_home_slide_animation_speed_pref_key, position);
                    editor.apply();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Spinner spinner_refresh_syncing_frequency = (Spinner) findViewById(R.id.spinner_refresh_syncing_frequency);
            ArrayAdapter<CharSequence> spinner_refresh_syncing_frequency_adapter = ArrayAdapter.createFromResource(this,
                    R.array.updates_syncing_frequency, R.layout.spinner_item);
            spinner_refresh_syncing_frequency_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_refresh_syncing_frequency.setAdapter(spinner_refresh_syncing_frequency_adapter);
            spinner_refresh_syncing_frequency.setSelection(sharedPreferences.getInt(SettingsPreferenceKeys.settings_panel_syncing_frequency_pref_key, 0));
            spinner_refresh_syncing_frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    editor.putInt(SettingsPreferenceKeys.settings_panel_syncing_frequency_pref_key, position);
                    editor.apply();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            View settings_panel_download_over_wifi = findViewById(R.id.settings_panel_download_over_wifi);
            View settings_panel_home_slide_animation_on_off = findViewById(R.id.settings_panel_home_slide_animation_on_off);
            View settings_panel_home_slide_animation_speed = findViewById(R.id.settings_panel_home_slide_animation_speed);
            View settings_panel_sign_sign_up = findViewById(R.id.settings_panel_sign_sign_up);
            View settings_panel_syncing_frequency = findViewById(R.id.settings_panel_syncing_frequency);
            View settings_panel_update_events = findViewById(R.id.settings_panel_update_events);
            View settings_panel_check_app_update = findViewById(R.id.settings_panel_check_app_update);
            final ImageView imageview_download_over_wifi = (ImageView)findViewById(R.id.imageview_download_over_wifi);
            final ImageView settings_home_slide_anim_imageview = (ImageView)findViewById(R.id.settings_home_slide_anim_imageview);

            if(sharedPreferences.getInt(SettingsPreferenceKeys.settings_panel_download_over_wifi_pref_key, 0) == 1) {
                imageview_download_over_wifi.setImageResource(R.drawable.check_box_selected);
            } else {
                imageview_download_over_wifi.setImageResource(R.drawable.check_box_unchecked);
            }

            if(sharedPreferences.getInt(SettingsPreferenceKeys.settings_panel_home_slide_animation_on_off_pref_key, 1) == 1) {
                imageview_download_over_wifi.setImageResource(R.drawable.check_box_selected);
            } else {
                imageview_download_over_wifi.setImageResource(R.drawable.check_box_unchecked);
            }

            settings_panel_download_over_wifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sharedPreferences.getInt(SettingsPreferenceKeys.settings_panel_download_over_wifi_pref_key, 0) == 1) {
                        editor.putInt(SettingsPreferenceKeys.settings_panel_download_over_wifi_pref_key, 0);
                        imageview_download_over_wifi.setImageResource(R.drawable.check_box_unchecked);
                    } else {
                        editor.putInt(SettingsPreferenceKeys.settings_panel_download_over_wifi_pref_key, 1);
                        imageview_download_over_wifi.setImageResource(R.drawable.check_box_selected);
                    }
                    editor.apply();
                }
            });

            settings_panel_home_slide_animation_on_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sharedPreferences.getInt(SettingsPreferenceKeys.settings_panel_home_slide_animation_on_off_pref_key, 1) == 1) {
                        editor.putInt(SettingsPreferenceKeys.settings_panel_home_slide_animation_on_off_pref_key, 0);
                        settings_home_slide_anim_imageview.setImageResource(R.drawable.check_box_unchecked);
                    } else {
                        editor.putInt(SettingsPreferenceKeys.settings_panel_home_slide_animation_on_off_pref_key, 1);
                        settings_home_slide_anim_imageview.setImageResource(R.drawable.check_box_selected);
                    }
                    editor.apply();
                }
            });

            settings_panel_sign_sign_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));
                }
            });


            settings_panel_check_app_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), UpdateCenterActivity.class));
                }
            });

            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            //Utils.setHeadingTextFont((TextView) findViewById(R.id.doanload_over_wifi_h));
            //Utils.setHeadingTextFont((TextView)findViewById(R.id.home_slide_anim_on_off));
            //Utils.setHeadingTextFont((TextView)findViewById(R.id.home_slide_ani_speed));
            //Utils.setHeadingTextFont((TextView)findViewById(R.id.sign_signup));
            //Utils.setHeadingTextFont((TextView)findViewById(R.id.syncing_fr));
            //Utils.setHeadingTextFont((TextView)findViewById(R.id.event_updates_chceck));
            //Utils.setHeadingTextFont((TextView)findViewById(R.id.check_app_updtes));

        } catch (Exception e) {
            postSmallErros(e);
        }
    }

    private void postSmallErros( final Object error ) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showModifiedToast(getApplicationContext(), error);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
