package com.xplorer.hope.object;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Raghavendra on 11-01-2015.
 */
@ParseClassName("attendance")
public class Attendance extends ParseObject {

    public Attendance(){}


    public String getWorkerID() {
        return getString("Worker");
    }

    public void setWorkerID(String Worker) {
        put("Worker", Worker);
    }

    public String getEmployerID() {
        return getString("employerID");
    }

    public void setEmployerID(String employerID) { put("employerID", employerID); }

    public String getWorkID() {
        return getString("workID");
    }

    public void setWorkID(String workID) {
        put("workID", workID);
    }


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
