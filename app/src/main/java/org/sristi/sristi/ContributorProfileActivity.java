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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.Utils;

import java.util.Arrays;
import java.util.Vector;

public class ContributorProfileActivity extends Activity {

    private int current_profile_pos = 0;
    private final long fade_time = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_contributor_profile);

            final ViewGroup prifile_viewer = (ViewGroup)findViewById(R.id.profile_viewer_panel);
            final ViewGroup left_arrow_panel = (ViewGroup)findViewById(R.id.left_arrow_panel);
            final ViewGroup right_arrow_panel = (ViewGroup)findViewById(R.id.right_arrow_panel);

            final String[] names = getResources().getStringArray(R.array.contributor_names);
            final String[] emails = getResources().getStringArray(R.array.contributor_emails);
            final String[] mobiles = getResources().getStringArray(R.array.contributor_mobiles);
            final String[] profile_links = getResources().getStringArray(R.array.contributor_profile_links);
            final String[] contribution_purpose = getResources().getStringArray(R.array.contributor_contribution_purpose);

            //profile pic id
            final int[] profile_pic_resource_id = new int[9];
            profile_pic_resource_id[0] = R.drawable.me;
            profile_pic_resource_id[1] = R.drawable.anurag_profile;
            profile_pic_resource_id[2] = R.drawable.debtanu_profile;
            profile_pic_resource_id[3] = R.drawable.thoripa_profile;
            profile_pic_resource_id[4] = R.drawable.asish_profile;
            profile_pic_resource_id[5] = R.drawable.nishit_profile;
            profile_pic_resource_id[6] = R.drawable.sayon_profile;
            profile_pic_resource_id[7] = R.drawable.siddharnt_profile_pic;
            profile_pic_resource_id[8] = R.drawable.soumydeeo_profile_pic;

            int[] lns = new int[] {names.length, emails.length, mobiles.length, profile_links.length, contribution_purpose.length, profile_pic_resource_id.length};
            Arrays.sort(lns);
            int ln = lns[0];

            final Vector<ViewGroup> profiles = new Vector<>();
            for(int i=0; i<ln; i++) {
                ViewGroup profile = (ViewGroup)getLayoutInflater().inflate(R.layout.contributor_single_profile, null);
                ((TextView)profile.findViewById(R.id.top_name_title)).setText(names[i]);
                ((TextView)profile.findViewById(R.id.top_short_note)).setText(contribution_purpose[i]);
                ((TextView)profile.findViewById(R.id.email_short_note)).setText(emails[i]);
                ((TextView)profile.findViewById(R.id.profile_link_short_note)).setText(profile_links[i]);
                ((TextView)profile.findViewById(R.id.mobile_short_note)).setText(mobiles[i]);
                ((ImageView)profile.findViewById(R.id.profile_pic_imageview)).setImageResource(profile_pic_resource_id[i]);

                final int temp1 = i;
                profile.findViewById(R.id.email_explore_btn_panel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse("mailto:"));
                            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emails[temp1]});
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Sristi");
                            intent.putExtra(Intent.EXTRA_TEXT, "Hi, " + names[temp1]);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            } else {
                                Utils.showModifiedToast(getApplicationContext(), "No email app is available");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                profile.findViewById(R.id.call_explore_btn_panel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + mobiles[temp1]));
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

                profile.findViewById(R.id.browser_open_btn_panel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            Uri uri = Uri.parse(profile_links[temp1]);
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

                profile.findViewById(R.id.googleplus_btn).setOnClickListener(new View.OnClickListener() {
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

                profile.findViewById(R.id.twitter_btn).setOnClickListener(new View.OnClickListener() {
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

                profile.findViewById(R.id.fb_btn).setOnClickListener(new View.OnClickListener() {
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



                Utils.setFontsToAllTextViews(profile);
                Utils.setHeadingTextFont(((TextView) profile.findViewById(R.id.top_name_title)));
                Utils.setHeadingTextFont(((TextView) profile.findViewById(R.id.email_title)));
                Utils.setHeadingTextFont(((TextView) profile.findViewById(R.id.profile_link_title)));
                Utils.setHeadingTextFont(((TextView) profile.findViewById(R.id.mobile_title)));
                Utils.setHeadingTextFont(((TextView) profile.findViewById(R.id.follow_me_on_title)));

                profiles.add(profile);
            }


            left_arrow_panel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        int pos = current_profile_pos - 1;
                        if(pos < 0) {
                            pos = profiles.size() - 1;
                        }

                        final int temp1 = pos;
                        final AlphaAnimation aa = new AlphaAnimation(1, 0);
                        aa.setDuration(fade_time);
                        final ViewGroup current_profile = (ViewGroup)prifile_viewer.getChildAt(0);
                        aa.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                left_arrow_panel.setEnabled(false);
                                right_arrow_panel.setEnabled(false);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                try {

                                    prifile_viewer.removeAllViews();
                                    current_profile.setAlpha(1);

                                    ViewGroup toShowProfile = profiles.get(temp1);
                                    AlphaAnimation aa2 = new AlphaAnimation(0, 1);
                                    aa2.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            current_profile_pos = temp1;
                                            left_arrow_panel.setEnabled(true);
                                            right_arrow_panel.setEnabled(true);
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                    aa2.setDuration(fade_time);
                                    prifile_viewer.addView(toShowProfile);
                                    toShowProfile.startAnimation(aa2);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        current_profile.startAnimation(aa);

                    } catch (Exception e) {
                        //Utils.showModifiedToast(getApplicationContext(), e);
                    }
                }
            });

            right_arrow_panel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        int pos = current_profile_pos + 1;
                        if(pos > profiles.size() - 1) {
                            pos = 0;
                        }

                        final int temp1 = pos;
                        final AlphaAnimation aa = new AlphaAnimation(1, 0);
                        aa.setDuration(fade_time);
                        final ViewGroup current_profile = (ViewGroup)prifile_viewer.getChildAt(0);
                        aa.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                right_arrow_panel.setEnabled(false);
                                left_arrow_panel.setEnabled(false);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                try {

                                    prifile_viewer.removeAllViews();
                                    current_profile.setAlpha(1);

                                    ViewGroup toShowProfile = profiles.get(temp1);
                                    AlphaAnimation aa2 = new AlphaAnimation(0, 1);
                                    aa2.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            current_profile_pos = temp1;
                                            left_arrow_panel.setEnabled(true);
                                            right_arrow_panel.setEnabled(true);
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                    aa2.setDuration(fade_time);
                                    prifile_viewer.addView(toShowProfile);
                                    toShowProfile.startAnimation(aa2);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        current_profile.startAnimation(aa);


                    } catch (Exception e) {
                        //Utils.showModifiedToast(getApplicationContext(), e);
                    }
                }
            });

            prifile_viewer.removeAllViews();
            prifile_viewer.addView(profiles.get(0));
            current_profile_pos = 0;

        } catch (Exception e) {
            e.printStackTrace();
            Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contributor_profile, menu);
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
