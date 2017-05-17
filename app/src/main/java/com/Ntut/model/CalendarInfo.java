package com.Ntut.model;

import java.util.Date;

/**
 * Created by blackmaple on 2017/5/9.
 */

public class CalendarInfo {
    private String event;
    private Date startDate;
    private Date endDate;

    public CalendarInfo(String event, Date startDate, Date endDate) {
        this.event = event;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CalendarInfo(String event, Date startDate) {
        this.event = event;
        this.startDate = startDate;
        this.endDate = startDate;
    }

    public String getEvent() {
        return event;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
