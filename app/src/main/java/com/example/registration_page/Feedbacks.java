package com.example.registration_page;

public class Feedbacks {

    String id;
    String nm;
    int f;
    String date;
    public Feedbacks()
    {

    }
    public Feedbacks(String id,String nm,int f,String date)
    {
        this.id=id;
        this.nm=nm;
        this.f=f;
        this.date=date;
    }
    public String getId() {
        return id;
    }

    public String getNm() {
        return nm;
    }

    public String getDate() {
        return date;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getF() {
        return f;
    }
    public void setF(int f) {
        this.f = f;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
