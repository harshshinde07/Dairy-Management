package com.kshitijharsh.dairymanagement.model;

/**
 * Created by harsh on 15/9/18.
 */

public class Collection {
    String membType;
    String membName;
    String cowbfType;
    double degree;
    double fat;
    double snf;
    double rate;
    double amount;

    public String getMembType() {
        return membType;
    }

    public void setMembType(String membType) {
        this.membType = membType;
    }

    public String getMembName() {
        return membName;
    }

    public void setMembName(String membName) {
        this.membName = membName;
    }

    public String getCowbfType() {
        return cowbfType;
    }

    public void setCowbfType(String cowbfType) {
        this.cowbfType = cowbfType;
    }

    public double getDegree() {
        return degree;
    }

    public void setDegree(double degree) {
        this.degree = degree;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getSnf() {
        return snf;
    }

    public void setSnf(double snf) {
        this.snf = snf;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Collection(String membType, String membName, String cowbfType, double degree, double fat, double snf, double rate, double amount) {

        this.membType = membType;
        this.membName = membName;
        this.cowbfType = cowbfType;
        this.degree = degree;
        this.fat = fat;
        this.snf = snf;
        this.rate = rate;
        this.amount = amount;
    }

    public Collection() {

    }
}
