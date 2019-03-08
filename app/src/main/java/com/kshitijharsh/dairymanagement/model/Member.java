package com.kshitijharsh.dairymanagement.model;

public class Member {
    String code;
    String name;
    String cowbfType;
    String membType;

    public Member(String code, String name, String cowbfType, String membType) {
        this.code = code;
        this.name = name;
        this.cowbfType = cowbfType;
        this.membType = membType;
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
}
