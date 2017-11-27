package org.sristi.sristi;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import org.sristi.sristi.utils.Utils;

import java.util.Vector;

public class FaqsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_faqs);

            findViewById(R.id.back_imageeview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            View show_details_1 = findViewById(R.id.show_details_1);
            View show_details_2 = findViewById(R.id.show_details_2);
            View show_details_3 = findViewById(R.id.show_details_3);
            View show_details_4 = findViewById(R.id.show_details_4);
            View show_details_5 = findViewById(R.id.show_details_5);
            View show_details_6 = findViewById(R.id.show_details_6);
            View show_details_7 = findViewById(R.id.show_details_7);

            final View answer_panel_1 = findViewById(R.id.answer_panel_1);
            View answer_panel_2 = findViewById(R.id.answer_panel_2);
            View answer_panel_3 = findViewById(R.id.answer_panel_3);
            View answer_panel_4 = findViewById(R.id.answer_panel_4);
            View answer_panel_5 = findViewById(R.id.answer_panel_5);
            View answer_panel_6 = findViewById(R.id.answer_panel_6);
            View answer_panel_7 = findViewById(R.id.answer_panel_7);

            int h1 = dpToPx(170);//dp
            int h2 = dpToPx(173);//dp
            int h3 = dpToPx(190);//dp
            int h4 = dpToPx(210);//dp
            int h5 = dpToPx(210);//dp
            int h6 = dpToPx(100);//dp
            int h7 = dpToPx(140);//dp

            Vector<View> show_details = new Vector<View>();
            show_details.add(show_details_1);
            show_details.add(show_details_2);
            show_details.add(show_details_3);
            show_details.add(show_details_4);
            show_details.add(show_details_5);
            show_details.add(show_details_6);
            show_details.add(show_details_7);

            Vector<View> answer_panels = new Vector<View>();
            answer_panels.add(answer_panel_1);
            answer_panels.add(answer_panel_2);
            answer_panels.add(answer_panel_3);
            answer_panels.add(answer_panel_4);
            answer_panels.add(answer_panel_5);
            answer_panels.add(answer_panel_6);
            answer_panels.add(answer_panel_7);

            Vector<Integer> heights = new Vector<Integer>();
            heights.add(h1);
            heights.add(h2);
            heights.add(h3);
            heights.add(h4);
            heights.add(h5);
            heights.add(h6);
            heights.add(h7);


            for (int i=0; i<show_details.size(); i++) {
                final View show_dtls = show_details.get(i);
                final View answer_panl = answer_panels.get(i);
                final int h = heights.get(i);
                final ValueAnimator va = new ValueAnimator();
                va.setDuration(1000);
                va.setIntValues(0, h);
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ViewGroup.LayoutParams param = answer_panl.getLayoutParams();
                        param.height = (Integer)animation.getAnimatedValue();
                        answer_panl.setLayoutParams(param);
                    }
                });

                show_dtls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(answer_panl.getVisibility() == View.GONE || answer_panl.getHeight() == 0) {
                            ViewGroup.LayoutParams param = answer_panl.getLayoutParams();
                            param.height = 0;
                            answer_panl.setLayoutParams(param);
                            answer_panl.setVisibility(View.VISIBLE);
                            va.start();
                        } else {
                            va.reverse();
                        }
                    }
                });

            }

            Utils.setFontsToAllTextViews((ViewGroup)findViewById(R.id.root));
        /*Utils.setHeadingTextFont((TextView) findViewById(R.id.qustion_1));
        Utils.setHeadingTextFont((TextView)findViewById(R.id.qustion_2));
        Utils.setHeadingTextFont((TextView)findViewById(R.id.qustion_3));
        Utils.setHeadingTextFont((TextView)findViewById(R.id.qustion_4));
        Utils.setHeadingTextFont((TextView)findViewById(R.id.qustion_5));
        Utils.setHeadingTextFont((TextView)findViewById(R.id.qustion_6));
        Utils.setHeadingTextFont((TextView)findViewById(R.id.qustion_7));*/

            ViewGroup faqs_go_btn = (ViewGroup)findViewById(R.id.faqs_go_btn);
            faqs_go_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Uri uri = Uri.parse("http://sristi.org.in");
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
            Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    public void onWindowFocusChanged(boolean r) {
        if(hasWindowFocus())  {
            /*final View v = findViewById(R.id.lp);
            final int h = v.getHeight();
            VLog.d("sr", h);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    while (i<=h) {
                        final int temp = i;
                        VLog.d("sr", i);
                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                v.setTranslationY(temp);
                            }
                        });
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                            VLog.d("s", e);
                        }
                        i++;
                    }
                }
            }).start();*/

            //final View v = findViewById(R.id.pnl);
            //final int h = v.getHeight();
            //TranslateAnimation ta = new TranslateAnimation(0, 0, 0, h);
            //ta.setDuration(1000);
           // v.startAnimation(ta);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_faqs, menu);
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

    public int dpToPx(int dps) {
        return (int)Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, getResources().getDisplayMetrics()));
    }
}


















