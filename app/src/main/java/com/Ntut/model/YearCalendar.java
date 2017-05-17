package com.Ntut.model;

import com.Ntut.utility.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by blackmaple on 2017/5/9.
 */

public class YearCalendar {
    private ArrayList<CalendarInfo> eventList = null;
    private String semester = null;

    public ArrayList<CalendarInfo> getEventList() {
        return eventList;
    }

    public void setEventList(ArrayList<CalendarInfo> eventList) {
        this.eventList = eventList;
    }

    public int getYear() {
        return Integer.parseInt(semester) + 1911;
    }

    public String getSemesterYear() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public ArrayList<CalendarInfo> searchEventList(String keyword) {
        ArrayList<CalendarInfo> resultList = new ArrayList<CalendarInfo>();
        if (eventList != null && keyword != null) {
            for (CalendarInfo calendarInfo : eventList) {
                if (calendarInfo.getEvent().contains(keyword)) {
                    resultList.add(calendarInfo);
                }
            }
        }
        return resultList;
    }

    public ArrayList<CalendarInfo> getMonthEventList(String year, String month) {
        ArrayList<CalendarInfo> resultList = new ArrayList<CalendarInfo>();
        if (eventList != null && month != null) {
            for (CalendarInfo calendarInfo : eventList) {
                if (Utility.getMonth(calendarInfo.getStartDate()).equals(month)
                        && Utility.getYear(calendarInfo.getStartDate()).equals(
                        year)) {
                    resultList.add(calendarInfo);
                }
            }
        }
        return resultList;
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month + 1, day, 0, 0, 0);
        Date date = cal.getTime();
        return date;
    }

    public static Date getDate(String date_string) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);
        Date date = sdf.parse(date_string);
        return date;
    }

    public ArrayList<String> findEvents(Date date) {
        ArrayList<String> resultList = new ArrayList<String>();
        if (eventList != null && date != null) {
            for (CalendarInfo calendarInfo : eventList) {
                if (calendarInfo.getStartDate().equals(date)) {
                    resultList.add(calendarInfo.getEvent());
                }
            }
        }
        return resultList;
    }
}
