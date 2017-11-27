package org.sristi.sristi;

import android.animation.Animator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.audiofx.BassBoost;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.app.DialogFragment;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.sristi.sristi.utils.EventUpdatesDownloader;
import org.sristi.sristi.utils.FilesData;
import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.SettingsPreferenceKeys;
import org.sristi.sristi.utils.Utils;
import org.sristi.sristi.utils.VLog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

public class HomeActivity extends Activity {

    private int HOME_NOTIF_PANEL_ANIM_DURATION = 400;
    public int LEFT_DRAWER_WIDTH = 0;
    public int HOME_SLIDE_WIDTH = 0;
    private long HOME_FIRST_BACK_PRESSED_TIMESTAMP = System.currentTimeMillis();
    private long HOME_INTERVAL_TWO_BACK_PRESSED = 2*1000;//10sec

    private LinearLayout left_drawer_content_item_holder = null;
    private ViewGroup left_drawer_bottom_login_register_area = null;
    private DrawerLayout drawer_layout = null;
    private ViewGroup left_drawer_root = null;

    private Thread home_slider_thread = null;
    ViewGroup home_content_panel = null;
    ViewGroup home_root = null;
    ViewGroup home_slide_viwer = null;

    private ViewGroup home_image_slider_root = null;
    private ViewGroup home_slide_1 = null;
    private ViewGroup home_slide_2 = null;
    private ViewGroup home_slide_3 = null;

    private String SHERLOCKED_NOTIF_TITLE = "Sherlocked In App";

    private Activity thisActivity = null;

    private ViewGroup home_notif_panel_root = null;
    private Vector<LinearLayout> home_notif_item_panles = new Vector<LinearLayout>();

    private ViewGroup home = null;
    private ViewGroup about = null;
    private ViewGroup event = null;
    private ViewGroup schedele = null;
    private ViewGroup map = null;
    private ViewGroup contact = null;
    private ViewGroup help = null;
    private ViewGroup faq = null;
    private ViewGroup contributors = null;
    private ViewGroup about_app = null;
    private ViewGroup sherlocked_game = null;

    private ImageView drawer_open_close_view = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            thisActivity = this;

