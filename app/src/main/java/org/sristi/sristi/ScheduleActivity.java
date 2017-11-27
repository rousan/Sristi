package org.sristi.sristi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.sristi.sristi.utils.RemoteServerConstants;
import org.sristi.sristi.utils.Utils;

public class ScheduleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_schedule);

            finish();
            Intent intent = new Intent(getApplicationContext(), WebBrowserActivity.class);
            intent.putExtra("URL", RemoteServerConstants.getFullAddressOfRemoteServerSchedulePage().toString());
            intent.putExtra("LOADING_TEXT", "Loading Schedule, Please Wait...");
            intent.putExtra("REFRESH_URL", RemoteServerConstants.getFullAddressOfRemoteServerSchedulePage().toString());
            startActivity(intent);

        } catch (Exception e) {
            Utils.showModifiedToast(getApplicationContext(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
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
