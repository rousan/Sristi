package org.sristi.sristi;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.sristi.sristi.R;

public class LoadingAnimationFragment extends Fragment {

    public LoadingAnimationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;

        try {

            ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_loading_animation, container, false);
            WebView webView = (WebView)root.findViewById(R.id.loading_ani_wv);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient());
            webView.setBackgroundColor(0);
            webView.loadUrl("file:///android_asset/web/loading_animation_fragment_layout.html");

            view = root;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}