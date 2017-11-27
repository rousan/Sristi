package org.sristi.sristi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sristi.sristi.utils.Utils;

public class ContributorsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_contributors);

            findViewById(R.id.back_imageeview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            final TextView open_profiles_btn = (TextView)findViewById(R.id.open_profiles_btn);
            open_profiles_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String txt = open_profiles_btn.getText().toString();
                    open_profiles_btn.setEnabled(false);
                    open_profiles_btn.setBackgroundResource(R.drawable.btn_bg_1_pressed);
                    open_profiles_btn.setText("Loading...");
                    Handler handler = new Handler(getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            open_profiles_btn.setEnabled(true);
                            open_profiles_btn.setBackgroundResource(R.drawable.btn_bg_1_state);
                            open_profiles_btn.setText(txt);
                        }
                    }, 2000);
                    startActivity(new Intent(getApplicationContext(), ContributorProfileActivity.class));
                }
            });

            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.h1));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.h2));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.sristi_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.open_profiles_btn));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.h3));

        } catch (Exception e) {
            e.printStackTrace();
            //Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contributors, menu);
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
