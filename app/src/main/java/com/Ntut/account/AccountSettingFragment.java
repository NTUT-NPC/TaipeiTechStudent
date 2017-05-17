package com.Ntut.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.Ntut.BaseFragment;
import com.Ntut.PortalActivity;
import com.Ntut.R;
import com.Ntut.model.Model;
import com.Ntut.runnable.AccountRunnable;
import com.Ntut.utility.NportalConnector;
import com.Ntut.utility.WifiUtility;

/**
 * Created by blackmaple on 2017/5/13.
 */

public class AccountSettingFragment extends BaseFragment implements View.OnClickListener{
    private static View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_account_setting, container,
                false);
        fragmentView.setOnClickListener(this);
        View save_button = fragmentView.findViewById(R.id.save_button);
        save_button.setOnClickListener(this);
        View clear_button = fragmentView.findViewById(R.id.clear_button);
        clear_button.setOnClickListener(this);
        View nportal_link = fragmentView.findViewById(R.id.nportal_link);
        nportal_link.setOnClickListener(this);
        View right_link = fragmentView.findViewById(R.id.right_link);
        right_link.setOnClickListener(this);
        refreshView();
        return fragmentView;
    }

    private void refreshView() {
        String account = Model.getInstance().getAccount();
        String password = Model.getInstance().getPassword();
        EditText account_edittext = (EditText) fragmentView
                .findViewById(R.id.account_edittext);
        EditText password_edittext = (EditText) fragmentView
                .findViewById(R.id.password_edittext);
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
                Toast.makeText(getActivity(), R.string.save_success, Toast.LENGTH_SHORT).show();
                break;
            case R.id.clear_button:
                Model.getInstance().deleteAccountPassword();
                refreshView();
                break;
            case R.id.nportal_link:
                if (WifiUtility.isNetworkAvailable(getActivity())) {
                    Intent intent = new Intent(getActivity(), PortalActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), R.string.check_network_available,
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.right_link:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.right_text);
                builder.setMessage(R.string.right);
                builder.setPositiveButton(R.string.back, null);
                builder.show();
                break;
            default:
                closeSoftKeyboard();
                break;
        }
    }

    private boolean validateAccount() {
        TextInputLayout accountInputLayout = (TextInputLayout) fragmentView.findViewById(R.id.account_input_layout);
        EditText accountEditText = (EditText) fragmentView
                .findViewById(R.id.account_edittext);
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
        TextInputLayout passwordInputLayout = (TextInputLayout) fragmentView.findViewById(R.id.password_input_layout);
        EditText passwordEditText = (EditText) fragmentView
                .findViewById(R.id.password_edittext);
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
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void writeSettings() {
        EditText accountEditText = (EditText) fragmentView
                .findViewById(R.id.account_edittext);
        String accountText = accountEditText.getText().toString();
        EditText passwordEditText = (EditText) fragmentView
                .findViewById(R.id.password_edittext);
        String passwordText = passwordEditText.getText().toString();
        if (accountText.length() > 0 && passwordText.length() > 0) {
            Model.getInstance().saveAccountPassword(accountText, passwordText);
            Handler handler = new Handler();
            Runnable accountRunnable = new AccountRunnable(handler, getActivity());
            handler.postDelayed(accountRunnable, 500);
        }
    }



    @Override
    public int getTitleColorId() {
        return R.color.dark_purple;
    }

    @Override
    public int getTitleStringId() {
        return R.string.account_setting;
    }
}
