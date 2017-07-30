package com.Ntut.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.Ntut.R;
import com.Ntut.model.EventInfo;

import java.util.concurrent.ExecutionException;

/**
 * Created by Andy on 2017/7/23.
 */

public class EventDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView eventDetailImage;
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
        location = (TextView) findViewById(R.id.event_detail_location);
        host = (TextView) findViewById(R.id.event_detail_host);
        url = (TextView) findViewById(R.id.event_detail_url);
        date = (TextView) findViewById(R.id.event_detail_date);
        content = (TextView) findViewById(R.id.event_detail_content);
        setToolbar();
        setData();
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
        collapsingToolbar.setTitle(event.getTitle());
        event.getImage(getBaseContext()).fitCenter().into(eventDetailImage);
        location.setText("地點：" + event.getLocation());
        host.setText("主辦單位：" + event.getHost());
        url.setText("詳細內容：" + event.getUrl());
        date.setText("日期：" + event.getStartDate() + " ~ " + event.getEndDate());
        content.setText("內容：" + event.getContent());
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
}
