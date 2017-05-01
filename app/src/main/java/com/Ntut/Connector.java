package com.Ntut;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by blackmaple on 2017/5/2.
 */

public class Connector {
    public void syncGet() {
        //同步的 Http Get
        OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
        final OkHttpClient mOkHttpClient = mOkHttpClientBuilder.build();
        new Thread(new Runnable() {

            @Override
            public void run() {
                Request request = new Request.Builder().url("https://www.google.com").build();
                Call call = mOkHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    // 或者直接Throw IOException
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void asyncGet() {
        //非同步的 Http Get
        OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
        final OkHttpClient mOkHttpClient = mOkHttpClientBuilder.build();
        Request request = new Request.Builder().url("https://www.google.com").build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public void syncPost() {
        //同步的 Http Post
        OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
        final OkHttpClient mOkHttpClient = mOkHttpClientBuilder.build();
        new Thread(new Runnable() {

            @Override
            public void run() {
                Headers.Builder headersBuilder = new Headers.Builder()
                        .add("headerKey", "headerValue");
                Headers requestHeaders = headersBuilder.build();

                FormBody.Builder formBodyBuilder = new FormBody.Builder()
                        .add("paramKey", "ParamValue");
                RequestBody requestBody = formBodyBuilder.build();

                Request request = new Request.Builder()
                        .url("https://www.google.com")
                        .headers(requestHeaders)
                        .post(requestBody)
                        .build();
                try {
                    Response response = mOkHttpClient.newCall(request).execute();
                    // 或者直接Throw IOException
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void asyncPost() {
        //非同步的 Http Post
        OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
        final OkHttpClient mOkHttpClient = mOkHttpClientBuilder.build();
        Headers.Builder headersBuilder = new Headers.Builder()
                .add("headerKey", "headerValue");
        Headers requestHeaders = headersBuilder.build();

        FormBody.Builder formBodyBuilder = new FormBody.Builder()
                .add("paramKey", "paramValue");
        RequestBody requestBody = formBodyBuilder.build();

        Request request = new Request.Builder()
                .url("https://www.google.com")
                .headers(requestHeaders)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
}
