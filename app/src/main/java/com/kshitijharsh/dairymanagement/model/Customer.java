package com.kshitijharsh.dairymanagement.model;

public class Customer {
    private String name, branch, branchCode;
    private byte[] id;
    private byte[] password;

    Customer() {
    }

    public Customer(byte[] id, String name, String branch, String branchCode, byte[] password) {
        this.name = name;
        this.branch = branch;
        this.branchCode = branchCode;
        this.id = id;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }
}
