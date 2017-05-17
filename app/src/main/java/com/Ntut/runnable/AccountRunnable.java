package com.Ntut.runnable;

import android.content.Context;
import android.os.Handler;

import com.Ntut.MainActivity;
import com.Ntut.course.CourseFragment;

/**
 * Created by blackmaple on 2017/5/13.
 */

public class AccountRunnable extends BaseRunnable {
    private final MainActivity context;

    public AccountRunnable(Handler handler, Context context) {
        super(handler);
        this.context = (MainActivity) context;
    }

    @Override
    public void run() {
        context.changeFragment(new CourseFragment());
    }
}
