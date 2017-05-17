package com.Ntut.runnable;

import android.os.Handler;

import com.Ntut.utility.CreditConnector;

import java.util.ArrayList;

/**
 * Created by blackmaple on 2017/5/15.
 */

public class StandardDivisionRunnable extends BaseRunnable {
    private String year;

    public StandardDivisionRunnable(Handler handler, String year) {
        super(handler);
        this.year = year;
    }

    @Override
    public void run() {
        try {
            ArrayList<String> result = CreditConnector.getDivisionList(year);
            sendRefreshMessage(result);
        } catch (Exception e) {
            sendErrorMessage(e.getMessage());
        }
    }
}
