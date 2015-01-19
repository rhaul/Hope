package com.xplorer.hope.config;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
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

import java.util.HashMap;
import java.util.List;

public class HopeApp extends Application {
    // app
    private static HopeApp instance;
    // Debugging tag for the application
    public static HashMap<String,List<WorkAd>> workAdsStorage = new HashMap<String, List<WorkAd>>();
    public static final String APPTAG = "HopeApp";
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
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseInstallation.getCurrentInstallation().put("userId", "unregistered");
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
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

    public static void sendJobAppliedRequest(final String userId) {

        ParseQuery<ParseUser> query;
        query = ParseUser.getQuery();

        query.getInBackground(userId,   new GetCallback<ParseUser>() {
            public void done(ParseUser object, ParseException e) {
                if (e == null) {
                    UserInfo usr = (UserInfo) object;

                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    Log.d("raghav", userId);
                    pushQuery.whereEqualTo("userId", userId);
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    String message = currentUser.getString("name") + " says Hi!";

                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery); // Set our Installation query
                    push.setMessage(message);

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
                }else{
                    Toast.makeText(instance,"Request not sent!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
