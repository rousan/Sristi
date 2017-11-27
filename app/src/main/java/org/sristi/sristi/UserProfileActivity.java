package org.sristi.sristi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;

import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.SettingsPreferenceKeys;
import org.sristi.sristi.utils.Utils;

import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_profile);

            TextView name_change_btn = (TextView) findViewById(R.id.name_change_btn);
            TextView college_name_change_btn = (TextView) findViewById(R.id.college_name_change_btn);
            TextView password_change_btn = (TextView) findViewById(R.id.password_change_btn);
            TextView email_change_btn = (TextView) findViewById(R.id.email_change_btn);
            TextView year_change_btn = (TextView) findViewById(R.id.year_change_btn);

            ViewGroup name_change_btn_panel2 = (ViewGroup)findViewById(R.id.name_change_btn_panel2);
            ViewGroup email_change_btn_panel2 = (ViewGroup)findViewById(R.id.email_change_btn_panel2);
            ViewGroup password_change_btn_panel2 = (ViewGroup)findViewById(R.id.password_change_btn_panel2);
            ViewGroup college_name_change_btn_panel2 = (ViewGroup)findViewById(R.id.college_name_change_btn_panel2);
            ViewGroup year_change_btn_panel2 = (ViewGroup)findViewById(R.id.year_change_btn_panel2);


            final ViewGroup control_panel = (ViewGroup)findViewById(R.id.control_panel);
            final ViewGroup edit_panel = (ViewGroup)findViewById(R.id.edit_panel);
            final EditText edit_input = (EditText)findViewById(R.id.edit_input);
            final TextView action_btn = (TextView)findViewById(R.id.action_button);
            final ViewGroup pb_holder_panel = (ViewGroup)findViewById(R.id.pb_holder_panel);
            final TextView cancel_btn = (TextView)findViewById(R.id.cancel_button);

            findViewById(R.id.sign_up_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        SharedPreferences sp = getSharedPreferences(SettingsPreferenceKeys.user_account_shared_pref_file, 0);
                        SharedPreferences.Editor editor = sp.edit();
                        SettingsPreferenceKeys.setupUserAccountUserPrefs(getApplicationContext());
                        editor.putString("FIRSTNAME", "");
                        editor.putString("LASTNAME", "");
                        editor.putString("PASSWORD", "");
                        editor.putString("EMAIL", "");
                        editor.putString("COLLEGE", "");
                        editor.putString("YEAR", "");
                        editor.apply();
                        finish();

                        try {
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.putExtra("NEW_SIGNED_UP", true);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlphaAnimation aa = new AlphaAnimation(1, 0);
                    aa.setDuration(300);
                    aa.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            edit_panel.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    edit_panel.startAnimation(aa);
                }
            });

            action_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final String input_text = edit_input.getText().toString().trim();
                        String column_data = "";
                        String PREFF_KEY = "";
                        boolean flag_full_name = false;
                        String hint_text = edit_input.getHint().toString().trim().toLowerCase();//important for idenetify the oparation
                        if(hint_text.equals("full name")) {
                            column_data = "fname";
                            PREFF_KEY = "FIRSTNAME";
                            flag_full_name = true;
                            if(input_text.isEmpty()) {
                                Utils.showModifiedToast(getApplicationContext(), "Invalid name");
                                return;
                            }
                        } else if(hint_text.equals("college name")) {
                            column_data = "college";
                            PREFF_KEY = "COLLEGE";
                            if(input_text.isEmpty()) {
                                Utils.showModifiedToast(getApplicationContext(), "Invalid college name");
                                return;
                            }
                        } else if(hint_text.equals("password")) {
                            column_data = "password";
                            PREFF_KEY = "PASSWORD";
                            if(input_text.isEmpty()) {
                                Utils.showModifiedToast(getApplicationContext(), "Invalid password");
                                return;
                            }
                        } else if(hint_text.equals("email")) {
                            PREFF_KEY = "EMAIL";
                            column_data = "email";
                            if(input_text.isEmpty() || !input_text.contains("@")) {
                                Utils.showModifiedToast(getApplicationContext(), "Invalid email");
                                return;
                            }
                        } else if(hint_text.equals("year")) {
                            PREFF_KEY = "YEAR";
                            column_data = "year";
                            if(input_text.isEmpty() || !input_text.matches("\\d+")) {
                                Utils.showModifiedToast(getApplicationContext(), "Invalid year");
                                return;
                            }
                        }

                        if(!RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext())) {
                            Utils.showModifiedToast(getApplicationContext(), "No internet connection");
                            return;
                        }

                        action_btn.setVisibility(View.GONE);
                        pb_holder_panel.setVisibility(View.VISIBLE);
                        cancel_btn.setEnabled(false);
                        final String temp1 = column_data;
                        final String temp2 = PREFF_KEY;
                        final boolean temp3 = flag_full_name;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    SharedPreferences sp = getSharedPreferences(SettingsPreferenceKeys.user_account_shared_pref_file, 0);
                                    SettingsPreferenceKeys.setupUserAccountUserPrefs(getApplicationContext());

                                    if (temp3) {

                                        try {

                                            String full_name = input_text.trim();
                                            String[] name_parts = full_name.split("\\s+");
                                            String fname = "";
                                            String lname = "";
                                            try {
                                                if(name_parts.length >= 1) {
                                                    fname = name_parts[0];
                                                    for(int i=0; i<name_parts.length; i++) {
                                                        if (i==0)
                                                            continue;
                                                        lname += name_parts[i] + " ";
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            if (fname.trim().isEmpty() || lname.trim().isEmpty()) {
                                                Utils.showModifiedToast(getApplicationContext(), "Give Full Name");
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        pb_holder_panel.setVisibility(View.GONE);
                                                        action_btn.setVisibility(View.VISIBLE);
                                                        cancel_btn.setEnabled(true);
                                                    }
                                                });
                                                return;
                                            }

                                            HashMap<String, String> outs1 = RemoteServerConstants.updateUserData(sp.getString("EMAIL", ""), sp.getString("PASSWORD", ""), "fname", fname);
                                            HashMap<String, String> outs2 = RemoteServerConstants.updateUserData(sp.getString("EMAIL", ""), sp.getString("PASSWORD", ""), "lname", lname);

                                            if (outs1 == null || outs2 == null) {
                                                Utils.showModifiedToast(getApplicationContext(), "Server or Internet problem occurred");
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        pb_holder_panel.setVisibility(View.GONE);
                                                        action_btn.setVisibility(View.VISIBLE);
                                                        cancel_btn.setEnabled(true);
                                                    }
                                                });
                                                return;
                                            }

                                            if(outs1.get("STATUS").equals("1") && outs2.get("STATUS").equals("1")) {

                                                Utils.showModifiedToast(getApplicationContext(), "Change Successfully completed");

                                                SharedPreferences.Editor editor = sp.edit();
                                                SettingsPreferenceKeys.setupUserAccountUserPrefs(getApplicationContext());
                                                editor.putString("FIRSTNAME", fname);
                                                editor.putString("LASTNAME", lname);
                                                editor.apply();

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        pb_holder_panel.setVisibility(View.GONE);
                                                        action_btn.setVisibility(View.VISIBLE);
                                                        cancel_btn.setEnabled(true);
                                                    }
                                                });

                                                finish();
                                                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));

                                            } else {
                                                Utils.showModifiedToast(getApplicationContext(), outs1.get("CAUSE") + "\n" + outs1.get("CAUSE"));
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        pb_holder_panel.setVisibility(View.GONE);
                                                        action_btn.setVisibility(View.VISIBLE);
                                                        cancel_btn.setEnabled(true);
                                                    }
                                                });
                                                return;
                                            }



                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    pb_holder_panel.setVisibility(View.GONE);
                                                    action_btn.setVisibility(View.VISIBLE);
                                                    cancel_btn.setEnabled(true);
                                                }
                                            });
                                        }

                                        return;
                                    }

                                    HashMap<String, String> outs = RemoteServerConstants.updateUserData(sp.getString("EMAIL", ""), sp.getString("PASSWORD", ""), temp1, input_text);

                                    if (outs == null) {
                                        Utils.showModifiedToast(getApplicationContext(), "Server or Internet problem occurred");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                pb_holder_panel.setVisibility(View.GONE);
                                                action_btn.setVisibility(View.VISIBLE);
                                                cancel_btn.setEnabled(true);
                                            }
                                        });
                                        return;
                                    }

                                    if(outs.get("STATUS").equals("1")) {

                                        Utils.showModifiedToast(getApplicationContext(), "Change Successfully completed");

                                        SharedPreferences.Editor editor = sp.edit();
                                        SettingsPreferenceKeys.setupUserAccountUserPrefs(getApplicationContext());
                                        editor.putString(temp2, input_text);
                                        editor.apply();

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                pb_holder_panel.setVisibility(View.GONE);
                                                action_btn.setVisibility(View.VISIBLE);
                                                cancel_btn.setEnabled(true);
                                            }
                                        });

                                        finish();
                                        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));

                                    } else {
                                        Utils.showModifiedToast(getApplicationContext(), outs.get("CAUSE"));
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                pb_holder_panel.setVisibility(View.GONE);
                                                action_btn.setVisibility(View.VISIBLE);
                                                cancel_btn.setEnabled(true);
                                            }
                                        });
                                        return;
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            pb_holder_panel.setVisibility(View.GONE);
                                            action_btn.setVisibility(View.VISIBLE);
                                            cancel_btn.setEnabled(true);
                                        }
                                    });
                                    Utils.showModifiedToast(getApplicationContext(), e);
                                }
                            }
                        }).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.showModifiedToast(getApplicationContext(), e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb_holder_panel.setVisibility(View.GONE);
                                action_btn.setVisibility(View.VISIBLE);
                                cancel_btn.setEnabled(true);
                            }
                        });
                    }
                }
            });

            Vector<ViewGroup> change_btns = new Vector<>();
            change_btns.add(name_change_btn_panel2);
            change_btns.add(college_name_change_btn_panel2);
            change_btns.add(password_change_btn_panel2);
            change_btns.add(email_change_btn_panel2);
            change_btns.add(year_change_btn_panel2);

            for(ViewGroup tv : change_btns) {
                try {

                    String hintText = null;
                    if(tv == name_change_btn_panel2) {
                        hintText = "Full Name";
                    } else if(tv == college_name_change_btn_panel2) {
                        hintText = "College Name";
                    } else if(tv == password_change_btn_panel2) {
                        hintText = "Password";
                    } else if(tv == email_change_btn_panel2) {
                        hintText = "Email";
                    } else if(tv == year_change_btn_panel2) {
                        hintText = "Year";
                    }

                    final String temp = hintText;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            edit_input.setHint(temp);
                            edit_input.setText("");
                            edit_panel.setVisibility(View.VISIBLE);
                            AlphaAnimation aa = new AlphaAnimation(0, 1);
                            aa.setDuration(300);
                            edit_panel.startAnimation(aa);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            SharedPreferences sp = getSharedPreferences(SettingsPreferenceKeys.user_account_shared_pref_file, 0);
            SettingsPreferenceKeys.setupUserAccountUserPrefs(getApplicationContext());
            String year = sp.getString("YEAR", "");

            try {

                Matcher m = Pattern.compile("\\d+").matcher(year);
                if(m.find()) {
                    year = year.substring(m.start(), m.end());
                    int yr = Integer.parseInt(year);
                    switch (yr) {
                        case 0:
                            year = "0th Year";
                            break;
                        case 1:
                            year = "1th Year";
                            break;
                        case 2:
                            year = "2th Year";
                            break;
                        case 3:
                            year = "3rd Year";
                            break;
                        case 4:
                            year = "4th Year";
                            break;
                        case 5:
                            year = "5th Year";
                            break;

                    }
                } else {
                    year += "year";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            ((TextView) findViewById(R.id.top_name_title)).setText(sp.getString("FIRSTNAME", "") + " " + sp.getString("LASTNAME", ""));
            ((TextView) findViewById(R.id.name_short_note)).setText(sp.getString("FIRSTNAME", "") + " " + sp.getString("LASTNAME", ""));
            ((TextView)findViewById(R.id.top_short_note)).setText("Student, " + year);
            ((TextView)findViewById(R.id.email_short_note)).setText(sp.getString("EMAIL", ""));
            ((TextView)findViewById(R.id.college_name_short_note)).setText(sp.getString("COLLEGE", ""));
            ((TextView)findViewById(R.id.year_short_note)).setText(year);


            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.name_title));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.name_change_btn));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.email_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.email_change_btn));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.password_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.password_change_btn));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.college_name_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.college_name_change_btn));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.year_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.year_change_btn));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.top_name_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.action_button));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.cancel_button));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.sign_up_btn));

        } catch (Exception e) {
            Utils.showModifiedToast(getApplicationContext(), e);
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
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
