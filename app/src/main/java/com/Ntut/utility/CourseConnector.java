package com.Ntut.utility;



import com.Ntut.MainApplication;
import com.Ntut.model.CourseInfo;
import com.Ntut.model.Semester;
import com.Ntut.model.StudentCourse;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by blackmaple on 2017/5/4.
 */

public class CourseConnector {
    private static boolean isLogin = false;
    private static final String POST_COURSES_URI_TW = "https://app.ntut.edu.tw/ssoIndex.do?apOu=aa_0010-&apUrl=https://aps.ntut.edu.tw/course/tw/courseSID.jsp";
    private static final String COURSES_URI_TW = "https://aps.ntut.edu.tw/course/tw/courseSID.jsp";
    private static final String COURSE_URI_TW = "https://aps.ntut.edu.tw/course/tw/Select.jsp";
    private static final String POST_COURSES_URI_EN = "https://app.ntut.edu.tw/ssoIndex.do?apOu=aa_0010-&apUrl=https://aps.ntut.edu.tw/course/en/courseSID.jsp";
    private static final String COURSES_URI_EN = "https://aps.ntut.edu.tw/course/en/courseSID.jsp";
    private static final String COURSE_URI_EN = "https://aps.ntut.edu.tw/course/en/Select.jsp";

    private static String getPostCoursesUri(String lang) {
        if (lang.equals("zh"))
            return POST_COURSES_URI_TW;
        return POST_COURSES_URI_EN;
    }

    private static String getCoursesUri(String lang) {
        if (lang.equals("zh"))
            return COURSES_URI_TW;
        return COURSES_URI_EN;
    }


    private static String getCourseUri(String lang) {
        if (lang.equals("zh"))
            return COURSE_URI_TW;
        return COURSE_URI_EN;
    }

    public static String loginCourse() throws Exception {
        try {
            isLogin = false;
            String result = Connector.getDataByGet(getPostCoursesUri("zh"), "utf-8", "https://app.ntut.edu.tw/aptreeList.do?apDn=ou=aa,ou=aproot,o=ldaproot");
            TagNode tagNode;
            tagNode = new HtmlCleaner().clean(result);
            TagNode[] nodes = tagNode.getElementsByAttValue("name",
                    "sessionId", true, false);
            String sessionId = nodes[0].getAttributeByName("value");
            nodes = tagNode
                    .getElementsByAttValue("name", "userid", true, false);
            String userid = nodes[0].getAttributeByName("value");
            nodes = tagNode.getElementsByAttValue("name", "userType", true,
                    false);
            String userType = nodes[0].getAttributeByName("value");
            HashMap<String, String> courseParams = new HashMap<>();
            courseParams.put("sessionId", sessionId);
            courseParams.put("reqFrom", "Portal");
            courseParams.put("userid", userid);
            courseParams.put("userType", userType);
            result = Connector.getDataByPost(getCoursesUri("zh"), courseParams, "big5");
            isLogin = true;
            return result;
        } catch (Exception e) {
            NportalConnector.reset();
            e.printStackTrace();
            throw new Exception("登入課程系統時發生錯誤");
        }
    }

