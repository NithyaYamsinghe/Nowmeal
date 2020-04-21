package com.example.nowmeal.shipper.model;

public class ShipperUserModel {

    private String uid, name, address, phone;
    boolean active;

    public ShipperUserModel() {
    }

    public ShipperUserModel(String uid, String name, String address, String phone, boolean active) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.active = active;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public  void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        return active;
    }
}
