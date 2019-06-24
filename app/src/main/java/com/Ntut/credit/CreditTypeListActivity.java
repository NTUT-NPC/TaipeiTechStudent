package com.Ntut.credit;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Ntut.R;
import com.Ntut.model.CreditInfo;
import com.Ntut.model.Model;
import com.Ntut.model.SemesterCredit;
import com.Ntut.utility.Utility;

import java.util.ArrayList;

/**
 * Created by blackmaple on 2017/5/15.
 */

public class CreditTypeListActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private int type;
    private LinearLayout credit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_type_list);
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        Intent i = getIntent();
        type = i.getIntExtra("type", 0);
        String type_text = getResources().getStringArray(R.array.type_name)[type - 1];
        setActionBar(type_text + "  "+getString(R.string.credit_sum)
                + Model.getInstance().getStudentCredit().getTypeCredits(type));
        credit = findViewById(R.id.credit);
        createSemesterGroups();
    }

    private void createSemesterGroups() {
        LayoutInflater inflater = getLayoutInflater();
        for (SemesterCredit semesterCredit : Model.getInstance().getStudentCredit()
                .getSemesterCredits()) {
            CreditGroupView group = new CreditGroupView(this);
            group.setGroupTitle(semesterCredit.getYear() + "-"
                    + semesterCredit.getSemester());
            group.setGroupPS("小計：" + semesterCredit.getTypeCredits(type));
            ArrayList<CreditInfo> credits = semesterCredit.getCredits();
            int i = 0;
            for (CreditInfo credit : credits) {
                if (credit.getType() == type) {
                    View item = inflater.inflate(R.layout.credit_item, null,
                            false);
                    item.setBackgroundResource(R.color.white);
                    TextView courseNo = item.findViewById(R.id.courseNo);
                    courseNo.setText(credit.getCourseNo());
                    TextView courseName = item.findViewById(R.id.courseName);
                    courseName.setText(credit.getCourseName());
                    TextView credit_text = item.findViewById(R.id.credit);
                    credit_text.setText(String.valueOf(credit.getCredit()));
                    Spinner type = item.findViewById(R.id.type);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                            this, R.layout.credit_type_textview, getResources()
                            .getStringArray(R.array.credit_type));
                    dataAdapter
                            .setDropDownViewResource(R.layout.credit_type_textview);
                    type.setAdapter(dataAdapter);
                    type.setSelection(credit.getType());
                    type.setClickable(false);
                    type.setEnabled(false);
                    TextView score = item.findViewById(R.id.score);
                    score.setText(credit.getScore());
                    group.addView(item);
                    i++;
                }
            }
            if (i > 0) {
                credit.addView(group);
            }
        }
    }

    public void setActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(v -> onBackPressed());
            actionBar.setTitle(title);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.credit_color)));
        }
        Utility.setStatusBarColor(this, getResources().getColor(R.color.credit_color));
    }
}
