package com.Ntut;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private BottomNavigationBar bottomNavigationBar;
    private int lastSelectedPosition;
    private Fragment currentFragment;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initToolbar();
        initNavigation();
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNavigation(){
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.
                setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar
                .setActiveColor(R.color.colorPrimary);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.course_icon))
                .addItem(new BottomNavigationItem(R.drawable.calendar_icon))
                .addItem(new BottomNavigationItem(R.drawable.credit_icon))
                .addItem(new BottomNavigationItem(R.drawable.nportal_icon))
                .addItem(new BottomNavigationItem(R.drawable.other_iocn))
                .initialise();
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
        switch (position) {
            case 0:
                break;
        }
    }

    private void changeFragment(Fragment newFragment) {
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
    }
}
