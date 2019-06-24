package com.Ntut.credit;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSpinner;

/**
 * Created by blackmaple on 2017/5/15.
 */

public class MenuSpinner extends AppCompatSpinner {

    OnItemSelectedListener listener;

    public MenuSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (listener != null) {
            listener.onItemSelected(null, null, position, 0);
        }
    }

    public void setOnItemSelectedEvenIfUnchangedListener(
            OnItemSelectedListener listener) {
        this.listener = listener;
    }
}
