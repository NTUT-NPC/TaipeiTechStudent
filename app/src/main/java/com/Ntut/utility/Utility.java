package com.Ntut.utility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.Ntut.MainActivity;
import com.Ntut.model.Model;
import com.Ntut.R;
import com.Ntut.model.StudentCourse;
import com.Ntut.model.StudentCredit;
import com.google.gson.Gson;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by blackmaple on 2017/5/8.
 */

public class Utility {
    private static int notification_index = 1;
    private static final int hint_notification_index = 0;
    public static final int INT_PROGRESS_DIALOG_TIMEOUT_MILLISECOND = 5000;

    public static void showNotification(Context context, String title,
                                        String message, boolean is_hint) {
        if (context == null) {
            return;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        builder.setStyle(
                new NotificationCompat
                        .BigTextStyle(builder)
                        .bigText(message)
                        .setBigContentTitle(title))
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(message);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        TaskStackBuilder stack_builder = TaskStackBuilder.create(context);
        stack_builder.addParentStack(MainActivity.class);
        stack_builder.addNextIntent(intent);
        PendingIntent pending_intent = stack_builder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pending_intent);
        NotificationManager notify_manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notify_manager.notify(is_hint ? hint_notification_index
                : notification_index, builder.build());
        if (!is_hint) {
            notification_index++;
        }
    }

    public static void showActivityNotification(Context context, String title,
                                                String message, Bitmap bitmap){
        if (context == null) {
            return;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.event_icon)
                .setLargeIcon(BitmapUtility.loadBitmap(context, R.mipmap.ic_launcher))
                .setStyle(
                        new NotificationCompat
                                .BigPictureStyle()
                                .bigPicture(bitmap)
                                .setSummaryText(message));
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        TaskStackBuilder stack_builder = TaskStackBuilder.create(context);
        stack_builder.addParentStack(MainActivity.class);
        stack_builder.addNextIntent(intent);
        PendingIntent pending_intent = stack_builder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pending_intent);
        NotificationManager notify_manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notify_manager.notify(notification_index, builder.build());
        notification_index++;
    }

    static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static <T> ArrayList<T> castListObject(Object object,
                                                  Class<T> type_class) {
        ArrayList<T> result = new ArrayList<>();
        if (object instanceof ArrayList<?>) {
            for (Object item : (ArrayList<?>) object) {
                if (type_class.isInstance(item)) {
                    result.add(type_class.cast(item));
                }
            }
        }
        return result.size() > 0 ? result : null;
    }

    static StudentCourse cleanString(StudentCourse student) {
        Gson gson = new Gson();
        String json = gson.toJson(student);
        json = json.replace("　", " ");
        json = json.replace("\\n", " ");
        student = gson.fromJson(json, StudentCourse.class);
        return student;
    }

    static StudentCredit cleanString(StudentCredit student) {
        Gson gson = new Gson();
        String json = gson.toJson(student);
        json = json.replace("　", "");
        json = json.replace("\\n", "");
        json = json.replace(" ", "");
        student = gson.fromJson(json, StudentCredit.class);
        return student;
    }

    static String cleanString(String s) {
        s = s.replace("　", " ");
        s = s.replace(System.getProperty("line.separator"), " ");
        s = s.replaceAll("(\r\n|\r|\n|\n\r)", "");
        s = s.replace(" ", "");
        return s;
    }

    public static String cleanSpace(String s) {
        s = s.replace("　", " ");
        s = s.replace(" ", "");
        return s;
    }

    public static ArrayList<String> splitTime(String timeString) {
        ArrayList<String> infos = new ArrayList<>();
        String[] temp = timeString.split(" ");
        for (String t : temp) {
            switch (t) {
                case "A":
                    infos.add("10");
                    break;
                case "B":
                    infos.add("11");
                    break;
                case "C":
                    infos.add("12");
                    break;
                case "D":
                    infos.add("13");
                    break;
                default:
                    infos.add(t);
                    break;
            }
        }
        return infos;
    }

    public static void showDialog(String title, String msg, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("返回", null);
        builder.show();
    }

    public static String getDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int date_no = cal.get(Calendar.DAY_OF_MONTH);
        if (date_no < 10)
            return "0" + String.valueOf(date_no);
        return String.valueOf(date_no);
    }

    public static String getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        return String.valueOf(month);
    }

    public static String getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    public static String getDateString(String format, Date date, Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.TAIWAN);
        DateFormatSymbols dfs = new DateFormatSymbols();
        dfs.setShortWeekdays(new String[]{"", "六", "一", "二", "三", "四", "五",
                "六"});
        sdf.setDateFormatSymbols(dfs);
        return sdf.format(date);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            if (color == Color.BLACK
                    && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setStatusBarColor(color);
        }
    }

    public static boolean checkAccount(Context context) {
        String account = Model.getInstance().getAccount();
        String password = Model.getInstance().getPassword();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            Toast.makeText(context, "請先至帳號管理，設定校園入口網站帳號密碼！", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
