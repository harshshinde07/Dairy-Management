package com.kshitijharsh.dairymanagement.model;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by harsh on 15/9/18.
 */

public class Sale {
    private String id, date, memId, memName, morEve, milkType, qty, fat, rate, amount;

    Sale() {
    }

    public Sale(String id, String date, String memId, String memName, String morEve, String milkType, String qty, String fat, String rate, String amount) {
        this.id = id;
        this.date = convertDateFromDB(date);
        this.memId = memId;
        this.memName = memName;
        this.morEve = morEve;
        this.milkType = milkType;
        this.qty = qty;
        this.fat = fat;
        this.rate = rate;
        this.amount = amount;
    }

    private static String convertDateFromDB(String date) {

        String[] array = date.split("/");
        int day = Integer.valueOf(array[1]);
        int month = Integer.valueOf(array[0]);
        int year = Integer.valueOf(array[2]);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month - 1, day);
        Date formattedDate = cal.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        return sdf.format(formattedDate);
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

    public String getMorEve() {
        return morEve;
    }

    public void setMorEve(String morEve) {
        this.morEve = morEve;
    }

    public String getMilkType() {
        return milkType;
    }

    public void setMilkType(String milkType) {
        this.milkType = milkType;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
