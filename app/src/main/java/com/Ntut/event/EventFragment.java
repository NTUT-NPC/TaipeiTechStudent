package com.Ntut.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.Ntut.BaseFragment;
import com.Ntut.R;
import com.Ntut.model.EventInfo;

import com.Ntut.model.Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by blackmaple on 2017/5/10.
 */

public class EventFragment extends BaseFragment implements ValueEventListener, View.OnClickListener {

    private View fragmentView;
    private RecyclerView recyclerView;
    private static List<EventInfo> eventList;
    private EventAdapter adapter;
    private View start_button;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_event, container, false);
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.event_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        start_button = fragmentView.findViewById(R.id.start_button);
        start_button.setOnClickListener(this);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("events");
        databaseReference.addValueEventListener(this);

        setData();
    }

    private void setData() {
        eventList = Model.getInstance().getEventArray();
        if (eventList == null) {
            eventList = new ArrayList<>();
            start_button.setVisibility(View.VISIBLE);
        } else {
            start_button.setVisibility(View.GONE);
        }
        adapter = new EventAdapter(eventList, context);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.e(getClass().getName(), "Firebase data change");
        adapter.clear();
        Log.e(getClass().getSimpleName(), "EventAdapter clear");
        for (DataSnapshot ds : dataSnapshot.getChildren() ){
            EventInfo eventInfo = new EventInfo(
                    ds.child("image").getValue().toString(),
                    ds.child("title").getValue().toString(),
                    ds.child("start_date").getValue().toString(),
                    ds.child("end_date").getValue().toString(),
                    ds.child("location").getValue().toString(),
                    ds.child("host").getValue().toString(),
                    ds.child("content").getValue().toString(),
                    ds.child("url").getValue().toString());
            adapter.add(eventInfo);
        }
        adapter.notifyDataSetChanged();
        adapter.saveData();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_button:
                if (adapter.getItemCount() > 0) {
                    View start_button = fragmentView.findViewById(R.id.start_button);
                    start_button.setVisibility(View.GONE);
                } else {
                    showAlertMessage(getString(R.string.check_network_available));
                }
                break;
        }
    }

    @Override
    public int getTitleColorId() {
        return R.color.event_color;
    }

    @Override
    public int getTitleStringId() {
        return R.string.activity_text;
    }
}
