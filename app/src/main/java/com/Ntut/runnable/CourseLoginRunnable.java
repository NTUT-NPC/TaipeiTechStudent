package com.Ntut.runnable;

import android.os.Handler;

import com.Ntut.utility.CourseConnector;

/**
 * Created by blackmaple on 2017/5/9.
 */

public class CourseLoginRunnable extends BaseRunnable {
    public CourseLoginRunnable(Handler handler) {
        super(handler);
    }

    @Override
    public void run() {
        try {
            String result = CourseConnector.loginCourse();
            sendRefreshMessage(result);
        } catch (Exception e) {
            sendErrorMessage(e.getMessage());
        }
    }
}
