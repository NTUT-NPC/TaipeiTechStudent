package com.Ntut.credit;

import android.content.Context;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.TextView;

import com.Ntut.R;
import com.Ntut.utility.SlideAnimator;

/**
 * Created by blackmaple on 2017/5/15.
 */

public class CreditGroupView extends LinearLayout implements View.OnClickListener {
    private SlideAnimator animator;

    public CreditGroupView(Context context) {
        super(context);
        inflate(context, R.layout.credit_group, this);
        View title_background = findViewById(R.id.title_background);
        title_background.setOnClickListener(this);
        LinearLayout container = findViewById(R.id.container);
        container.setVisibility(View.GONE);
        animator = new SlideAnimator(context, container);
    }

    public void setSlideAnimationListener(SlideAnimator.SlideAnimationListener listener) {
        animator.setSlideAnimationListener(listener);
    }

    public void setGroupTitle(String title) {
        TextView group_title = findViewById(R.id.group_title);
        group_title.setText(title);
    }

    public void setGroupPS(String ps) {
        TextView group_ps = findViewById(R.id.group_ps);
        group_ps.setText(ps);
    }

    @Override
    public void addView(View v) {
        LinearLayout container = findViewById(R.id.container);
        container.addView(v);
    }

    @Override
    public void removeAllViews() {
        LinearLayout container = findViewById(R.id.container);
        container.removeAllViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_background:
                animator.toggle();
                break;

        }
    }
}
