package com.Ntut.runnable;

import android.os.Handler;

import com.Ntut.utility.CourseConnector;

import java.util.ArrayList;

/**
 * Created by blackmaple on 2017/5/9.
 */

public class CourseDetailRunnable extends BaseRunnable {
    private String courseNo;

    public CourseDetailRunnable(Handler handler, String courseNo) {
        super(handler);
        this.courseNo = courseNo;
    }

    @Override
    public void run() {
        try {
            ArrayList<String> result = CourseConnector
                    .GetCourseDetail(courseNo);
            sendRefreshMessage(result);
        } catch (Exception e) {
            sendErrorMessage(e.getMessage());
        }
    }
}
