package com.Ntut.runnable;

import android.os.Handler;

/**
 * Created by blackmaple on 2017/5/9.
 */

public class CourseTableSearchRunnable extends BaseRunnable {

    public CourseTableSearchRunnable(Handler handler, String sid, String year,
                                     String semester) {
        super(handler);
        String sid1 = sid;
        String year1 = year;
        String semester1 = semester;
    }

    @Override
    public void run() {

    }
}
