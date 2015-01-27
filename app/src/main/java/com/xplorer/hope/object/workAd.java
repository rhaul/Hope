package com.xplorer.hope.object;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by Raghavendra on 11-01-2015.
 */

@ParseClassName("WorkAd")

public class WorkAd extends ParseObject {

    public WorkAd(){
    }
    public String imgURL;

    public String getCategory() {
        return getString("category");
    }

    public void setCategory(String category) {  put("category", category); }

    public String getAddress() {
        return getString("address");
    }

    public void setAddress(String address) {
        put("address", address);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public String getPhoneNo() {
        return getString("phoneNo");
    }

    public void setPhoneNo(String phoneNo) {
        put("phoneNo", phoneNo);
    }

    public int getEmployerPhoneNo() {
        return getInt("employerPhoneNo");
    }

    public void setEmployerPhoneNo(int employerPhoneNo) {
        put("employerPhoneNo", employerPhoneNo);
    }

    public String getDateType() {
        return getString("dateType");
    }

    public void setDateType(String dateType) {
        put("dateType", dateType);
    }

    public String getDateFrom() {
        return getString("dateFrom");
    }

    public void setDateFrom(String dateFrom) {
        put("dateFrom", dateFrom);
    }

    public String getDateTo() {
        return getString("dateTo");
    }

    public void setDateTo(String dateTo) {
        put("dateTo", dateTo);
    }

    public String getTimeType() {
        return getString("timeType");
    }

    public void setTimeType(String timeType) {
        put("timeType", timeType);
    }

    public void setS1StartingTime(String time){
        put("s1StartingTime",time);
    }
    public String getS1StartingTime(){
        return getString("s1StartingTime");
    }
    public void setS1EndingTime(String time){
        put("s1EndingTime",time);
    }
    public String getS1EndingTime(){
        return getString("s1EndingTime");
    }
    public void setS2StartingTime(String time){
        put("s2StartingTime",time);
    }
    public String getS2StartingTime(){
        return getString("s2StartingTime");
    }
    public void setS2EndingTime(String time){
        put("s2EndingTime",time);
    }
    public String getS2EndingTime(){
        return getString("s2EndingTime");
    }

    public long getWageLowerLimit() {
        return getLong("wageLowerLimit");
    }

    public void setWageLowerLimit(long wageLowerLimit) {
        put("wageLowerLimit", wageLowerLimit);
    }

    public long getWageHigherLimit() {
        return getLong("wageHigherLimit");
    }

    public void setWageHigherLimit(long wageHigherLimit) {
        put("wageHigherLimit", wageHigherLimit);
    }

    public void setUserId(String userObjId){
        put("userID",userObjId);
    }
    public String getUserId(){
        return getString("userID");
    }

    public void setUserName(String name_){
        put("userName",name_);
    }
    public String getUserName(){
        return getString("userName");
    }


    public void setAddressGP(ParseGeoPoint gp){
        put("addressGP",gp);
    }
    public ParseGeoPoint getAddressGP(){
        return getParseGeoPoint("addressGP");
    }

}
