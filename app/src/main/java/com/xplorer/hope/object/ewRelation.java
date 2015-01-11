package com.xplorer.hope.object;

import com.parse.ParseObject;

/**
 * Created by Raghavendra on 11-01-2015.
 */
public class ewRelation extends ParseObject {

    public ewRelation(){}

    public String userID;
    public String employerID;
    public String workID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmployerID() {
        return employerID;
    }

    public void setEmployerID(String employerID) {
        this.employerID = employerID;
    }

    public String getWorkID() {
        return workID;
    }

    public void setWorkID(String workID) {
        this.workID = workID;
    }
}
