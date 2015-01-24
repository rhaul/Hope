package com.xplorer.hope.config;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;
import com.xplorer.hope.R;
import com.xplorer.hope.object.Attendance;
import com.xplorer.hope.object.EWRelation;
import com.xplorer.hope.object.Schedule;
import com.xplorer.hope.object.UserInfo;
import com.xplorer.hope.object.WorkAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class HopeApp extends Application {
    // app
    private static HopeApp instance;
    // Debugging tag for the application
    public static HashMap<String,List<WorkAd>> workAdsStorage = new HashMap<String, List<WorkAd>>();
    public static final String APPTAG = "HopeApp";
    public ParseQuery<WorkAd> filteredQuery ;
    public static final String[] SORT_TYPES = {
            "Wage: Higher to Lower",
            "Wage: Lower to Higher",
            "Distance: Nearest to Farthest",
            "Date: Latest to Oldest",
            "Date: Oldest to Latest"
    };
    public static final String[] TITLES = {
            "Dish Washing",
            "House Cleaning",
            "Cloth Washing",
            "Cooking",
            "Construction",
            "Wall paint",
            "Driver",
            "Guard",
            "Shop Worker",
            "Gardening",
            "Miscellaneous"
    };
    public static final int[] ImgUrl = {
            R.drawable.dishwashing,
            R.drawable.cleaning,
            R.drawable.washing,
            R.drawable.cooking,
            R.drawable.construction,
            R.drawable.wallpaint,
            R.drawable.driver,
            R.drawable.guard,
            R.drawable.shopworker,
            R.drawable.gardening,
            R.drawable.misc
    };
    public static final String[] drawerTitlesWorker = {
            "Profile",
            "My Schedule",
            "Holidays",
            "History",
            "Employers",
            "Balance",
            "My Ads"
    };
    //SP variables

    public static String SELECTED_LANGUAGE = "selectedLanguage";
    public static String SELECTED_USER_TYPE = "selectedUserType";

    /**
     * Create main application
     */
    public HopeApp() {

    }

    /**
     * Create main application
     *
     * @param context
     */
    public HopeApp(final Context context) {
        this();
        attachBaseContext(context);
    }


    public static void setSPString(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static void setSPBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    public static void setSPInteger(String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static Integer getSPInteger(String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getInstance());
        return sharedPreferences.getInt(key, 0);
    }

    public static String getSPString(String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getInstance());
        return sharedPreferences.getString(key, "");
    }

    public static Boolean getSPBoolean(String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getInstance());
        return sharedPreferences.getBoolean(key, true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


        parseInit();


    }

    public void parseInit(){
        // Register subclass
        ParseObject.registerSubclass(WorkAd.class);
        ParseUser.registerSubclass(UserInfo.class);
        ParseObject.registerSubclass(EWRelation.class);
        ParseObject.registerSubclass(Schedule.class);
        ParseObject.registerSubclass(Attendance.class);

        // Initialize Crash Reporting.
        ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "JY7mTiuGK4GHacsLmKg3ZlA2ctxcZF6j8Z7S7XVJ", "PX7zmXw36LJC7D7zOiWvYRs4ITG0SrcaVPSfrm5j");


        // ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        //ParseUser.getCurrentUser().saveInBackground();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }

    /**
     * Create main application
     *
     * @param instrumentation
     */
    public HopeApp(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }

    public static HopeApp getInstance() {

        if (instance == null) {
            instance = new HopeApp();
            return instance;

        } else {
            return instance;
        }
    }

    public static void sendPushMessage(final String ReceiverUserId, final String SenderUserId,final String workAdId, final String title, final String msg,final String type) {


        ParseQuery pushQuery = ParseInstallation.getQuery();

        pushQuery.whereEqualTo("userId", ReceiverUserId);

        JSONObject data = new JSONObject();
        try {
            Log.d("raghav uri","com.xplorer.hope.activity.PushNotificationActivity");
            // data.put("uri", Uri.parse("com.xplorer.hope.activity.PushNotificationActivity"));
            data.put("title", title);
            data.put("senderId",SenderUserId );
            data.put("workId",workAdId );
            data.put("message",msg);
            data.put("type",type);

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        //uri
        //title "Job Application"

        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        push.setMessage(msg);
        push.setData(data);

        push.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if(e== null){
                    Toast.makeText(instance,"Request sent!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(instance,"Request not sent!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static void removeEWRelation(String workId, String workerId, String employerId) {
        ParseQuery<EWRelation> query = ParseQuery.getQuery("EWRelation");
        query.whereEqualTo("workID",workId);
        query.whereEqualTo("userID",workerId);
        query.whereEqualTo("employerID",employerId);
        query.findInBackground(new FindCallback<EWRelation>() {
            @Override
            public void done(List<EWRelation> parseObjects, ParseException e) {
                if (e == null) {
                    for(int i=0;i<parseObjects.size();i++) {
                        parseObjects.get(i).deleteInBackground();
                    }
                }
            }
        });
    }

    public static void setEWRelationTrue(String workId, String workerId, String employerId) {
        ParseQuery<EWRelation> query = ParseQuery.getQuery("EWRelation");
        query.whereEqualTo("workID", workId);
        query.whereEqualTo("userID", workerId);
        query.whereEqualTo("employerID", employerId);

        Log.d("hope setEWRelationTrue", workId + ":" + workerId + ":" + employerId);

        query.findInBackground(new FindCallback<EWRelation>() {
            @Override
            public void done(List<EWRelation> parseObjects, ParseException e) {
                if (e == null) {
                    Log.d("hope setEWRelationTrue(done)", String.valueOf(parseObjects.size()));
                    for (int i = 0; i < parseObjects.size(); i++) {
                        Log.d("hope setEWRelationTrue(done)", parseObjects.get(i).getEmployerID());
                        parseObjects.get(i).setApprove(true);
                        parseObjects.get(i).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                            }
                        });
                    }
                }
            }
        });

    }

    public void setWorkAdSortBy(int selection){
        if(filteredQuery == null) {
            filteredQuery = ParseQuery.getQuery("WorkAd");
        }
        switch (selection){
            case 0:{
                filteredQuery.addDescendingOrder("wageHigherLimit");
            }
            break;
            case 1:{
                filteredQuery.addAscendingOrder("wageHigherLimit");
            }
            break;
            case 2:{
                filteredQuery.whereWithinKilometers("location",HopeApp.getMyLocation(),5);
            }
            break;
            case 3:{
                filteredQuery.addDescendingOrder("createdAt");
            }
            break;
            case 4:{
                filteredQuery.addAscendingOrder("createdAt");
            }
            break;
        }
    }

    public void setWorkAdFilter(){

    }
    private static ParseGeoPoint getMyLocation() {
        return null;
    }
}
