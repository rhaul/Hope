package com.xplorer.hope.object;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raghavendra on 11-01-2015.
 */
public class schedule extends ParseObject {

    public schedule(){
    }

    public void setDefaultSched(){
        setGenericDefaultSched(mondayAM,mondayPM);
        setGenericDefaultSched(tuesdayAM,tuesdayPM);
        setGenericDefaultSched(wednesdayAM,wednesdayPM);
        setGenericDefaultSched(thursdayAM,thursdayPM);
        setGenericDefaultSched(fridayAM,fridayPM);
        setGenericDefaultSched(saturdayAM,saturdayPM);
        setGenericDefaultSched(sundayAM,sundayPM);
    }

    public void setGenericDefaultSched(List<String> listAM, List<String> listPM){

        listAM.add("Not Available");
        listAM.add("Not Available");
        listAM.add("Not Available");
        listAM.add("Not Available");
        listAM.add("Not Available");
        listAM.add("Not Available");
        listAM.add("Not Available");
        listAM.add("Available");
        listAM.add("Available");
        listAM.add("Available");
        listAM.add("Available");
        listAM.add("Available");


        listPM.add("Available");
        listPM.add("Available");
        listPM.add("Available");
        listPM.add("Available");
        listPM.add("Available");
        listPM.add("Available");
        listPM.add("Available");
        listPM.add("Available");
        listPM.add("Available");
        listPM.add("Not Available");
        listPM.add("Not Available");
        listPM.add("Not Available");
    }
    public String userID;
    public List<String> mondayAM = new ArrayList<String>();
    public List<String> mondayPM = new ArrayList<String>();
    public List<String> tuesdayAM = new ArrayList<String>();
    public List<String> tuesdayPM = new ArrayList<String>();
    public List<String> wednesdayAM = new ArrayList<String>();
    public List<String> wednesdayPM = new ArrayList<String>();
    public List<String> thursdayAM = new ArrayList<String>();
    public List<String> thursdayPM = new ArrayList<String>();
    public List<String> fridayAM = new ArrayList<String>();
    public List<String> fridayPM = new ArrayList<String>();
    public List<String> saturdayAM = new ArrayList<String>();
    public List<String> saturdayPM = new ArrayList<String>();
    public List<String> sundayAM = new ArrayList<String>();
    public List<String> sundayPM = new ArrayList<String>();

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<String> getMondayAM() {
        return mondayAM;
    }

    public void setMondayAM(List<String> mondayAM) {
        this.mondayAM = mondayAM;
    }

    public List<String> getMondayPM() {
        return mondayPM;
    }

    public void setMondayPM(List<String> mondayPM) {
        this.mondayPM = mondayPM;
    }

    public List<String> getTuesdayAM() {
        return tuesdayAM;
    }

    public void setTuesdayAM(List<String> tuesdayAM) {
        this.tuesdayAM = tuesdayAM;
    }

    public List<String> getTuesdayPM() {
        return tuesdayPM;
    }

    public void setTuesdayPM(List<String> tuesdayPM) {
        this.tuesdayPM = tuesdayPM;
    }

    public List<String> getWednesdayAM() {
        return wednesdayAM;
    }

    public void setWednesdayAM(List<String> wednesdayAM) {
        this.wednesdayAM = wednesdayAM;
    }

    public List<String> getWednesdayPM() {
        return wednesdayPM;
    }

    public void setWednesdayPM(List<String> wednesdayPM) {
        this.wednesdayPM = wednesdayPM;
    }

    public List<String> getThursdayAM() {
        return thursdayAM;
    }

    public void setThursdayAM(List<String> thursdayAM) {
        this.thursdayAM = thursdayAM;
    }

    public List<String> getThursdayPM() {
        return thursdayPM;
    }

    public void setThursdayPM(List<String> thursdayPM) {
        this.thursdayPM = thursdayPM;
    }

    public List<String> getFridayAM() {
        return fridayAM;
    }

    public void setFridayAM(List<String> fridayAM) {
        this.fridayAM = fridayAM;
    }

    public List<String> getFridayPM() {
        return fridayPM;
    }

    public void setFridayPM(List<String> fridayPM) {
        this.fridayPM = fridayPM;
    }

    public List<String> getSaturdayAM() {
        return saturdayAM;
    }

    public void setSaturdayAM(List<String> saturdayAM) {
        this.saturdayAM = saturdayAM;
    }

    public List<String> getSaturdayPM() {
        return saturdayPM;
    }

    public void setSaturdayPM(List<String> saturdayPM) {
        this.saturdayPM = saturdayPM;
    }

    public List<String> getSundayAM() {
        return sundayAM;
    }

    public void setSundayAM(List<String> sundayAM) {
        this.sundayAM = sundayAM;
    }

    public List<String> getSundayPM() {
        return sundayPM;
    }

    public void setSundayPM(List<String> sundayPM) {
        this.sundayPM = sundayPM;
    }
}
