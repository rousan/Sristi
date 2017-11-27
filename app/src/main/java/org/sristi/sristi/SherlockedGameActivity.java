package org.sristi.sristi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.sristi.sristi.utils.JavascriptInterface;
import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.SettingsPreferenceKeys;
import org.sristi.sristi.utils.Utils;
import org.sristi.sristi.utils.VLog;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SherlockedGameActivity extends Activity {

    private String answers_array = "";
    private int answrs_of_level = 0;
    private String question_url_of_level = "";
    private WebView wv;
    private ViewGroup loading_panel;
    private ViewGroup con_error_panel;
    private ViewGroup next_panel;
    private ViewGroup all_done_panel;
    private View submit_btn;
    private boolean submitEnabled = true;
    private String submitDesabledCause = "Now data is fetching, wait...";
    private ViewGroup refresh_btn_panel;
    private TextView playing_level_shower_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sherlocked_game);

            wv = (WebView)findViewById(R.id.wv);
            loading_panel = (ViewGroup)findViewById(R.id.loader_panel);
            con_error_panel = (ViewGroup)findViewById(R.id.connection_error_panel);
            next_panel = (ViewGroup)findViewById(R.id.next_level_panel);
            all_done_panel = (ViewGroup)findViewById(R.id.all_complete_level_panel);
            submit_btn = findViewById(R.id.submit_button);
            refresh_btn_panel = (ViewGroup)findViewById(R.id.refresh_btn_panel);
            playing_level_shower_tv = (TextView)findViewById(R.id.playing_level_shower_tv);

            loading_panel.setVisibility(View.GONE);
            con_error_panel.setVisibility(View.GONE);
            next_panel.setVisibility(View.GONE);
            all_done_panel.setVisibility(View.GONE);

            loading_panel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loading_panel.setVisibility(View.GONE);
                }
            });

            wv.setBackgroundColor(0);
            wv.setWebViewClient(new WebViewClient() {
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    //none
                }

                public void onPageFinished(WebView view, String url) {
                    try {
                        //none
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                @Override
                public void onReceivedError(WebView view, int erorcode, String desc, String errurl) {
                    try {

                        String con_title = "";
                        String con_short_note = "";
                        String conn_setting_btn = "";
                        String conn_refresh_btn = "";

                        //Utils.showModifiedToast(getApplicationContext(), erorcode);

                        if (erorcode == WebViewClient.ERROR_TIMEOUT) {
                            wv.stopLoading();
                            con_title = "Connection Timed Out";
                            con_short_note = "Server is slow to respond, try again later";
                            conn_refresh_btn = "Retry";
                            conn_setting_btn = "Settings";
                        } else if (erorcode == WebViewClient.ERROR_FILE_NOT_FOUND) {
                            wv.stopLoading();
                            con_title = "Content Not Found";
                            con_short_note = "Server might be temporarily down at this moment";
                            conn_refresh_btn = "Refresh";
                            conn_setting_btn = "Settings";
                        } else if (erorcode == WebViewClient.ERROR_HOST_LOOKUP) {
                            wv.stopLoading();
                            con_title = "No Internet Connection";
                            con_short_note = "No internet connection or error host";
                            conn_refresh_btn = "Refresh";
                            conn_setting_btn = "Turn On";
                        } else {
                            return;
                        }

                        final String tem1 = con_title;
                        final String tem2 = con_short_note;
                        final String tem3 = conn_refresh_btn;
                        final String tem4 = conn_setting_btn;

                        ((TextView) findViewById(R.id.connection_error_title)).setText(tem1);
                        ((TextView) findViewById(R.id.connection_error_short_note)).setText(tem2);
                        ((TextView) findViewById(R.id.connection_error_refresh_btn)).setText(tem3);
                        ((TextView) findViewById(R.id.connection_error_settings_explore_btn)).setText(tem4);
                        con_error_panel.setVisibility(View.VISIBLE);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            wv.addJavascriptInterface(new JavascriptInterface(this), "JI");
            wv.setWebChromeClient(new WebChromeClient());
            wv.getSettings().setJavaScriptEnabled(true);
            wv.addJavascriptInterface(new EJI(), "EJI");
            wv.loadUrl("file:///android_asset/web/sherlocked_game_page.html");

            try {
                SharedPreferences sp = getSharedPreferences(SettingsPreferenceKeys.user_account_shared_pref_file, 0);
                ((TextView) findViewById(R.id.user_name_tv)).setText(sp.getString("FIRSTNAME", "-") + " " + sp.getString("LASTNAME", "-"));
               ((TextView) findViewById(R.id.user_level_passed_tv)).setText("--");
            } catch (Exception e) {
                e.printStackTrace();
            }

            playing_level_shower_tv.setText("Level " + answrs_of_level);

            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.user_name_tv));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.submit_button));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.connection_error_title));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.connection_error_settings_explore_btn));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.connection_error_refresh_btn));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.done_title));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.next_button));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.done_title2));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.done_all_refresh_button));
            Utils.setHeadingTextFont(playing_level_shower_tv);

            findViewById(R.id.connection_error_refresh_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), SherlockedGameActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            findViewById(R.id.done_all_refresh_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), SherlockedGameActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            findViewById(R.id.option_btn_panel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                        Menu menu = popupMenu.getMenu();
                        menu.add("Leader Board");
                        menu.add("Exit");
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                try {
                                    if (item.getTitle().toString().trim().toUpperCase().equals("LEADER BOARD")) {

                                        /*Intent i = new Intent(getApplicationContext(), WebBrowserActivity.class);
                                        i.putExtra("URL", "http://app.sristi.org.in/app/online_games/sherlocked/controllers/leader_board.php");
                                        i.putExtra("LOADING_TEXT", "Loading Sherlocked Leader Board, Please wait...");
                                        i.putExtra("REFRESH_URL", "http://app.sristi.org.in/app/online_games/sherlocked/controllers/leader_board.php");
                                        i.putExtra("SHOW_REFRESH_ICON", true);
                                        startActivity(i);*/

                                        Uri uri = Uri.parse("http://app.sristi.org.in/app/online_games/sherlocked/controllers/leader_board.php");
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(intent);
                                        } else {
                                            Utils.showModifiedToast(getApplicationContext(), "There are no browser in your system to show this web page!");
                                        }

                                    } else if (item.getTitle().toString().trim().toUpperCase().equals("EXIT")) {
                                        finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.connection_error_settings_explore_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Utils.showModifiedToast(getApplicationContext(), "No Settings Handler is available in your system");
                    }
                }
            });

            findViewById(R.id.submit_btn_parent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        if (!submit_btn.isEnabled()) {
                            Utils.showModifiedToast(getApplicationContext(), submitDesabledCause);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            final ViewGroup pb_holder = (ViewGroup)findViewById(R.id.pb_holder_panel);
            final EditText input = (EditText)findViewById(R.id.ans_input);
            pb_holder.setVisibility(View.GONE);
            submit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        if (!submitEnabled) {
                            Utils.showModifiedToast(getApplicationContext(), submitDesabledCause);
                            return;
                        }

                        final String answer = input.getText().toString().trim();
                        if (answer.trim().isEmpty()) {
                            Utils.showModifiedToast(getApplicationContext(), "Enter answer of level " + answrs_of_level);
                            return;
                        }

                        if (question_url_of_level.trim().toUpperCase().equals("NULL")) {
                            all_done_panel.setVisibility(View.VISIBLE);
                            return;
                        }

                        String[] answers = answers_array.split(",");
                        boolean matched = false;
                        for (String str : answers) {
                            if (answer.trim().toUpperCase().contains(str.trim().toUpperCase())) {
                                matched = true;
                                break;
                            }
                        }
                        if (matched) {

                            Utils.showModifiedToast(getApplicationContext(), "Answer is correct, Now it will be uploaded, please wait");
                            submit_btn.setVisibility(View.GONE);
                            pb_holder.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        if (!RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext())) {
                                            Utils.showModifiedToast(getApplicationContext(), "No internet connection");
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    submit_btn.setVisibility(View.VISIBLE);
                                                    pb_holder.setVisibility(View.GONE);
                                                }
                                            });
                                            return;
                                        }

                                        SharedPreferences sp = getSharedPreferences(SettingsPreferenceKeys.user_account_shared_pref_file, 0);
                                        String email = sp.getString("EMAIL", "");
                                        String pass = sp.getString("PASSWORD", "");
                                        if (email.trim().isEmpty()) {
                                            Utils.showModifiedToast(getApplicationContext(), "Please log in first");
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    submit_btn.setVisibility(View.VISIBLE);
                                                    pb_holder.setVisibility(View.GONE);
                                                }
                                            });
                                            return;
                                        }
                                        HashMap<String, String> outs = RemoteServerConstants.submitSherlockedAnswer(email, pass, answrs_of_level + "", answer);
                                        if (outs == null) {
                                            Utils.showModifiedToast(getApplicationContext(), "Internet Error or server error");
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    submit_btn.setVisibility(View.VISIBLE);
                                                    pb_holder.setVisibility(View.GONE);
                                                }
                                            });
                                            return;
                                        }
                                        String status = outs.get("STATUS").trim();
                                        if (status.equals("1")) {

                                            Utils.showModifiedToast(getApplicationContext(), "Submission Successful");
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        submit_btn.setVisibility(View.VISIBLE);
                                                        pb_holder.setVisibility(View.GONE);
                                                        ((TextView) findViewById(R.id.done_short_note)).setText("You have completed level " + answrs_of_level);
                                                        ((TextView) findViewById(R.id.user_level_passed_tv)).setText(answrs_of_level + "");
                                                        next_panel.setVisibility(View.VISIBLE);
                                                        playing_level_shower_tv.setText("Level " + (answrs_of_level + 1) + "");
                                                        submitEnabled = false;
                                                        submitDesabledCause = "Answer of " + answrs_of_level + " level is submitted before";
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                            return;

                                        } else {
                                            Utils.showModifiedToast(getApplicationContext(), outs.get("CAUSE"));
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    submit_btn.setVisibility(View.VISIBLE);
                                                    pb_holder.setVisibility(View.GONE);
                                                }
                                            });
                                            return;
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();


                        } else {
                            Utils.showModifiedToast(getApplicationContext(), "Wrong answer");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        con_error_panel.setVisibility(View.GONE);
                        next_panel.setVisibility(View.GONE);
                        refresh();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

           refresh_btn_panel.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   try {

                       con_error_panel.setVisibility(View.GONE);
                       next_panel.setVisibility(View.GONE);
                       all_done_panel.setVisibility(View.GONE);
                       refresh(true);

                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
           });

            loading_panel.setVisibility(View.VISIBLE);
            final TextView loader_short_note = (TextView)findViewById(R.id.loader_short_note);
            loader_short_note.setText("Fetching answer details, please wait...");
            submitEnabled = false;
            submitDesabledCause = "Now data is fetching, wait...";
            wv.loadUrl("javascript:clearQuestionPanel()");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        SharedPreferences sp = getSharedPreferences(SettingsPreferenceKeys.user_account_shared_pref_file, 0);
                            String email = sp.getString("EMAIL", "");
                            String pass = sp.getString("PASSWORD", "");
                            if(email.trim().isEmpty()) {
                                Utils.showModifiedToast(getApplicationContext(), "Please log in first");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading_panel.setVisibility(View.GONE);
                                    }
                                });
                            return;
                        }
                        if(!RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext())) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    con_error_panel.setVisibility(View.VISIBLE);
                                    ((TextView) findViewById(R.id.connection_error_title)).setText("No Connection");
                                    ((TextView) findViewById(R.id.connection_error_short_note)).setText("No internet Connection");
                                    ((TextView) findViewById(R.id.connection_error_settings_explore_btn)).setText("Turn On");
                                }
                            });
                            return;
                        }
                        //Utils.showModifiedToast(getApplicationContext(), "ll");
                        final HashMap<String, String> outs = RemoteServerConstants.getUserSherlockedGameInfo(email, pass);
                        //Utils.showModifiedToast(getApplicationContext(), outs);
                        if (outs == null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    con_error_panel.setVisibility(View.VISIBLE);
                                    ((TextView) findViewById(R.id.connection_error_title)).setText("Connection Error");
                                    ((TextView) findViewById(R.id.connection_error_short_note)).setText("Server is slow to give response");
                                    ((TextView) findViewById(R.id.connection_error_settings_explore_btn)).setText("Settings");
                                }
                            });
                            return;
                        }

                        String status = outs.get("STATUS").trim();
                        if(status.equals("1")) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((TextView) findViewById(R.id.user_name_tv)).setText(outs.get("FULLNAME"));
                                    ((TextView) findViewById(R.id.user_level_passed_tv)).setText(outs.get("CURRENTLEVEL"));
                                }
                            });

                            String curr_level = "0";
                            int next_level = 1;
                            try {
                                Matcher m = Pattern.compile("\\d+").matcher(outs.get("CURRENTLEVEL"));
                                if(m.find()) {
                                    String sub = outs.get("CURRENTLEVEL").substring(m.start(), m.end());
                                    next_level = Integer.parseInt(sub) + 1;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            answrs_of_level = next_level;
                            answers_array = outs.get("NEXTLEVELANSWER");
                            question_url_of_level = outs.get("NEXTLEVELQUESTION");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    playing_level_shower_tv.setText("Level " + answrs_of_level);
                                }
                            });
                            //Utils.showModifiedToast(getApplicationContext(), question_url_of_level);

                            if (question_url_of_level.trim().toUpperCase().equals("NULL")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Utils.showModifiedToast(getApplicationContext(), question_url_of_level);
                                        all_done_panel.setVisibility(View.VISIBLE);
                                    }
                                });
                                return;
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loader_short_note.setText("Fetching " + answrs_of_level + " level's question, please wait...");
                                    submitEnabled = true;
                                }
                            });

                            //download question
                            try {
                                URL url = null;
                                try {
                                    url = new URL(question_url_of_level + "");
                                } catch (Exception e) {
                                    url = null;
                                    e.printStackTrace();
                                }

                                if (url != null) {
                                    try {
                                       // Utils.showModifiedToast(getApplicationContext(), "f");
                                        final String outs_qs = RemoteServerConstants.processingURL(url, "POST", "");
                                        //Utils.showModifiedToast(getApplicationContext(), outs_qs);
                                        if (outs_qs == null || outs_qs.trim().isEmpty()) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    con_error_panel.setVisibility(View.VISIBLE);
                                                    ((TextView) findViewById(R.id.connection_error_title)).setText("Connection Error");
                                                    ((TextView) findViewById(R.id.connection_error_short_note)).setText("Server is slow to give response");
                                                    ((TextView) findViewById(R.id.connection_error_settings_explore_btn)).setText("Settings");
                                                }
                                            });
                                            return;

                                        } else {
                                            try {

                                                String copy = outs_qs;
                                                String randomStr = "GJC4I7Y4GCFGFJEG";
                                                copy = copy.trim();
                                                copy = copy.replaceAll("(\\n)|(\\r)", randomStr);
                                                //Utils.showModifiedToast(getApplicationContext(), copy);
                                                //Utils.showModifiedToast(getApplicationContext(), "l");
                                                final String temp1 = copy;
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loading_panel.setVisibility(View.GONE);
                                                        //in outs_qs var, no new line will stay
                                                        wv.loadUrl("javascript:displayQuestion('" + temp1 + "')");
                                                    }
                                                });

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            return;
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            wv.loadUrl("javascript:visibleQuestionUnavailablePagePanel(true)");
                                            loading_panel.setVisibility(View.GONE);
                                        }
                                    });
                                    return;
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    con_error_panel.setVisibility(View.VISIBLE);
                                    ((TextView) findViewById(R.id.connection_error_title)).setText("Connection Error");
                                    ((TextView) findViewById(R.id.connection_error_short_note)).setText(outs.get("CAUSE"));
                                    ((TextView) findViewById(R.id.connection_error_settings_explore_btn)).setText("Settings");
                                }
                            });
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        refresh(false);
    }

    private void refresh(boolean REFESH_BTN_FLAG) {
        try {

            Runnable runnable1 = new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_z_infinite);
                                refresh_btn_panel.startAnimation(animation);
                                refresh_btn_panel.setEnabled(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            };

            Runnable runnable2 = new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                refresh_btn_panel.clearAnimation();
                                refresh_btn_panel.setEnabled(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            };

            if (!REFESH_BTN_FLAG) {
                runnable1 = new Runnable() {
                    @Override
                    public void run() {

                    }
                };
                runnable2 = new Runnable() {
                    @Override
                    public void run() {

                    }
                };
            }

            Runnable temp1 = runnable1;
            final Runnable temp2 = runnable2;

            loading_panel.setVisibility(View.VISIBLE);
            final TextView loader_short_note = (TextView)findViewById(R.id.loader_short_note);
            loader_short_note.setText("Fetching answer details, please wait...");
            submitEnabled = false;
            submitDesabledCause = "Now data is fetching, wait...";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    wv.loadUrl("javascript:clearQuestionPanel()");
                }
            });
            temp1.run();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        SharedPreferences sp = getSharedPreferences(SettingsPreferenceKeys.user_account_shared_pref_file, 0);
                        String email = sp.getString("EMAIL", "");
                        String pass = sp.getString("PASSWORD", "");
                        if(email.trim().isEmpty()) {
                            Utils.showModifiedToast(getApplicationContext(), "Please log in first");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loading_panel.setVisibility(View.GONE);
                                }
                            });
                            temp2.run();
                            return;
                        }
                        if(!RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext())) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    con_error_panel.setVisibility(View.VISIBLE);
                                    ((TextView) findViewById(R.id.connection_error_title)).setText("No Connection");
                                    ((TextView) findViewById(R.id.connection_error_short_note)).setText("No internet Connection");
                                    ((TextView) findViewById(R.id.connection_error_settings_explore_btn)).setText("Turn On");
                                }
                            });
                            temp2.run();
                            return;
                        }
                       // Utils.showModifiedToast(getApplicationContext(), "ll");
                        final HashMap<String, String> outs = RemoteServerConstants.getUserSherlockedGameInfo(email, pass);
                        //Utils.showModifiedToast(getApplicationContext(), outs);
                        if (outs == null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    con_error_panel.setVisibility(View.VISIBLE);
                                    ((TextView) findViewById(R.id.connection_error_title)).setText("Connection Error");
                                    ((TextView) findViewById(R.id.connection_error_short_note)).setText("Server is slow to give response");
                                    ((TextView) findViewById(R.id.connection_error_settings_explore_btn)).setText("Settings");
                                }
                            });
                            temp2.run();
                            return;
                        }

                        String status = outs.get("STATUS").trim();
                        if(status.equals("1")) {
                            temp2.run();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((TextView) findViewById(R.id.user_name_tv)).setText(outs.get("FULLNAME"));
                                    ((TextView) findViewById(R.id.user_level_passed_tv)).setText(outs.get("CURRENTLEVEL"));
                                }
                            });

                            String curr_level = "0";
                            int next_level = 1;
                            try {
                                Matcher m = Pattern.compile("\\d+").matcher(outs.get("CURRENTLEVEL"));
                                if(m.find()) {
                                    String sub = outs.get("CURRENTLEVEL").substring(m.start(), m.end());
                                    next_level = Integer.parseInt(sub) + 1;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            answrs_of_level = next_level;
                            answers_array = outs.get("NEXTLEVELANSWER");
                            question_url_of_level = outs.get("NEXTLEVELQUESTION");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    playing_level_shower_tv.setText("Level " + answrs_of_level);
                                }
                            });
                            //Utils.showModifiedToast(getApplicationContext(), question_url_of_level);

                            if (question_url_of_level.trim().toUpperCase().equals("NULL")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Utils.showModifiedToast(getApplicationContext(), question_url_of_level);
                                        all_done_panel.setVisibility(View.VISIBLE);
                                    }
                                });
                                return;
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loader_short_note.setText("Fetching " + answrs_of_level + " level's question, please wait...");
                                    submitEnabled = true;
                                }
                            });

                            //download question
                            try {
                                URL url = null;
                                try {
                                    url = new URL(question_url_of_level + "");
                                } catch (Exception e) {
                                    url = null;
                                    e.printStackTrace();
                                }

                                if (url != null) {
                                    try {
                                        //Utils.showModifiedToast(getApplicationContext(), "f");
                                        final String outs_qs = RemoteServerConstants.processingURL(url, "POST", "");
                                        //Utils.showModifiedToast(getApplicationContext(), outs_qs);
                                        if (outs_qs == null || outs_qs.trim().isEmpty()) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    con_error_panel.setVisibility(View.VISIBLE);
                                                    ((TextView) findViewById(R.id.connection_error_title)).setText("Connection Error");
                                                    ((TextView) findViewById(R.id.connection_error_short_note)).setText("Server is slow to give response");
                                                    ((TextView) findViewById(R.id.connection_error_settings_explore_btn)).setText("Settings");
                                                }
                                            });
                                            return;

                                        } else {
                                            try {

                                                String copy = outs_qs;
                                                String randomStr = "GJC4I7Y4GCFGFJEG";
                                                copy = copy.trim();
                                                copy = copy.replaceAll("(\\n)|(\\r)", randomStr);
                                                //Utils.showModifiedToast(getApplicationContext(), copy);
                                                //Utils.showModifiedToast(getApplicationContext(), "l");
                                                final String temp1 = copy;
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loading_panel.setVisibility(View.GONE);
                                                        //in outs_qs var, no new line will stay
                                                        wv.loadUrl("javascript:displayQuestion('" + temp1 + "')");
                                                    }
                                                });

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            return;
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            wv.loadUrl("javascript:visibleQuestionUnavailablePagePanel(true)");
                                            loading_panel.setVisibility(View.GONE);
                                        }
                                    });
                                    return;
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            temp2.run();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    con_error_panel.setVisibility(View.VISIBLE);
                                    ((TextView) findViewById(R.id.connection_error_title)).setText("Connection Error");
                                    ((TextView) findViewById(R.id.connection_error_short_note)).setText(outs.get("CAUSE"));
                                    ((TextView) findViewById(R.id.connection_error_settings_explore_btn)).setText("Settings");
                                }
                            });
                            return;
                        }
                        temp2.run();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sherlocked_game, menu);
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

    private class EJI {

        @android.webkit.JavascriptInterface
        public String d(String url) {
            String outs = "";
            try {

                outs = RemoteServerConstants.processingURL(new URL(url), "POST", "");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return outs;
        }

    }
}
































