package com.example.registration_page;

import java.util.Date;

public class Menus {
    String date;
    String roti, sabji1, sabji2, sweetdish, rice, dahitak, papad, specialdish, sider1, sider2,mealtime;

    public Menus(String date,String mealtime, String roti, String sabji1, String sabji2, String sider1, String sider2, String sweetdish, String rice, String dahitak, String papad, String specialdish) {
        this.date = date;
        this.mealtime=mealtime;
        this.roti = roti;
        this.sabji1 = sabji1;
        this.sabji2 = sabji2;
        this.sider1 = sider1;
        this.sider2 = sider2;
        this.sweetdish = sweetdish;
        this.rice = rice;
        this.dahitak = dahitak;
        this.papad = papad;
        this.specialdish = specialdish;
    }
//alt+printscreen to create constructor and getter/setter


    public void setDate(String date) {
        this.date = date;
    }

    public Menus(String mealtime) {
        this.mealtime = mealtime;
    }

    public void setRoti(String roti) {
        this.roti = roti;
    }

    public void setSabji1(String sabji1) {
        this.sabji1 = sabji1;
    }

    public void setSabji2(String sabji2) {
        this.sabji2 = sabji2;
    }

    public void setSweetdish(String sweetdish) {
        this.sweetdish = sweetdish;
    }

    public void setRice(String rice) {
        this.rice = rice;
    }

    public void setDahitak(String dahitak) {
        this.dahitak = dahitak;
    }

    public void setPapad(String papad) {
        this.papad = papad;
    }

    public void setSpecialdish(String specialdish) {
        this.specialdish = specialdish;
    }

    public void setSider1(String sider1) {
        this.sider1 = sider1;
    }

    public void setSider2(String sider2) {
        this.sider2 = sider2;
    }

    public String getDate() {
        return date;
    }

    public String getRoti() {
        return roti;
    }

    public String getSabji1() {
        return sabji1;
    }

    public String getSabji2() {
        return sabji2;
    }

    public String getSweetdish() {
        return sweetdish;
    }

    public String getRice() {
        return rice;
    }

    public String getDahitak() {
        return dahitak;
    }

    public String getPapad() {
        return papad;
    }

    public String getSpecialdish() {
        return specialdish;
    }

    public String getSider1() {
        return sider1;
    }

    public String getSider2() {
        return sider2;
    }

    public void setMealtime(String mealtime) {
        this.mealtime = mealtime;
    }

    public String getMealtime() {
        return mealtime;
    }
}