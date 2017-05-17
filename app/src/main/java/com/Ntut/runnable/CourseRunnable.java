package com.Ntut.runnable;

import android.os.Handler;

import com.Ntut.model.StudentCourse;
import com.Ntut.utility.CourseConnector;

/**
 * Created by blackmaple on 2017/5/9.
 */

public class CourseRunnable extends BaseRunnable {
    private String sid;
    private String year;
    private String semester;

    public CourseRunnable(Handler handler, String sid, String year,
                          String semester) {
        super(handler);
        this.sid = sid;
        this.year = year;
        this.semester = semester;
    }

    @Override
    public void run() {
        try {
            StudentCourse result = CourseConnector.getStudentCourse(sid, year,
                    semester);
            sendRefreshMessage(result);
        } catch (Exception e) {
            sendErrorMessage(e.getMessage());
        }
    }
}
