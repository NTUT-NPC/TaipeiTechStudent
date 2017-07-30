package com.Ntut.feedback;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.Ntut.R;
import com.Ntut.utility.Utility;
import com.Ntut.utility.WifiUtility;

/**
 * Created by Andy on 2017/7/27.
 */

public class FeedbackActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private WebView webview;
    private final static String CACHE_DIRNAME = "feedback_webview";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        setActionBar();
        webview = (WebView) findViewById(R.id.feedback_webview);
        webview.setWebViewClient(new WebViewClient());
        webview.setOnKeyListener(new View.OnKeyListener(){

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && webview.canGoBack()) {
                    webview.goBack();
                    return true;
                }

                return false;
            }

        });
        initWebViewSettings();
        webview.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSdC-O3Kit2ejBG91u4KsdZUMb7Lpzln6ik1p_b_Njy3pKJOxw/viewform");
    }

    private void initWebViewSettings() {
        WebSettings webSetting = webview.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setAllowFileAccess(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setLoadsImagesAutomatically(true);
        webSetting.setDefaultTextEncodingName("utf-8");
        if (WifiUtility.isConnected(this)) {
            webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webSetting.setDomStorageEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setAppCacheEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + CACHE_DIRNAME;
        webSetting.setAppCachePath(cacheDirPath);
    }

    public void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            actionBar.setTitle(R.string.club_text);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple)));
        }
        Utility.setStatusBarColor(this, getResources().getColor(R.color.purple));
    }
}
