package com.Ntut.runnable;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.Ntut.MainActivity;
import com.Ntut.account.AccountActivity;

/**
 * Created by blackmaple on 2017/5/13.
 */

public class AccountRunnable extends BaseRunnable {
    private final AccountActivity context;

    public AccountRunnable(Handler handler, Context context) {
        super(handler);
         this.context = (AccountActivity) context;
    }

    @Override
    public void run() {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
