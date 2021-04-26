package com.example.booksstorage;

import java.util.HashMap;

public class Date {
    HashMap<Integer, String> dateToMonth = new HashMap<Integer, String>() {{
        put(1, "January");
        put(2, "February");
        put(3, "March");
        put(4, "April");
        put(5, "May");
        put(6, "June");
        put(7, "July");
        put(8, "August");
        put(9, "September");
        put(10, "October");
        put(11, "November");
        put(12, "December");
    }};
    private int year;
    private int month;
    private int day;

    private String sMonth;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getsMonth() {
        return sMonth;
    }

    public void setsMonth(String sMonth) {
        this.sMonth = sMonth;
    }

    public Date(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
        this.sMonth = dateToMonth.get(month);
    }

    public String numsDate(){
        return this.month + "/" + this.day + "/" + this.year;
    }
    public String numsDateNoDay() {return this.month + "/" + this.year; }
    public String numsDateJustYear() {return this.year+""; }

    public String dateWithMonthWord(){
        return this.sMonth + " " + this.day + ", " + this.year;
    }
}