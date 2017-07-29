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
import android.widget.ImageView;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        eventDetailImage = (ImageView) findViewById(R.id.event_detail_image);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        getWindow().setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.red));
        actionBar.setDisplayHomeAsUpEnabled(true);
        EventInfo event = getIntent().getParcelableExtra("detail");
        collapsingToolbar.setTitle(event.getTitle());
        event.getImage(getBaseContext()).fitCenter().into(eventDetailImage);
    }
}
