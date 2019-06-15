package com.Ntut.event;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Ntut.BaseFragment;
import com.Ntut.R;
import com.Ntut.model.EventInfo;

import com.Ntut.model.EventList;
import com.Ntut.model.Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

/**
 * Created by blackmaple on 2017/5/10.
 */

public class EventFragment extends BaseFragment implements ValueEventListener, View.OnClickListener {

    private View fragmentView;
    private RecyclerView recyclerView;
    private EventAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_event, container, false);
        recyclerView = fragmentView.findViewById(R.id.event_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
        List<EventInfo> eventList = Model.getInstance().getEventArray();
        if (eventList == null) {
            eventList = new EventList();
        }
        adapter = new EventAdapter(eventList, context);
        adapter.add(new EventInfo(
                "https://scontent-tpe1-1.xx.fbcdn.net/v/t31.0-8/22424434_1718823984828619_5454788814235147620_o.jpg?_nc_cat=108&_nc_ht=scontent-tpe1-1.xx&oh=e503d4597b54a50d4200f56540fafb70&oe=5D9014FA",
                "程式設計研究社",
                "",
                "",
                "北科大",
                "程式設計研究社",
                "此 APP 由北科程式設計社開發與維護",
                "https://www.facebook.com/NPC.OwO"));
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
        return R.string.event_text;
    }
}
