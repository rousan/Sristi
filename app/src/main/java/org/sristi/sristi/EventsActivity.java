package org.sristi.sristi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import org.sristi.sristi.utils.FilesData;
import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.Utils;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_events);

            final Activity this_Activity = this;

            findViewById(R.id.back_imageeview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            Vector<ViewGroup> events_btn_panel = new Vector<>();
            events_btn_panel.add((ViewGroup)findViewById(R.id.eagle_claw_event_btn_panel));
            events_btn_panel.add((ViewGroup) findViewById(R.id.robo_soccer_event_btn_panel));
            events_btn_panel.add((ViewGroup) findViewById(R.id.mission_entente_btn_panel));
            events_btn_panel.add((ViewGroup) findViewById(R.id.firebird_ii_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.afs_1_0_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.devils_advocate_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.incognito_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.picture_perception_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.fifa_11_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.cs_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.nfs_mw_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.call_of_duty_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.its_binary_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.tech_quiz_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.technically_yours_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.what_the_hack_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.lens_flair_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.sudoku_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.chess_event_btn_panel));
            events_btn_panel.add((ViewGroup)findViewById(R.id.bridge_the_gap_event_btn_panel));

            final Vector<String> top_titles = new Vector<>();
            top_titles.add("Eagle Claw");
            top_titles.add("Robo Soccer");
            top_titles.add("Mission Entente");
            top_titles.add("FireBird II");
            top_titles.add("AFS 1.0");
            top_titles.add("Devil's Advocate");
            top_titles.add("Incognito");
            top_titles.add("Picture Perception");
            top_titles.add("FIFA 11");
            top_titles.add("CS 1.6");
            top_titles.add("NFS MW");
            top_titles.add("Call of Duty 1");
            top_titles.add("It's Binary");
            top_titles.add("Tech Quiz");
            top_titles.add("Technically Yours");
            top_titles.add("What the Hack");
            top_titles.add("Lens Flair");
            top_titles.add("Sudoku");
            top_titles.add("Chess");
            top_titles.add("Bridge The Gap");

            final Vector<String> top_short_notes = new Vector<>();
            top_short_notes.add("Event under Robotix");
            top_short_notes.add("Event under Robotix");
            top_short_notes.add("Event under Robotix");
            top_short_notes.add("Event under Robotix");
            top_short_notes.add("Event under Management");
            top_short_notes.add("Event under Management");
            top_short_notes.add("Event under Management");
            top_short_notes.add("Event under Management");
            top_short_notes.add("Event under Game Zone");
            top_short_notes.add("Event under Game Zone");
            top_short_notes.add("Event under Game Zone");
            top_short_notes.add("Event under Game Zone");
            top_short_notes.add("Event under Technozion");
            top_short_notes.add("Event under Technozion");
            top_short_notes.add("Event under Technozion");
            top_short_notes.add("Event under Technozion");
            top_short_notes.add("Event under J-Buzz");
            top_short_notes.add("Event under J-Buzz");
            top_short_notes.add("Event under J-Buzz");
            top_short_notes.add("Event under J-Buzz");


            final Vector<String[]> contacts = new Vector<>();
            contacts.add(new String[] {"Sujay Bharadwaj @ +91 9836073638", "Somtirtha Mukherjee @ +91 9874226008", "Shuvam Mandal @ +91 8670314782"});
            contacts.add(new String[] {"Nitesh Kumar Sahu @ +91 9831749877", "Arpita Kundu @ +91 8961137512", "Partha Sarathi Ghosh @ +91 9832799698"});
            contacts.add(new String[] {"Ashish Kumar Dasara @ +91 9563030886", "Biswanath Bhaduri @ +91 9733547050", "Akash Chowdhuri @ +91 9433625058"});
            contacts.add(new String[] {"Anupam Sharma @ +91 9051170355)", "Aroondhuti Paul @ +91 9126147393", "Prabhat Kumar @ +91 8420638529"});
            contacts.add(new String[] {"SHASHANK CHAKRABORTY @ +91 8116365901"});
            contacts.add(new String[] {"ROHIT SRIVASTAVA @ NULL"});
            contacts.add(new String[] {"ROHIT SRIVASTAVA @ NULL"});
            contacts.add(new String[] {});
            contacts.add(new String[] {"Avik Chakraborty @ NULL"});
            contacts.add(new String[] {"Chandan Paul @ NULL"});
            contacts.add(new String[] {"Sudipta Bhattacharjee @ NULL"});
            contacts.add(new String[] {"Sourav Nandi @ NULL"});
            contacts.add(new String[] {"Amar Kumar Barnwal @ +91 9635795916", "Kumar Arindam Singh @ +91 8116954281", "Rousan Ali @ +91 8116840517"});
            contacts.add(new String[] {"Kanchan Gurung @ +91 8759257096", "Mitesh Barnwal @ +91 9832113240"});
            contacts.add(new String[] {"Sikharlal Pradhan @ NULL", "Priyajit Ghosh @ +91 7872774697"});
            contacts.add(new String[] {"Aman Singh @ +91 9831834213", "Subham Biswas @ +91 7797158773"});
            contacts.add(new String[] {"Suman Pan @ +91 9007345524"});
            contacts.add(new String[] {"Madhumita Bose @ +91 07278152867"});
            contacts.add(new String[] {"Rounak Bhattacharya @ +91 09903782562", "Jaideep Pal @ +91 9046344278"});
            contacts.add(new String[] {"Debiprasad Bhakta @ +91 9091521599", "Subhankar Pal @ +91 9735773310"});

            final Vector<String> urls_wv = new Vector<>();
            urls_wv.add("file:///android_asset/web/eagle_claw_event.html");
            urls_wv.add("file:///android_asset/web/robo_soccer_event.html");
            urls_wv.add("file:///android_asset/web/mission_entente_event.html");
            urls_wv.add("file:///android_asset/web/firebird_ii_event.html");
            urls_wv.add("file:///android_asset/web/afs_1_0_event.html");
            urls_wv.add("file:///android_asset/web/devils_advocate_event.html");
            urls_wv.add("file:///android_asset/web/incognito_event.html");
            urls_wv.add("file:///android_asset/web/picture_perception_event.html");
            urls_wv.add("file:///android_asset/web/fifa_11_event.html");
            urls_wv.add("file:///android_asset/web/cs_event.html");
            urls_wv.add("file:///android_asset/web/nfs_mw_event.html");
            urls_wv.add("file:///android_asset/web/call_of_duty_event.html");
            urls_wv.add("file:///android_asset/web/its_binary_event.html");
            urls_wv.add("file:///android_asset/web/tech_quiz_event.html");
            urls_wv.add("file:///android_asset/web/technically_yours_event.html");
            urls_wv.add("file:///android_asset/web/what_the_hack_event.html");
            urls_wv.add("file:///android_asset/web/lens_flair_event.html");
            urls_wv.add("file:///android_asset/web/sudoku_event.html");
            urls_wv.add("file:///android_asset/web/chess_event.html");
            urls_wv.add("file:///android_asset/web/bridge_the_gap_event.html");

            final Vector<Integer> top_pic_res_ids = new Vector<>();
            top_pic_res_ids.add(R.drawable.robotix_event_pic);
            top_pic_res_ids.add(R.drawable.robotix_event_pic);
            top_pic_res_ids.add(R.drawable.robotix_event_pic);
            top_pic_res_ids.add(R.drawable.robotix_event_pic);
            top_pic_res_ids.add(R.drawable.management_event_pic);
            top_pic_res_ids.add(R.drawable.management_event_pic);
            top_pic_res_ids.add(R.drawable.management_event_pic);
            top_pic_res_ids.add(R.drawable.management_event_pic);
            top_pic_res_ids.add(R.drawable.game_zone_event_pic);
            top_pic_res_ids.add(R.drawable.game_zone_event_pic);
            top_pic_res_ids.add(R.drawable.game_zone_event_pic);
            top_pic_res_ids.add(R.drawable.game_zone_event_pic);
            top_pic_res_ids.add(R.drawable.technozion_event_pic);
            top_pic_res_ids.add(R.drawable.technozion_event_pic);
            top_pic_res_ids.add(R.drawable.technozion_event_pic);
            top_pic_res_ids.add(R.drawable.technozion_event_pic);
            top_pic_res_ids.add(R.drawable.j_buzz_event_pic);
            top_pic_res_ids.add(R.drawable.j_buzz_event_pic);
            top_pic_res_ids.add(R.drawable.j_buzz_event_pic);
            top_pic_res_ids.add(R.drawable.j_buzz_event_pic);




            for(int i=0; i<events_btn_panel.size(); i++) {
                try {
                    ViewGroup panel = events_btn_panel.get(i);
                    final String top_title = top_titles.get(i);
                    final String top_short_note = top_short_notes.get(i);
                    final String[] contact = contacts.get(i);
                    final String wv_url = urls_wv.get(i);
                    final int top_pic_res_id = top_pic_res_ids.get(i);
                    panel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {

                                Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                i.putExtra("TOP_TITLE", top_title);
                                i.putExtra("TOP_SHORT_NOTE", top_short_note);
                                i.putExtra("TOP_CONTACTS", contact);
                                i.putExtra("TOP_PIC_RES_ID", top_pic_res_id);
                                i.putExtra("DATA_WV_URL", wv_url);
                                startActivity(i);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            findViewById(R.id.robotix_panel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        final AlertDialog ad = new AlertDialog.Builder(EventsActivity.this).create();

                        ViewGroup root = (ViewGroup)getLayoutInflater().inflate(R.layout.robotix_sub_events_dialog_layout, null);
                        Utils.setFontsToAllTextViews(root);
                        Utils.setHeadingTextFont((TextView) root.findViewById(R.id.top_title_dialog));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.dialog_okay_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.eagle_claw_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.robo_soccer_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.mission_entente_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.firebird_ii_btn));

                        root.findViewById(R.id.eagle_claw_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(0));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(0));
                                    i.putExtra("TOP_CONTACTS", contacts.get(0));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(0));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(0));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.robo_soccer_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(1));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(1));
                                    i.putExtra("TOP_CONTACTS", contacts.get(1));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(1));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(1));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.mission_entente_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(2));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(2));
                                    i.putExtra("TOP_CONTACTS", contacts.get(2));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(2));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(2));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                        root.findViewById(R.id.firebird_ii_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(3));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(3));
                                    i.putExtra("TOP_CONTACTS", contacts.get(3));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(3));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(3));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.dialog_okay_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    ad.cancel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        ad.setView(root);
                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ad.show();
                        //Utils.showModifiedToast(getApplicationContext(), Utils.getScreenDimentions(thisActivity).x + " " + Utils.getScreenDimentions(thisActivity).y);
                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ad.getWindow().setLayout((int) (Utils.getScreenDimentions(this_Activity).x * 0.9), (int) (Utils.getScreenDimentions(this_Activity).y * 0.6));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            findViewById(R.id.management_event_panel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        final AlertDialog ad = new AlertDialog.Builder(EventsActivity.this).create();

                        ViewGroup root = (ViewGroup)getLayoutInflater().inflate(R.layout.management_sub_event_dialog_layout, null);
                        Utils.setFontsToAllTextViews(root);
                        Utils.setHeadingTextFont((TextView) root.findViewById(R.id.top_title_dialog));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.dialog_okay_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.afs_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.devil_advocate_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.incognito_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.piccture_perception_btn));

                        root.findViewById(R.id.afs_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(4));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(4));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(4));
                                    i.putExtra("TOP_CONTACTS", contacts.get(4));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(4));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.devil_advocate_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(5));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(5));
                                    i.putExtra("TOP_CONTACTS", contacts.get(5));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(5));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(5));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.incognito_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(6));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(6));
                                    i.putExtra("TOP_CONTACTS", contacts.get(6));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(6));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(6));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                        root.findViewById(R.id.piccture_perception_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(7));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(7));
                                    i.putExtra("TOP_CONTACTS", contacts.get(7));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(7));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(7));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.dialog_okay_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    ad.cancel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        ad.setView(root);
                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ad.show();
                        //Utils.showModifiedToast(getApplicationContext(), Utils.getScreenDimentions(thisActivity).x + " " + Utils.getScreenDimentions(thisActivity).y);
                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ad.getWindow().setLayout((int) (Utils.getScreenDimentions(this_Activity).x * 0.9), (int) (Utils.getScreenDimentions(this_Activity).y * 0.6));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.gamezone_event_panel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        final AlertDialog ad = new AlertDialog.Builder(EventsActivity.this).create();

                        ViewGroup root = (ViewGroup)getLayoutInflater().inflate(R.layout.game_zone_sub_event_dialog_layout, null);
                        Utils.setFontsToAllTextViews(root);
                        Utils.setHeadingTextFont((TextView) root.findViewById(R.id.top_title_dialog));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.dialog_okay_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.fifa_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.cs_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.nfs_mw_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.call_of_duty_btn));

                        root.findViewById(R.id.fifa_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(8));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(8));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(8));
                                    i.putExtra("TOP_CONTACTS", contacts.get(8));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(8));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.cs_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(9));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(9));
                                    i.putExtra("TOP_CONTACTS", contacts.get(9));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(9));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(9));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.nfs_mw_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(10));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(10));
                                    i.putExtra("TOP_CONTACTS", contacts.get(10));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(10));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(10));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                        root.findViewById(R.id.call_of_duty_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(11));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(11));
                                    i.putExtra("TOP_CONTACTS", contacts.get(11));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(11));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(11));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.dialog_okay_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    ad.cancel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        ad.setView(root);
                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ad.show();
                        //Utils.showModifiedToast(getApplicationContext(), Utils.getScreenDimentions(thisActivity).x + " " + Utils.getScreenDimentions(thisActivity).y);
                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ad.getWindow().setLayout((int) (Utils.getScreenDimentions(this_Activity).x * 0.9), (int) (Utils.getScreenDimentions(this_Activity).y * 0.6));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            findViewById(R.id.technozion_event_panel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        final AlertDialog ad = new AlertDialog.Builder(EventsActivity.this).create();

                        ViewGroup root = (ViewGroup)getLayoutInflater().inflate(R.layout.technozion_sub_event_dialog_layout, null);
                        Utils.setFontsToAllTextViews(root);
                        Utils.setHeadingTextFont((TextView) root.findViewById(R.id.top_title_dialog));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.dialog_okay_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.its_binary_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.tech_quiz_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.technically_yours_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.what_the_hack_btn));

                        root.findViewById(R.id.its_binary_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(12));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(12));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(12));
                                    i.putExtra("TOP_CONTACTS", contacts.get(12));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(12));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.tech_quiz_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(13));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(13));
                                    i.putExtra("TOP_CONTACTS", contacts.get(13));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(13));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(13));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.technically_yours_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(14));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(14));
                                    i.putExtra("TOP_CONTACTS", contacts.get(14));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(14));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(14));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                        root.findViewById(R.id.what_the_hack_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(15));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(15));
                                    i.putExtra("TOP_CONTACTS", contacts.get(15));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(15));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(15));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.dialog_okay_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    ad.cancel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        ad.setView(root);
                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ad.show();
                        //Utils.showModifiedToast(getApplicationContext(), Utils.getScreenDimentions(thisActivity).x + " " + Utils.getScreenDimentions(thisActivity).y);
                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ad.getWindow().setLayout((int) (Utils.getScreenDimentions(this_Activity).x * 0.9), (int) (Utils.getScreenDimentions(this_Activity).y * 0.6));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.sherlocked_event_panel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(getApplicationContext(), SherlockedGameStarterPage.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            findViewById(R.id.j_buzz_event_panel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        final AlertDialog ad = new AlertDialog.Builder(EventsActivity.this).create();

                        ViewGroup root = (ViewGroup)getLayoutInflater().inflate(R.layout.j_buzz_sub_event_dialog_panel, null);
                        Utils.setFontsToAllTextViews(root);
                        Utils.setHeadingTextFont((TextView) root.findViewById(R.id.top_title_dialog));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.dialog_okay_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.lens_flair_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.sudoku_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.chess_btn));
                        Utils.setHeadingTextFont((TextView)root.findViewById(R.id.bridge_the_gap_btn));

                        root.findViewById(R.id.lens_flair_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(16));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(16));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(16));
                                    i.putExtra("TOP_CONTACTS", contacts.get(16));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(16));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.sudoku_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(17));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(17));
                                    i.putExtra("TOP_CONTACTS", contacts.get(17));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(17));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(17));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.chess_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(18));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(18));
                                    i.putExtra("TOP_CONTACTS", contacts.get(18));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(18));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(18));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                        root.findViewById(R.id.bridge_the_gap_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), EventViewerActivity.class);
                                    i.putExtra("TOP_TITLE", top_titles.get(19));
                                    i.putExtra("TOP_SHORT_NOTE", top_short_notes.get(19));
                                    i.putExtra("TOP_CONTACTS", contacts.get(19));
                                    i.putExtra("TOP_PIC_RES_ID", top_pic_res_ids.get(19));
                                    i.putExtra("DATA_WV_URL", urls_wv.get(19));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.dialog_okay_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    ad.cancel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        ad.setView(root);
                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ad.show();
                        //Utils.showModifiedToast(getApplicationContext(), Utils.getScreenDimentions(thisActivity).x + " " + Utils.getScreenDimentions(thisActivity).y);
                        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ad.getWindow().setLayout((int) (Utils.getScreenDimentions(this_Activity).x * 0.9), (int) (Utils.getScreenDimentions(this_Activity).y * 0.6));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.play_sherlocked_btn_panel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(getApplicationContext(), SherlockedGameStarterPage.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            Utils.setFontsToAllTextViews((ViewGroup) findViewById(R.id.root));
            Utils.setHeadingTextFont((TextView) findViewById(R.id.robotix_event_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.management_event_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.game_zone_event_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.tj_buzz_event_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.technozion_event_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.sherlocked_event_title));
            Utils.setHeadingTextFont((TextView)findViewById(R.id.sristi_title));

        } catch (Exception e) {
            postSmallErros(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 89) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }


}
