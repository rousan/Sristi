package org.sristi.sristi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.Utils;

public class CampusMapActivity extends Activity {

    private final String jgec_lattitude = "26.5434772";
    private final String jgec_longitude = "88.72052559999997";
    private final int zoom_leve_map = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_campus_map);

            findViewById(R.id.back_imageeview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            View view_map = findViewById(R.id.view_map);
            final String uri = "geo:" + jgec_lattitude + "," + jgec_longitude + "?z=" + zoom_leve_map;
            view_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext())) {
                        Utils.showModifiedToast(getApplicationContext(), "No Internet Connection");
                        return;
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(uri));
                    if(intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        postSmallErros("Your device is not support the Google Map!");
                    }

                }
            });

            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.address_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.global_pos_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.places_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.view_map));

        } catch (Exception e) {
            e.printStackTrace();
            Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_campus_map, menu);
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
