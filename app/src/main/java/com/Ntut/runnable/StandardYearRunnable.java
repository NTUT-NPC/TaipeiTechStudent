package com.Ntut.runnable;

import android.os.Handler;

import com.Ntut.utility.CreditConnector;

import java.util.ArrayList;

/**
 * Created by blackmaple on 2017/5/15.
 */

public class StandardYearRunnable extends BaseRunnable {
    public StandardYearRunnable(Handler handler) {
        super(handler);
    }

    @Override
    public void run() {
        try {
            ArrayList<String> result = CreditConnector.getYearList();
            sendRefreshMessage(result);
        } catch (Exception e) {
            sendErrorMessage(e.getMessage());
        }
    }
}
