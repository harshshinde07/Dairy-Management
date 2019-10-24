package com.kshitijharsh.dairymanagement.model;

public class CattleFeed {
    private String id, date, memId, name, itemName, rate, qty, amt, particulars;

    CattleFeed() {
    }

    public CattleFeed(String id, String date, String memId, String name, String itemName, String rate, String qty, String amt, String particulars) {
        this.id = id;
        this.date = date;
        this.memId = memId;
        this.name = name;
        this.itemName = itemName;
        this.rate = rate;
        this.qty = qty;
        this.amt = amt;
        this.particulars = particulars;
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
