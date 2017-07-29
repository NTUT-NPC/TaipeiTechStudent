package com.Ntut;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by blackmaple on 2017/7/15.
 */

public class BaseActivity extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    protected void showAlertMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("返回", null);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.create().show();
    }

    protected void showAlertMessage(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("返回", null);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.create().show();
    }
}
