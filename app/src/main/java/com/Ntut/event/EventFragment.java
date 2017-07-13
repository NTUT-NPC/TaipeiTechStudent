package com.Ntut.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.Ntut.BaseFragment;
import com.Ntut.R;
import com.Ntut.model.EventInfo;

import com.Ntut.model.Model;
import com.Ntut.utility.WifiUtility;
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

public class EventFragment extends BaseFragment implements ValueEventListener {

    private View fragmentView;
    private RecyclerView recyclerView;
    private static List<EventInfo> eventList;
    private EventAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_event, container, false);
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.event_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference reference_contacts = FirebaseDatabase.getInstance().getReference("events");
        reference_contacts.addValueEventListener(this);
        setData();
    }

    private void setData() {
        eventList = Model.getInstance().getEventArray();
        if (eventList == null) {
            eventList = new ArrayList<>();
        }
        adapter = new EventAdapter(eventList, context);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getTitleColorId() {
        return R.color.red;
    }

    @Override
    public int getTitleStringId() {
        return R.string.event;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        adapter.clear();
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
}
