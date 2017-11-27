package org.sristi.sristi;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends Activity {

    private int LEFT_DRAWER_WIDTH = 0;

    public final static String EVENT_DAY_TAG = "DAY";
    public final static String EVENT_TITLE_TAG = "TITLE";
    public final static String EVENT_SHORT_NOTE = "SHORTNOTES";
    public final static String EVENTS_DIR = "events";
    public final static String EVENTS_CONFIG_FILE = "config.xml";
    public final static String ASSETS_DIR = "assets";

    private Vector<Integer> ALL_SHOWN_EVENT_IDS = new Vector<Integer>();

    private ViewGroup CURRENT_LEFT_CLICKED_ITEM = null;

    private FrameLayout main_content = null;
    private ViewGroup root_main_content = null;
    //private ViewGroup left_drawer_panel = null;
   // private ViewGroup left_drawer_content_holder = null;
    private LinearLayout left_drawer_content = null;
    private ViewGroup left_drawer_rotator = null;
    private LinearLayout left_drawer_content_item_holder = null;
    //private ViewGroup left_drawer_content_mask = null;
    private DrawerLayout drawer_Layout = null;
    private ScrollView event_list_scrollView = null;
    private ViewGroup contact_layout = null;
    private ViewGroup about_layout = null;
    private ViewGroup help_layout = null;
    private ViewGroup faqs_layout = null;

    private ImageView drawer_open_close_icon = null;

    //  private ActionBarDrawerToggle actionBarDrawerToggle = null;

    private ViewGroup home = null;
    private ViewGroup about = null;
    private ViewGroup event = null;
    private ViewGroup schedele = null;
    private ViewGroup map = null;
    private ViewGroup contact = null;
    private ViewGroup help = null;
    private ViewGroup faq = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }*/

        //copy all web assets in internal storage
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    File root = new File( Environment.getExternalStorageDirectory(), "web" );
                    root.mkdirs();

                    String[] files = getAssets().list("web");//all files, not folder
                    for(String f : files) {
                        try {

                            InputStream is = getAssets().open("web/" + f);
                            OutputStream os = new FileOutputStream(new File(root, f));
                            byte[] buff = new byte[1024];
                            int r = 0;
                            while ( (r=is.read(buff)) != -1 ) {
                                os.write(buff, 0, r);
                            }

                            is.close();
                            os.flush();
                            os.close();

                        } catch (Exception e) {
                            postSmallErros(e);
                            continue;
                        }
                    }


                } catch (Exception e) {
                    postSmallErros(e);
                }
            }
        });


        //startService(new Intent(getApplicationContext(), UpdaterService.class).putExtra("k", "p"));
        // startService(new Intent(getApplicationContext(), UpdaterService.class).putExtra("k", "o"));

        //initialize variables
        drawer_Layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        left_drawer_content = (LinearLayout) findViewById(R.id.left_drawer);
        left_drawer_rotator = (ViewGroup) findViewById(R.id.left_drawer_rotator);
        //left_drawer_content_mask = (ViewGroup)findViewById(R.id.left_drawer_mask);
        left_drawer_content_item_holder = (LinearLayout) findViewById(R.id.left_drawer_item_holder);
        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        LEFT_DRAWER_WIDTH = (int) (90 * p.x / 100);
        //left_drawer_panel = (ViewGroup)findViewById(R.id.left_drawer_panel);
        //left_drawer_content_holder = (ViewGroup)findViewById(R.id.left_drawer_content_holder);
        main_content = (FrameLayout) findViewById(R.id.content_frame);
        root_main_content = (ViewGroup) findViewById(R.id.main_root_content);
        home = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
        about = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
        contact = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
        help = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
        map = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
        schedele = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
        event = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
        faq = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
        event_list_scrollView = (ScrollView) getLayoutInflater().inflate(R.layout.events_list_layout, null);
        contact_layout = (ViewGroup) getLayoutInflater().inflate(R.layout.contact_leyout, null);
        about_layout = (ViewGroup) getLayoutInflater().inflate(R.layout.about_layout, null);
        help_layout = (ViewGroup) getLayoutInflater().inflate(R.layout.help_layout, null);
        faqs_layout = (ViewGroup) getLayoutInflater().inflate(R.layout.faqs_layout, null);
        drawer_open_close_icon = (ImageView)findViewById(R.id.drawer_open_close);

        //changes
        left_drawer_content.getLayoutParams().width = LEFT_DRAWER_WIDTH;
        drawer_Layout.closeDrawer(left_drawer_content);
        left_drawer_content.setBackgroundColor(Color.BLACK);
        ((ImageView) home.getChildAt(1)).setImageResource(R.drawable.ic_home_white_48dp);
        ((TextView) home.getChildAt(2)).setText("Home");
        ((ImageView) about.getChildAt(1)).setImageResource(R.drawable.ic_info_white_48dp);
        ((TextView) about.getChildAt(2)).setText("About Us");
        ((ImageView) help.getChildAt(1)).setImageResource(R.drawable.ic_live_help_white_48dp);
        ((TextView) help.getChildAt(2)).setText("Help");
        ((ImageView) map.getChildAt(1)).setImageResource(R.drawable.ic_place_white_48dp);
        ((TextView) map.getChildAt(2)).setText("Campus Map");
        ((ImageView) event.getChildAt(1)).setImageResource(R.drawable.ic_event_white_48dp);
        ((TextView) event.getChildAt(2)).setText("Events");
        ((ImageView) faq.getChildAt(1)).setImageResource(R.drawable.ic_help_white_48dp);
        ((TextView) faq.getChildAt(2)).setText("FAQs");
        ((ImageView) contact.getChildAt(1)).setImageResource(R.drawable.ic_contacts_white_48dp);
        ((TextView) contact.getChildAt(2)).setText("Contact Us");
        ((ImageView) schedele.getChildAt(1)).setImageResource(R.drawable.ic_schedule_white_48dp);
        ((TextView) schedele.getChildAt(2)).setText("Schedule");
        //final ViewGroup.LayoutParams param1 = left_drawer_content_holder.getLayoutParams();
        ///param1.width = LEFT_DRAWER_WIDTH;
        //left_drawer_content_holder.setLayoutParams(param1);
        //left_drawer_panel.setBackgroundColor(Color.parseColor("#040007"));
        //left_drawer_content_holder.setBackgroundColor(Color.parseColor("#040007"));
        //left_drawer_content_holder.setPivotX(0);
        //left_drawer_content_holder.setPivotY(left_drawer_content_holder.getLayoutParams().width);


        //adidng to left_contents
        left_drawer_content_item_holder.setBackgroundColor(Color.parseColor("#040007"));
        left_drawer_content_item_holder.addView(home);
        left_drawer_content_item_holder.addView(schedele);
        left_drawer_content_item_holder.addView(event);
        left_drawer_content_item_holder.addView(map);
        left_drawer_content_item_holder.addView(faq);
        left_drawer_content_item_holder.addView(contact);
        left_drawer_content_item_holder.addView(help);
        left_drawer_content_item_holder.addView(about);

        drawer_Layout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                root_main_content.setTranslationX(slideOffset*drawerView.getWidth());
                //left_drawer_rotator.setRotationY(90 * (1 - slideOffset));
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawer_open_close_icon.setImageResource(R.drawable.drawer_open_close_icon2);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawer_open_close_icon.setImageResource(R.drawable.drawer_action2);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        //adding events to left drawer navigators
       for (int i = 0; i < left_drawer_content_item_holder.getChildCount(); i++) {
            final ViewGroup elem = (ViewGroup) left_drawer_content_item_holder.getChildAt(i);
            elem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //adding layouts
                    main_content.removeAllViews();
                    if (elem == event) {
                        main_content.addView(event_list_scrollView);
                    } else if (elem == contact) {
                        main_content.addView(contact_layout);
                    } else if (elem == about) {
                        main_content.addView(about_layout);
                    } else if(elem == help) {
                        main_content.addView(help_layout);
                    } else if(elem == faq) {
                        main_content.addView(help_layout);
                        main_content.addView(faqs_layout);
                    }

                    if (CURRENT_LEFT_CLICKED_ITEM != null) {
                        CURRENT_LEFT_CLICKED_ITEM.getChildAt(0).setVisibility(View.INVISIBLE);
                    }
                    CURRENT_LEFT_CLICKED_ITEM = elem;
                    elem.getChildAt(0).setVisibility(View.VISIBLE);
                    drawer_Layout.closeDrawer(left_drawer_content);
                }
            });
    }


        /*root_main_content.setOnTouchListener(new View.OnTouchListener() {
            private float x1 = 0f;
            private float y1 = 0f;
            private final float MIN_WIDTH = 50f;
            @Override
            public boolean onTouch(View v, MotionEvent evt) {



                //postSmallErros(evt.getX() + " " + evt.getAction() + " " + v.getAlpha());

                try {

                    int action = evt.getAction();

                    if(action == MotionEvent.ACTION_DOWN) {
                        x1 = evt.getX();
                        y1 = evt.getY();
                    } else if(action == MotionEvent.ACTION_MOVE) {

                        if(x1 > MIN_WIDTH) {
                            return true;
                        }

                        float x2 = evt.getX();
                        float y2 = evt.getY();

                        float def = x2 - x1;
                        if(def < 0) {
                            return true;
                        }

                        ViewGroup.LayoutParams param2 = left_drawer_content_mask.getLayoutParams();
                        param2.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        left_drawer_content_mask.setLayoutParams(param2);

                        ViewGroup.LayoutParams param3 = left_drawer_panel.getLayoutParams();
                        if(def >= LEFT_DRAWER_WIDTH) {
                            param3.width = LEFT_DRAWER_WIDTH;
                            left_drawer_panel.setLayoutParams(param3);
                        } else {
                            param3.width = (int)def;
                            left_drawer_panel.setLayoutParams(param3);
                        }

                       // left_drawer_content_mask.setAlpha( (float)(0.6 * left_drawer_panel.getLayoutParams().width)/LEFT_DRAWER_WIDTH );

                        int cwP = (100*left_drawer_panel.getLayoutParams().width)/LEFT_DRAWER_WIDTH;
                        float deg = 0f;

                        if(cwP > 90) {
                            deg = 0f;
                        } else if(cwP > 80 && cwP < 90) {
                            deg = 2;
                        } else if(cwP > 70 && cwP < 80) {
                            deg = 4;
                        } else if(cwP > 60 && cwP < 70) {
                            deg = 6;
                        } else if(cwP > 50 && cwP < 60) {
                            deg = 8;
                        } else if(cwP > 40 && cwP < 50) {
                            deg = 10;
                        }

                        left_drawer_content_holder.setRotationY(deg);

                    } else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                        x1 = 0f;
                        y1 = 0f;
                    }

                } catch (Exception e) {
                    postSmallErros(e);
                }
                return true;
            }
        });

        left_drawer_content_mask.setOnTouchListener(new View.OnTouchListener() {
            private float x1 = 0f;
            private float y1 = 0f;
            @Override
            public boolean onTouch(View v, MotionEvent evt) {

                //postSmallErros(evt.getX() + " " + evt.getAction() + " " + v.getAlpha());

                try {

                    int action = evt.getAction();

                    if(action == MotionEvent.ACTION_DOWN) {
                        x1 = evt.getX();
                        y1 = evt.getY();
                    } else if(action == MotionEvent.ACTION_MOVE) {

                        float x2 = evt.getX();
                        float y2 = evt.getY();

                        float def = x2 - x1;

                        ViewGroup.LayoutParams param3 = left_drawer_panel.getLayoutParams();
                        int tot = param3.width + (int)def;
                        if(tot >= LEFT_DRAWER_WIDTH) {
                            param3.width = LEFT_DRAWER_WIDTH;
                            left_drawer_panel.setLayoutParams(param3);
                        } else {
                            param3.width = (int)def + param3.width;
                            left_drawer_panel.setLayoutParams(param3);
                        }
                        root_main_content.setTranslationX(left_drawer_panel.getLayoutParams().width);

                       // left_drawer_content_mask.setAlpha((float) (0.6 * param3.width) / LEFT_DRAWER_WIDTH);

                        int cwP = (100*left_drawer_panel.getLayoutParams().width)/LEFT_DRAWER_WIDTH;
                        float deg = 0f;

                        if(cwP > 90) {
                            deg = 0;
                        } else if(cwP < 30) {
                            deg = 45;
                        } else {
                            deg = 90 - (float)(90 * param3.width)/(60*LEFT_DRAWER_WIDTH/100);
                        }

                        left_drawer_content_holder.setRotationY(deg);
                        //necesary
                        x1 = x2;
                        y1 = y2;

                    }  else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                        x1 = 0f;
                        y1 = 0f;
                    }

                } catch (Exception e) {
                    postSmallErros(e);
                }

                return true;
            }
        });

        left_drawer_content_holder.setOnTouchListener(new View.OnTouchListener() {
            private float x1 = 0f;
            private float y1 = 0f;
            @Override
            public boolean onTouch(View v, MotionEvent evt) {


                postSmallErros("dafaf");
                try {

                    int action = evt.getAction();

                    if(action == MotionEvent.ACTION_DOWN) {
                        x1 = evt.getX();
                        y1 = evt.getY();
                    } else if(action == MotionEvent.ACTION_MOVE) {

                        float x2 = evt.getX();
                        float y2 = evt.getY();

                        float def = x2 - x1;

                        ViewGroup.LayoutParams param3 = left_drawer_panel.getLayoutParams();
                        int tot = param3.width + (int)def;
                        if(tot >= LEFT_DRAWER_WIDTH) {
                            param3.width = LEFT_DRAWER_WIDTH;
                            left_drawer_panel.setLayoutParams(param3);
                        } else {
                            param3.width = (int)def + param3.width;
                            left_drawer_panel.setLayoutParams(param3);
                        }

                        //left_drawer_content_mask.setAlpha((float) (0.6 * param3.width) / LEFT_DRAWER_WIDTH);
                        left_drawer_content_holder.setRotationY(30 - (float)(30 * param3.width)/LEFT_DRAWER_WIDTH );
                        //necesary
                        x1 = x2;
                        y1 = y2;

                    }  else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                        x1 = 0f;
                        y1 = 0f;
                    }

                } catch (Exception e) {
                    postSmallErros(e);
                }

                return true;
            }
        });

        /*final ViewGroup vg1 = (ViewGroup)findViewById(R.id.main_root_content);
        final ViewGroup vg2 = (ViewGroup)findViewById(R.id.left_pnl);
        ViewGroup vg3 = (ViewGroup)findViewById(R.id.root);

        vg3.setOnTouchListener(new View.OnTouchListener() {
            float x1 = 0;
            float y1 = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    x1 = event.getX();
                    y1 = event.getY();
                } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    float x2 = event.getX();
                    float y2 = event.getY();


                    ViewGroup.LayoutParams prm = vg2.getLayoutParams();
                    prm.width = (int)event.getX();
                    vg2.setLayoutParams(prm);

                    vg1.setTranslationX((int)event.getX());

                } else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    x1 = 0;
                    y1 = 0;
                }

                return true;
            }
        });*/

        //add events to aaction bar icons
        drawer_open_close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
                    anim.setDuration(100);
                    anim.setRepeatCount(0);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            if(!drawer_Layout.isDrawerOpen(left_drawer_content)) {
                                drawer_open_close_icon.setImageResource(R.drawable.drawer_open_close_icon2);
                            } else {
                                drawer_open_close_icon.setImageResource(R.drawable.drawer_action2);
                            }

                            AlphaAnimation anim2 = new AlphaAnimation(0.0f, 1.0f);
                            anim2.setDuration(100);
                            anim2.setRepeatCount(0);
                            anim2.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {

                                    if(!drawer_Layout.isDrawerOpen(left_drawer_content)) {
                                        drawer_Layout.openDrawer(left_drawer_content);
                                    } else {
                                        drawer_Layout.closeDrawer(left_drawer_content);
                                    }

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            drawer_open_close_icon.startAnimation(anim2);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    drawer_open_close_icon.startAnimation(anim);



                } catch (Exception e) {
                    postSmallErros(e);
                }
            }
        });

        //list down all events available in events directory, start a thread separatly
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File events_dir = getDir(EVENTS_DIR, 0);
                    File[] event_dir_ids = null;
                    while (true) {
                        try {
                            event_dir_ids = events_dir.listFiles();
                            for (File event : event_dir_ids) {
                                try {
                                    Integer id = Integer.parseInt(event.getName());
                                    if (ALL_SHOWN_EVENT_IDS.contains(id)) {
                                        continue;
                                    }

                                    String date_day = null;
                                    String title = null;
                                    String short_note = null;

                                    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(event, EVENTS_CONFIG_FILE));
                                    date_day = doc.getElementsByTagName(EVENT_DAY_TAG).item(0).getTextContent();
                                    title = doc.getElementsByTagName(EVENT_TITLE_TAG).item(0).getTextContent();
                                    short_note = doc.getElementsByTagName(EVENT_SHORT_NOTE).item(0).getTextContent();

                                    final LinearLayout single_event = (LinearLayout) getLayoutInflater().inflate(R.layout.single_event_layout, null);
                                    ((TextView) single_event.getChildAt(0)).setText(date_day);
                                    ((TextView) (((ViewGroup) single_event.getChildAt(1)).getChildAt(0))).setText(title);
                                    ((TextView) (((ViewGroup) single_event.getChildAt(1)).getChildAt(1))).setText(short_note);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((ViewGroup) event_list_scrollView.getChildAt(0)).addView(single_event, 0);
                                        }
                                    });
                                    ALL_SHOWN_EVENT_IDS.add(id);

                                } catch (Exception e) {
                                    postSmallErros(e.getMessage());
                                }
                            }
                        } catch (Exception e) {
                            postSmallErros(e.getMessage());
                        }

                        Thread.sleep(5000);
                    }

                } catch (Exception e) {
                    postSmallErros(e.getMessage());
                }
            }
        });

    }

    public void onHelpSend(View view) {
        try {




        } catch (Exception e) {
            postSmallErros(e);
        }
    }

    public boolean onTouchEvent(MotionEvent evt) {

        //Toast.makeText(getApplicationContext(), evt.getX() + "", Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
