package org.sristi.sristi.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import org.sristi.sristi.R;
import org.sristi.sristi.WebBrowserActivity;

import java.net.URL;

/**
 * Created by Ariyan Khan on 23-03-2016.
 */
public class JavascriptInterface {

    Activity host;

    public JavascriptInterface(Activity host) {
        this.host = host;
    }

    @android.webkit.JavascriptInterface
    public void toastAlert(final String text) {
        host.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(host.getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    @android.webkit.JavascriptInterface
    public void finishActivity() {
        host.finish();
    }

    @android.webkit.JavascriptInterface
    public void startBrowserIntent(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(host.getPackageManager()) != null) {
            host.startActivity(intent);
        } else {
            toastAlert("There are no browser in your system to show this web page!");
        }
    }

    @android.webkit.JavascriptInterface
    public void startShareIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        if (intent.resolveActivity(host.getPackageManager()) != null) {
            host.startActivity(intent);
        } else {
            toastAlert("There are no apps in your system to share this contents!");
        }
    }

    @android.webkit.JavascriptInterface
    public void startDialPadIntent(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        if (intent.resolveActivity(host.getPackageManager()) != null) {
            host.startActivity(intent);
        } else {
            toastAlert("Your device does not support the calling API!!");
        }
    }

    @android.webkit.JavascriptInterface
    public void startEmailIntent(String[] emails, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if(intent.resolveActivity(host.getPackageManager()) != null) {
            host.startActivity(intent);
        } else {
            toastAlert("No email app is installed in your device!");
        }
    }
}


















