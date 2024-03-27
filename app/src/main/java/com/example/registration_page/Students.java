package com.example.registration_page;

public class Students {

    String name,grn,degree,cls,p,cp;
    String phn,rm;

    public Students() {
    }

    public Students(String name, String grn, String degree, String cls, String phn, String rm,String p, String cp) {
        this.name = name;
        this.grn = grn;
        this.degree = degree;
        this.cls = cls;
        this.phn = phn;
        this.rm = rm;
        this.p=p;
        this.cp=cp;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrn() {
        return grn;
    }

    public void setGrn(String grn) {
        this.grn = grn;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degreet) {
        this.degree = degree;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) {
        this.rm = rm;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }
}
