package com.Ntut.model;

import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blackmaple on 2017/5/14.
 */

public class OtherInfo {
    private String title;
    private int iconId;

    public OtherInfo(String title, int iconId) {
        this.title = title;
        this.iconId = iconId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
