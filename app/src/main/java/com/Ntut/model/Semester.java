package com.Ntut.model;

/**
 * Created by blackmaple on 2017/5/4.
 */

public class Semester {
    private String year;
    private String semester;

    public Semester(String year, String semester) {
        this.year = year;
        this.semester = semester;
    }

    public String getYear() {
        return year;
    }

    public String getSemester() {
        return semester;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", year, semester);
    }
}
