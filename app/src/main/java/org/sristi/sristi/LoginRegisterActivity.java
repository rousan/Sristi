package org.sristi.sristi;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.SettingsPreferenceKeys;
import org.sristi.sristi.utils.Utils;
import org.sristi.sristi.utils.VLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class LoginRegisterActivity extends Activity {

    private EditText name_input = null;
    private EditText email_input = null;
    private EditText password_input = null;
    private EditText college_name_input = null;
    private ImageView name_validation_imageview = null;
    private ImageView email_validation_imageview = null;
    private ImageView password_validation_imageview = null;
    ImageView college_name_validation_imageview = null;
    private View full_name_below_border = null;
    private ViewGroup name_input_panel = null;
    private ViewGroup sign_up_extra_area = null;
    private ViewGroup email_full_area = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login_register);

            final EditText year_input = (EditText)findViewById(R.id.year_input);
            final ImageView year_validation_imageview = (ImageView)findViewById(R.id.year_validation_imageview);
            name_input = (EditText)findViewById(R.id.name_input);
            email_input = (EditText)findViewById(R.id.email_input);
            password_input = (EditText)findViewById(R.id.password_input);
            name_validation_imageview = (ImageView)findViewById(R.id.name_validation_imageview);
            email_validation_imageview = (ImageView)findViewById(R.id.email_validation_imageview);
            password_validation_imageview = (ImageView)findViewById(R.id.password_validation_imageview);
            full_name_below_border = findViewById(R.id.full_name_below_border);
            name_input_panel = (ViewGroup)findViewById(R.id.name_input_panel);
            sign_up_extra_area = (ViewGroup)findViewById(R.id.sign_up_extra_area);
            email_full_area = (ViewGroup)findViewById((R.id.email_full_area));
            college_name_input = (EditText)findViewById(R.id.college_name_input);
            college_name_validation_imageview = (ImageView)findViewById(R.id.college_name_validation_imageview);

            try {
                //set adapter to email_input
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)email_input;
                Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
                HashSet<String> stringHashSet = new HashSet<>();
                for(Account account : accounts) {
                    if(account.name.contains("@")) {
                        stringHashSet.add(account.name);
                    }
                }
                SharedPreferences sp = getSharedPreferences(SettingsPreferenceKeys.user_account_shared_pref_file, 0);
                SettingsPreferenceKeys.setupUserAccountUserPrefs(getApplicationContext());
                String[] signed_emails = sp.getString(SettingsPreferenceKeys.user_account_shared_pref_signed_in_emails, "").split(",");
                //Utils.showModifiedToast(getApplicationContext(), Arrays.toString(signed_emails));
                for(String email : signed_emails) {
                    if (email.trim().isEmpty())
                        continue;
                    stringHashSet.add(email.trim());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_item_theme, new ArrayList<String>(stringHashSet));
                autoCompleteTextView.setAdapter(arrayAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                //set adapter to college_name_input
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)college_name_input;
                String[] collge_names = getResources().getStringArray(R.array.colllge_names_suggestions);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_item_theme, collge_names);
                autoCompleteTextView.setAdapter(arrayAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

            findViewById(R.id.back_imageeview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            final View login_with_fb = findViewById(R.id.login_with_fb);
            setClickableOnTouchListener(login_with_fb,
                    new Runnable() {
                        @Override
                        public void run() {
                            login_with_fb.setAlpha(0.7f);
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            login_with_fb.setAlpha(1.0f);
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            if(!RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext()))
                                Utils.showModifiedToast(getApplicationContext(), "No internet connection available");
                            else
                                Utils.showModifiedToast(getApplicationContext(), "This Feature is not available");
                        }
                    }
            );

            final View login_with_googleplus = findViewById(R.id.login_with_googleplus);
            setClickableOnTouchListener(login_with_googleplus,
                    new Runnable() {
                        @Override
                        public void run() {
                            login_with_googleplus.setAlpha(0.5f);
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            login_with_googleplus.setAlpha(1.0f);
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            if (!RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext()))
                                Utils.showModifiedToast(getApplicationContext(), "No internet connection available");
                            else
                                Utils.showModifiedToast(getApplicationContext(), "This Feature is not available");
                        }
                    }
            );

            final Vector<EditText> inputs = new Vector<EditText>();
            inputs.add(name_input);
            inputs.add(email_input);
            inputs.add(password_input);
            inputs.add(college_name_input);
            inputs.add(year_input);
            for(EditText et : inputs) {
                final EditText temp = et;
                et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus) {
                            ((ViewGroup)temp.getParent()).setBackgroundColor(Color.parseColor("#33000000"));
                        } else {
                            ((ViewGroup)temp.getParent()).setBackgroundColor(Color.parseColor("#00000000"));
                        }
                    }
                });
            }


            final ViewGroup pb_holder_panel = (ViewGroup)findViewById(R.id.pb_holder_panel);
            final TextView action_button = (TextView)findViewById(R.id.action_button);
            final TextView changing_button = (TextView)findViewById(R.id.changing_button);
            changing_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (changing_button.getText().toString().toLowerCase().equals("sign up")) {
                        sign_up_extra_area.setVisibility(View.VISIBLE);
                        sign_up_extra_area.getLayoutParams().height = 0;
                        Utils.animateHeight(sign_up_extra_area, 0, Utils.dpToPx((40 + 1) * 3, getApplicationContext()), 300, null, null, true);
                        name_input.requestFocus();
                        action_button.setText("SIGN UP");
                        changing_button.setText("SIGN IN");
                    } else {
                        Utils.animateHeight(sign_up_extra_area, 0, Utils.dpToPx((40 + 1) * 3, getApplicationContext()), 300, null, new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sign_up_extra_area.setVisibility(View.GONE);
                                        email_input.requestFocus();
                                    }
                                });
                            }
                        }, false);
                        action_button.setText("SIGN IN");
                        changing_button.setText("SIGN UP");
                    }
                }
            });

            action_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        if(action_button.getText().toString().toUpperCase().equals("SIGN IN")) {
                            final String email_val = email_input.getText().toString().trim();
                            final String pass_val = password_input.getText().toString().trim();
                            if(email_val.isEmpty() || !email_val.toLowerCase().contains("@")) {
                                postSmallErros("Invalid Email");
                                return;
                            }
                            if(pass_val.isEmpty() || pass_val.length() < 0) {
                                postSmallErros("Invalid Password");
                                return;
                            }
                            if(!RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext())) {
                                postSmallErros("No internet connection");
                                return;
                            }

                            action_button.setVisibility(View.GONE);
                            pb_holder_panel.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        HashMap<String, String> outs = RemoteServerConstants.userLogIn(email_val, pass_val);
                                        if (outs == null) {
                                            Utils.showModifiedToast(getApplicationContext(), "Server or Internet problem occurred");
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    pb_holder_panel.setVisibility(View.GONE);
                                                    action_button.setVisibility(View.VISIBLE);
                                                }
                                            });
                                            return;
                                        }
                                        if(outs.get("STATUS").equals("1")) {

                                            SharedPreferences sp = getSharedPreferences(SettingsPreferenceKeys.user_account_shared_pref_file, 0);
                                            SharedPreferences.Editor editor = sp.edit();
                                            SettingsPreferenceKeys.setupUserAccountUserPrefs(getApplicationContext());
                                            editor.putString("FIRSTNAME", outs.get("FIRSTNAME"));
                                            editor.putString("LASTNAME", outs.get("LASTNAME"));
                                            editor.putString("PASSWORD", outs.get("PASSWORD"));
                                            editor.putString("EMAIL", outs.get("EMAIL"));
                                            editor.putString("COLLEGE", outs.get("COLLEGE"));
                                            editor.putString("YEAR", outs.get("YEAR"));
                                            editor.putString("LEVEL", outs.get("LEVEL"));
                                            String signed_emails = sp.getString(SettingsPreferenceKeys.user_account_shared_pref_signed_in_emails, "");
                                            if(!signed_emails.contains(outs.get("EMAIL"))) {
                                                editor.putString(SettingsPreferenceKeys.user_account_shared_pref_signed_in_emails, signed_emails + outs.get("EMAIL") + ",");
                                            }
                                            editor.apply();

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    pb_holder_panel.setVisibility(View.GONE);
                                                    action_button.setVisibility(View.VISIBLE);
                                                }
                                            });
                                            finish();

                                            try {
                                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                intent.putExtra("NEW_SIGNED_IN", true);
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        } else {
                                            Utils.showModifiedToast(getApplicationContext(), outs.get("CAUSE"));
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    pb_holder_panel.setVisibility(View.GONE);
                                                    action_button.setVisibility(View.VISIBLE);
                                                }
                                            });
                                            return;
                                        }

                                    } catch (Exception e) {
                                        postSmallErros(e);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                pb_holder_panel.setVisibility(View.GONE);
                                                action_button.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }
                                }
                            }).start();

                        } else {
                            final String email = email_input.getText().toString().trim();
                            String college_name = college_name_input.getText().toString().trim();
                            String full_name = name_input.getText().toString().trim();
                            final String pass = password_input.getText().toString().trim();
                            String year = year_input.getText().toString().trim();
                            if(full_name.isEmpty()) {
                                postSmallErros("Invalid Name");
                                return;
                            }
                            //not required
                            if(college_name.isEmpty()) {
                                college_name = "JGEC";
                                postSmallErros("Default College JGEC is used");
                            }
                            if(year.isEmpty()) {
                                year = 1 + "";
                                postSmallErros("Default year 1 is used");
                            }
                            if(email.isEmpty() || !email.toLowerCase().contains("@")) {
                                postSmallErros("Invalid Email");
                                return;
                            }
                            if(pass.isEmpty() || pass.length() < 0) {
                                postSmallErros("Invalid Password");
                                return;
                            }
                            if(!RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext())) {
                                postSmallErros("No internet connection");
                                return;
                            }

                            full_name = full_name.trim();
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

                            action_button.setVisibility(View.GONE);
                            pb_holder_panel.setVisibility(View.VISIBLE);
                            final String temp1 = fname;
                            final String temp2 = lname;
                            final String temp3 = college_name;
                            final String temp4 = year;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        HashMap<String, String> outs = RemoteServerConstants.userRegister(temp1, temp2, email, pass, temp3, temp4);
                                        if (outs == null) {
                                            Utils.showModifiedToast(getApplicationContext(), "Server or Internet problem occurred");
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    pb_holder_panel.setVisibility(View.GONE);
                                                    action_button.setVisibility(View.VISIBLE);
                                                }
                                            });
                                            return;
                                        }

                                        if(outs.get("STATUS").equals("1")) {

                                            Utils.showModifiedToast(getApplicationContext(), "Register Successfully Completed");

                                            SharedPreferences sp = getSharedPreferences(SettingsPreferenceKeys.user_account_shared_pref_file, 0);
                                            SharedPreferences.Editor editor = sp.edit();
                                            SettingsPreferenceKeys.setupUserAccountUserPrefs(getApplicationContext());
                                            editor.putString("FIRSTNAME", temp1);
                                            editor.putString("LASTNAME", temp2);
                                            editor.putString("PASSWORD", pass);
                                            editor.putString("EMAIL", email);
                                            editor.putString("COLLEGE", temp3);
                                            editor.putString("YEAR", temp4);
                                            editor.putString("LEVEL", "0");
                                            String signed_emails = sp.getString(SettingsPreferenceKeys.user_account_shared_pref_signed_in_emails, "");
                                            //Utils.showModifiedToast(getApplicationContext(), signed_emails);
                                            if(!signed_emails.contains(email)) {
                                                editor.putString(SettingsPreferenceKeys.user_account_shared_pref_signed_in_emails, signed_emails + email + ",");
                                            }
                                            editor.apply();

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    pb_holder_panel.setVisibility(View.GONE);
                                                    action_button.setVisibility(View.VISIBLE);
                                                }
                                            });
                                            finish();

                                            try {
                                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                intent.putExtra("NEW_SIGNED_IN", true);
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        } else {
                                            Utils.showModifiedToast(getApplicationContext(), outs.get("CAUSE"));
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    pb_holder_panel.setVisibility(View.GONE);
                                                    action_button.setVisibility(View.VISIBLE);
                                                }
                                            });
                                            return;
                                        }

                                    } catch (Exception e) {
                                        postSmallErros(e);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                pb_holder_panel.setVisibility(View.GONE);
                                                action_button.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }
                                }
                            }).start();
                        }

                    } catch (Exception e) {
                        postSmallErros(e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb_holder_panel.setVisibility(View.GONE);
                                action_button.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            });

            name_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (name_input.getText().toString().isEmpty()) {
                        name_validation_imageview.setImageResource(R.drawable.eror);
                        name_validation_imageview.setVisibility(View.VISIBLE);
                    } else {
                        name_validation_imageview.setImageResource(R.drawable.yes);
                        name_validation_imageview.setVisibility(View.VISIBLE);
                    }
                }
            });

            email_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if(email_input.getText().toString().isEmpty() || !email_input.getText().toString().toLowerCase().contains("@")) {
                        email_validation_imageview.setImageResource(R.drawable.eror);
                        email_validation_imageview.setVisibility(View.VISIBLE);
                    } else {
                        email_validation_imageview.setImageResource(R.drawable.yes);
                        email_validation_imageview.setVisibility(View.VISIBLE);
                    }

                }
            });


            college_name_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (college_name_input.getText().toString().isEmpty()) {
                        college_name_validation_imageview.setImageResource(R.drawable.eror);
                        college_name_validation_imageview.setVisibility(View.VISIBLE);
                    } else {
                        college_name_validation_imageview.setImageResource(R.drawable.yes);
                        college_name_validation_imageview.setVisibility(View.VISIBLE);
                    }

                }
            });
            year_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (year_input.getText().toString().isEmpty()) {
                        year_validation_imageview.setImageResource(R.drawable.eror);
                        year_validation_imageview.setVisibility(View.VISIBLE);
                    } else {
                        year_validation_imageview.setImageResource(R.drawable.yes);
                        year_validation_imageview.setVisibility(View.VISIBLE);
                    }

                }
            });

            password_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if(password_input.getText().toString().length() < 0) {
                        password_validation_imageview.setImageResource(R.drawable.eror);
                        password_validation_imageview.setVisibility(View.VISIBLE);
                    } else {
                        password_validation_imageview.setImageResource(R.drawable.yes);
                        password_validation_imageview.setVisibility(View.VISIBLE);
                    }

                }
            });

            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.sristi_title));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.changing_button));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.action_button));

        } catch (Exception e) {
            Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_register, menu);
        return true;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if(!hasFocus) {
            return;
        }
    }

    private void setClickableOnTouchListener(final View target, final Runnable action_on_touch_dowon, final Runnable action_on_touch_up, final Runnable action_on_click) {
        target.setOnTouchListener(new View.OnTouchListener() {
            private boolean outside = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect();
                target.getHitRect(rect);
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    action_on_touch_dowon.run();
                } else if (action == MotionEvent.ACTION_MOVE) {
                    if (!rect.contains((int) event.getX(), (int) event.getY())) {
                        action_on_touch_up.run();
                        outside = true;
                    }
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    action_on_touch_up.run();
                    if (!outside) {
                        action_on_click.run();
                    }
                    outside = false;
                }
                return true;
            }
        });
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

    private void postSmallErros( final Object error ) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showModifiedToast(getApplicationContext(), error);
            }
        });
    }
}
