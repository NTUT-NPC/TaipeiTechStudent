package com.Ntut.credit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Ntut.BaseActivity;
import com.Ntut.R;
import com.Ntut.model.CreditInfo;
import com.Ntut.model.GeneralCredit;
import com.Ntut.model.Model;
import com.Ntut.model.SemesterCredit;
import com.Ntut.model.StandardCredit;
import com.Ntut.model.StudentCredit;
import com.Ntut.runnable.BaseRunnable;
import com.Ntut.runnable.CreditLoginRunnable;
import com.Ntut.runnable.CreditRunnable;
import com.Ntut.utility.CreditConnector;
import com.Ntut.utility.NportalConnector;
import com.Ntut.utility.SlideAnimator;
import com.Ntut.utility.Utility;
import com.Ntut.utility.WifiUtility;

import java.util.ArrayList;

/**
 * Created by Andy on 2017/4/28.
 */

public class CreditActivity extends BaseActivity implements View.OnClickListener, CreditStandardDialog.DialogListener {

    private ProgressDialog pd;
    private Thread nextThread = null;
    private LinearLayout credit;
    private CreditGroupView total_group = null;
    public static int CONTENT_ROW_HEIGHT = 100;
    private boolean isUser = false;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        CONTENT_ROW_HEIGHT = Math.round(displaymetrics.widthPixels / 8);
        setContentView(R.layout.activity_credit);
        credit = (LinearLayout) findViewById(R.id.credit);
        View start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(this);
        initView();
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        setActionBar();
    }

    private void initView() {
        View start_button = findViewById(R.id.start_button);
        StudentCredit studentCredit = Model.getInstance().getStudentCredit();
        credit.removeAllViews();
        total_group = null;
        if (studentCredit != null) {
            start_button.setVisibility(View.GONE);
            createTotalGroup(studentCredit);
            if (studentCredit.getGeneralCredits().size() > 0) {
                createGeneralGroup(studentCredit);
            }
            createSemesterGroups(studentCredit);
        } else {
            start_button.setVisibility(View.VISIBLE);
        }
    }

    public void refreshTotal() {
        StudentCredit studentCredit = Model.getInstance().getStudentCredit();
        String types[] = getResources().getStringArray(R.array.type_name);
        if (total_group != null) {
            String credit_text;
            StandardCredit standardCredit = Model.getInstance()
                    .getStandardCredit();
            if (standardCredit != null) {
                credit_text = String.valueOf(studentCredit.getTotalCredits())
                        + " / " + standardCredit.getCredits().get(7);
            } else {
                credit_text = String.valueOf(studentCredit.getTotalCredits());
            }
            total_group.setGroupPS(getString(R.string.credit) + credit_text);
            total_group.removeAllViews();
            for (int i = 1; i < 7; i++) {
                LayoutInflater inflater = LayoutInflater.from(this);
                TextView text = (TextView) inflater.inflate(
                        R.layout.credit_textview, null, false);
                text.setBackgroundResource(R.color.white);
                if (standardCredit != null) {
                    credit_text = String.valueOf(studentCredit
                            .getTypeCredits(i))
                            + " / "
                            + standardCredit.getCredits().get(i - 1);
                } else {
                    credit_text = String.valueOf(studentCredit
                            .getTypeCredits(i));
                }
                text.setText(types[i - 1] + "：" + credit_text);
                text.setTag(i);
                if (studentCredit.getTypeCredits(i) > 0) {
                    text.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(),
                                    CreditTypeListActivity.class);
                            intent.putExtra("type", (Integer) view.getTag());
                            startActivity(intent);
                        }
                    });
                }
                total_group.addView(text);
            }
        }
    }

    private void createTotalGroup(StudentCredit studentCredit) {
        total_group = new CreditGroupView(this);
        total_group.setGroupTitle(getString(R.string.overview_credit));
        refreshTotal();
        credit.addView(total_group);
    }

    private void createGeneralGroup(StudentCredit studentCredit) {
        CreditGroupView group = new CreditGroupView(this);
        group.setGroupTitle(getString(R.string.overview_general));
        group.setGroupPS(getString(R.string.credit_general_core) + studentCredit.getGeneralCoreCredits()
                + "  "+getString(R.string.credit_general_elective) + studentCredit.getGeneralCommonCredits());
        int count = studentCredit.getGeneralCredits().size();
        //String[] titles = new String[count];
        String[] titles = {getString(R.string.cultural_dimension), getString(R.string.historical_dimension),
                getString(R.string.philosophical_dimension), getString(R.string.legal_dimension), getString(R.string.social_dimension),
                getString(R.string.natural_dimension), getString(R.string.social_philosophy_dimension), getString(R.string.creative_dimension), getString(R.string.aesthetic_dimension), getString(R.string.literary_history_dimension)};
        float[] totals = new float[count];
        float[] cores = new float[count];
        int i = 0;
        for (GeneralCredit general : studentCredit.getGeneralCredits()) {
            //titles[i] = general.getTypeName();
            totals[i] = general.getHadCoreCredit()
                    + general.getHadCommonCredit();
            cores[i] = general.getHadCoreCredit();
            i++;
        }
        int width = (int) (Utility.getScreenWidth(this) * 0.95);
        LinearLayout.LayoutParams chart_params = new LinearLayout.LayoutParams(width, width);
        chart_params.gravity = Gravity.CENTER_HORIZONTAL;
        RadarChartView radar_chart = new RadarChartView(this, count,
                titles);
        radar_chart.setId(R.id.radar_chart);
        radar_chart.setDuration(700);
        radar_chart.setOnClickListener(this);
        radar_chart.setTotalValues(totals);
        radar_chart.setCoreValues(cores);
        radar_chart.setLayoutParams(chart_params);
        group.addView(radar_chart);
        group.setSlideAnimationListener(new SlideAnimator.SlideAnimationListener() {

            @Override
            public void onSlidedUp(View v) {
                RadarChartView radar_chart = (RadarChartView) v
                        .findViewById(R.id.radar_chart);
                if (radar_chart != null) {
                    radar_chart.resetAnimation();
                }
            }

            @Override
            public void onSlidedDown(View v) {
                RadarChartView radar_chart = (RadarChartView) v
                        .findViewById(R.id.radar_chart);
                if (radar_chart != null) {
                    radar_chart.startAnimation();
                }
            }
        });
        credit.addView(group);
    }

    private void createSemesterGroups(StudentCredit studentCredit) {
        LayoutInflater inflater = getLayoutInflater();
        for (SemesterCredit semesterCredit : studentCredit.getSemesterCredits()) {
            CreditGroupView group = new CreditGroupView(this);
            group.setGroupTitle(semesterCredit.getYear() + "-"
                    + semesterCredit.getSemester());
            ArrayList<CreditInfo> credits = semesterCredit.getCredits();
            for (int i = 0; i < credits.size(); i++) {
                View item = inflater.inflate(R.layout.credit_item, null, false);
                item.setBackgroundResource(R.color.white);
                CreditInfo creditInfo = credits.get(i);

                TextView coursNo = (TextView) item.findViewById(R.id.courseNo);
                coursNo.setText(creditInfo.getCourseNo());
                TextView coursName = (TextView) item
                        .findViewById(R.id.courseName);
                coursName.setText(creditInfo.getCourseName());
                coursName.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            Toast.makeText(v.getContext(),
                                    ((TextView) v).getText(),
                                    Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                TextView credit = (TextView) item.findViewById(R.id.credit);
                credit.setText(String.valueOf(creditInfo.getCredit()));
                Spinner type = (Spinner) item.findViewById(R.id.type);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                        this, R.layout.credit_type_textview,
                        getResources().getStringArray(R.array.credit_type));
                dataAdapter
                        .setDropDownViewResource(R.layout.credit_type_textview);
                type.setAdapter(dataAdapter);
                type.setOnItemSelectedListener(iSlis);
                type.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            isUser = true;
                        }
                        return false;
                    }
                });
                type.setTag(creditInfo);
                type.setSelection(creditInfo.getType());
                TextView score = (TextView) item.findViewById(R.id.score);
                score.setText(String.valueOf(credits.get(i).getScore()));
                group.addView(item);
            }
            credit.addView(group);
        }
    }

    private void loginNportal() {
        String account = Model.getInstance().getAccount();
        String password = Model.getInstance().getPassword();
        if (account.length() > 0 && password.length() > 0) {
            NportalConnector.login(account, password, loginHandler);
        } else {
            pd.dismiss();
            showAlertMessage(getString(R.string.none_account_error));
        }
    }

    private Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseRunnable.REFRESH:
                    if (nextThread != null) {
                        nextThread.start();
                        nextThread = null;
                    } else {
                        pd.dismiss();
                    }
                    break;
                case BaseRunnable.ERROR:
                    pd.dismiss();
                    showAlertMessage(getString(R.string.hint), (String) msg.obj);
            }
        }
    };

    private Handler creditLoginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseRunnable.REFRESH:
                    Thread t = new Thread(new CreditRunnable(creditHandler,
                            progressHandler));
                    t.start();
                    break;
                case BaseRunnable.ERROR:
                    pd.dismiss();
                    showAlertMessage(getString(R.string.hint), (String) msg.obj);
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler creditHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseRunnable.REFRESH:
                    if (msg.obj instanceof StudentCredit) {
                        StudentCredit result = (StudentCredit) msg.obj;
                        Model.getInstance().setStudentCredit(result);
                        Model.getInstance().saveStudentCredit();
                        initView();
                        pd.dismiss();
                        if (CreditConnector.isHaveError) {
                            showAlertMessage(getString(R.string.credit_imformation_complete), getString(R.string.credit_final)+"\n"+getString(R.string.credit_error));

                        } else {
                            CreditStandardDialog dialog = new CreditStandardDialog(
                                    CreditActivity.this, Model.getInstance().getStandardCredit());
                            dialog.setDialogListener(CreditActivity.this);
                            dialog.show();
                            showAlertMessage(getString(R.string.credit_imformation_complete), getString(R.string.credit_final));
                        }
                    }
                    break;
                case BaseRunnable.ERROR:
                    pd.dismiss();
                    Utility.showDialog(getString(R.string.hint), (String) msg.obj, getApplicationContext());
                    break;
            }
        }
    };

    public Handler progressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (msg.obj instanceof Integer) {
                        int i = (Integer) msg.obj;
                        pd.setProgress(i);
                    }
                    break;
                case 1:
                    if (msg.obj instanceof Integer) {
                        int count = (Integer) msg.obj;
                        pd.setMax(count);
                    }
                    break;
            }
        }
    };

    private void queryCredit() {
        if (WifiUtility.isNetworkAvailable(this)) {
            if(Utility.checkAccount(this)) {
                pd = new ProgressDialog(this);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setProgress(0);
                pd.setTitle(getString(R.string.credit_updating));
                pd.setCancelable(false);
                pd.show();
                nextThread = new Thread(new CreditLoginRunnable(creditLoginHandler));
                loginNportal();
            }
        } else {
            Toast.makeText(this, R.string.check_network_available,
                    Toast.LENGTH_LONG).show();
        }
    }

    private AdapterView.OnItemSelectedListener iSlis = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View v, int position,
                                   long arg3) {
            if (isUser) {
                CreditInfo creditInfo = (CreditInfo) ((View) v.getParent())
                        .getTag();
                if (creditInfo != null) {
                    creditInfo.setType(position);
                    Model.getInstance().saveStudentCredit();
                    refreshTotal();
                }
            }
            isUser = false;
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_button:
                queryCredit();
                break;
            default:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.overview_general));
                View view = new CreditGeneralView(this, Model
                        .getInstance().getStudentCredit());
                builder.setView(view);
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.back, null);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
    }

    @Override
    public void onSaveButtonClick(StandardCredit standardCredit) {
        Model.getInstance().setStandardCredit(standardCredit);
        Model.getInstance().saveStandardCredit();
        refreshTotal();
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
            actionBar.setTitle(R.string.credit_text);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.credit_color)));
        }
        Utility.setStatusBarColor(this, getResources().getColor(R.color.credit_color));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_credit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_query:
                queryCredit();
                break;
            case R.id.item_standard_credit:
                CreditStandardDialog dialog = new CreditStandardDialog(
                        this, Model.getInstance().getStandardCredit());
                dialog.setDialogListener(CreditActivity.this);
                dialog.show();
                break;
            case R.id.item_clear:
                Model.getInstance().deleteStudentCredit();
                initView();
                break;
            case R.id.item_clear_standard_credit:
                Model.getInstance().deleteStandardCredit();
                refreshTotal();
                break;
        }
        return true;
    }
}
