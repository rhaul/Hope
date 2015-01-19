package com.xplorer.hope.object;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Raghavendra on 11-01-2015.
 */
@ParseClassName("EWRelation")
public class EWRelation extends ParseObject {

    public EWRelation(){}


    public String getUserID() { return getString("userID");
    }

    public void setUserID(String userID) {
        put("userID", userID);
    }

    public String getEmployerID() { return getString("employerID"); }

    public void setEmployerID(String employerID) {
        put("employerID", employerID);
    }

    public String getWorkID() {
        return getString("workID");
    }

    public void setWorkID(String workID) {
        put("workID", workID);
    }

    public Boolean getApprove() {
        return getBoolean("Approve");
    }

    public void setApprove(Boolean Approve) {
        put("Approve", Approve);
    }


}
