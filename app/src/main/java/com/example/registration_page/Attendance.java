package com.example.registration_page;

public class Attendance {

    int p;
    String nm;
    String date;
    String id;
    String mealtime;

    public Attendance() {
        // Empty constructor required for Firebase
    }
    public Attendance(String id, String nm, int p,String mealtime,String date)
    {
        this.id=id;
        this.nm=nm;
        this.p=p;
        this.mealtime = mealtime;
        this.date=date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getNm() {
        return nm;
    }

    public void setMealtime(String mealtime) {
        this.mealtime = mealtime;
    }

    public String getMealtime() {
        return mealtime;
    }

    public int getP() {
        return p;
    }
    public void setP(int p) {
        this.p = p;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }
}
