package com.xplorer.hope.object;

import com.parse.ParseUser;

/**
 * Created by Raghavendra on 11-01-2015.
 */
public class user extends ParseUser {

    public user(){}
    public String type;
    public String name;
    public int dob;
    public String gender;
    public String address;
    public int phoneNo;
    public String qualRW = "No";
    public String qualLicense = "No";
    public String qualCooking = "Low";
    public String qualGardening = "Low";


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDob() {
        return dob;
    }

    public void setDob(int dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(int phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getQualRW() {
        return qualRW;
    }

    public void setQualRW(String qualRW) {
        this.qualRW = qualRW;
    }

    public String getQualLicense() {
        return qualLicense;
    }

    public void setQualLicense(String qualLicense) {
        this.qualLicense = qualLicense;
    }

    public String getQualCooking() {
        return qualCooking;
    }

    public void setQualCooking(String qualCooking) {
        this.qualCooking = qualCooking;
    }

    public String getQualGardening() {
        return qualGardening;
    }

    public void setQualGardening(String qualGardening) {
        this.qualGardening = qualGardening;
    }
}
