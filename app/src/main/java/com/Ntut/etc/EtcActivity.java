package com.Ntut.etc;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.Ntut.BaseActivity;
import com.Ntut.MainActivity;
import com.Ntut.MainApplication;
import com.Ntut.R;
import com.Ntut.utility.Utility;

import java.util.Locale;

import static com.Ntut.MainApplication.lang;


/**
 * Created by kamisakihideyoshi on 2017/02/27.
 */

public class EtcActivity extends BaseActivity {
    private TextView uiLang_textView;
    private TextView uiLangHint_textView;
    private TextView uiE_textView;
    private TextView uiC_textView;
    private TextView uiJ_textView;
    private TextView courseLang_textView;
    private TextView courseLangHint_textView;
    private TextView courseE_textView;
    private TextView courseC_textView;
    private SeekBar uiLang_seekBar;
    private SeekBar courseLang_seekBar;
    private Toolbar mToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        setActionBar();
        uiLang_textView = (TextView) findViewById(R.id.uiLang_textView);
        uiLangHint_textView = (TextView) findViewById(R.id.uiLangHint_textView);
        uiE_textView = (TextView) findViewById(R.id.uiE_textView);
        uiC_textView = (TextView) findViewById(R.id.uiC_textView);
        uiJ_textView = (TextView) findViewById(R.id.uiJ_textView);
        courseLang_textView = (TextView) findViewById(R.id.courseLang_textView);
        courseLangHint_textView = (TextView) findViewById(R.id.courseLangHint_textView);
        courseE_textView = (TextView) findViewById(R.id.courseE_textView);
        courseC_textView = (TextView) findViewById(R.id.courseC_textView);
        uiLang_seekBar = (SeekBar) findViewById(R.id.uiLang_seekBar);
        courseLang_seekBar = (SeekBar) findViewById(R.id.courseLang_seekBar);
        initView();
    }

    private void initView() {
        uiLang_textView.setText(R.string.etc_uilanguage_text);
        uiLangHint_textView.setText(R.string.etc_uilanguage_hint);
        uiE_textView.setText(R.string.etc_language_en);
        uiC_textView.setText(R.string.etc_language_zh);
        uiJ_textView.setText(R.string.etc_language_ja);
        courseLang_textView.setText(R.string.etc_courselanguage_text);
        courseLangHint_textView.setText(R.string.etc_courselanguage_hint);
        courseE_textView.setText(R.string.etc_language_en);
        courseC_textView.setText(R.string.etc_language_zh);
        uiLang_seekBar.setProgress(getCurrentUILang(MainApplication.readSetting("uiLang")));
        courseLang_seekBar.setProgress(getCurrentCourseLang(lang));
        uiLang_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int mProgress = seekBar.getProgress();
                int uiLang = getCurrentUILang(MainApplication.readSetting("uiLang"));
                if (mProgress >= 0 & mProgress < 33) {
                    seekBar.setProgress(0);
                    if (uiLang != 0) {
                        MainApplication.writeSetting("uiLang", "en");
                        switchLanguage("en");
                    }
                } else if (mProgress > 32 & mProgress < 66) {
                    seekBar.setProgress(50);
                    if (uiLang != 50) {
                        MainApplication.writeSetting("uiLang", "zh");
                        switchLanguage("zh");
                    }
                } else {
                    seekBar.setProgress(100);
                    if (uiLang != 100) {
                        MainApplication.writeSetting("uiLang", "ja");
                        switchLanguage("ja");
                    }
                }
            }
        });
        courseLang_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int mProgress = seekBar.getProgress();
                int courseLang = getCurrentCourseLang(MainApplication.readSetting("courseLang"));
                if(mProgress < 50) {
                    seekBar.setProgress(0);
                    if (courseLang != 0) {
                        MainApplication.writeSetting("courseLang", "en");
                        lang = "en";
                        Toast.makeText(getApplicationContext(), R.string.etc_courselanguage_applied, Toast.LENGTH_LONG).show();
                    }
                } else {
                    seekBar.setProgress(100);
                    if (courseLang != 100) {
                        MainApplication.writeSetting("courseLang", "zh");
                        lang = "zh";
                        Toast.makeText(getApplicationContext(), R.string.etc_courselanguage_applied, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    protected void switchLanguage(String lang) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        switch (lang) {
            case "zh":
                configuration.locale = Locale.TAIWAN;
                break;
            case "ja":
                configuration.locale = Locale.JAPAN;
                break;
            default:
                configuration.locale = Locale.ENGLISH;
                break;
        }

        resources.updateConfiguration(configuration, displayMetrics);
        MainApplication.writeSetting("uiLang", lang);
        //*
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        //*/
    }

    private int getCurrentUILang(String lang) {
        switch (lang) {
            case "zh":
                return 50;
            case "ja":
                return 100;
            default:
                return 0;
        }
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
            actionBar.setTitle(R.string.etc_text);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.setting_color)));
        }
        Utility.setStatusBarColor(this, getResources().getColor(R.color.setting_color));
    }

    private int getCurrentCourseLang(String lang) {
        if(lang.equals("zh") || lang.equals("ja")) {
            return 100;
        } else{
            return 0;
        }
    }
}
