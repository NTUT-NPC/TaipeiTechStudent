package com.Ntut;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.Ntut.account.AccountActivity;
import com.Ntut.account.AccountSettingFragment;
import com.Ntut.calendar.CalendarFragment;
import com.Ntut.course.CourseFragment;
import com.Ntut.event.EventFragment;
import com.Ntut.model.Model;
import com.Ntut.other.OtherFragment;
import com.Ntut.portal.PortalFragment;
import com.Ntut.utility.WifiUtility;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Locale;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    private BaseFragment currentFragment;
    private CourseFragment courseFragment = new CourseFragment();
    private CalendarFragment calendarFragment = new CalendarFragment();
    private EventFragment eventFragment = new EventFragment();
    private PortalFragment portalFragment = new PortalFragment();
    private OtherFragment otherFragment = new OtherFragment();
    private AccountSettingFragment accountSettingFragment = new AccountSettingFragment();
    private Boolean lockFinish = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseMessaging.getInstance().subscribeToTopic("main");
        if (MainApplication.readSetting("uiLang").isEmpty() || MainApplication.readSetting("courseLang").isEmpty()) {
            MainApplication.writeSetting("uiLang", Locale.getDefault().getLanguage());
            MainApplication.writeSetting("courseLang", Locale.getDefault().getLanguage());
        }
        switchLanguage(MainApplication.readSetting("uiLang"));
        setContentView(R.layout.activity_main);
        initToolbar();
        initNavigation();
        switchLanguage(MainApplication.readSetting("uiLang"));
        if (MainApplication.readSetting("account") == null || MainApplication.readSetting("password") == null) {
            changeFragment(accountSettingFragment);
        }
        String first_func = MainApplication.readSetting("first_func");
        SharedPreferences firstOpen = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        if ("FirebaseMessaging".equals(getIntent().getStringExtra("from"))) {
            Log.e(getClass().getSimpleName(), "succeed");
            onTabSelected(2);
        } else if (TextUtils.isEmpty(first_func)) {
            MainApplication.writeSetting("first_func", "0");
            Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
            startActivity(intent);
        } else {
            onTabSelected(0);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNavigation() {
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.
                setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.course_icon).setActiveColorResource(R.color.course_color))
                .addItem(new BottomNavigationItem(R.drawable.calendar_icon).setActiveColorResource(R.color.calendar_color))
                .addItem(new BottomNavigationItem(R.drawable.event_icon).setActiveColorResource(R.color.event_color))
                .addItem(new BottomNavigationItem(R.drawable.nportal_icon).setActiveColorResource(R.color.portal_color))
                .addItem(new BottomNavigationItem(R.drawable.other_icon).setActiveColorResource(R.color.other_color))
                .initialise();
    }

    @Override
    public void onTabSelected(int position) {
        switchFragment(position);
    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {
    }

    private void switchFragment(int position) {
        BaseFragment fragment = null;
        switch (position) {
            case 0:
                fragment = courseFragment;
                break;
            case 1:
                fragment = calendarFragment;
                break;
            case 2:
                fragment = eventFragment;
                break;
            case 3:
                String account = Model.getInstance().getAccount();
                String password = Model.getInstance().getPassword();
                if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
                    if (WifiUtility.isNetworkAvailable(this)) {
                        fragment = portalFragment;
                    } else {
                        showAlertMessage(getString(R.string.check_network_available));
                    }
                } else {
                    Toast.makeText(getBaseContext(), R.string.none_account_error, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, AccountActivity.class);
                    startActivity(intent);
                    return;
                }

                break;
            case 4:
                fragment = otherFragment;
                break;
        }
        changeFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        if (currentFragment == portalFragment && PortalFragment.canGoBack()) {
            PortalFragment.goBack();
        } else {
            if (lockFinish) {
                Toast.makeText(MainActivity.this, R.string.press_again_to_exit,
                        Toast.LENGTH_SHORT).show();
                lockFinish = false;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            synchronized (this) {
                                wait(2000);
                            }
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        lockFinish = true;
                    }
                };
                thread.start();
            } else {
                finish();
            }
        }
    }


    public void changeFragment(BaseFragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            if (currentFragment.equals(newFragment)) {
                return;
            }
            if (!newFragment.isAdded()) {
                transaction.hide(currentFragment).add(R.id.fragment_container, newFragment).commit();
            } else {
                transaction.hide(currentFragment).show(newFragment).commit();
            }
        } else {
            transaction.add(R.id.fragment_container, newFragment).commit();
        }
        currentFragment = newFragment;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(currentFragment.getTitleStringId());
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getBaseContext(), currentFragment.getTitleColorId())));
        }
        setStatusBarColor(getResources().getColor(currentFragment.getTitleColorId()));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            if (color == Color.BLACK
                    && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setStatusBarColor(color);
        }
    }

    //  與 EtcFragment 的方法相似，因 getResources() 問題在此複製一份使用
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
        /*  避免重複寫入
        MainApplication.writeSetting("uiLang", lang);
        //*/
    }
}
