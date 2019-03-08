package com.kshitijharsh.dairymanagement.model;

/**
 * Created by harsh on 15/9/18.
 */

public class Sale {
    String date;
    String branch;
    String mornEve;
    String cowbfType;
    String cashCredit;
    String customerName;
    double litre;
    double fat;
    double rate;
    double amount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getMornEve() {
        return mornEve;
    }

    public void setMornEve(String mornEve) {
        this.mornEve = mornEve;
    }

    public String getCowbfType() {
        return cowbfType;
    }

    public void setCowbfType(String cowbfType) {
        this.cowbfType = cowbfType;
    }

    public String getCashCredit() {
        return cashCredit;
    }

    public void setCashCredit(String cashCredit) {
        this.cashCredit = cashCredit;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getLitre() {
        return litre;
    }

    public void setLitre(double litre) {
        this.litre = litre;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
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

    public Sale() {

    }

    public Sale(String date, String branch, String mornEve, String cowbfType, String cashCredit, String customerName, double litre, double fat, double rate, double amount) {

        this.date = date;
        this.branch = branch;
        this.mornEve = mornEve;
        this.cowbfType = cowbfType;
        this.cashCredit = cashCredit;
        this.customerName = customerName;
        this.litre = litre;
        this.fat = fat;
        this.rate = rate;
        this.amount = amount;
    }
}
