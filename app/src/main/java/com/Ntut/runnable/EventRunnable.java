package com.Ntut.runnable;

import android.content.Context;
import android.os.Handler;

/**
 * Created by blackmaple on 2017/5/17.
 */

public class EventRunnable extends BaseRunnable {

    private Context context;

    public EventRunnable(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public void run() {

    }
}
