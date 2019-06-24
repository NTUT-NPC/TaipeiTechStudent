package com.Ntut.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.viewpager.widget.PagerAdapter;

import com.Ntut.R;
import com.Ntut.model.CalendarInfo;
import com.Ntut.model.Model;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by blackmaple on 2017/5/12.
 */

public class CalendarPageAdapter extends PagerAdapter {
    private Model model = Model.getInstance();
    private Context context;
    private Calendar calendar;
    private LayoutInflater inflater;

    public CalendarPageAdapter(Context context, Calendar calendar) {
        this.context = context;
        this.calendar = calendar;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view.equals(obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ListView listView = (ListView) inflater.inflate(
                R.layout.calendar_listview, container, false);
        String year;
        String month;
        Calendar temp_calendar = (Calendar) calendar.clone();
        temp_calendar.add(Calendar.MONTH, position - 1);
        year = String.valueOf(temp_calendar.get(Calendar.YEAR));
        month = String.valueOf(temp_calendar.get(Calendar.MONTH) + 1);
        ArrayList<CalendarInfo> event_list = model.getYearCalendar()
                .getMonthEventList(year, month);
        listView.setAdapter(new CalendarListAdapter(context, event_list));
        container.addView(listView);
        return listView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
