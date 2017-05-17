package com.Ntut.runnable;

import android.os.Handler;

import com.Ntut.utility.CreditConnector;

/**
 * Created by blackmaple on 2017/5/15.
 */

public class CreditLoginRunnable extends BaseRunnable {
    public CreditLoginRunnable(Handler handler) {
        super(handler);
    }

    @Override
    public void run() {
        try {
            String result = CreditConnector.loginCredit();
            sendRefreshMessage(result);
        } catch (Exception e) {
            sendErrorMessage(e.getMessage());
        }
    }
}
