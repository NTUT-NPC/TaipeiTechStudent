package com.Ntut.utility;

import com.Ntut.model.YearCalendar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

/**
 * Created by blackmaple on 2017/5/12.
 */

public class CalendarConnector {
    private final static String CALENDAR_URI = HttpHelper.SERVER_HOST + "calendar.json";

    public static YearCalendar getEventList() throws Exception {
        try {
            String result = Connector.getDataByGet(CALENDAR_URI, "utf-8");
            JSONObject jObject = new JSONObject(result);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            YearCalendar yearCalendar = gson.fromJson(jObject
                    .toString(), YearCalendar.class);
            return yearCalendar;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("行事曆讀取時發生錯誤");
        }
    }
}
