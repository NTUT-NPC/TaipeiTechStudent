package com.Ntut;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Toast;

import com.Ntut.account.AccountActivity;
import com.Ntut.account.AccountSettingFragment;
import com.Ntut.calendar.CalendarFragment;
import com.Ntut.course.CourseFragment;
import com.Ntut.event.EventFragment;
import com.Ntut.model.Model;
import com.Ntut.other.OtherFragment;
import com.Ntut.portal.PortalFragment;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    private BottomNavigationBar bottomNavigationBar;
    private int lastSelectedPosition;
    private BaseFragment currentFragment;
    private FirebaseAnalytics firebaseAnalytics;
    private BaseFragment fragment;
    private Toolbar toolbar;
    private CourseFragment courseFragment = new CourseFragment();
    private CalendarFragment calendarFragment = new CalendarFragment();
    private EventFragment eventFragment = new EventFragment();
    private PortalFragment portalFragment = new PortalFragment();
    private OtherFragment otherFragment = new OtherFragment();
    private AccountSettingFragment accountSettingFragment = new AccountSettingFragment();
    private Boolean lockFinish = true;
    private SharedPreferences firstOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initToolbar();
        initNavigation();
        if (MainApplication.readSetting("account") == null || MainApplication.readSetting("password") == null) {
            changeFragment(accountSettingFragment);
        }
        changeFragment(courseFragment);
        String first_func = MainApplication.readSetting("first_func");
        firstOpen = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        if (TextUtils.isEmpty(first_func)) {
            MainApplication.writeSetting("first_func", "0");
            first_func = MainApplication.readSetting("first_func");
            switchFragment(Integer.parseInt(first_func));
            showFirstopen();
        } else {
            switchFragment(0);
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNavigation() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
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

    private void showFirstopen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("T.T.S.北科學生APP");
        builder.setMessage(R.string.firstopen_text);
        builder.setPositiveButton("關閉", null);
        builder.setNegativeButton("前往帳號登錄", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    switchFragment(3);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onTabSelected(int position) {
        lastSelectedPosition = position;
        switchFragment(position);
    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {
    }

    private void switchFragment(int position) {
        fragment = null;
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
                    fragment = portalFragment;
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
        getWindow().setStatusBarColor(ContextCompat.getColor(getBaseContext(), currentFragment.getTitleColorId()));
    }
}
