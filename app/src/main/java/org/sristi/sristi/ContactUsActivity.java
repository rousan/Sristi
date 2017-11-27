package org.sristi.sristi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.Utils;

import java.util.Vector;

public class ContactUsActivity extends Activity {

    private final String jgec_lattitude = "26.5434772";
    private final String jgec_longitude = "88.72052559999997";
    private final int zoom_leve_map = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_contact_us);


            findViewById(R.id.back_imageeview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


            View email_view_panel_1 = findViewById(R.id.email_view_panel_1);

            View call_view_panel_1 = findViewById(R.id.call_view_panel_1);
            View call_view_panel_2 = findViewById(R.id.call_view_panel_2);

            View direction_view_panel_1 = findViewById(R.id.direction_view_panel_1);

            Vector<View> email_panels = new Vector<View>();
            email_panels.add(email_view_panel_1);

            Vector<String> emails = new Vector<String>();
            emails.add("mail@sristi.org");

            Vector<View> call_panels = new Vector<View>();
            call_panels.add(call_view_panel_1);
            call_panels.add(call_view_panel_2);

            Vector<String> mobiles = new Vector<String>();
            mobiles.add("+91 8348301952");
            mobiles.add("+91 8116840517");

            for(int i=0; i<email_panels.size(); i++) {
                View email_panel = email_panels.get(i);
                final String email = emails.get(i);
                email_panel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Sristi");
                        intent.putExtra(Intent.EXTRA_TEXT, "Hi, Sristi");
                        if(intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            postSmallErros("No email app is installed in your device!");
                        }
                    }
                });
            }

            for(int i=0; i<call_panels.size(); i++) {
                View call_panel = call_panels.get(i);
                final String mobile = mobiles.get(i);
                call_panel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + mobile));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            postSmallErros("Your device does not support the calling API!!");
                        }
                    }
                });
            }


            final String uri = "geo:" + jgec_lattitude + "," + jgec_longitude + "?z=" + zoom_leve_map;
            direction_view_panel_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(uri));
                    if(intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        postSmallErros("Your device is not support the Google Map!");
                    }
                }
            });

            findViewById(R.id.instagram_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("https://instagram.com/sristi_jgec");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Utils.showModifiedToast(getApplicationContext(), "There are no browser in your system to show this web page!");
                    }
                }
            });

            findViewById(R.id.twitter_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(RemoteServerConstants.getFullAddressOfRemoteServer().toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Utils.showModifiedToast(getApplicationContext(), "There are no browser in your system to show this web page!");
                    }
                }
            });

            findViewById(R.id.fb_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("https://facebook.com/sristi15");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Utils.showModifiedToast(getApplicationContext(), "There are no browser in your system to show this web page!");
                    }
                }
            });

            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.email_tv));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.mobile_tv));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.address_tv));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.we_are_on_tv));


        } catch (Exception e) {
            e.printStackTrace();
            Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_us, menu);
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

    private void postSmallErros( final Object error ) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showModifiedToast(getApplicationContext(), error);
            }
        });
    }
}
