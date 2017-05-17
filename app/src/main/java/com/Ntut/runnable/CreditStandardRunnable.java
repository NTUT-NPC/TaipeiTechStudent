package com.Ntut.runnable;

import android.os.Handler;

import com.Ntut.utility.CreditConnector;

import java.util.ArrayList;

/**
 * Created by blackmaple on 2017/5/15.
 */

public class CreditStandardRunnable extends BaseRunnable {
    private String year;
    private String department;
    private int index;

    public CreditStandardRunnable(Handler handler, String year, int index,
                                  String department) {
        super(handler);
        this.year = year;
        this.index = index;
        this.department = department;
    }

    @Override
    public void run() {
        try {
            ArrayList<String> result = CreditConnector.getStandardCredit(year,
                    index, department);
            sendRefreshMessage(result);
        } catch (Exception e) {
            sendErrorMessage(e.getMessage());
        }
    }
}