    public static ArrayList<Semester> getCourseSemesters(String sid)
            throws Exception {
        ArrayList<Semester> semesters = new ArrayList<>();
        String result;
        TagNode tagNode;
        try {
            if (!isLogin) {
                loginCourse();
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("format", "-3");
            params.put("code", sid);
            result = Connector.getDataByPost(getCourseUri("zh"), params, "big5");
            tagNode = new HtmlCleaner().clean(result);
        } catch (Exception e) {
            isLogin = false;
            throw new Exception("學期資料讀取時發生錯誤");
        }
        if (result.contains("查無該學號的學生基本資料")) {
            throw new Exception("查無該學號的學生基本資料");
        }
        try {
            TagNode[] nodes = tagNode.getElementsByName("a", true);
            for (TagNode a : nodes) {
                String[] split = a.getText().toString().split(" ");
                semesters.add(new Semester(split[0], split[2]));
            }
        } catch (Exception e) {
            isLogin = false;
            throw new Exception("學期資料讀取時發生錯誤");
        }
        return semesters;
    }

    public static StudentCourse getStudentCourse(String sid, String year,
                                                 String semester) throws Exception {
        try {
            if (!isLogin) {
                loginCourse();
            }
            StudentCourse student = new StudentCourse();
            student.setSid(sid);
            student.setYear(year);
            student.setSemester(semester);
            ArrayList<CourseInfo> courseList = getCourses(sid, year, semester);
            student.setCourseList(courseList);
            student = Utility.cleanString(student);
            return student;
        } catch (Exception e) {
            isLogin = false;
            throw new Exception("課表讀取時發生錯誤");
        }
    }

    public static ArrayList<String> GetCourseDetail(String courseNo)
            throws Exception {
        try {
            if (!isLogin) {
                loginCourse();
            }
            ArrayList<String> courseDetail = new ArrayList<>();
            HashMap<String, String> params = new HashMap<>();
            params.put("format", "-1");
            params.put("code", courseNo);
            String result = Connector.getDataByPost(getCourseUri("zh"), params, "big5");
            TagNode tagNode;
            tagNode = new HtmlCleaner().clean(result);
            TagNode[] tables = tagNode.getElementsByAttValue("border", "1",
                    true, false);

            TagNode[] rows = tables[0].getElementsByName("tr", true);
            for (TagNode row : rows) {
                TagNode[] cols = row.getElementsByName("th", true);
                String d = cols[0].getText().toString();
                cols = row.getElementsByName("td", true);
                d = d + "：" + cols[0].getText().toString();
                d = d.replace("　", " ");
                d = d.replace("\n", " ");
                courseDetail.add(d);
            }
            return courseDetail;
        } catch (Exception e) {
            isLogin = false;
            throw new Exception("課程資訊讀取時發生錯誤");
        }
    }

    public static ArrayList<String> GetClassmate(String courseNo)
            throws Exception {
        ArrayList<String> classmates = new ArrayList<>();
        HashMap<String, String> params = new HashMap<>();
        params.put("format", "-1");
        params.put("code", courseNo);
        String result;
        try {
            result = Connector.getDataByPost(getCourseUri("zh"), params, "big5");
        } catch (Exception e) {
            throw new Exception("學生名單讀取時發生錯誤");
        }
        TagNode tagNode;
        tagNode = new HtmlCleaner().clean(result);
        TagNode[] tables = tagNode.getElementsByAttValue("border", "1", true,
                false);

        TagNode[] rows = tables[1].getElementsByName("tr", true);
        for (int i = 1; i < rows.length; i++) {
            TagNode[] cols = rows[i].getElementsByName("td", true);
            String d = cols[0].getText().toString();
            d = d + "," + cols[1].getText().toString();
            d = d + "," + cols[2].getText().toString();
            d = d.replace("　", "");
            d = d.replace("\n", "");
            classmates.add(d);
        }
        return classmates;
    }

    static String getCourseType(String courseNo) throws Exception {
        try {
            if (!isLogin) {
                loginCourse();
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("format", "-1");
            params.put("code", courseNo);
            String result = Connector.getDataByPost(getCourseUri("zh"), params, "big5");
            TagNode tagNode;
            tagNode = new HtmlCleaner().clean(result);
            TagNode[] tables = tagNode.getElementsByAttValue("border", "1",
                    true, false);

            TagNode[] rows = tables[0].getElementsByName("tr", true);
            TagNode[] temp = rows[7].getElementsByName("td", true);
            return temp[0].getText().toString();
        } catch (Exception ex) {
            throw new Exception("課程類別讀取時發生錯誤");
        }
    }

    private static ArrayList<CourseInfo> getCourses(String sid, String year,
                                                    String semester) throws Exception {
        if (!isLogin) {
            loginCourse();
        }
        ArrayList<CourseInfo> courses = new ArrayList<>();
        HashMap<String, String> params = new HashMap<>();
        params.put("format", "-2");
        params.put("code", sid);
        params.put("year", year);
        params.put("sem", semester);
        String result = Connector.getDataByPost(getCourseUri(MainApplication.readSetting("courseLang")), params, "big5");
        TagNode tagNode;
        tagNode = new HtmlCleaner().clean(result);
        TagNode[] nodes = tagNode.getElementsByAttValue("border", "1", true,
                false);
        TagNode[] rows = nodes[0].getElementsByName("tr", true);
        if (MainApplication.readSetting("courseLang").equals("zh")) {
            for (int i = 3; i < rows.length - 1; i++) {
                TagNode[] cols = rows[i].getElementsByName("td", true);
                if (isWithdraw(cols)){
                    continue;
                }
                CourseInfo course = new CourseInfo();
                TagNode[] a = cols[0].getElementsByName("a", true);
                if (a.length == 0) {
                    course.setCourseNo("0");
                } else {
                    course.setCourseNo(a[0].getText().toString());
                }
                course.setCourseName(cols[1].getText().toString());
                course.setCourseTeacher(cols[6].getText().toString());
                course.setCourseRoom(cols[15].getText().toString());
                course.setCourseTime(new String[]{cols[8].getText().toString(),
                        cols[9].getText().toString(),
                        cols[10].getText().toString(),
                        cols[11].getText().toString(),
                        cols[12].getText().toString(),
                        cols[13].getText().toString(),
                        cols[14].getText().toString()});
                courses.add(course);
            }
        }
        else {
            for (int i = 1; i < rows.length - 1; i++) {
                TagNode[] cols = rows[i].getElementsByName("td", true);
                if (isWithdraw(cols)){
                    continue;
                }
                CourseInfo course = new CourseInfo();
                if (cols[0].isEmpty()) {
                    course.setCourseNo("0");
                } else {
                    course.setCourseNo(cols[0].getText().toString());
                }
                course.setCourseName(cols[1].getText().toString());
                course.setCourseTeacher(cols[4].getText().toString());
                course.setCourseRoom(cols[13].getText().toString());
                course.setCourseTime(new String[]{cols[6].getText().toString(),
                        cols[7].getText().toString(),
                        cols[8].getText().toString(),
                        cols[9].getText().toString(),
                        cols[10].getText().toString(),
                        cols[11].getText().toString(),
                        cols[12].getText().toString()});
                courses.add(course);
            }
        }
        return courses;
    }

    private static boolean isWithdraw(TagNode[] node) throws Exception {
        try{
            if (MainApplication.readSetting("courseLang").equals("zh"))
                return node[16].getText().toString().contains("撤選");
            return node[14].getText().toString().contains("Withdraw");
        } catch (Exception e) {
            isLogin = false;
            throw new Exception("撤選資訊讀取時發生錯誤");
        }
    }

    public static boolean isLogin() {
        return isLogin;
    }
}