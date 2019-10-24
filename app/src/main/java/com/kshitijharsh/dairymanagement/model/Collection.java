package com.kshitijharsh.dairymanagement.model;

/**
 * Created by harsh on 15/9/18.
 */

public class Collection {
    private String id, date, memId, memName, memType, milktype, morEve, fat, snf, degree, qty, rate, amt;

    Collection() {
    }

    public Collection(String id, String date, String memId, String memName, String memType, String milktype, String morEve, String fat, String qty, String rate, String amt) {
        this.id = id;
        this.date = date;
        this.memId = memId;
        this.memName = memName;
        this.memType = memType;
        this.milktype = milktype;
        this.morEve = morEve;
        this.fat = fat;
        this.qty = qty;
        this.rate = rate;
        this.amt = amt;
    }

    public Collection(String id, String date, String memId, String memName, String memType, String milktype, String morEve, String fat, String snf, String qty, String rate, String amt) {
        this.id = id;
        this.date = date;
        this.memId = memId;
        this.memName = memName;
        this.memType = memType;
        this.milktype = milktype;
        this.morEve = morEve;
        this.fat = fat;
        this.snf = snf;
        this.qty = qty;
        this.rate = rate;
        this.amt = amt;
    }

    public Collection(String id, String date, String memId, String memName, String memType, String milktype, String morEve, String fat, String snf, String degree, String qty, String rate, String amt) {
        this.id = id;
        this.date = date;
        this.memId = memId;
        this.memName = memName;
        this.memType = memType;
        this.milktype = milktype;
        this.morEve = morEve;
        this.fat = fat;
        this.snf = snf;
        this.degree = degree;
        this.qty = qty;
        this.rate = rate;
        this.amt = amt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }

    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public String getMemType() {
        return memType;
    }

    public void setMemType(String memType) {
        this.memType = memType;
    }

    public String getMilktype() {
        return milktype;
    }

    public void setMilktype(String milktype) {
        this.milktype = milktype;
    }

    public String getMorEve() {
        return morEve;
    }

    public void setMorEve(String morEve) {
        this.morEve = morEve;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getSnf() {
        return snf;
    }

    public void setSnf(String snf) {
        this.snf = snf;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }
}
