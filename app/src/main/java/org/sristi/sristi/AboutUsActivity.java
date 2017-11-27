package org.sristi.sristi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.sristi.sristi.utils.Utils;

import java.util.Vector;

public class AboutUsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about_us);

            TextView about_title = (TextView)findViewById(R.id.about_title);

            Vector<View> panels = new Vector<View>();
            panels.add(findViewById(R.id.facebook_panel));
            panels.add(findViewById(R.id.instagram_panel));
            panels.add(findViewById(R.id.twitter_panel));
            panels.add(findViewById(R.id.legal_panel));
            panels.add(findViewById(R.id.rating_panel));

            Vector<String> urls = new Vector<>();
            urls.add("https://facebook.com/sristi15");
            urls.add("https://instagram.com/sristi_jgec");
            urls.add("https://twitter.com/sristi_jgec");
            urls.add("http://sristi.org.in");
            urls.add("http://sristi.org.in");

        /*for(final View panel : panels) {
            setClickableOnTouchListener(panel,
                    new Runnable() {
                        @Override
                        public void run() {
                            panel.setBackgroundColor(Color.parseColor("#eef3f5"));
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            panel.setBackgroundColor(Color.parseColor("#00000000"));
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            //nothing now
                        }
                    }
            );
        }*/

            for(int i=0; i<panels.size(); i++) {
                try {
                    final String url = urls.get(i);
                    panels.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Uri uri = Uri.parse(url);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            findViewById(R.id.back_imageeview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            Utils.setHeadingTextFont(about_title);

        } catch (Exception e) {
            e.printStackTrace();
            Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    private void setClickableOnTouchListener(final View target, final Runnable action_on_touch_dowon, final Runnable action_on_touch_up, final Runnable action_on_click) {
        target.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    action_on_touch_dowon.run();
                } else if (action == MotionEvent.ACTION_MOVE) {
                    //no checking
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    action_on_touch_up.run();
                    action_on_click.run();
                }
                return true;
            }
        });

        Utils.setFontsToAllTextViews((ViewGroup)findViewById(R.id.root));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_us, menu);
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
