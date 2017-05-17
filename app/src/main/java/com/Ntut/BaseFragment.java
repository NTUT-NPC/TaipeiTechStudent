package com.Ntut;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Andy on 2017/4/28.
 */

public abstract class BaseFragment extends Fragment {

    private Animation.AnimationListener mAnimationListener;
    private View rootView;
    protected Context context;
    private Boolean hasInitData = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void showAlertMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("返回", null);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.create().show();
    }


    public void closeSoftKeyboard() {
        Activity activity = getActivity();
        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), 0);
        }
    }



    public void setAnimationListener(Animation.AnimationListener listener) {
        mAnimationListener = listener;
    }

    public abstract int getTitleColorId();

    public abstract int getTitleStringId();
}
