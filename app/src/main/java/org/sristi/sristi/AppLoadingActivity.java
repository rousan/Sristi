package org.sristi.sristi;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.larvalabs.svgandroid.SVGParser;

import org.sristi.sristi.utils.FileDownloader;
import org.sristi.sristi.utils.FilesData;
import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppLoadingActivity extends Activity {

    private boolean loadingIconAnimIsFinished = false;

    private ImageView loader_app_icon = null;
    private TextView loader_first_txt_ani = null;
    private TextView loader_second_txt_ani = null;
    private WebView loading_ani_wv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(R.layout.activity_app_loading);
            //getWindow().setBackgroundDrawableResource(R.drawable.bk_berry_ga);//imp

            startService(new Intent(getApplicationContext(), EventUpdaterService.class));
            startService(new Intent(getApplicationContext(), AppUpdaterService.class));
            startService(new Intent(getApplicationContext(), SristiService.class));

            ((TextView)findViewById(R.id.app_loading_copyright_text_view)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Play-Regular.ttf"));

            loader_app_icon = (ImageView)findViewById(R.id.loader_app_icon);
            loader_first_txt_ani = (TextView)findViewById(R.id.loader_first_txt_ani);
            loader_second_txt_ani = (TextView)findViewById(R.id.loader_second_txt_ani);
            loading_ani_wv = (WebView)findViewById(R.id.loading_ani_wv);
            loading_ani_wv.getSettings().setJavaScriptEnabled(true);
            loading_ani_wv.setWebViewClient(new WebViewClient());
            loading_ani_wv.setBackgroundColor(0);
            loading_ani_wv.addJavascriptInterface(new JAB(getApplicationContext()), "JAB");
            


            loader_first_txt_ani.setTypeface(Typeface.createFromAsset(getAssets(), "web/venera-900.otf"));
            loader_second_txt_ani.setTypeface(Typeface.createFromAsset(getAssets(), "web/venera-900.otf"));
            loader_app_icon.setImageResource(R.drawable.app_icon_2);//imp

            loader_app_icon.setAlpha(0.0f);

            loader_first_txt_ani.setTranslationX(-500f);
            loader_first_txt_ani.setAlpha(0.0f);
            loader_second_txt_ani.setTranslationX(500f);
            loader_second_txt_ani.setAlpha(0.0f);

            final ViewPropertyAnimator viewPropertyAnimatorForLoader_app_icon = loader_app_icon.animate().setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    loadingIconAnimIsFinished = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    loadingIconAnimIsFinished = true;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).alpha(1.0f).setDuration(1400);

            ViewPropertyAnimator viewPropertyAnimatorForFirst = loader_first_txt_ani.animate().setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    viewPropertyAnimatorForLoader_app_icon.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    viewPropertyAnimatorForLoader_app_icon.start();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).translationX(6f).setDuration(1400).alpha(1.0f).setInterpolator(new AccelerateDecelerateInterpolator());

            ViewPropertyAnimator viewPropertyAnimatorForSecond = loader_second_txt_ani.animate().translationX(-6f).setDuration(1400).alpha(1.0f).setInterpolator(new AccelerateDecelerateInterpolator());


            //first load then anim starts
            loading_ani_wv.loadUrl("file:///android_asset/web/loading_anim.html");
            viewPropertyAnimatorForFirst.start();
            viewPropertyAnimatorForSecond.start();

            Log.d("Sristi", "jkhjkhkjhhj");

            //extractLogToFileAndWeb();


            RemoteServerConstants.userLogIn("", "");

        } catch (Exception e) {
            postSmallErros(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_loading, menu);
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

    public File extractLogToFileAndWeb(){
        //set a file
        Date datum = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY);
        String fullName = df.format(datum)+"appLog.log";
        File file = new File (Environment.getExternalStorageDirectory(), fullName);

        //clears a file
        if(file.exists()){
            file.delete();
        }


        //write log to file
        int pid = android.os.Process.myPid();
        try {

            String command = String.format("logcat -d -v threadtime *:*");
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String currentLine = null;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine != null) {
                    result.append(currentLine);
                    result.append("\n");
                }
            }

            FileWriter out = new FileWriter(file);
            out.write(result.toString());
            out.close();

            //Runtime.getRuntime().exec("logcat -d -v time -f "+file.getAbsolutePath());
        } catch (IOException e) {
            Utils.showModifiedToast(getApplicationContext(), e);
        }


        //clear the log
        try {
            Runtime.getRuntime().exec("logcat -c");
        } catch (IOException e) {
            Utils.showModifiedToast(getApplicationContext(), e);
        }

        return file;
    }

    //JS ANDROID BRIDGE
    public class JAB {

        private Context context = null;

        public JAB(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public boolean loadingAnimWillBeRun() {
            return (loadingIconAnimIsFinished);
        }

        @android.webkit.JavascriptInterface
        public void startNextActivity() {
            //start home activity
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            finish();
        }

    }
}











