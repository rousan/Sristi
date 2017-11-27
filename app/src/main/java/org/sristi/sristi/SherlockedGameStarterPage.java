package org.sristi.sristi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sristi.sristi.utils.SettingsPreferenceKeys;
import org.sristi.sristi.utils.Utils;
import org.w3c.dom.Text;

public class SherlockedGameStarterPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       try {

           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_sherlocked_game_starter_page);

           SharedPreferences sp = getSharedPreferences(SettingsPreferenceKeys.user_account_shared_pref_file, 0);
           final String email = sp.getString("EMAIL", "");
           final String pass = sp.getString("PASSWORD", "");

           findViewById(R.id.log_in_btn).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   try {
                       startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));
                       finish();
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
           });

           findViewById(R.id.start_geme_btn).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   try {
                       /*
                       Intent i = new Intent(getApplicationContext(), WebBrowserActivity.class);
                       i.putExtra("URL", "http://app.sristi.org.in/app/online_games/sherlocked/controllers/sherlocked_page_starter.php?email=demo@gmail.com&pass=demo");
                       i.putExtra("LOADING_TEXT", "Loading Sherlocked Game, Please wait...");
                       i.putExtra("REFRESH_URL", "file:///android_asset/web/sherlocked_game_page.html");
                       i.putExtra("SHOW_REFRESH_ICON", false);
                       startActivity(i);*/

                       startActivity(new Intent(getApplicationContext(), SherlockedGameActivity.class));
                       
                       finish();
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
           });

           if(email.trim().isEmpty()) {
               findViewById(R.id.sign_in_panel).setVisibility(View.VISIBLE);
               findViewById(R.id.start_page_panel).setVisibility(View.GONE);
           } else {
               findViewById(R.id.sign_in_panel).setVisibility(View.GONE);
               findViewById(R.id.start_page_panel).setVisibility(View.VISIBLE);
           }


           Utils.setFontsToAllTextViews((ViewGroup)findViewById(R.id.root));
           Utils.setHeadingTextFont((TextView) findViewById(R.id.log_in_title));
           Utils.setHeadingTextFont((TextView)findViewById(R.id.start_page_title));
           Utils.setHeadingTextFont((TextView)findViewById(R.id.log_in_btn));
           Utils.setHeadingTextFont((TextView)findViewById(R.id.start_geme_btn));;

       } catch (Exception e) {
           e.printStackTrace();
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sherlocked_game_starter_page, menu);
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
