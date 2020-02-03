package com.kshitijharsh.dairymanagement.model;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CattleFeed {
    private String id, date, memId, name, itemName, rate, qty, amt, particulars;

    public CattleFeed(String id, String date, String memId, String name, String itemName, String rate, String qty, String amt, String particulars) {
        this.id = id;
        this.date = convertDateFromDB(date);
        this.memId = memId;
        this.name = name;
        this.itemName = itemName;
        this.rate = rate;
        this.qty = qty;
        this.amt = amt;
        this.particulars = particulars;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }
}
