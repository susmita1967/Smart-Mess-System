package com.example.registration_page;

public class Votes {
    int vote;
    String nm;
    String date;
    String mealtime;
    String id;

    public Votes(String id,String nm,int vote,String mealtime,String date)
    {
        this.id=id;
        this.nm=nm;
        this.vote=vote;
        this.mealtime=mealtime;
        this.date=date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getVote() {
        return vote;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMealtime(String mealtime) {
        this.mealtime = mealtime;
    }

    public String getNm() {
        return nm;
    }

    public String getDate() {
        return date;
    }

    public String getMealtime() {
        return mealtime;
    }

    public void setVote(int p) {
        this.vote = vote;
    }

}
