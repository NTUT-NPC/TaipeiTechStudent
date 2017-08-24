package com.Ntut.credit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Ntut.R;
import com.Ntut.model.StandardCredit;
import com.Ntut.runnable.BaseRunnable;
import com.Ntut.runnable.CreditStandardRunnable;
import com.Ntut.runnable.StandardDepartmentRunnable;
import com.Ntut.runnable.StandardDivisionRunnable;
import com.Ntut.runnable.StandardYearRunnable;
import com.Ntut.utility.Utility;
import com.Ntut.utility.WifiUtility;

import java.util.ArrayList;

import static com.Ntut.MainApplication.lang;

/**
 * Created by blackmaple on 2017/5/15.
 */

public class CreditStandardDialog extends AlertDialog implements
        View.OnClickListener, DialogInterface.OnShowListener {
    private View contentView;
    private String year = null;
    private int division_index;
    private MenuSpinner year_list;
    private ArrayAdapter<String> year_adapter;
    private MenuSpinner division_list;
    private ArrayAdapter<String> division_adapter;
    private MenuSpinner department_list;
    private ArrayAdapter<String> department_adapter;
    private ArrayList<String> years = new ArrayList<>();
    private ArrayList<String> divisions = new ArrayList<>();
    private ArrayList<String> departments = new ArrayList<>();
    private Boolean isUser = false;
    private StandardCredit standardCredit = null;
    private Boolean isCorrect = false;
    private ProgressDialog progressDialog;
    private DialogListener dialogListener;

    public CreditStandardDialog(Context context, StandardCredit standardCredit) {
        super(context);
            setTitle(context.getString(R.string.credit_setting_graduation));
        setView(getView(standardCredit));
        setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel), (DialogInterface.OnClickListener) null);
        setButton(BUTTON_POSITIVE, context.getString(R.string.save), (DialogInterface.OnClickListener) null);
        setOnShowListener(this);
    }

    public void setDialogListener(DialogListener listener) {
        dialogListener = listener;
    }

    private View getView(StandardCredit standardCredit) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        contentView = inflater.inflate(R.layout.credit_standard_dialog, null);
        if (standardCredit != null) {
            years.add(standardCredit.getYearText());
            divisions.add(standardCredit.getDivisionText());
            departments.add(standardCredit.getDepartmentText());
        } else {
            years.add(contentView.getContext().getString(R.string.credit_choose_enter_semester));
        }
        year_list = (MenuSpinner) contentView.findViewById(R.id.year_list);
        year_adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, years);
        year_list.setAdapter(year_adapter);
        year_adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_list
                .setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        isCorrect = false;
                        if (years.size() == 1) {
                            return;
                        }
                        if (isUser) {
                            progressDialog = ProgressDialog.show(getContext(),
                                    null, getContext().getString(R.string.credit_loading_academic_system), true);
                            if(lang.equals("zh") || lang.equals("ja"))
                                year = years.get(position).split(" ")[1];
                            else
                                year = years.get(position).split(" ")[2];
//                            year = years.get(position).split(" ")[1];
                            Thread t = new Thread(new StandardDivisionRunnable(
                                    divisionHandler, year));
                            t.start();
                            lockSpinner(division_list, true);
                            lockSpinner(department_list, true);
                        }
                        isUser = false;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        year_list.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (WifiUtility.isNetworkAvailable(getContext())) {
                        progressDialog = ProgressDialog.show(getContext(),
                                null, getContext().getString(R.string.credit_loading_semester_list), true);
                        Thread t = new Thread(new StandardYearRunnable(
                                yearHandler));
                        t.start();
                        isUser = false;
                    } else {
                        Toast.makeText(getContext(),
                                R.string.check_network_available,
                                Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            }
        });

        division_list = (MenuSpinner) contentView
                .findViewById(R.id.division_list);
        division_adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, divisions);
        division_list.setAdapter(division_adapter);
        division_adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        division_list
                .setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                        isCorrect = false;
                        if (isUser) {
                            progressDialog = ProgressDialog.show(getContext(),
                                    null, getContext().getString(R.string.credit_loading_faculty_list), true);
                            division_index = position;
                            Thread t = new Thread(
                                    new StandardDepartmentRunnable(
                                            departmentHandler, year,
                                            division_index));
                            t.start();
                            lockSpinner(department_list, true);
                        }
                        isUser = false;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        lockSpinner(division_list, true);

        department_list = (MenuSpinner) contentView
                .findViewById(R.id.department_list);
        department_adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, departments);
        department_list.setAdapter(department_adapter);
        department_adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department_list
                .setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                        isCorrect = false;
                        if (isUser) {
                            progressDialog = ProgressDialog.show(getContext(),
                                    null, getContext().getString(R.string.credit_loading_graduation_credits), true);
                            Thread t = new Thread(new CreditStandardRunnable(
                                    creditsHandler, year, division_index,
                                    departments.get(position)));
                            t.start();
                        }
                        isUser = false;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
        lockSpinner(department_list, true);
        showStandardCredits(standardCredit);
        return contentView;
    }

    private void lockSpinner(MenuSpinner spinner, boolean isLock) {
        if (isLock) {
            spinner.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        } else {
            spinner.setOnTouchListener(null);
        }
    }

    private void showStandardCredits(StandardCredit standardCredit) {
        if (standardCredit != null) {
            String types[] = getContext().getResources().getStringArray(
                    R.array.type_name);
            LinearLayout container = (LinearLayout) contentView
                    .findViewById(R.id.container);
            container.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            for (int i = 0; i < 8; i++) {
                TextView text = new TextView(getContext());
                text.setLayoutParams(params);
                text.setTextAppearance(getContext(),
                        android.R.style.TextAppearance_Medium);
                text.setTextColor(getContext().getResources().getColor(
                        R.color.darken));
                text.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                if (i % 2 == 0) {
                    text.setBackgroundResource(R.color.cloud);
                } else {
                    text.setBackgroundResource(android.R.color.transparent);
                }
                text.setText(types[i] + "："
                        + String.valueOf(standardCredit.getCredits().get(i)));
                container.addView(text);
            }
        }
    }

    private ArrayList<String> castStringArray(Object object) {
        if (!(object instanceof ArrayList)) {
            return null;
        }
        ArrayList<?> list = (ArrayList<?>) object;
        ArrayList<String> temp = new ArrayList<>();
        for (Object ob : list) {
            if (ob instanceof String) {
                temp.add((String) ob);
            } else if (ob == null) {
                temp.add(null);
            } else {
                return null;
            }
        }
        return temp;
    }

    private Handler yearHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseRunnable.REFRESH:
                    if (msg.obj instanceof ArrayList<?>) {
                        ArrayList<String> result = castStringArray(msg.obj);
                        years.clear();
                        years.addAll(result);
                        year_adapter.notifyDataSetChanged();
                        year_list.setOnTouchListener(null);
                        year_list.setSelection(0);
                        year_list.performClick();
                    }
                    break;
                case BaseRunnable.ERROR:
                    Utility.showDialog(getContext().getString(R.string.hint), (String) msg.obj, getContext());
                    break;
            }
            progressDialog.dismiss();
            isUser = true;
        }
    };

    private Handler divisionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseRunnable.REFRESH:
                    if (msg.obj instanceof ArrayList<?>) {
                        ArrayList<String> result = castStringArray(msg.obj);
                        divisions.clear();
                        divisions.addAll(result);
                        division_adapter.notifyDataSetChanged();
                        division_list.setOnTouchListener(null);
                        division_list.setSelection(0);
                    }
                    break;
                case BaseRunnable.ERROR:
                    Utility.showDialog(getContext().getString(R.string.hint), (String) msg.obj, getContext());
                    break;
            }
            progressDialog.dismiss();
            isUser = true;
        }
    };

    private Handler departmentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseRunnable.REFRESH:
                    if (msg.obj instanceof ArrayList<?>) {
                        ArrayList<String> result = castStringArray(msg.obj);
                        departments.clear();
                        departments.addAll(result);
                        department_adapter.notifyDataSetChanged();
                        department_list.setOnTouchListener(null);
                        department_list.setSelection(0);
                    }
                    break;
                case BaseRunnable.ERROR:
                    Utility.showDialog(getContext().getString(R.string.hint), (String) msg.obj, getContext());
                    break;
            }
            progressDialog.dismiss();
            isUser = true;
        }
    };

    private Handler creditsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseRunnable.REFRESH:
                    if (msg.obj instanceof ArrayList<?>) {
                        ArrayList<String> result = castStringArray(msg.obj);
                        standardCredit = new StandardCredit();
                        standardCredit.setCredits(result);
                        standardCredit.setYearText((String) year_list
                                .getSelectedItem());
                        standardCredit.setDivisionText((String) division_list
                                .getSelectedItem());
                        standardCredit.setDepartmentText((String) department_list
                                .getSelectedItem());
                        showStandardCredits(standardCredit);
                        isCorrect = true;
                    }
                    break;
                case BaseRunnable.ERROR:
                    Utility.showDialog(getContext().getString(R.string.hint), (String) msg.obj, getContext());
                    break;
            }
            progressDialog.dismiss();
            isUser = true;
        }
    };

    @Override
    public void onClick(View v) {
        if (standardCredit != null && isCorrect) {
            dialogListener.onSaveButtonClick(standardCredit);
            dismiss();
        } else {
            Toast.makeText(getContext(), R.string.credit_admission_criteria_error, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        getButton(BUTTON_POSITIVE).setOnClickListener(this);

    }

    public interface DialogListener {
        void onSaveButtonClick(StandardCredit standardCredit);

    }

}
