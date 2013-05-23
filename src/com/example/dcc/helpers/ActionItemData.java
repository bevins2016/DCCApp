package com.example.dcc.helpers;

import java.util.Date;

public class ActionItemData {

    String name = "";
    String day = "0";
    String month = "0";
    String date;
    String studentNumber;
    String year;
    String dependability;
    String reliability;
    String issues;


    public ActionItemData(String name, String studentNumber){
        this.name = name;
        this.studentNumber = studentNumber;
        Date use = new Date();
        int daynum = use.getDate();
        int monthNum = use.getMonth()+1;
        year = "" + use.getYear();

        if(daynum<10){
            day += "" + daynum;
        }else{
            day = "" + daynum;
        }
        if(monthNum<10){
            month += "" + monthNum;
        }else{
            month = "" + monthNum;
        }

        date = month + "/" + day + "/" + year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }


}