package com.Ntut.account;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Ntut.PortalActivity;
import com.Ntut.R;
import com.Ntut.model.Model;
import com.Ntut.runnable.AccountRunnable;
import com.Ntut.utility.NportalConnector;
import com.Ntut.utility.Utility;
import com.Ntut.utility.WifiUtility;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Created by blackmaple on 2017/5/21.
 */

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        View save_button = findViewById(R.id.save_button);
        save_button.setOnClickListener(this);
        View clear_button = findViewById(R.id.clear_button);
        clear_button.setOnClickListener(this);
        View nportal_link = findViewById(R.id.nportal_link);
        nportal_link.setOnClickListener(this);
        View right_link = findViewById(R.id.right_link);
        right_link.setOnClickListener(this);
        refreshView();
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        setActionBar();
    }

    private void refreshView() {
        String account = Model.getInstance().getAccount();
        String password = Model.getInstance().getPassword();
        EditText account_edittext = findViewById(R.id.account_edittext);
        EditText password_edittext = findViewById(R.id.password_edittext);
        account_edittext.setText(account);
        password_edittext.setText(password);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.save_button:
                if (!validateAccount()) {
                    return;
                }
                if (!validatePassword()) {
                    return;
                }
                NportalConnector.reset();
                writeSettings();
                Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show();
                break;
            case R.id.clear_button:
                Model.getInstance().deleteAccountPassword();
                refreshView();
                break;
            case R.id.nportal_link:
                if (WifiUtility.isNetworkAvailable(this)) {
                    Intent intent = new Intent(this, PortalActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.check_network_available,
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.right_link:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.right_text);
                builder.setMessage(R.string.right);
                builder.setPositiveButton(R.string.back, null);
                builder.show();
                break;
            default:
                break;
        }
    }

    private boolean validateAccount() {
        TextInputLayout accountInputLayout = findViewById(R.id.account_input_layout);
        EditText accountEditText = findViewById(R.id.account_edittext);
        String accountText = accountEditText.getText().toString();
        if (TextUtils.isEmpty(accountText)) {
            accountInputLayout.setError(getString(R.string.account_empty_error_message));
            requestFocus(accountEditText);
            return false;
        } else {
            accountInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        TextInputLayout passwordInputLayout = findViewById(R.id.password_input_layout);
        EditText passwordEditText = findViewById(R.id.password_edittext);
        String passwordText = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(passwordText)) {
            passwordInputLayout.setError(getString(R.string.password_empty_error_message));
            requestFocus(passwordEditText);
            return false;
        } else {
            passwordInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(v -> finish());
            actionBar.setTitle(R.string.account_setting_text);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.account_color)));
        }
        Utility.setStatusBarColor(this, getResources().getColor(R.color.account_color));
    }

    private void writeSettings() {
        EditText accountEditText = findViewById(R.id.account_edittext);
        String accountText = accountEditText.getText().toString();
        EditText passwordEditText = findViewById(R.id.password_edittext);
        String passwordText = passwordEditText.getText().toString();
        if (accountText.length() > 0 && passwordText.length() > 0) {
            Model.getInstance().saveAccountPassword(accountText, passwordText);
            Handler handler = new Handler();
            Runnable accountRunnable = new AccountRunnable(handler, this);
            handler.postDelayed(accountRunnable, 500);
            finish();
        }
    }
}
