package com.Ntut.club;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Ntut.R;
import com.Ntut.utility.Utility;
import com.Ntut.utility.WifiUtility;

/**
 * Created by Andy on 2017/7/23.
 */

public class ClubActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private WebView webview;
    private final static String CACHE_DIRNAME = "club_webview";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        setActionBar();
        webview = findViewById(R.id.club_webview);
        webview.setWebViewClient(new WebViewClient());
        webview.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == MotionEvent.ACTION_UP
                    && webview.canGoBack()) {
                webview.goBack();
                return true;
            }

            return false;
        });
        initWebViewSettings();
        webview.loadUrl("https://www.google.com.tw/search?q=android+webview&rlz=1C1CHZL_zh-TWTW722TW722&oq=android+webview&aqs=chrome..69i57j69i65j35i39l2j0l2.5582j0j7&sourceid=chrome&ie=UTF-8");
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
            mToolbar.setNavigationOnClickListener(v -> finish());
            actionBar.setTitle(R.string.club_text);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
        }
        Utility.setStatusBarColor(this, getResources().getColor(R.color.blue));
    }
}
