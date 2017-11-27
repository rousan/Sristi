package org.sristi.sristi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.sristi.sristi.utils.JavascriptInterface;
import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.Utils;

import java.net.URL;

public class WebBrowserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_web_browser);

            final ViewGroup refresh_btn_panel = (ViewGroup)findViewById(R.id.refresh_btn_panel);
            final ViewGroup loader_panel = (ViewGroup)findViewById(R.id.loader_holder_panel);
            final ViewGroup connection_error_showing_panel = (ViewGroup)findViewById(R.id.error_showing_panel);
            final TextView loading_text = (TextView)findViewById(R.id.loader_text);
            final TextView connection_error_title = (TextView)findViewById(R.id.connection_error_title);
            final TextView connection_error_short_note = (TextView)findViewById(R.id.connection_error_short_note);
            final TextView connection_error_refresh_btn = (TextView)findViewById(R.id.connection_error_refresh_btn);
            final TextView connection_error_settings_explore_btn = (TextView)findViewById(R.id.connection_error_settings_explore_btn);
            final WebView wv = (WebView)findViewById(R.id.wv);

            String url_val = RemoteServerConstants.getFullAddressOfRemoteServer().toString();
            String loading_text_val = "Loading Content, Please Wait...";
            String refresh_url_val = url_val;
            boolean showRefreshIcon = true;
            Intent intent = getIntent();
            if(intent != null) {
                url_val = intent.getStringExtra("URL");
                loading_text_val = intent.getStringExtra("LOADING_TEXT");
                refresh_url_val = intent.getStringExtra("REFRESH_URL");
                showRefreshIcon = intent.getBooleanExtra("SHOW_REFRESH_ICON", true);
            }
            if(url_val == null || url_val.trim().isEmpty())
                url_val = RemoteServerConstants.getFullAddressOfRemoteServer().toString();
            try {
                new URL(url_val);
            } catch (Exception e){
                url_val = RemoteServerConstants.getFullAddressOfRemoteServer().toString();
                e.printStackTrace();
            }
            if(loading_text_val == null || loading_text_val.trim().isEmpty()) {
                loading_text_val = "Loading Content, Please Wait...";
            }
            if(refresh_url_val == null || refresh_url_val.trim().isEmpty()) {
                refresh_url_val = url_val;
            }
            try {
                new URL(refresh_url_val);
            } catch (Exception e) {
                refresh_url_val = url_val;
                e.printStackTrace();
            }

            if (showRefreshIcon) {
                findViewById(R.id.refresh_btn_panel).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.refresh_btn_panel).setVisibility(View.GONE);
            }

            loading_text.setText(loading_text_val);
            loader_panel.setVisibility(View.VISIBLE);
            connection_error_showing_panel.setVisibility(View.GONE);
            final String temp1 = url_val;
            final String temp2 = loading_text_val;
            final String temp3 = refresh_url_val;
            connection_error_refresh_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), WebBrowserActivity.class);
                    i.putExtra("URL", temp1);
                    i.putExtra("LOADING_TEXT", temp2);
                    i.putExtra("REFRESH_URL", temp3);
                    startActivity(i);
                    finish();
                }
            });
            connection_error_settings_explore_btn.setOnClickListener(new View.OnClickListener() {
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

            refresh_btn_panel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_z_infinite);
                        wv.stopLoading();
                        wv.setWebViewClient(new WebViewClient() {
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                refresh_btn_panel.setEnabled(false);
                                refresh_btn_panel.startAnimation(animation);
                            }

                            public void onPageFinished(WebView view, String url) {
                                try {

                                    refresh_btn_panel.setEnabled(true);
                                    refresh_btn_panel.clearAnimation();

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

                                    connection_error_title.setText(tem1);
                                    connection_error_short_note.setText(tem2);
                                    connection_error_refresh_btn.setText(tem3);
                                    connection_error_settings_explore_btn.setText(tem4);
                                    connection_error_showing_panel.setVisibility(View.VISIBLE);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        wv.loadUrl(temp3);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            /*if(!RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext())) {
                try {
                    loader_panel.setVisibility(View.GONE);
                    connection_error_title.setText("No Internet Connection");
                    connection_error_short_note.setText("No internet connection available in your system");
                    connection_error_refresh_btn.setText("Refresh");
                    connection_error_settings_explore_btn.setText("Turn On");
                    connection_error_showing_panel.setVisibility(View.VISIBLE);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/

            loader_panel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loader_panel.setVisibility(View.GONE);
                }
            });

            wv.setBackgroundColor(0);
            wv.setWebViewClient(new WebViewClient() {
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                }

                public void onPageFinished(WebView view, String url) {
                    try {

                        loader_panel.setVisibility(View.GONE);

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

                        Utils.showModifiedToast(getApplicationContext(), erorcode);

                        if (erorcode == ERROR_TIMEOUT) {
                            wv.stopLoading();
                            con_title = "Connection Timed Out";
                            con_short_note = "Server is slow to respond, try again later";
                            conn_refresh_btn = "Retry";
                            conn_setting_btn = "Settings";
                        } else if (erorcode == ERROR_FILE_NOT_FOUND) {
                            wv.stopLoading();
                            con_title = "Content Not Found";
                            con_short_note = "Server might be temporarily down at this moment";
                            conn_refresh_btn = "Refresh";
                            conn_setting_btn = "Settings";
                        }  else if (erorcode == WebViewClient.ERROR_HOST_LOOKUP) {
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

                        connection_error_title.setText(tem1);
                        connection_error_short_note.setText(tem2);
                        connection_error_refresh_btn.setText(tem3);
                        connection_error_settings_explore_btn.setText(tem4);
                        connection_error_showing_panel.setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            wv.addJavascriptInterface(new JavascriptInterface(this), "JI");
            wv.addJavascriptInterface(new ExtraJSInterface(), "EJI");
            wv.setWebChromeClient(new WebChromeClient());
            wv.getSettings().setJavaScriptEnabled(true);
            wv.loadUrl(url_val);

            Utils.setFontsToAllTextViews(loader_panel);
            Utils.setFontsToAllTextViews(connection_error_showing_panel);
            Utils.setHeadingTextFont(connection_error_refresh_btn);
            Utils.setHeadingTextFont(connection_error_settings_explore_btn);
            Utils.setHeadingTextFont(connection_error_title);


        } catch (Exception e) {
            Utils.showModifiedToast(getApplicationContext(), e);
            e.printStackTrace();
        }
    }

    public class ExtraJSInterface {

        public ExtraJSInterface() {}

        @android.webkit.JavascriptInterface
        public void refreshPage() {
            startActivity(new Intent(getApplicationContext(), WebBrowserActivity.class).putExtra("URL", getIntent().getStringExtra("URL")));
            finish();
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
        getMenuInflater().inflate(R.menu.menu_web_browser, menu);
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






