            ((TextView)findViewById(R.id.app_title_tv)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Philosopher-Regular.ttf"));


            //must
            setDefaultSettingsPrefs();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    setup();
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    initialize9Notifications();
                }
            }).start();

           // new Thread(new ).start();

            Point p = new Point();
            getWindowManager().getDefaultDisplay().getSize(p);
            LEFT_DRAWER_WIDTH = (int) (70 * p.x / 100);
            HOME_SLIDE_WIDTH = LEFT_DRAWER_WIDTH/3;

            Utils.setParagraphTextFont((TextView) findViewById(R.id.sign_textv));

            home_notif_panel_root = (ViewGroup)findViewById(R.id.home_notif_panel_root);

            home_slide_viwer = (ViewGroup)findViewById(R.id.home_slide_viwer);
            home_root = (ViewGroup)findViewById(R.id.home_root);
            home_content_panel = (ViewGroup)findViewById(R.id.home_content_panel);
            drawer_open_close_view = (ImageView)findViewById(R.id.drawer_open_close_view);
            left_drawer_bottom_login_register_area = (ViewGroup)findViewById(R.id.left_drawer_bottom_login_register_area);
            drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
            left_drawer_root = (ViewGroup)findViewById(R.id.left_drawer_root);
            ViewGroup.LayoutParams param1 = left_drawer_root.getLayoutParams();
            param1.width = LEFT_DRAWER_WIDTH;
            left_drawer_root.setLayoutParams(param1);

            home_image_slider_root = (ViewGroup)findViewById(R.id.home_image_slider_root);
            home_slide_1 = (ViewGroup)getLayoutInflater().inflate(R.layout.home_slide_1, null);
            //((ImageView)home_slide_1.findViewById(R.id.home_image_slider_imageview)).setImageResource(R.drawable.robo3);//imp
            home_slide_2 = (ViewGroup)getLayoutInflater().inflate(R.layout.home_slide_2, null);
            //((ImageView)home_slide_2.findViewById(R.id.home_image_slider_imageview)).setImageResource(R.drawable.img78);//imp
            home_slide_3 = (ViewGroup)getLayoutInflater().inflate(R.layout.home_slide_3, null);
            //((ImageView)home_slide_3.findViewById(R.id.home_image_slider_imageview)).setImageResource(R.drawable.argumentahauntelle2);//imp

            home = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
            about = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
            contact = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
            help = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
            sherlocked_game = (ViewGroup)getLayoutInflater().inflate(R.layout.left_navigation_item, null);
            map = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
            schedele = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
            event = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
            faq = (ViewGroup) getLayoutInflater().inflate(R.layout.left_navigation_item, null);
            contributors = (ViewGroup)getLayoutInflater().inflate(R.layout.left_navigation_item, null);
            about_app = (ViewGroup)getLayoutInflater().inflate(R.layout.left_navigation_item, null);
            left_drawer_content_item_holder = (LinearLayout) findViewById(R.id.left_drawer_item_holder);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        while (!hasWindowFocus()) {}

                        Thread.sleep(5000);

                        File data_folder = getDir(FilesData.INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE);
                        File version_code_file = new File(data_folder.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_UPDATES + File.separator + FilesData.INTERNAL_FOLDER_APP_UPDATES + File.separator + FilesData.INTERNAL_FILE_APP_UPDATES_APP_UPDATED_VERSION_CODE);

                        int version = Utils.getAppVersionCode();
                        try {
                            version = Integer.parseInt(FilesData.readFile(version_code_file));
                            //postSmallErros(version);
                        } catch (Exception e) {
                            //postSmallErros(e);
                            e.printStackTrace();
                        }
                        if (version <= Utils.getAppVersionCode())
                            return;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(getApplicationContext(), UpdateCenterActivity.class);
                                i.putExtra("ONLINE_CHECK", false);
                                startActivity(i);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            findViewById(R.id.drawer_open_close_panel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drawer_layout.isDrawerOpen(left_drawer_root)) {
                        drawer_layout.closeDrawer(left_drawer_root);
                    } else {
                        drawer_layout.openDrawer(left_drawer_root);
                    }
                }
            });


            final ViewGroup home_conn_error_panel = (ViewGroup)findViewById(R.id.home_conn_error_panel);
            TextView desc_tv = (TextView)findViewById(R.id.desc_tv);
            TextView turn_on_tv = (TextView)findViewById(R.id.turn_on_btn);
            desc_tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf"));
            turn_on_tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf"));
            turn_on_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    if(intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Utils.showModifiedToast(getApplicationContext(), "No Settings Handler is available in your system");
                    }
                }
            });
            if (RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext())) {
                home_conn_error_panel.setVisibility(View.GONE);
            } else {
                home_conn_error_panel.setVisibility(View.VISIBLE);
            }

            BroadcastReceiver netCheckerReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        boolean connected = RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext());
                        if (!connected) {
                            if(home_conn_error_panel.getVisibility() != View.VISIBLE) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        home_conn_error_panel.setVisibility(View.VISIBLE);
                                        home_conn_error_panel.getLayoutParams().height = 0;
                                        Utils.animateHeight(home_conn_error_panel, 0, Utils.dpToPx(60, getApplicationContext()), 500, null, null, true);
                                    }
                                });
                            }
                        } else {
                            if(home_conn_error_panel.getVisibility() != View.GONE) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.animateHeight(home_conn_error_panel, 0, Utils.dpToPx(60, getApplicationContext()), 500, null, new Runnable() {
                                            @Override
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        home_conn_error_panel.setVisibility(View.GONE);
                                                    }
                                                });
                                            }
                                        }, false);
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        postSmallErros(e);
                    }
                }
            };
            registerReceiver(netCheckerReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while(true) {
                            try {
                                boolean connected = RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext());
                                if (!connected) {
                                    if(home_conn_error_panel.getVisibility() != View.VISIBLE) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                home_conn_error_panel.setVisibility(View.VISIBLE);
                                                home_conn_error_panel.getLayoutParams().height = 0;
                                                Utils.animateHeight(home_conn_error_panel, 0, Utils.dpToPx(60, getApplicationContext()), 500, null, null, true);
                                            }
                                        });
                                    }
                                } else {
                                    if(home_conn_error_panel.getVisibility() != View.GONE) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Utils.animateHeight(home_conn_error_panel, 0, Utils.dpToPx(60, getApplicationContext()), 500, null, new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                home_conn_error_panel.setVisibility(View.GONE);
                                                            }
                                                        });
                                                    }
                                                }, false);
                                            }
                                        });
                                    }
                                }
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                postSmallErros(e);
                            }
                        }
                    } catch (Exception e) {
                        postSmallErros(e);
                    }
                }
            }).start();*/


            ((ImageView) home.getChildAt(1)).setImageResource(R.drawable.ic_home_white_48dp);
            ((TextView) home.getChildAt(2)).setText("Home");
            ((ImageView) sherlocked_game.getChildAt(1)).setImageResource(R.drawable.sherlocked_icon);
            ((TextView) sherlocked_game.getChildAt(2)).setText("Sherlocked");
            //((TextView) home.getChildAt(2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Philosopher-Regular.ttf"));
            ((ImageView) about.getChildAt(1)).setImageResource(R.drawable.ic_info_white_48dp);
            ((TextView) about.getChildAt(2)).setText("About Us");
            //((TextView) about.getChildAt(2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Philosopher-Regular.ttf"));
            ((ImageView) help.getChildAt(1)).setImageResource(R.drawable.ic_help_white_48dp);
            ((TextView) help.getChildAt(2)).setText("Help");
            //((TextView) help.getChildAt(2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Philosopher-Regular.ttf"));
            ((ImageView) map.getChildAt(1)).setImageResource(R.drawable.ic_place_white_48dp);
            ((TextView) map.getChildAt(2)).setText("Campus Map");
            //((TextView) map.getChildAt(2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Philosopher-Regular.ttf"));
            ((ImageView) event.getChildAt(1)).setImageResource(R.drawable.ic_event_white_48dp);
            ((TextView) event.getChildAt(2)).setText("Events");
            //((TextView) event.getChildAt(2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Philosopher-Regular.ttf"));
            ((ImageView) faq.getChildAt(1)).setImageResource(R.drawable.ic_live_help_white_48dp);
            ((TextView) faq.getChildAt(2)).setText("FAQs");
            //((TextView) faq.getChildAt(2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Philosopher-Regular.ttf"));
            ((ImageView) contact.getChildAt(1)).setImageResource(R.drawable.ic_perm_contact_calendar_white_24dp);
            ((TextView) contact.getChildAt(2)).setText("Contact Us");
            //((TextView)contact.getChildAt(2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Philosopher-Regular.ttf"));
            ((ImageView) schedele.getChildAt(1)).setImageResource(R.drawable.ic_schedule_white_48dp);
            ((TextView) schedele.getChildAt(2)).setText("Schedule");
            //((TextView) schedele.getChildAt(2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Philosopher-Regular.ttf"));
            ((ImageView) contributors.getChildAt(1)).setImageResource(R.drawable.ic_group_work_white_24dp);
            ((TextView) contributors.getChildAt(2)).setText("Contributors");
            //((TextView) schedele.getChildAt(2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Philosopher-Regular.ttf"));
            ((ImageView) about_app.getChildAt(1)).setImageResource(R.drawable.ic_apps_white_24dp);
            ((TextView) about_app.getChildAt(2)).setText("About App");
            //((TextView) schedele.getChildAt(2)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Philosopher-Regular.ttf"))

            left_drawer_content_item_holder.addView(home);
            left_drawer_content_item_holder.addView(event);
            left_drawer_content_item_holder.addView(schedele);
            left_drawer_content_item_holder.addView(sherlocked_game);
            left_drawer_content_item_holder.addView(map);
            left_drawer_content_item_holder.addView(contributors);
            left_drawer_content_item_holder.addView(contact);
            left_drawer_content_item_holder.addView(help);
            left_drawer_content_item_holder.addView(about);
            left_drawer_content_item_holder.addView(about_app);
            left_drawer_content_item_holder.addView(faq);

            for(int i=0; i<left_drawer_content_item_holder.getChildCount(); i++) {
                final ViewGroup item = (ViewGroup)left_drawer_content_item_holder.getChildAt(i);
                ((TextView)item.findViewById(R.id.textview)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf"));

                Class<? extends Activity> activityClass = null;
                if(item == about) {
                    activityClass = AboutUsActivity.class;
                } else if(item == contact) {
                    activityClass = ContactUsActivity.class;
                } else if(item == help) {
                    activityClass = HelpCentreActivity.class;
                } else if(item == faq) {
                    activityClass = FaqsActivity.class;
                } else if(item == event) {
                    activityClass = EventsActivity.class;
                } else if(item == map) {
                    activityClass = CampusMapActivity.class;
                } else if(item == contributors) {
                    activityClass = ContributorsActivity.class;
                } else if(item == about_app) {
                    activityClass = AboutAppActivity.class;
                } else if(item == schedele) {
                    activityClass = ScheduleActivity.class;
                } else if(item == sherlocked_game) {
                    activityClass = SherlockedGameStarterPage.class;
                } else  {
                    activityClass = HomeActivity.class;
                }
                final Class<? extends Activity> temp1 = activityClass;
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i=0; i<left_drawer_content_item_holder.getChildCount(); i++) {
                            left_drawer_content_item_holder.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        }
                        item.setBackgroundResource(R.drawable.left_drawer_item_pressed_bg);
                        drawer_layout.closeDrawer(left_drawer_root);
                        startActivity(new Intent(getApplicationContext(), temp1));
                    }
                });
            }

            left_drawer_bottom_login_register_area.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String context = ((TextView)left_drawer_bottom_login_register_area.findViewById(R.id.sign_textv)).getText().toString();
                        if(context.toLowerCase().trim().contains("sign in")) {
                            drawer_layout.closeDrawer(left_drawer_root);
                            startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));
                        } else {
                            drawer_layout.closeDrawer(left_drawer_root);
                            startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });




           /* Vector<ViewGroup> home_notif_panel = new Vector<ViewGroup>();
            home_notif_panel.add(home_notif_item_1_panel);
            home_notif_panel.add(home_notif_item_2_panel);
            home_notif_panel.add(home_notif_item_3_panel);
            home_notif_panel.add(home_notif_item_4_panel);
            home_notif_panel.add(home_notif_item_5_panel);
            home_notif_panel.add(home_notif_item_6_panel);


            for (ViewGroup panel : home_notif_panel) {

                View mask = null;
                if(panel == home_notif_item_1_panel) {
                    mask = home_notif_item_1_mask;
                } else if(panel == home_notif_item_2_panel) {
                    mask = home_notif_item_2_mask;
                } else if(panel == home_notif_item_3_panel) {
                    mask = home_notif_item_3_mask;
                } else if(panel == home_notif_item_4_panel) {
                    mask = home_notif_item_4_mask;
                } else if(panel == home_notif_item_5_panel) {
                    mask = home_notif_item_5_mask;
                } else if(panel == home_notif_item_6_panel) {
                    mask = home_notif_item_6_mask;
                }

                final View tempMask = mask;

                panel.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent evt) {
                        int action = evt.getAction();
                        if (action == MotionEvent.ACTION_DOWN) {
                            tempMask.clearAnimation();
                            tempMask.animate().setDuration(HOME_NOTIF_PANEL_ANIM_DURATION).setInterpolator(new DecelerateInterpolator()).alpha(0.7f).start();
                        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                            tempMask.clearAnimation();
                            tempMask.animate().setDuration(HOME_NOTIF_PANEL_ANIM_DURATION).setInterpolator(new DecelerateInterpolator()).alpha(0.0f).start();
                            return false;
                        }

                        return true;
                    }
                });

                panel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postSmallErros("c");
                        tempMask.clearAnimation();
                        tempMask.animate().setDuration(400).setInterpolator(new DecelerateInterpolator()).alpha(1.0f).start();
                    }
                });

            }*/


            /*final Vector<ViewGroup> slide_panels = new Vector<ViewGroup>();
            slide_panels.add(home_slide_1);
            slide_panels.add(home_slide_2);
            slide_panels.add(home_slide_3);
            Utils.setFontsToAllTextViews(home_slide_1);
            Utils.setFontsToAllTextViews(home_slide_2);
            Utils.setFontsToAllTextViews(home_slide_3);
            final String[] speeds = getResources().getStringArray(R.array.home_slide_animation_rate_seconds);*/

            home_slider_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        /*
                        int i = 0;
                        while(true) {
                            final ViewGroup slide_panel = slide_panels.get(i);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        home_image_slider_root.removeAllViews();

                                        slide_panel.findViewById(R.id.home_image_slider_imageview).setAlpha(0.0f);
                                        slide_panel.findViewById(R.id.home_slider_descrption_panel).setAlpha(0.0f);
                                        slide_panel.findViewById(R.id.home_slider_descrption_mask).setAlpha(0.0f);
                                        home_image_slider_root.addView(slide_panel);

                                        slide_panel.findViewById(R.id.home_image_slider_imageview).animate()
                                                .setListener(new Animator.AnimatorListener() {
                                                    @Override
                                                    public void onAnimationStart(Animator animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {

                                                        ViewGroup.LayoutParams param = slide_panel.findViewById(R.id.home_slider_descrption_mask).getLayoutParams();
                                                        param.width = slide_panel.findViewById(R.id.home_slider_descrption_panel).getWidth();
                                                        param.height = slide_panel.findViewById(R.id.home_slider_descrption_panel).getHeight();
                                                        slide_panel.findViewById(R.id.home_slider_descrption_mask).setLayoutParams(param);

                                                        slide_panel.findViewById(R.id.home_slider_descrption_panel).animate()
                                                                .setDuration(1000)
                                                                .setInterpolator(new DecelerateInterpolator())
                                                                .alpha(1.0f)
                                                                .start();
                                                        slide_panel.findViewById(R.id.home_slider_descrption_mask).animate()
                                                                .setDuration(1000)
                                                                .setInterpolator(new DecelerateInterpolator())
                                                                .alpha(0.5f)
                                                                .start();
                                                    }

                                                    @Override
                                                    public void onAnimationCancel(Animator animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animator animation) {

                                                    }
                                                })
                                                .setDuration(1000)
                                                .setInterpolator(new DecelerateInterpolator())
                                                .alpha(1.0f)
                                                .start();


                                    } catch (Exception e) {
                                        postSmallErros(e);
                                    }
                                }
                            });

                            int sleepTimeIndex = getSharedPreferences(SettingsPreferenceKeys.settings_shared_pref_file, MODE_PRIVATE).getInt(SettingsPreferenceKeys.settings_panel_home_slide_animation_speed_pref_key, 4);
                            int speed = 5;
                            try {
                                speed = Integer.parseInt(speeds[sleepTimeIndex].replaceAll("s|S", ""));
                            } catch (Exception e) {
                                speed = 5;
                            }
                            Thread.sleep(10 * 1000);
                            i++;
                            if(i >= slide_panels.size()) {
                                i = 0;
                            }
                        }*/

                    } catch (Exception e) {
                        //Thread.currentThread().stop();
                        postSmallErros(e);
                    }
                }
            });
           // home_slider_thread.start();

            /*home_image_slider_root.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        int action = event.getAction();
                        if(action == MotionEvent.ACTION_DOWN) {
                            home_slide_viwer.removeAllViews();
                            home_slide_viwer.setVisibility(View.VISIBLE);
                            View view_c = home_image_slider_root.getChildAt(0);
                            home_image_slider_root.removeView(view_c);
                            home_slide_viwer.addView(view_c);
                        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                            home_image_slider_root.removeAllViews();
                            View view_c = home_slide_viwer.getChildAt(0);
                            home_slide_viwer.removeView(view_c);
                            home_image_slider_root.addView(view_c);
                            home_slide_viwer.setVisibility(View.INVISIBLE);
                        }

                    } catch (Exception e) {
                        postSmallErros(e);
                    }
                    return true;
                }
            });*/




           /* LinearLayout home_notif_item_panel_1 = (LinearLayout)getLayoutInflater().inflate(R.layout.home_notif_item_panel, null);
            ViewGroup home_notif_item_1_1 = (ViewGroup)getLayoutInflater().inflate(R.layout.home_notif_item, null);
            ((ImageView)home_notif_item_1_1.findViewById(R.id.imageview)).setImageResource(R.drawable.bg12);
            //((TextView)home_notif_item_1_1.findViewById(R.id.textview)).setText("Artist");
            ViewGroup home_notif_item_2_1 = (ViewGroup)getLayoutInflater().inflate(R.layout.home_notif_item, null);
            ((ImageView)home_notif_item_2_1.findViewById(R.id.imageview)).setImageResource(R.drawable.bg11);
            //((TextView)home_notif_item_2_1.findViewById(R.id.textview)).setText("Artist");
            ViewGroup home_notif_item_3_1 = (ViewGroup)getLayoutInflater().inflate(R.layout.home_notif_item, null);
            ((ImageView)home_notif_item_3_1.findViewById(R.id.imageview)).setImageResource(R.drawable.bg13);
            //((TextView)home_notif_item_3_1.findViewById(R.id.textview)).setText("Artist");
            home_notif_item_panel_1.addView(home_notif_item_1_1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            home_notif_item_panel_1.addView(home_notif_item_2_1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            home_notif_item_panel_1.addView(home_notif_item_3_1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

            LinearLayout home_notif_item_panel_2 = (LinearLayout)getLayoutInflater().inflate(R.layout.home_notif_item_panel, null);
            ViewGroup home_notif_item_1_2 = (ViewGroup)getLayoutInflater().inflate(R.layout.home_notif_item, null);
            ((ImageView)home_notif_item_1_2.findViewById(R.id.imageview)).setImageResource(R.drawable.bg9);
            //((TextView)home_notif_item_1_2.findViewById(R.id.textview)).setText("Artist");
            ViewGroup home_notif_item_2_2 = (ViewGroup)getLayoutInflater().inflate(R.layout.home_notif_item, null);
            ((ImageView)home_notif_item_2_2.findViewById(R.id.imageview)).setImageResource(R.drawable.bg44);
            //((TextView)home_notif_item_2_2.findViewById(R.id.textview)).setText("Artist");
            ViewGroup home_notif_item_3_2 = (ViewGroup)getLayoutInflater().inflate(R.layout.home_notif_item, null);
            ((ImageView)home_notif_item_3_2.findViewById(R.id.imageview)).setImageResource(R.drawable.bg66);
            //((TextView)home_notif_item_3_2.findViewById(R.id.textview)).setText("Artist");
            home_notif_item_panel_2.addView(home_notif_item_1_2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            home_notif_item_panel_2.addView(home_notif_item_2_2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            home_notif_item_panel_2.addView(home_notif_item_3_2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

            LinearLayout home_notif_item_panel_3 = (LinearLayout)getLayoutInflater().inflate(R.layout.home_notif_item_panel, null);
            ViewGroup home_notif_item_1_3 = (ViewGroup)getLayoutInflater().inflate(R.layout.home_notif_item, null);
            ((ImageView)home_notif_item_1_3.findViewById(R.id.imageview)).setImageResource(R.drawable.bg12);
            //((TextView)home_notif_item_1_3.findViewById(R.id.textview)).setText("Artist");
            ViewGroup home_notif_item_2_3 = (ViewGroup)getLayoutInflater().inflate(R.layout.home_notif_item, null);
            ((ImageView)home_notif_item_2_3.findViewById(R.id.imageview)).setImageResource(R.drawable.bg11);
            //((TextView)home_notif_item_2_3.findViewById(R.id.textview)).setText("Artist");
            ViewGroup home_notif_item_3_3 = (ViewGroup)getLayoutInflater().inflate(R.layout.home_notif_item, null);
            ((ImageView)home_notif_item_3_3.findViewById(R.id.imageview)).setImageResource(R.drawable.bg13);
            //((TextView)home_notif_item_3_3.findViewById(R.id.textview)).setText("Artist");
            home_notif_item_panel_3.addView(home_notif_item_1_3, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            home_notif_item_panel_3.addView(home_notif_item_2_3, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            home_notif_item_panel_3.addView(home_notif_item_3_3, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

            TextView tv = new TextView(getApplicationContext());
            home_notif_panel_root.addView(home_notif_item_panel_1);
            home_notif_panel_root.addView(home_notif_item_panel_2);
            home_notif_panel_root.addView(home_notif_item_panel_3);

            home_notif_item_panles.add(home_notif_item_panel_1);
            home_notif_item_panles.add(home_notif_item_panel_2);
            home_notif_item_panles.add(home_notif_item_panel_3); */

            //REFRESH ALL NOTIFICATIONS
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while(true) {
                            try {
                                renderUpdatedNotifs();
                                Thread.sleep(30000);
                            } catch (Exception e) {
                                postSmallErros(e);
                            }
                        }
                    } catch (Exception e) {
                        postSmallErros(e);
                    }
                }
            }).start();

            drawer_layout.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    float fraction = slideOffset;
                    float deg = fraction * 180;
                    drawer_open_close_view.setRotationY(deg);
                    if (deg < 90) {
                        drawer_open_close_view.setImageResource(R.drawable.drawer_action2);
                    } else {
                        drawer_open_close_view.setImageResource(R.drawable.drawer_open_close_icon2);
                    }
                    home_root.setTranslationX(slideOffset * HOME_SLIDE_WIDTH);
                    //home_root.setScaleX(1-slideOffset);
                    //home_root.setScaleY(1-slideOffset);
                }

                @Override
                public void onDrawerOpened(View drawerView) {

                }

                @Override
                public void onDrawerClosed(View drawerView) {

                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });

            drawer_open_close_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drawer_layout.isDrawerOpen(left_drawer_root)) {
                        drawer_layout.closeDrawer(left_drawer_root);
                    } else {
                        drawer_layout.openDrawer(left_drawer_root);
                    }
                }
            });

            View setting_icon = findViewById(R.id.setting_icon);
            setting_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                }
            });
            setting_icon.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        Utils.showModifiedToast(getApplicationContext(), Gravity.TOP, 0, 100, "Settings");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });


            View app_share_imageview = findViewById(R.id.app_share_imageview);
            app_share_imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.APP_SHARE_TEXT));
                    intent.setType("text/plain");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        postSmallErros("No app to share this app!");
                    }
                }
            });
            app_share_imageview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Utils.showModifiedToast(getApplicationContext(), Gravity.TOP, -10, 100, "Share App");
                    return false;
                }
            });


            final View home_sync_imageview = (ImageView)findViewById(R.id.home_sync_imageview);
            home_sync_imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_z_infinite);
                        home_sync_imageview.setEnabled(false);
                        home_sync_imageview.clearAnimation();
                        //checking network
                        if (!RemoteServerConstants.isNetworkOrInternetConnected(getApplicationContext())) {
                            postSmallErros("No internet connection available");
                            home_sync_imageview.setEnabled(true);
                            return;
                        }

                        home_sync_imageview.startAnimation(animation);
                        home_sync_imageview.setEnabled(false);
                        //startService(new Intent(getApplicationContext(), EventUpdaterService.class).putExtra("EVENT_CHECK_EXTRA", true));
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EventUpdaterService.checkEventUpdate(getApplicationContext());
                                    renderUpdatedNotifs();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            home_sync_imageview.setEnabled(true);
                                            home_sync_imageview.clearAnimation();
                                        }
                                    });
                                } catch (Exception e) {
                                    postSmallErros(e);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            home_sync_imageview.setEnabled(true);
                                            home_sync_imageview.clearAnimation();
                                        }
                                    });
                                }
                            }
                        }).start();



                        /*new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    FilesData.setup(getApplicationContext());
                                    File notif_folder = new File(getDir(FilesData.INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE).getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_UPDATES + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATIONS);
                                    String data_will_be_sent = "";
                                    for (File dir : notif_folder.listFiles()) {
                                        if(!dir.isDirectory()) {
                                            continue;
                                        }
                                        data_will_be_sent += dir.getName() + "x"; //separator is x
                                    }
                                    data_will_be_sent = "NOTIF_IDS=" + data_will_be_sent;
                                    Log.d("s", data_will_be_sent);
                                    Log.d("s", "net checking");

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            home_sync_imageview.startAnimation(animation);
                                            home_sync_imageview.setEnabled(false);
                                        }
                                    });
                                    String outs_xml = RemoteServerConstants.processingURL(RemoteServerConstants.getFullAddressOfRemoteServerEventUpdatesChecker(), "POST", data_will_be_sent);
                                    if(outs_xml == null || outs_xml.isEmpty()) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                home_sync_imageview.setEnabled(true);
                                                home_sync_imageview.clearAnimation();
                                            }
                                        });
                                        postSmallErros("Failed to refresh updates due to slow internet connection or any other server problem!");
                                        return;
                                    }

                                    Log.d("s", outs_xml);
                                    //remove all extra text sent from server
                                    //<UPDATES>.....</UPDATES>
                                    String regex = Pattern.quote("<UPDATES>") + ".*" + Pattern.quote("</UPDATES>");
                                    Matcher matcher = Pattern.compile(regex).matcher(outs_xml);
                                    if(matcher.find()) {
                                        int start = matcher.start();
                                        int end = matcher.end();
                                        outs_xml = outs_xml.substring(start, end);
                                    }

                                    outs_xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " + outs_xml;

                                    Document docs = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outs_xml)));
                                    NodeList nl = docs.getElementsByTagName("NOTIFICATION");
                                    for(int i=0; i<nl.getLength(); i++) {
                                        Element notif_elem = (Element)nl.item(i);
                                        new Thread(new EventUpdatesDownloader(getApplicationContext(), notif_elem)).start();
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            home_sync_imageview.setEnabled(true);
                                            home_sync_imageview.clearAnimation();
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();*/

                    } catch (Exception e) {
                        postSmallErros(e);
                    }
                }
            });
            home_sync_imageview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Utils.showModifiedToast(getApplicationContext(), Gravity.TOP, -20, 100, "Sync Updates");
                    return false;
                }
            });

            SharedPreferences sp = getSharedPreferences(SettingsPreferenceKeys.user_account_shared_pref_file, 0);
            SettingsPreferenceKeys.setupUserAccountUserPrefs(getApplicationContext());
            String email = sp.getString("EMAIL", "");
            String pass = sp.getString("PASSWORD", "");
            if (!email.trim().isEmpty()) {
                ((TextView)left_drawer_bottom_login_register_area.findViewById(R.id.sign_textv)).setText("My Account");
            } else {
                ((TextView)left_drawer_bottom_login_register_area.findViewById(R.id.sign_textv)).setText("Sign In/Sign Up");
            }

            /**
             * Font settings area
             */
            Utils.setFontsToAllTextViews(home_root);
            ((TextView)findViewById(R.id.app_title_tv)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Philosopher-Regular.ttf"));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.left_sristi_heading));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.turn_on_btn));

            closeOpenDrawer(true); //must


            try {

                Vector<Integer> alt_imgs = new Vector<>();
                alt_imgs.add(R.drawable.alt1);
                alt_imgs.add(R.drawable.alt2);
                alt_imgs.add(R.drawable.alt3);
                alt_imgs.add(R.drawable.alt4);

                Random random = new Random(System.currentTimeMillis());
                int pos = random.nextInt(4);
                if (pos > 3 || pos < 0) {
                    pos = new Random(System.currentTimeMillis()).nextInt(3);
                }

                ImageView imagev = (ImageView)findViewById(R.id.home_top_pic_holder_imageview);
                imagev.setImageResource(alt_imgs.get(pos));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            postSmallErros(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
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

    public void onBackPressed() {
        try {

            if(Math.abs(System.currentTimeMillis() - HOME_FIRST_BACK_PRESSED_TIMESTAMP) <= HOME_INTERVAL_TWO_BACK_PRESSED) {
                finish();
            } else {
                postSmallErros("Press again to exit");
                HOME_FIRST_BACK_PRESSED_TIMESTAMP = System.currentTimeMillis();
            }

        } catch (Exception e) {
            postSmallErros(e);
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

    private void setDefaultSettingsPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences(SettingsPreferenceKeys.settings_shared_pref_file, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!sharedPreferences.contains(SettingsPreferenceKeys.settings_panel_syncing_frequency_pref_key)) {
            editor.putInt(SettingsPreferenceKeys.settings_panel_syncing_frequency_pref_key, 0);
        }
        if(!sharedPreferences.contains(SettingsPreferenceKeys.settings_panel_download_over_wifi_pref_key)) {
            editor.putInt(SettingsPreferenceKeys.settings_panel_download_over_wifi_pref_key, 0);
        }
        if(!sharedPreferences.contains(SettingsPreferenceKeys.settings_panel_home_slide_animation_speed_pref_key)) {
            editor.putInt(SettingsPreferenceKeys.settings_panel_home_slide_animation_speed_pref_key, 4);
        }
        if(!sharedPreferences.contains(SettingsPreferenceKeys.settings_panel_home_slide_animation_on_off_pref_key)) {
            editor.putInt(SettingsPreferenceKeys.settings_panel_home_slide_animation_on_off_pref_key, 1);
        }
        editor.apply();
    }

    private void setup() {
        try {

            File data_folder = getDir(FilesData.INTERNAL_FOLDER_DATA, MODE_PRIVATE);
            if(!data_folder.isDirectory()) {
                data_folder.mkdirs();
            }

            File updates_folder = new File(data_folder, FilesData.INTERNAL_FOLDER_UPDATES);
            if(!updates_folder.isDirectory()) {
                updates_folder.mkdirs();
            }

            File notifications_folder = new File(updates_folder, FilesData.INTERNAL_FOLDER_NOTIFICATIONS);
            if(!notifications_folder.isDirectory()) {
                notifications_folder.mkdirs();
            }
        } catch (Exception e) {
            postSmallErros(e);
        }
    }

    public void initialize9Notifications() {
        try {

            Vector<String> notif_titles = new Vector<String>();
            Vector<String> notif_date = new Vector<String>();
            Vector<String> notif_shortnotes = new Vector<String>();
            Vector<String> notif_image_url = new Vector<String>();
            Vector<String> notif_html_url = new Vector<String>();
            Vector<String> notif_image_asset_path = new Vector<String>();

            notif_titles.add("Workshops");
            notif_titles.add("Tech4Fun");
            notif_titles.add(SHERLOCKED_NOTIF_TITLE);
            notif_titles.add("Seminars");
            notif_titles.add("Lens Flair Contest");
            notif_titles.add("Explore Exhibition");
            notif_titles.add("Sristi Invites All");
            notif_titles.add("Sristi Logo");
            notif_titles.add("Facilities");

            notif_date.add("12-12-03-03-2016");
            notif_date.add("00-00-01-05-2016");
            notif_date.add("00-00-01-05-2016");
            notif_date.add("12-12-03-03-2016");
            notif_date.add("12-12-04-03-2016");
            notif_date.add("12-12-05-03-2016");
            notif_date.add("00-00-01-01-2016");
            notif_date.add("00-00-01-01-2016");
            notif_date.add("00-00-01-01-2016");

            notif_shortnotes.add("Swarm robotics $ two workshops arranged today");
            notif_shortnotes.add("Interactive games $ For very first time we have arranged interactive game");
            notif_shortnotes.add("Enjoy it $ Sherlocked is available in this sristi app, Enjoy it.");
            notif_shortnotes.add("Technological innovations $ We arranged some seminars today");
            notif_shortnotes.add("Photography contest $ Sristi represents the ultimate photography contest");
            notif_shortnotes.add("First this year $ Exhibition which will take place this year");
            notif_shortnotes.add("Largest tech fest $ Welcome to The Annual Largest Techno Sristi");
            notif_shortnotes.add("Significance logo $ The Logo is made from Einstein’s famous Light Cone Rule");
            notif_shortnotes.add("Facilities during sristi $ Facilities During Sristi");

            notif_image_url.add(RemoteServerConstants.getFullAddressOfRemoteServer().toString());
            notif_image_url.add(RemoteServerConstants.getFullAddressOfRemoteServer().toString());
            notif_image_url.add(RemoteServerConstants.getFullAddressOfRemoteServer().toString());
            notif_image_url.add(RemoteServerConstants.getFullAddressOfRemoteServer().toString());
            notif_image_url.add(RemoteServerConstants.getFullAddressOfRemoteServer().toString());
            notif_image_url.add(RemoteServerConstants.getFullAddressOfRemoteServer().toString());
            notif_image_url.add(RemoteServerConstants.getFullAddressOfRemoteServer().toString());
            notif_image_url.add(RemoteServerConstants.getFullAddressOfRemoteServer().toString());
            notif_image_url.add(RemoteServerConstants.getFullAddressOfRemoteServer().toString());

            notif_html_url.add("http://app.sristi.org.in/app/20161/updates/notification_0_9_assets/0.html");
            notif_html_url.add("http://app.sristi.org.in/app/20161/updates/notification_0_9_assets/1.html");
            notif_html_url.add("http://app.sristi.org.in/app/20161/updates/notification_0_9_assets/2.html");
            notif_html_url.add("http://app.sristi.org.in/app/20161/updates/notification_0_9_assets/3.html");
            notif_html_url.add("http://app.sristi.org.in/app/20161/updates/notification_0_9_assets/4.html");
            notif_html_url.add("http://app.sristi.org.in/app/20161/updates/notification_0_9_assets/5.html");
            notif_html_url.add("http://app.sristi.org.in/app/20161/updates/notification_0_9_assets/6.html");
            notif_html_url.add("http://app.sristi.org.in/app/20161/updates/notification_0_9_assets/7.html");
            notif_html_url.add("http://app.sristi.org.in/app/20161/updates/notification_0_9_assets/8.html");


            notif_image_asset_path.add("built_in_notif/notif0.jpg");
            notif_image_asset_path.add("built_in_notif/notif1.jpg");
            notif_image_asset_path.add("built_in_notif/notif2.jpg");
            notif_image_asset_path.add("built_in_notif/notif3.jpg");
            notif_image_asset_path.add("built_in_notif/notif4.jpg");
            notif_image_asset_path.add("built_in_notif/notif5.jpg");
            notif_image_asset_path.add("built_in_notif/notif6.jpg");
            notif_image_asset_path.add("built_in_notif/notif7.jpg");
            notif_image_asset_path.add("built_in_notif/notif8.jpg");


            FilesData.setup(getApplicationContext());
            File notif_folder = new File(getDir(FilesData.INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE).getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_UPDATES + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATIONS);
            //notif_folder = new File(Environment.getExternalStorageDirectory(), "notif");
            notif_folder.mkdirs();

            for(int i=0; i<9; i++) {
                try {

                    File id_file = new File(notif_folder, i + "");
                    if(id_file.isDirectory()) {
                        continue; //important
                    } else {
                        id_file.mkdirs();
                    }

                    FilesData.writeFile(new File(id_file, FilesData.INTERNAL_FILE_NOTIFICATION_TITLE), notif_titles.get(i), false);
                    FilesData.writeFile(new File(id_file, FilesData.INTERNAL_FILE_NOTIFICATION_DATE), notif_date.get(i), false);
                    FilesData.writeFile(new File(id_file, FilesData.INTERNAL_FILE_NOTIFICATION_SHORT_NOTE), notif_shortnotes.get(i), false);
                    new File(id_file.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATION_IMAGE).mkdirs();//must
                    new File(id_file.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATION_HTML).mkdirs();//must
                    FilesData.writeFile(new File(id_file.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATION_IMAGE + File.separator + FilesData.INTERNAL_FILE_NOTIFICATION_IMAGE_URL), notif_image_url.get(i), false);
                    FilesData.writeFile(new File(id_file.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATION_HTML + File.separator + FilesData.INTERNAL_FILE_NOTIFICATION_HTML_URL), notif_html_url.get(i), false);

                    File image_data_file = new File(id_file.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATION_IMAGE + File.separator + FilesData.INTERNAL_FILE_NOTIFICATION_IMAGE);
                    FileOutputStream fos = new FileOutputStream(image_data_file);
                    InputStream is = getAssets().open(notif_image_asset_path.get(i));
                    byte[] buff = new byte[1024*1024];
                    int r;
                    while((r=is.read(buff)) != -1) {
                        fos.write(buff, 0, r);
                    }
                    fos.flush();
                    fos.close();
                    is.close();

                } catch (Exception e) {
                    postSmallErros(e);
                }
            }

        } catch (Exception e) {
            postSmallErros(e);
        }
    }

    public void changeSignInSignUpContextToLogedIn(final String context) {
        try {

            final TextView sign_tv = (TextView)findViewById(R.id.sign_textv);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sign_tv.setText(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                 }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeOpenDrawer(boolean open) {
        try {
            if (open) {
                drawer_layout.openDrawer(left_drawer_root);
            } else {
                drawer_layout.closeDrawer(left_drawer_root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renderUpdatedNotifs() {
        try {

            int column_width = 100;//in dp
            final GridView notif_gridview = new GridView(getApplicationContext());
            notif_gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
            notif_gridview.setColumnWidth(Utils.dpToPx(column_width, getApplicationContext()));
            notif_gridview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            notif_gridview.setNumColumns(3);
            int padding = Utils.dpToPx(3, getApplicationContext());
            notif_gridview.setVerticalSpacing(padding);
            notif_gridview.setHorizontalSpacing(padding);
            notif_gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            notif_gridview.setGravity(Gravity.CENTER);
            notif_gridview.setClipToPadding(true);

            final File notif_folder = new File(getDir(FilesData.INTERNAL_FOLDER_DATA, Context.MODE_PRIVATE).getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_UPDATES + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATIONS);
            //notif_folder = new File(Environment.getExternalStorageDirectory(), "notif");
            final Vector<ViewGroup> home_notif_items = new Vector<ViewGroup>();
            String[] notif_ids = notif_folder.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    if(filename.trim().matches("\\d+")) {
                        return true;
                    }
                    return false;
                }
            });

            long[] notif_ids_num = new long[notif_ids.length];
            for(int i=0; i<notif_ids_num.length; i++) {
                notif_ids_num[i] = Long.parseLong(notif_ids[i]);
            }

            Arrays.sort(notif_ids_num);
            long[] copy = new long[notif_ids_num.length];
            for(int i=0; i<notif_ids_num.length; i++) {
                copy[i] = notif_ids_num[notif_ids_num.length - 1 - i];
            }
            notif_ids_num = copy;

            Random random = null;
            Vector<Integer> alt_imgs = new Vector<>();
            alt_imgs.add(R.drawable.def_pic1);
            alt_imgs.add(R.drawable.def_pic2);
            alt_imgs.add(R.drawable.def_pic3);
            alt_imgs.add(R.drawable.def_pic4);
            alt_imgs.add(R.drawable.def_pic5);
            random = new Random(System.currentTimeMillis());

            for(int i=0; i<notif_ids_num.length; i++) {
                try {

                    File notif_id_file = new File(notif_folder, notif_ids_num[i] + "");
                    String notif_title = FilesData.readFile(new File(notif_id_file, FilesData.INTERNAL_FILE_NOTIFICATION_TITLE));
                    String notif_short_note = FilesData.readFile(new File(notif_id_file, FilesData.INTERNAL_FILE_NOTIFICATION_SHORT_NOTE));
                    String notif_date = FilesData.readFile(new File(notif_id_file, FilesData.INTERNAL_FILE_NOTIFICATION_DATE));
                    String notif_html_url = FilesData.readFile(new File(notif_id_file.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATION_HTML + File.separator + FilesData.INTERNAL_FILE_NOTIFICATION_HTML_URL));
                    String notif_image_url = FilesData.readFile(new File(notif_id_file.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATION_IMAGE + File.separator + FilesData.INTERNAL_FILE_NOTIFICATION_IMAGE_URL));
                    Bitmap image_notif_bitmap = null;
                    try {
                        image_notif_bitmap = BitmapFactory.decodeFile(new File(notif_id_file.getAbsolutePath() + File.separator + FilesData.INTERNAL_FOLDER_NOTIFICATION_IMAGE + File.separator + FilesData.INTERNAL_FILE_NOTIFICATION_IMAGE).getAbsolutePath());
                    } catch (Exception e) {
                        postSmallErros(e);
                    }


                    if(image_notif_bitmap == null) {
                        try {
                            int pos = random.nextInt(alt_imgs.size());
                            if (pos > (alt_imgs.size() - 1) || pos < 0) {
                                pos = random.nextInt((alt_imgs.size() - 1));
                            }
                            image_notif_bitmap = BitmapFactory.decodeResource(getResources(), alt_imgs.get(pos));
                        } catch (Exception e) {
                            image_notif_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_home_notif_pic);
                            e.printStackTrace();
                        }

                    }
                    if(notif_title == null || notif_title.trim().isEmpty()) {
                        notif_title = "Invalid";
                        continue;
                    }
                    if(notif_short_note == null || notif_short_note.trim().isEmpty()) {
                        notif_short_note = "Invalid";
                        continue;
                    }
                    if(notif_date == null ||notif_date.trim().isEmpty()) {
                        notif_date = "00-00-00-00-0000";
                        continue;
                    }
                    if(notif_html_url == null || notif_html_url.trim().isEmpty()) {
                        notif_html_url = RemoteServerConstants.getFullAddressOfRemoteServer().toString();
                        continue;
                    }
                    if(notif_image_url == null || notif_image_url.trim().isEmpty()) {
                        notif_image_url = RemoteServerConstants.getFullAddressOfRemoteServer().toString();
                        continue;
                    }


                    //"00-00-00-00-0000"; ->format MINUTE-HOUR-DAY-MONTH-YEAR
                    String date_day = "00";
                    try {
                        date_day = notif_date.split(Pattern.quote("-"))[2];
                    } catch (Exception e) {
                        postSmallErros(e);
                    }

                    String short_note1 = "not loaded";
                    String short_note2 = "Not Loaded";
                    try {

                        String[] parts = notif_short_note.split(Pattern.quote("$"));
                        short_note1 = parts[0];
                        short_note2 = parts[1];

                    } catch (Exception e) {
                        short_note2 = short_note1;
                        e.printStackTrace();
                    }

                     ViewGroup home_notif_item = (ViewGroup)getLayoutInflater().inflate(R.layout.home_notif_item, null);
                     View mask = home_notif_item.findViewById(R.id.mask);
                    ((TextView)home_notif_item.findViewById(R.id.textview_title)).setText(notif_title);
                    ((TextView)home_notif_item.findViewById(R.id.textview_short_text)).setText(short_note1);
                    ((TextView)home_notif_item.findViewById(R.id.notif_date_textview)).setText(date_day);
                    ImageView imageView = (ImageView)home_notif_item.findViewById(R.id.imageview);
                    imageView.setImageBitmap(image_notif_bitmap);


                    final Context that = getApplicationContext();
                    final FragmentManager fm = getFragmentManager();
                    final String temp_title = notif_title;
                    final String temp_html_url = notif_html_url;
                    final String temp_shortnote1 = short_note1;
                    final String temp_shortnote2 = short_note2;
                    final Bitmap temp_bitmap = image_notif_bitmap;
                    final String temp_date = notif_date;

                    mask.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {

                                if (temp_title.trim().toUpperCase().equals(SHERLOCKED_NOTIF_TITLE.trim().toUpperCase())) {
                                    startActivity(new Intent(getApplicationContext(), SherlockedGameStarterPage.class));
                                    return;
                                }

                                Intent intent = new Intent(getApplicationContext(), WebBrowserActivity.class);
                                intent.putExtra("URL", temp_html_url);
                                intent.putExtra("LOADING_TEXT", "Loading Event, Please Wait...");
                                intent.putExtra("REFRESH_URL", temp_html_url);
                                startActivity(intent);

                                /*
                                final AlertDialog ad = new AlertDialog.Builder(HomeActivity.this).create();
                                ViewGroup root = (ViewGroup) getLayoutInflater().inflate(R.layout.notif_dialog_layout, null);
                                ((ImageView) root.findViewById(R.id.pic_imageview)).setImageBitmap(temp_bitmap);
                                ((TextView) root.findViewById(R.id.short_note)).setText(temp_shortnote2);
                                ((TextView) root.findViewById(R.id.short_note2)).setText(temp_date);
                                ((TextView) root.findViewById(R.id.title)).setText(temp_title);
                                root.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            ad.cancel();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                root.findViewById(R.id.exploe_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {

                                            Intent intent = new Intent(getApplicationContext(), WebBrowserActivity.class);
                                            intent.putExtra("URL", temp_html_url);
                                            intent.putExtra("LOADING_TEXT", "Loading Event, Please Wait...");
                                            intent.putExtra("REFRESH_URL", temp_html_url);
                                            startActivity(intent);
                                            ad.cancel();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                Utils.setFontsToAllTextViews(root);
                                Utils.setHeadingTextFont(((TextView) root.findViewById(R.id.title)));
                                Utils.setHeadingTextFont(((TextView) root.findViewById(R.id.cancel_btn)));
                                Utils.setHeadingTextFont(((TextView) root.findViewById(R.id.exploe_btn)));

                                ad.setView(root);
                                ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                ad.show();
                                //Utils.showModifiedToast(getApplicationContext(), Utils.getScreenDimentions(thisActivity).x + " " + Utils.getScreenDimentions(thisActivity).y);
                                ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                ad.getWindow().setLayout((int) (Utils.getScreenDimentions(thisActivity).x * 0.9), (int) (Utils.getScreenDimentions(thisActivity).y * 0.7));
                                */
                            } catch (Exception e) {
                                postSmallErros(Arrays.toString(e.getStackTrace()));
                            }
                        }
                    });
                    Utils.setOthersOneTextFont(((TextView) home_notif_item.findViewById(R.id.textview_title)));
                    home_notif_items.add(home_notif_item);

                } catch (Exception e) {
                    postSmallErros(e);
                }
            }


            final BaseAdapter baseAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return home_notif_items.size();
                }

                @Override
                public Object getItem(int position) {
                    return home_notif_items.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    ViewGroup viewGroup = null;
                    viewGroup = home_notif_items.get(position);
                    viewGroup.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, Utils.getScreenDimentions(thisActivity).x/3));
                    return viewGroup;
                }
            };

            notif_gridview.setAdapter(baseAdapter);
            notif_gridview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Utils.showModifiedToast(getApplicationContext(), position);
                    } catch (Exception  e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        home_notif_panel_root.removeAllViews();
                        home_notif_panel_root.addView(notif_gridview);
                    } catch (Exception e) {
                        postSmallErros(e);
                    }
                }
            });

            /*
            int total_notif_panel = home_notif_items.size()/3;
            if(home_notif_items.size()%3 == 1) {
                home_notif_items.add(0, new LinearLayout(getApplicationContext()));
                home_notif_items.add(0, new LinearLayout(getApplicationContext()));
                total_notif_panel++;
            } else if(home_notif_items.size()%3 == 2) {
                home_notif_items.add(0, new LinearLayout(getApplicationContext()));
                total_notif_panel++;
            } else {
                //nothing
            }

            final Vector<LinearLayout> notif_panels = new Vector<LinearLayout>();
            for(int i=0; i<total_notif_panel; i++) {
                LinearLayout notif_item_panel = (LinearLayout)getLayoutInflater().inflate(R.layout.home_notif_item_panel, null);
                notif_panels.add(notif_item_panel);
            }

            int j = home_notif_items.size()- 1;
            for(LinearLayout panel : notif_panels) {
                if(j<0) continue;
                panel.addView(home_notif_items.get(j), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                j--;
                if(j<0) continue;
                panel.addView(home_notif_items.get(j), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                j--;
                if(j<0) continue;
                panel.addView(home_notif_items.get(j), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                j--;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        home_notif_panel_root.removeAllViews();
                        for(LinearLayout panel : notif_panels) {
                            home_notif_panel_root.addView(panel);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }); */

        } catch (Exception e) {
            postSmallErros(e);
        }
    }

    public void onNewIntent(Intent intent) {
        try {

            super.onNewIntent(intent);
            if(intent == null) {
                return;
            }

            boolean signed_New = intent.getBooleanExtra("NEW_SIGNED_IN", false);
            if(signed_New) {
                changeSignInSignUpContextToLogedIn("My Account");
                closeOpenDrawer(true);
            }

            boolean signd_up_New = intent.getBooleanExtra("NEW_SIGNED_UP", false);
            if(signd_up_New) {
                changeSignInSignUpContextToLogedIn("Sign In/Sign Up");
                closeOpenDrawer(true);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}















