package com.kshitijharsh.dairymanagement.model;

public class Member {
    private String code, name, cowbfType, membType, rateGrpNo;

    public Member(String code, String name, String cowbfType, String membType, String rateGrpNo) {
        this.code = code;
        this.name = name;
        this.cowbfType = cowbfType;
        this.membType = membType;
        this.rateGrpNo = rateGrpNo;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCowbfType() {
        return cowbfType;
    }

    public String getMembType() {
        return membType;
    }

    public String getRateGrpNo() {
        return rateGrpNo;
    }
}
