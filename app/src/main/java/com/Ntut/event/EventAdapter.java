package com.Ntut.event;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.Ntut.MainActivity;
import com.Ntut.R;
import com.Ntut.model.EventInfo;
import com.Ntut.model.Model;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blackmaple on 2017/5/15.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private MainActivity context;
    private List<EventInfo> eventList;

    public EventAdapter(List<EventInfo> eventList, Context context) {
        this.context = (MainActivity) context;
        this.eventList = eventList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView title;
        public TextView date;
        public TextView location;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.event_image);
            title = (TextView) itemView.findViewById(R.id.event_title);
            date = (TextView) itemView.findViewById(R.id.event_date);
            location = (TextView) itemView.findViewById(R.id.event_location);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EventInfo event = eventList.get(position);
        ImageView image = holder.image;
        loadImage(holder, event.getImage());
        TextView title = holder.title;
        title.setText(event.getTitle());
        TextView date = holder.date;
        date.setText(event.getStartDate());
        TextView location = holder.location;
        location.setText(event.getLocation());
    }

    @Override
    public int getItemCount() {
        return eventList==null ? 0 : eventList.size();
    }

    private void loadImage(ViewHolder viewHolder, String url){
        Glide.with(viewHolder.image.getContext())
                .load(url)
                .fitCenter()
                .into(viewHolder.image);

    }

    public void add(EventInfo eventInfo){
        eventList.add(eventInfo);
    }

    public void saveData(){
        Model.getInstance().setEventArray(eventList);
        Model.getInstance().saveEventArray();
    }

    public void clear(){
        eventList.clear();
    }
}
