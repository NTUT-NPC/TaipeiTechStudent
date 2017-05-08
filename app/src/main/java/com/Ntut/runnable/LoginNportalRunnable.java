package com.Ntut.runnable;

import android.graphics.Bitmap;
import android.os.Handler;

import com.Ntut.utility.NportalConnector;
import com.Ntut.utility.OCRUtility;

/**
 * Created by blackmaple on 2017/5/9.
 */

public class LoginNportalRunnable extends BaseRunnable {
    String account;
    String password;

    public LoginNportalRunnable(String account, String password, Handler handler) {
        super(handler);
        this.account = account;
        this.password = password;
    }

    @Override
    public void run() {
        try {
            Bitmap bmp = NportalConnector.loadAuthcodeImage();
            String authcode = OCRUtility.authOCR(
                    OCRUtility.bitmap2grayByteArry(bmp), bmp.getWidth(),
                    bmp.getHeight());
            String result = NportalConnector.login(account, password, authcode);
            sendRefreshMessage(result);
        } catch (Exception e) {
            sendErrorMessage(e.getMessage());
        }
    }
}
