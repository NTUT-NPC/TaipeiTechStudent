package com.Ntut.course;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.Ntut.BaseActivity;
import com.Ntut.R;
import com.Ntut.runnable.ClassmateRunnable;
import com.Ntut.runnable.CourseDetailRunnable;
import com.Ntut.utility.Utility;

import java.util.ArrayList;

/**
 * Created by blackmaple on 2017/5/9.
 */

public class CourseDetailActivity extends BaseActivity {
    Toolbar mToolbar;
    ProgressDialog mProgressDialog;
    String courseNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail);
        Intent i = getIntent();
        courseNo = i.getStringExtra("CourseNo");
        if (courseNo == null) {
            finish();
            return;
        }
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        setActionBar();
        mProgressDialog = ProgressDialog.show(this, null, "課程資料讀取中~", true);
        Thread t = new Thread(new CourseDetailRunnable(courseDetailHandler,
                courseNo));
        t.start();
    }

    public void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(v -> onBackPressed());
            actionBar.setTitle("課程資訊");
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getBaseContext(), R.color.course_color)));
        }
        setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.course_color));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            if (color == Color.BLACK
                    && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setStatusBarColor(color);
        }
    }

    private void showCourseDetail(ArrayList<String> courseDetail) {
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout courseInfo = findViewById(R.id.courseInfo);
        for (int i = 0; i < courseDetail.size(); i++) {
            if (i == 1 || i == 2 || i == 4 || i == 6 || i == 13 || i == 14
                    || i == 15 || i == 16 || i == 17) {

            } else {
                LinearLayout item = (LinearLayout) li.inflate(
                        R.layout.course_item, courseInfo, false);
                TextView text = item.findViewById(R.id.text);
                text.setTextColor(getResources().getColor(R.color.darken));
                text.setText(courseDetail.get(i));
                courseInfo.addView(item);
            }
        }
    }

    private void showClassmates(ArrayList<String> classmateList) {
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout classmates = findViewById(R.id.classmates);
        for (int i = 0; i < classmateList.size(); i++) {
            LinearLayout classmate = (LinearLayout) li.inflate(
                    R.layout.classmate_item, classmates, false);
            LinearLayout item = classmate.findViewById(R.id.classmate_item);
            if (i % 2 == 1) {
                item.setBackgroundResource(R.color.cloud);
            } else {
                item.setBackgroundResource(R.color.white);
            }
            String[] temp = classmateList.get(i).split(",");
            TextView text = classmate.findViewById(R.id.sclass);
            text.setText(temp[0]);
            text = classmate.findViewById(R.id.sid);
            text.setText(temp[1]);
            text = classmate.findViewById(R.id.sname);
            text.setText(temp[2]);
            Button submit = classmate.findViewById(R.id.submit);
            submit.setTag(temp[1]);
            submit.setOnClickListener(v -> {
                String sid = (String) v.getTag();
                Intent intent = new Intent();
                intent.putExtra("sid", sid);
                setResult(RESULT_OK, intent);
                finish();
            });
            classmates.addView(classmate);

        }
    }

    private Handler courseDetailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CourseDetailRunnable.REFRESH:
                    ArrayList<String> result = Utility.castListObject(msg.obj,
                            String.class);
                    if (result != null) {
                        showCourseDetail(result);
                        Thread t = new Thread(new ClassmateRunnable(
                                classmateHandler, courseNo));
                        t.start();
                    }
                    break;
                case CourseDetailRunnable.ERROR:
                    dismissProgressDialog();
                    showAlertMessage("查詢課程詳細資料時發生錯誤，請重新查詢！");
                    break;
            }
        }
    };
    private Handler classmateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dismissProgressDialog();
            switch (msg.what) {
                case ClassmateRunnable.REFRESH:
                    ArrayList<String> result = Utility.castListObject(msg.obj,
                            String.class);
                    if (result != null) {
                        showClassmates(result);
                    }
                    break;
                case ClassmateRunnable.ERROR:
                    showAlertMessage("查詢課程詳細資料時發生錯誤，請重新查詢！");
                    break;
            }
        }
    };

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            if (!isFinishing()) {
                mProgressDialog.dismiss();
            }
        }
    }
}
