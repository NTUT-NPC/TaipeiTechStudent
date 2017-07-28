package com.Ntut.model;


import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by blackmaple on 2017/5/15.
 */

public class EventInfo implements Parcelable {
    private String image;
    private String title;
    private String startDate;
    private String endDate;
    private String location;
    private String host;
    private String content;
    private String url;



    protected EventInfo(Parcel in) {
        image = in.readString();
        title = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        location = in.readString();
        host = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<EventInfo> CREATOR = new Creator<EventInfo>() {
        @Override
        public EventInfo createFromParcel(Parcel source) {
            return new EventInfo(source);
        }

        @Override
        public EventInfo[] newArray(int size) {
            return new EventInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(location);
        dest.writeString(host);
        dest.writeString(content);
        dest.writeString(url);
    }

    public EventInfo(String image, String title, String startDate, String endDate, String location, String host, String content, String url) {
        this.image = image;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.host = host;
        this.content = content;
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
