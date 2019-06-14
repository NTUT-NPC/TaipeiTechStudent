package com.Ntut.portal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.Ntut.BaseFragment;
import com.Ntut.R;
import com.Ntut.model.Model;
import com.Ntut.runnable.BaseRunnable;
import com.Ntut.runnable.LoginNportalRunnable;

import java.lang.ref.WeakReference;
import java.net.CookieHandler;
import java.net.HttpCookie;
import java.net.URI;

/**
 * Created by blackmaple on 2017/5/10.
 */

public class PortalFragment extends BaseFragment {

    private  View fragmentView;
    private ProgressDialog mProgressDialog;
    private static final String PORTAL_URL = "https://app.ntut.edu.tw/";
    private static WebView webview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fragmentView = inflater.inflate(R.layout.fragment_portal, container, false);
        webview = fragmentView.findViewById(R.id.portal_webview);
        initWebView();
        String account = Model.getInstance().getAccount();
        String password = Model.getInstance().getPassword();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
            mProgressDialog = ProgressDialog.show(this.getContext(), null,
                    getString(R.string.nportal_loggingin));
            Thread loginThread = new Thread(new LoginNportalRunnable(account, password,
                    new LoginHandler(this)));
            loginThread.start();
        }

        return fragmentView;
    }

    private void initWebView() {
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
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setDomStorageEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if(message!=null){
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
                result.cancel();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(getActivity())
                        .setMessage(message)
                        .setPositiveButton("確定", (dialogInterface, i) -> result.confirm())
                        .setNegativeButton("取消", (dialogInterface, i) -> result.cancel())
                        .show();
                return true;
            }
        });
        webview.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == MotionEvent.ACTION_UP
                    && webview.canGoBack()) {
                webview.goBack();
                return true;
            }

            return false;
        });
    }

    private class LoginHandler extends Handler {
        private WeakReference<PortalFragment> mActivityRef = null;

        public LoginHandler(PortalFragment activity) {
            mActivityRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PortalFragment fragment = mActivityRef.get();
            if (fragment == null) {
                return;
            }
            WebView webview = fragmentView.findViewById(R.id.portal_webview);
            switch (msg.what) {
                case BaseRunnable.REFRESH:
                    java.net.CookieStore rawCookieStore = ((java.net.CookieManager)
                            CookieHandler.getDefault()).getCookieStore();
                    if (rawCookieStore != null) {
                        CookieManager cookieManager = CookieManager.getInstance();
                        cookieManager.setAcceptCookie(true);
                        try {
                            URI uri = new URI(PORTAL_URL);
                            for (HttpCookie cookie : rawCookieStore.get(uri)) {
                                String cookieString = cookie.getName() + "="
                                        + cookie.getValue() + "; domain="
                                        + cookie.getDomain();
                                cookieManager.setCookie(PORTAL_URL + "myPortal.do",
                                        cookieString);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (webview != null) {
                        webview.loadUrl(PORTAL_URL + "aptreeList.do");
                    }
                    break;
                case BaseRunnable.ERROR:
                    if (webview != null) {
                        webview.loadUrl(PORTAL_URL);
                    }
                    break;
            }
            fragment.dismissProgressDialog();
            Toast.makeText(fragment.getContext(), R.string.web_back_hint, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public static boolean canGoBack(){
        return webview.canGoBack();
    }

    public static void goBack(){
        webview.goBack();
    }

    @Override
    public void onDestroy() {
        if (webview != null) {
            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview.clearHistory();
            ((ViewGroup) webview.getParent()).removeView(webview);
            webview.destroy();
            webview = null;
        }
        super.onDestroy();
    }

    @Override
    public int getTitleColorId() {
        return R.color.portal_color;
    }

    @Override
    public int getTitleStringId() {
        return R.string.portal;
    }
}
