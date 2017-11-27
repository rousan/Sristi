package org.sristi.sristi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.sristi.sristi.utils.FilesData;
import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

public class UpdateCenterActivity extends Activity {
    private boolean restriction_back = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_update_center);

            final ViewGroup update_found_panel = (ViewGroup)findViewById(R.id.update_found_panel);
            final ViewGroup no_updates_result_panel = (ViewGroup)findViewById(R.id.no_updates_result_panel);
            final ViewGroup loading_holder_panel = (ViewGroup)findViewById(R.id.loading_holder_panel);

            //initially
            update_found_panel.setVisibility(View.GONE);
            no_updates_result_panel.setVisibility(View.GONE);
            loading_holder_panel.setVisibility(View.GONE);

            ((TextView) no_updates_result_panel.findViewById(R.id.short_note)).setText("You are up to dated, Version " + Utils.getAppVersionName());

            boolean onlineCheck = true;
            Intent intent = getIntent();
            if (intent == null)
                onlineCheck = true;
            else {
                onlineCheck = intent.getBooleanExtra("ONLINE_CHECK", true);
            }


            no_updates_result_panel.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            final boolean temp1 = onlineCheck;
            no_updates_result_panel.findViewById(R.id.refresh_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(getApplicationContext(), UpdateCenterActivity.class);
                        i.putExtra("ONLINE_CHECK", temp1);
                        startActivity(i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            loading_holder_panel.findViewById(R.id.connection_error_refresh_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(getApplicationContext(), UpdateCenterActivity.class);
                        i.putExtra("ONLINE_CHECK", temp1);
                        startActivity(i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            loading_holder_panel.findViewById(R.id.connection_error_settings_explore_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            Utils.showModifiedToast(getApplicationContext(), "No Settings Handler is available in your system");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            final ViewGroup loading_data_anim_mask = (ViewGroup)update_found_panel.findViewById(R.id.loading_data_anim_mask);
            final WebView webView = (WebView)update_found_panel.findViewById(R.id.wv);
            webView.setWebViewClient(new WebViewClient() {
                public void onPageStarted(WebView view, String url, Bitmap favicon) {

                }

                public void onPageFinished(WebView view, String url) {
                    try {
                        loading_data_anim_mask.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            webView.setBackgroundColor(0);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());


            if(onlineCheck) {
                loading_holder_panel.setVisibility(View.VISIBLE);
                loading_holder_panel.findViewById(R.id.loading_ani_panel).setVisibility(View.VISIBLE);
                loading_holder_panel.findViewById(R.id.connection_error_panel).setVisibility(View.GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(!RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext())) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading_holder_panel.findViewById(R.id.loading_ani_panel).setVisibility(View.GONE);
                                        ((TextView) loading_holder_panel.findViewById(R.id.connection_error_title)).setText("No internet connection");
                                        ((TextView) loading_holder_panel.findViewById(R.id.connection_error_short_note)).setText("No internet connection available");
                                        ((TextView) loading_holder_panel.findViewById(R.id.connection_error_settings_explore_btn)).setText("Turn On");
                                        loading_holder_panel.findViewById(R.id.connection_error_panel).setVisibility(View.VISIBLE);
                                    }
                                });
                                return;
                            }
                            //Utils.showModifiedToast(getApplicationContext(), "loading");
                            String outs = RemoteServerConstants.checkAppUpdate();
                            //Utils.showModifiedToast(getApplicationContext(), "Completed");
                            if (outs == null || outs.trim().isEmpty()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading_holder_panel.findViewById(R.id.loading_ani_panel).setVisibility(View.GONE);
                                        ((TextView) loading_holder_panel.findViewById(R.id.connection_error_title)).setText("Connection Error");
                                        ((TextView) loading_holder_panel.findViewById(R.id.connection_error_short_note)).setText("Server is busy or any network error");
                                        ((TextView) loading_holder_panel.findViewById(R.id.connection_error_settings_explore_btn)).setText("Settings");
                                        loading_holder_panel.findViewById(R.id.connection_error_panel).setVisibility(View.VISIBLE);
                                    }
                                });
                                return;
                            }

                            String parse_str = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + outs;

                            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(parse_str)));
                            String status = ((Element)doc.getElementsByTagName("STATUS").item(0)).getTextContent();
                            if(status.trim().equals("1")) {
                                String version_code = ((Element)doc.getElementsByTagName("VERSIONCODE").item(0)).getTextContent();
                                FilesData.setup(getApplicationContext());

                                File data_folder = getDir(FilesData.INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE);
                                File version_code_file = new File(data_folder.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_UPDATES + File.separator + FilesData.INTERNAL_FOLDER_APP_UPDATES + File.separator + FilesData.INTERNAL_FILE_APP_UPDATES_APP_UPDATED_VERSION_CODE);
                                File app_updated_details = new File(data_folder.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_UPDATES + File.separator + FilesData.INTERNAL_FOLDER_APP_UPDATES + File.separator + FilesData.INTERNAL_FILE_APP_UPDATES_APP_UPDATED_DETAILS);

                                FilesData.writeFile(version_code_file, version_code.trim(), false);
                                FilesData.writeFile(app_updated_details, outs.trim(), false);

                                final String restriction = ((Element)doc.getElementsByTagName("RESTRICTION").item(0)).getTextContent();
                                final String version_name = ((Element)doc.getElementsByTagName("VERSIONNAME").item(0)).getTextContent();
                                final String size = ((Element)doc.getElementsByTagName("SIZE").item(0)).getTextContent();
                                final String download_link = ((Element)doc.getElementsByTagName("DOWNLOADLINK").item(0)).getTextContent();
                                final String extra_data_html_url = ((Element)doc.getElementsByTagName("EXTRADATAHTMLURL").item(0)).getTextContent();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((TextView) update_found_panel.findViewById(R.id.version_name_tv)).setText("SRISTI " + version_name);
                                        ((TextView) update_found_panel.findViewById(R.id.download_btn)).setText("Download Now (" + size.trim() + ")");
                                    }
                                });

                                final String temp2 = extra_data_html_url;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        webView.loadUrl(temp2);
                                        loading_data_anim_mask.setVisibility(View.VISIBLE);
                                    }
                                });

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        update_found_panel.findViewById(R.id.download_btn).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                try {

                                                    Uri uri = Uri.parse(download_link);
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                                        startActivity(intent);
                                                    } else {
                                                        Utils.showModifiedToast(getApplicationContext(), "There are no browser in your system");
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        update_found_panel.findViewById(R.id.download_later_btn).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                try {

                                                    if(Boolean.parseBoolean(restriction)) {
                                                        Utils.showModifiedToast(getApplicationContext(), "You must update this app to proceed");
                                                    } else {
                                                        finish();
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                });

                                restriction_back = Boolean.parseBoolean(restriction);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading_holder_panel.setVisibility(View.GONE);
                                        //Utils.showModifiedToast(getApplicationContext(), loading_holder_panel.getVisibility());
                                        update_found_panel.setVisibility(View.VISIBLE);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading_holder_panel.setVisibility(View.GONE);
                                        ((TextView) no_updates_result_panel.findViewById(R.id.short_note)).setText("You are up to dated, Version " + Utils.getAppVersionName());
                                        no_updates_result_panel.setVisibility(View.VISIBLE);
                                    }
                                });
                            }

                        } catch (Exception e) {
                            finish();
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                try {

                    FilesData.setup(getApplicationContext());
                    File data_folder = getDir(FilesData.INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE);
                    File version_code_file = new File(data_folder.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_UPDATES + File.separator + FilesData.INTERNAL_FOLDER_APP_UPDATES + File.separator + FilesData.INTERNAL_FILE_APP_UPDATES_APP_UPDATED_VERSION_CODE);
                    File app_updated_details = new File(data_folder.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_UPDATES + File.separator + FilesData.INTERNAL_FOLDER_APP_UPDATES + File.separator + FilesData.INTERNAL_FILE_APP_UPDATES_APP_UPDATED_DETAILS);

                    int version = Utils.getAppVersionCode();
                    try {
                        version = Integer.parseInt(FilesData.readFile(version_code_file));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (version <= Utils.getAppVersionCode()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading_holder_panel.setVisibility(View.GONE);
                                no_updates_result_panel.setVisibility(View.VISIBLE);
                                ((TextView) no_updates_result_panel.findViewById(R.id.short_note)).setText("You are up to dated, Version " + Utils.getAppVersionName());
                            }
                        });
                    } else {
                        String app_update_details = FilesData.readFile(app_updated_details);
                        app_update_details = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + app_update_details;

                        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(app_update_details)));

                        final String restriction = ((Element)doc.getElementsByTagName("RESTRICTION").item(0)).getTextContent();
                        final String version_name = ((Element)doc.getElementsByTagName("VERSIONNAME").item(0)).getTextContent();
                        final String size = ((Element)doc.getElementsByTagName("SIZE").item(0)).getTextContent();
                        final String download_link = ((Element)doc.getElementsByTagName("DOWNLOADLINK").item(0)).getTextContent();
                        final String extra_data_html_url = ((Element)doc.getElementsByTagName("EXTRADATAHTMLURL").item(0)).getTextContent();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) update_found_panel.findViewById(R.id.version_name_tv)).setText("SRISTI " + version_name);
                                ((TextView) update_found_panel.findViewById(R.id.download_btn)).setText("Download Now (" + size.trim() + ")");

                                webView.loadUrl(extra_data_html_url);
                                loading_data_anim_mask.setVisibility(View.VISIBLE);

                                update_found_panel.findViewById(R.id.download_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {

                                            Uri uri = Uri.parse(download_link);
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            if (intent.resolveActivity(getPackageManager()) != null) {
                                                startActivity(intent);
                                            } else {
                                                Utils.showModifiedToast(getApplicationContext(), "There are no browser in your system");
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                update_found_panel.findViewById(R.id.download_later_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {

                                            if(Boolean.parseBoolean(restriction)) {
                                                Utils.showModifiedToast(getApplicationContext(), "You must update this app to proceed");
                                            } else {
                                                finish();
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });

                        restriction_back = Boolean.parseBoolean(restriction);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading_holder_panel.setVisibility(View.GONE);
                                update_found_panel.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                } catch (Exception e) {
                    finish();
                    e.printStackTrace();
                }
            }

            Utils.setFontsToAllTextViews(update_found_panel);
            Utils.setFontsToAllTextViews(no_updates_result_panel);
            Utils.setFontsToAllTextViews(loading_holder_panel);

            Utils.setHeadingTextFont((TextView) update_found_panel.findViewById(R.id.version_name_tv));
            Utils.setHeadingTextFont((TextView) update_found_panel.findViewById(R.id.download_btn));
            Utils.setHeadingTextFont((TextView) update_found_panel.findViewById(R.id.download_later_btn));

            Utils.setHeadingTextFont((TextView) loading_holder_panel.findViewById(R.id.connection_error_title));
            Utils.setHeadingTextFont((TextView) loading_holder_panel.findViewById(R.id.connection_error_refresh_btn));
            Utils.setHeadingTextFont((TextView) loading_holder_panel.findViewById(R.id.connection_error_settings_explore_btn));

            Utils.setHeadingTextFont((TextView) no_updates_result_panel.findViewById(R.id.title));
            Utils.setHeadingTextFont((TextView) no_updates_result_panel.findViewById(R.id.cancel_btn));
            Utils.setHeadingTextFont((TextView) no_updates_result_panel.findViewById(R.id.refresh_btn));


        } catch (Exception e) {
            finish();
            Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_center, menu);
        return true;
    }

    public void onBackPressed() {
        try {

            if (restriction_back) {
                Utils.showModifiedToast(getApplicationContext(), "You must update this app to proceed");
            } else {
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
