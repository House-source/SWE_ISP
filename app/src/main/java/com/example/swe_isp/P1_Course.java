package com.example.swe_isp;

/**
 * Created by admin on 2018-07-19.
 */

class P1_Course {
    private String cname, cid;
    private String credit, lect, lab;

    public P1_Course(String cid, String cname, String credit, String lect, String lab) {
        this.cid = cid;
        this.cname = cname;
        this.credit = credit;
        this.lect = lect;
        this.lab = lab;
    }

    public String getCid() {
        return cid;
    }

    public String getCname() {
        return cname;
    }

    public String getCredit() {
        return credit;
    }

    public String getLect() {
        return lect;
    }

    public String getLab() {
        return lab;
    }

    public String toString() {
        return cid + " " + cname + " " + credit + " " + lect + " " + lab;
    }
}
