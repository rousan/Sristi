package org.sristi.sristi.utils;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Interpolator;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import org.sristi.sristi.R;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Ariyan Khan on 18-03-2016.
 */
public abstract class Utils {

    public static final int AppStarterNotificationId = 100001;
    public static final int AppNotificationsNotificationId = 100002;
    public static final int AppDaterNotificationId = 100003;

    public static <T extends View> Vector<T> getRecursiveViews(ViewGroup root, Class<T> clazz) {
        Vector<T> outs = null;
        try {



        } catch (Exception e) {
            e.printStackTrace();
        }
        return outs;
    }

    public static void setFontsToAllTextViews(ViewGroup root) {
        try {

            for(int i=0; i<root.getChildCount(); i++) {
                try {
                    View view = root.getChildAt(i);
                    if(view instanceof TextView) {
                        setParagraphTextFont((TextView)view);
                    } else if (view instanceof ViewGroup){
                        setFontsToAllTextViews((ViewGroup)view);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void vibrate(Context context, long millseconds) {
        try {

            Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(millseconds);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showModifiedToast(final Context context, final int gravity, final int xOffset, final int yOffset, Object object) {
        try {

            final String text = (object == null) ? ("null") : (object.toString());

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Toast toast = new Toast(context);
                        toast.setGravity(gravity, xOffset, yOffset);
                        ViewGroup viewGroup = (ViewGroup)((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.toast_layout, null);
                        TextView textView = (TextView)viewGroup.findViewById(R.id.textview);
                        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf"));
                        textView.setText(text);
                        toast.setView(viewGroup);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            Handler handler = new Handler(context.getMainLooper());
            handler.post(runnable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showModifiedToast(final Context context, Object object) {
        try {
            showModifiedToast(context, Gravity.BOTTOM, 0, 0, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Point getScreenDimentions(Activity activity) {
        Point p = new Point(0, 0);
        try {

            Display display = activity.getWindowManager().getDefaultDisplay();
            display.getSize(p);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    public static void animateHeight(final View view, int fromH, int toH, int duration, TimeInterpolator interpolator, Runnable callback, boolean forward) {
        try {

            if(view == null)
                return;

            final ViewGroup.LayoutParams params = view.getLayoutParams();
            final Handler handler = new Handler(Looper.getMainLooper());

            interpolator = (interpolator == null) ? (new LinearInterpolator()) : (interpolator);
            callback = (callback == null) ? (new Runnable() {
                @Override
                public void run() {

                }
            }) : (callback);

            ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setIntValues(fromH, toH);
            valueAnimator.setDuration(duration);
            valueAnimator.setInterpolator(interpolator);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        params.height = (Integer) animation.getAnimatedValue();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                view.setLayoutParams(params);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            final Runnable temp = callback;
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    temp.run();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            if(forward)
                valueAnimator.start();
            else
                valueAnimator.reverse();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getAppVersionCode() {
        return 20162;
    }

    public static String getAppVersionName() {
        return "1.0.1-Atom";
    }

    public static int dpToPx(int dps, Context context) {
        return (int)Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, context.getResources().getDisplayMetrics()));
    }

    public static void setHeadingTextFont(TextView textView) {
        try {

            textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/OpenSans-Bold.ttf"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setParagraphTextFont(TextView textView) {
        try {

            textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/OpenSans-Regular.ttf"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fade(final View view, final long duration, Runnable callback, final boolean fadeOut) {
        try {

            if (callback == null)
                callback = new Runnable() {
                    @Override
                    public void run() {

                    }
                };
            final Runnable temp1 = callback;

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        AlphaAnimation aa = new AlphaAnimation(1, 0);
                        if (!fadeOut)
                            aa = new AlphaAnimation(0, 1);
                        aa.setDuration(duration);
                        aa.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                temp1.run();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        view.startAnimation(aa);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setOthersOneTextFont(TextView textView) {
        try {

            textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/Noticons-Regular.otf"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }







}

















