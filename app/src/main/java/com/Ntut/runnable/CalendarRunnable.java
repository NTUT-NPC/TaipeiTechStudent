package com.Ntut.runnable;

import android.os.Handler;

import com.Ntut.model.YearCalendar;
import com.Ntut.utility.CalendarConnector;

/**
 * Created by blackmaple on 2017/5/12.
 */

public class CalendarRunnable extends BaseRunnable {
    public CalendarRunnable(Handler handler) {
        super(handler);
    }

    @Override
    public void run() {
        try {
            YearCalendar result = CalendarConnector.getEventList();
            sendRefreshMessage(result);
        } catch (Exception e) {
            sendErrorMessage(e.getMessage());
        }
    }
}
