package com.Ntut.etc;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.SeekBar;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.Ntut.BaseActivity;
import com.Ntut.MainActivity;
import com.Ntut.MainApplication;
import com.Ntut.R;
import com.Ntut.model.Model;
import com.Ntut.runnable.AccountRunnable;
import com.Ntut.utility.NportalConnector;
import com.Ntut.utility.Utility;

import java.util.Locale;

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
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        setActionBar();
        uiLang_textView = findViewById(R.id.uiLang_textView);
        uiLangHint_textView = findViewById(R.id.uiLangHint_textView);
        uiE_textView = findViewById(R.id.uiE_textView);
        uiC_textView = findViewById(R.id.uiC_textView);
        uiJ_textView = findViewById(R.id.uiJ_textView);
        courseLang_textView = findViewById(R.id.courseLang_textView);
        courseLangHint_textView = findViewById(R.id.courseLangHint_textView);
        courseE_textView = findViewById(R.id.courseE_textView);
        courseC_textView = findViewById(R.id.courseC_textView);
        uiLang_seekBar = findViewById(R.id.uiLang_seekBar);
        courseLang_seekBar = findViewById(R.id.courseLang_seekBar);
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
        courseLang_seekBar.setProgress(getCurrentCourseLang(MainApplication.readSetting("courseLang")));
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
                        updateCourse();
//                        Toast.makeText(getApplicationContext(), R.string.etc_courselanguage_applied, Toast.LENGTH_LONG).show();
                    }
                } else {
                    seekBar.setProgress(100);
                    if (courseLang != 100) {
                        MainApplication.writeSetting("courseLang", "zh");
                        updateCourse();
//                        Toast.makeText(getApplicationContext(), R.string.etc_courselanguage_applied, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void updateCourse() {
        NportalConnector.reset();
        Model.getInstance().deleteStudentCourse();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
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
            mToolbar.setNavigationOnClickListener(v -> finish());
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
