package com.Ntut.utility;


import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by blackmaple on 2017/5/2.
 */

public class CookieManager implements CookieJar {

    private static List<Cookie> cookies;

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
        this.cookies =  cookies;
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        if (null != cookies) {
            return cookies;
        } else {
            return new ArrayList<Cookie>();
        }
    }

    public static void resetCookies() {
        cookies = null;
    }

//    How to use:
//    OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
//    mOkHttpClientBuilder.cookieJar(new MyCookieJar());
//    OkHttpClient mOkHttpClient = mOkHttpClientBuilder.build();
}
