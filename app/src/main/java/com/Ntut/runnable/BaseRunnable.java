package com.Ntut.runnable;

import android.os.Handler;

/**
 * Created by blackmaple on 2017/5/9.
 */

public abstract class BaseRunnable implements Runnable {
    public static final int REFRESH = 1;
    public static final int ERROR = -1;
    protected Handler handler;

    public BaseRunnable(Handler handler) {
        this.handler = handler;
    }

    @Override
    public abstract void run();

    protected void sendRefreshMessage(Object obj) {
        handler.obtainMessage(REFRESH, obj).sendToTarget();
    }

    protected void sendErrorMessage(Object obj) {
        handler.obtainMessage(ERROR, obj).sendToTarget();
    }
}
