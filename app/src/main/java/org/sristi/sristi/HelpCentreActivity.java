package org.sristi.sristi;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.SettingsPreferenceKeys;
import org.sristi.sristi.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class HelpCentreActivity extends Activity {

    private EditText email_input = null;
    private EditText message_input = null;
    private ImageView message_validation_imageview = null;
    private ImageView email_validation_imageview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_help_centre);

            final ViewGroup pb_holder_panel = (ViewGroup)findViewById(R.id.pb_holder_panel);
            final TextView send_button = (TextView)findViewById(R.id.send_button);
            email_input = (EditText)findViewById(R.id.email_input);
            message_input = (EditText) findViewById(R.id.message_input);
            message_validation_imageview = (ImageView)findViewById(R.id.message_validation_imageview);
            email_validation_imageview = (ImageView)findViewById(R.id.email_validation_imageview);

            findViewById(R.id.back_imageeview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            final Vector<EditText> inputs = new Vector<EditText>();
            inputs.add(email_input);
            inputs.add(message_input);

            for(EditText et : inputs) {
                final EditText temp = et;
                et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus) {
                            ((ViewGroup)temp.getParent()).setBackgroundColor(Color.parseColor("#33000000"));
                        } else {
                            ((ViewGroup)temp.getParent()).setBackgroundColor(Color.parseColor("#00000000"));
                        /*if(temp == message_input) {
                            if (temp.getText().toString().isEmpty()) {
                                ((ViewGroup)temp.getParent()).setBackgroundColor(Color.parseColor("#00000000"));
                            } else {
                                ((ViewGroup)temp.getParent()).setBackgroundColor(Color.parseColor("#33000000"));
                            }
                        } else {
                            ((ViewGroup)temp.getParent()).setBackgroundColor(Color.parseColor("#00000000"));
                        }*/
                        }
                    }
                });
            }


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

            message_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if(message_input.getText().toString().isEmpty()) {
                        message_validation_imageview.setImageResource(R.drawable.eror);
                        message_validation_imageview.setVisibility(View.VISIBLE);
                    } else {
                        message_validation_imageview.setImageResource(R.drawable.yes);
                        message_validation_imageview.setVisibility(View.VISIBLE);
                    }

                }
            });

            send_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        final String email = email_input.getText().toString().trim();
                        final String messages = message_input.getText().toString().trim();
                        if(email.isEmpty() || !email.contains("@") ) {
                            Utils.showModifiedToast(getApplicationContext(), "Invalid email");
                            return;
                        }
                        if(messages.isEmpty()) {
                            Utils.showModifiedToast(getApplicationContext(), "Type messages");
                            return;
                        }

                        if(!RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext())) {
                            Utils.showModifiedToast(getApplicationContext(), "No internet connection");
                            return;
                        }

                        send_button.setVisibility(View.GONE);
                        pb_holder_panel.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    HashMap<String, String> outs = RemoteServerConstants.postHelpSuggestions(email, messages);
                                    if(outs == null) {
                                        Utils.showModifiedToast(getApplicationContext(), "Internal or server problem");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                pb_holder_panel.setVisibility(View.GONE);
                                                send_button.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        return;
                                    }

                                    if(outs.get("STATUS").equals("1")) {

                                        Utils.showModifiedToast(getApplicationContext(), "Your queries sent successfully");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                pb_holder_panel.setVisibility(View.GONE);
                                                send_button.setVisibility(View.VISIBLE);
                                            }
                                        });


                                    } else {
                                        Utils.showModifiedToast(getApplicationContext(), outs.get("CAUSE"));
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                pb_holder_panel.setVisibility(View.GONE);
                                                send_button.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        return;
                                    }

                                } catch (Exception e) {
                                    Utils.showModifiedToast(getApplicationContext(), e);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            pb_holder_panel.setVisibility(View.GONE);
                                            send_button.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        }).start();


                    } catch (Exception e) {
                        Utils.showModifiedToast(getApplicationContext(), e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb_holder_panel.setVisibility(View.GONE);
                                send_button.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            });

            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.send_button));

        } catch (Exception e) {
            Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help_centre, menu);
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
