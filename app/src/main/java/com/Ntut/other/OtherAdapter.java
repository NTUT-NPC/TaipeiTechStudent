package com.Ntut.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.Ntut.MainActivity;
import com.Ntut.R;
import com.Ntut.account.AccountActivity;
import com.Ntut.credit.CreditActivity;
import com.Ntut.etc.EtcActivity;
import com.Ntut.feedback.FeedbackActivity;
import com.Ntut.model.OtherInfo;

import java.util.List;

/**
 * Created by blackmaple on 2017/5/14.
 */

public class OtherAdapter extends RecyclerView.Adapter<OtherAdapter.ViewHolder> {

    private List<OtherInfo> otherInfos;
    private MainActivity context;

    public OtherAdapter(List<OtherInfo> otherInfos, Context context){
        this.otherInfos = otherInfos;
        this.context = (MainActivity) context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView otherText;
        public ImageView imageView;
        public  ViewHolderOnClick listener;
        public  ViewHolderOnClick viewHolderOnClick;
        public ViewHolder(View itemView, ViewHolderOnClick listener){
            super(itemView);
            otherText = (TextView) itemView.findViewById(R.id.other_text);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getLayoutPosition());
        }

        public interface ViewHolderOnClick{
            void onClick(View v, int position);
        }
    }

    @Override
    public OtherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView = LayoutInflater.from(context).inflate(R.layout.other_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView, new ViewHolder.ViewHolderOnClick() {
            @Override
            public void onClick(View v, int position) {
                Intent intent;
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation((Activity) context, v, "other");
                switch (position){
                    case 0:
                        intent = new Intent(new Intent(context, CreditActivity.class));
                        context.startActivity(intent, options.toBundle());
                        break;
                    case 1:
                        intent = new Intent(new Intent(context, AccountActivity.class));
                        context.startActivity(intent, options.toBundle());
                        break;
                    case 2:
                        intent = new Intent(new Intent(context, FeedbackActivity.class));
                        context.startActivity(intent, options.toBundle());
                        break;
                    case 3:
                        intent = new Intent(new Intent(context, EtcActivity.class));
                        context.startActivity(intent, options.toBundle());
                        break;
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OtherAdapter.ViewHolder holder, int position) {
        OtherInfo otherInfo = this.otherInfos.get(position);
        TextView otherText = holder.otherText;
        otherText.setText(otherInfo.getTitle());
//        ImageView imageView = holder.imageView;
//        imageView.setImageResource(otherInfo.getIconId());
    }

    @Override
    public int getItemCount() {
        return otherInfos.size();
    }


}
