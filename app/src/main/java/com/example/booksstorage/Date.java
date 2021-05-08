package com.example.booksstorage;

import java.util.HashMap;

public class Date extends Object{
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


    public int compareTo(Object publishDate) { //later: this? 1, other? -1, ==? 0
        if (publishDate instanceof Date){
            Date other = (Date) publishDate;

            if (this.getYear() < other.getYear()){
                return -1;
            } else if (this.getYear() > other.getYear()){
                return 1;
            } else {
                if (this.getMonth() == -1 && other.getMonth() == -1){
                    return 0;
                }
                if (this.getMonth() == -1) {
                    return other.getMonth() <= 6 ? 1 : -1;
                }
                if (other.getMonth() == -1){
                    return this.getMonth() <= 6 ? -1: 1;
                }

                if (this.getMonth() < other.getMonth()){
                    return -1;
                } else if (this.getMonth() > other.getMonth()){
                    return 1;
                } else {
                    if (this.getDay() == -1 && other.getDay() == -1){
                        return 0;
                    }
                    if (this.getDay() == -1){
                        return other.getDay() <= 15 ? 1 : -1;
                    }
                    if (other.getDay() == -1){
                        return this.getDay() <= 15 ? -1 : 1;
                    }

                    if (this.getDay() < other.getDay()){
                        return -1;
                    } else if (this.getDay() > other.getDay()){
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
        return 0;
    }
}