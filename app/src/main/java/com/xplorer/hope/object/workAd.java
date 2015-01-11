package com.xplorer.hope.object;

import com.parse.ParseObject;

/**
 * Created by Raghavendra on 11-01-2015.
 */
public class workAd extends ParseObject {

    public workAd(){}

    public String category;
    public String address;
    public String description;
    public double wageLowerLimit;
    public String employerID;
    public String employerName;
    public int employerPhoneNo;
    public String dateType;
    public double dateFrom;
    public double dateTo;
    public String timeType;
    public double[] timeSlot1;
    public double[] timeSlot2;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getWageLowerLimit() {
        return wageLowerLimit;
    }

    public void setWageLowerLimit(double wageLowerLimit) {
        this.wageLowerLimit = wageLowerLimit;
    }

    public String getEmployerID() {
        return employerID;
    }

    public void setEmployerID(String employerID) {
        this.employerID = employerID;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public int getEmployerPhoneNo() {
        return employerPhoneNo;
    }

    public void setEmployerPhoneNo(int employerPhoneNo) {
        this.employerPhoneNo = employerPhoneNo;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public double getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(double dateFrom) {
        this.dateFrom = dateFrom;
    }

    public double getDateTo() {
        return dateTo;
    }

    public void setDateTo(double dateTo) {
        this.dateTo = dateTo;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public double[] getTimeSlot1() {
        return timeSlot1;
    }

    public void setTimeSlot1(double[] timeSlot1) {
        this.timeSlot1 = timeSlot1;
    }

    public double[] getTimeSlot2() {
        return timeSlot2;
    }

    public void setTimeSlot2(double[] timeSlot2) {
        this.timeSlot2 = timeSlot2;
    }
}
