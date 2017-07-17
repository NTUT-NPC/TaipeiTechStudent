package com.Ntut.feedback;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Ntut.BaseActivity;
import com.Ntut.R;
import com.Ntut.runnable.BaseRunnable;
import com.Ntut.utility.Utility;
import com.Ntut.utility.WifiUtility;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;
import java.util.Calendar;

public class FeedbackActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener {
    Button send_button;
    EditText edt_feedback;
    EditText edt_contact_imformation;
    String feedback, contact_imformation;
    private static final String NO_MESSAGE = "NO_MESSAGE";
    private static final String NO_CONTACT_INFORMATION = "NO_CONTACT_INFORMATION";
    private static final String OK = "OK";
    private Toolbar mToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feedback);
        edt_feedback = (EditText) findViewById(R.id.feedback);
        edt_contact_imformation = (EditText) findViewById(R.id.contact_imformatoin);
        initButton();
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        setActionBar();
    }

    private void initButton(){
        send_button = (Button) findViewById(R.id.send_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(WifiUtility.isNetworkAvailable(getApplicationContext())){
                    switch (setMessage()) {
                        case NO_MESSAGE:
                            Toast.makeText(getApplicationContext(), R.string.feedback_blank_forbid,
                                    Toast.LENGTH_LONG).show();
                            return;
                        case NO_CONTACT_INFORMATION:
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
                            alertDialog.setMessage(R.string.feedback_check);
                            alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    writeDataBase(getMessage());
                                    initFeedback();
                                    Utility.showDialog(getString(R.string.feedback_send_suceed),
                                            getString(R.string.feedback_finish), getApplicationContext());
                                }
                            });
                            alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    return;
                                }
                            });
                            alertDialog.show();
                            break;
                        case OK:
                            writeDataBase(getMessage());
                            initFeedback();
                            Utility.showDialog(getString(R.string.feedback_send_suceed),
                                    getString(R.string.feedback_finish), getApplicationContext());
                            break;
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.check_network_available,
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void initFeedback() {
        edt_feedback.setText("");
        feedback = null;
        contact_imformation = null;
        edt_contact_imformation.setText("");
    }

    static class FeedbackHandler extends Handler {
        private WeakReference<FeedbackActivity> activityRef;

        public FeedbackHandler(FeedbackActivity fragment) {
            activityRef = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseRunnable.ERROR:
                    FeedbackActivity activity = activityRef.get();
                    if (activity != null) {
                        Utility.showDialog("提示", (String) msg.obj, activity.getApplicationContext());
                    }
                    break;
            }
        }
    }

    private String setMessage() {
        feedback = String.valueOf(edt_feedback.getText());
        contact_imformation = String.valueOf(edt_contact_imformation.getText());
        if(feedback.isEmpty()){
            return NO_MESSAGE;
        }
        else if (contact_imformation.isEmpty()){
            return NO_CONTACT_INFORMATION;
        }
        else {
            return OK;
        }
    }

    private String getMessage(){
        return (feedback+"，"+contact_imformation);
    }

    private void writeDataBase(String message){
        // Write a message to the database
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Calendar mCal = Calendar.getInstance();
        CharSequence s = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());
        DatabaseReference myRef = database.getReference((String) s);
        myRef.setValue(message);
    }

    public void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            actionBar.setTitle(R.string.feedback_text);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
        }
        Utility.setStatusBarColor(this, getResources().getColor(R.color.blue));
    }

    @Override
    public void onRefresh() {

    }
}
