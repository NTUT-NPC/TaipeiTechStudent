package com.Ntut.course.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.Ntut.course.CourseFragment;
import com.Ntut.model.Model;
import com.Ntut.utility.Constants;
import com.Ntut.utility.CourseConnector;
import com.Ntut.utility.NportalConnector;

import java.lang.ref.WeakReference;

/**
 * Created by blackmaple on 2017/5/9.
 */

public class SearchCourseTask extends AsyncTask<String, Void, Object> {
    private WeakReference<CourseFragment> mCourseFragmentWeakReference;
    private WeakReference<ProgressDialog> mProgressDialogWeakReference;

    public SearchCourseTask(CourseFragment fragment) {
        mCourseFragmentWeakReference = new WeakReference<>(fragment);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        CourseFragment fragment = mCourseFragmentWeakReference.get();
        if (fragment != null) {
            ProgressDialog progressDialog = ProgressDialog.show(fragment.getContext(), null, "課表查詢中…");
            mProgressDialogWeakReference = new WeakReference<>(progressDialog);
        } else {
            cancel(true);
        }
    }

    @Override
    protected Object doInBackground(String... params) {
        int retryCount = 0;
        Object result;
        do {
            try {
                if (!NportalConnector.isLogin()) {
                    String account = Model.getInstance().getAccount();
                    String password = Model.getInstance().getPassword();
                    NportalConnector.login(account, password);
                }
                if (!CourseConnector.isLogin()) {
                    CourseConnector.loginCourse();
                }
                result = CourseConnector.getStudentCourse(params[0], params[1],
                        params[2]);
                break;
            } catch (Exception e) {
                e.printStackTrace();
                result = e.getMessage();
                retryCount++;
            }
        } while (retryCount <= Constants.RETRY_MAX_COUNT_INT);
        return result;
    }

    @Override
    protected void onPostExecute(Object object) {
        super.onPostExecute(object);
        ProgressDialog progressDialog = mProgressDialogWeakReference.get();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        CourseFragment fragment = mCourseFragmentWeakReference.get();
        if (fragment != null) {
            fragment.obtainStudentCourse(object);
        }
    }
}
