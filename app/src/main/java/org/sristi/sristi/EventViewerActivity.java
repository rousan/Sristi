package org.sristi.sristi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.sristi.sristi.utils.Utils;

public class EventViewerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_event_viewer);

            final Activity this_Activity = this;

            Intent intent = getIntent();
            if(intent == null) {
                finish();
                return;
            }

            String top_title = intent.getStringExtra("TOP_TITLE");
            String top_short_note = intent.getStringExtra("TOP_SHORT_NOTE");
            final String[] contacts = intent.getStringArrayExtra("TOP_CONTACTS");
            String webview_url = intent.getStringExtra("DATA_WV_URL");
            int top_pic_res_id = intent.getIntExtra("TOP_PIC_RES_ID", R.drawable.notif1);

            ((TextView) findViewById(R.id.top_name_title)).setText(top_title);
            ((TextView) findViewById(R.id.top_short_note)).setText(top_short_note);
            ((ImageView) findViewById(R.id.top_pic)).setImageResource(top_pic_res_id);

            findViewById(R.id.contact_us_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        if (contacts.length == 0) {
                            Utils.showModifiedToast(getApplicationContext(), "No contacts available");
                            return;
                        }

                        final AlertDialog ad = new AlertDialog.Builder(EventViewerActivity.this).create();

                        ViewGroup root = (ViewGroup)getLayoutInflater().inflate(R.layout.event_contact_dialog_layout, null);
                        Utils.setFontsToAllTextViews(root);
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.dialog_okay_btn));
                        ViewGroup contact_holder = (ViewGroup)root.findViewById(R.id.contact_holder);
                        root.findViewById( R.id.dialog_okay_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               try {
                                   ad.cancel();
                               } catch (Exception e) {
                                   e.printStackTrace();
                               }
                            }
                        });
                        for(String contact : contacts) {
                            String[] parts = contact.trim().split("@");
                            String name = parts[0].trim();
                            final String mobile = parts[1].trim();
                            ViewGroup contact_single = (ViewGroup)getLayoutInflater().inflate(R.layout.event_single_contact_layout, null);
                            ((TextView)contact_single.findViewById(R.id.contact_name)).setText(name);
                            if (mobile.equals("NULL"))
                                contact_single.findViewById(R.id.bottom_panel).setVisibility(View.GONE);
                            else {
                                ((TextView)contact_single.findViewById(R.id.contact_mobile)).setText(mobile);
                                contact_single.findViewById(R.id.call_explore_btn_panel).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Intent intent = new Intent(Intent.ACTION_DIAL);
                                            intent.setData(Uri.parse("tel:" + mobile));
                                            if (intent.resolveActivity(getPackageManager()) != null) {
                                                startActivity(intent);
                                            } else {
                                                Utils.showModifiedToast(getApplicationContext(), "Device does not support calling feature");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            Utils.setFontsToAllTextViews(contact_single);
                            Utils.setHeadingTextFont((TextView)contact_single.findViewById(R.id.contact_name));
                            contact_holder.addView(contact_single, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        }

                        ad.setView(root);
                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ad.show();
                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ad.getWindow().setLayout((int) (Utils.getScreenDimentions(this_Activity).x * 0.9), (int) (Utils.getScreenDimentions(this_Activity).y * 0.6));


                    } catch (Exception e) {
                        Utils.showModifiedToast(getApplicationContext(), e);
                    }
                }
            });

            WebView webView = (WebView)findViewById(R.id.webview);
            webView.setBackgroundColor(0);
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
            webView.loadUrl(webview_url);

            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.top_name_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.contact_us_btn));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_viewer, menu);
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
