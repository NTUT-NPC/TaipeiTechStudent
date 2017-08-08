package com.Ntut.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Ntut.R;
import com.Ntut.model.EventInfo;

/**
 * Created by Andy on 2017/7/23.
 */

public class EventDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView eventDetailImage;
    private FloatingActionButton fab;
    private TextView title;
    private TextView location;
    private TextView host;
    private TextView url;
    private TextView date;
    private TextView content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        eventDetailImage = (ImageView) findViewById(R.id.event_detail_image);
        fab = (FloatingActionButton) findViewById(R.id.event_fab);
        title = (TextView) findViewById(R.id.event_detail_title);
        location = (TextView) findViewById(R.id.event_detail_location);
        host = (TextView) findViewById(R.id.event_detail_host);
        url = (TextView) findViewById(R.id.event_detail_url);
        date = (TextView) findViewById(R.id.event_detail_date);
        content = (TextView) findViewById(R.id.event_detail_content);
        setToolbar();
        setData();
        Transition transition = getWindow().getSharedElementEnterTransition();
        transition.addTarget("event_image");
        transition.addTarget("event_title");
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                hideElement();
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                showElement();
            }

            @Override
            public void onTransitionCancel(Transition transition) {
                showElement();
            }

            @Override
            public void onTransitionPause(Transition transition) {
                hideElement();
            }

            @Override
            public void onTransitionResume(Transition transition) {
                showElement();
            }
        });

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        showElement();
    }

    @Override
    public void finishAfterTransition() {
        super.finishAfterTransition();
        hideElement();
    }

    private void showElement() {
        location.setVisibility(View.VISIBLE);
        host.setVisibility(View.VISIBLE);
        url.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    private void hideElement() {
        location.setVisibility(View.INVISIBLE);
        host.setVisibility(View.INVISIBLE);
        url.setVisibility(View.INVISIBLE);
        date.setVisibility(View.INVISIBLE);
        content.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        getWindow().setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.red));
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setData() {
        EventInfo event = getIntent().getParcelableExtra("detail");
        String titleText = event.getTitle();
        collapsingToolbar.setTitle(titleText);
        event.getImage(getBaseContext()).into(eventDetailImage);
        title.setText(titleText);
        location.setText(R.string.event_location + event.getLocation());
        host.setText(getString(R.string.event_host) + event.getHost());
        url.setText(getString(R.string.event_url) + event.getUrl());
        date.setText(R.string.event_date + event.getStartDate() + " ~ " + event.getEndDate());
        content.setText(getString(R.string.event_detail) + event.getContent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
