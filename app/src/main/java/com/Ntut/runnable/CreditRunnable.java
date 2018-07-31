package com.Ntut.runnable;

import android.os.Handler;
import android.util.Log;

import com.Ntut.model.StudentCredit;
import com.Ntut.utility.CreditConnector;

/**
 * Created by blackmaple on 2017/5/15.
 */

public class CreditRunnable extends BaseRunnable {
    Handler progressHandler;

    public CreditRunnable(Handler handler, Handler progressHandler) {
        super(handler);
        this.progressHandler = progressHandler;
    }

    @Override
    public void run() {
        try {
            StudentCredit result = CreditConnector.getCredits(progressHandler);
            sendRefreshMessage(result);
        } catch (Exception e) {
            sendErrorMessage(e.getMessage());
        }
    }
}
