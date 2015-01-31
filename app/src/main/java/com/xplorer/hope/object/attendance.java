package com.xplorer.hope.object;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Raghavendra on 11-01-2015.
 */
@ParseClassName("Attendance")
public class Attendance extends ParseObject {

    public Attendance(){}


    public String getWorkerID() {
        return getString("workerID");
    }

    public void setWorkerID(String Worker) {
        put("workerID", Worker);
    }

    public String getEmployerID() {
        return getString("employerID");
    }

    public void setEmployerID(String employerID) { put("employerID", employerID); }

    public String getDate() {
        return getString("date");
    }

    public void setDate(String date) {
        put("date", date);
    }


    public String getTime() {
        return getString("time");
    }

    public void setTime(String time) {
        put("time", time);
    }



}
