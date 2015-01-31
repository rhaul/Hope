package com.xplorer.hope.config;

import android.app.Application;
import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
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
import com.xplorer.hope.adapter.EmployerAdaptor;
import com.xplorer.hope.adapter.ListViewAdapter;
import com.xplorer.hope.adapter.WorkAdaptor;
import com.xplorer.hope.adapter.WorkerAdaptor;
import com.xplorer.hope.object.Attendance;
import com.xplorer.hope.object.EWRelation;
import com.xplorer.hope.object.Schedule;
import com.xplorer.hope.object.UserInfo;
import com.xplorer.hope.object.WorkAd;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HopeApp extends Application {
    // app
    private static HopeApp instance;
    // Debugging tag for the application


    public static HashMap<String, List<WorkAd>> workAdsStorage = new HashMap<String, List<WorkAd>>();

    public static final String APPTAG = "HopeApp";

    public static final String LAST_BEAMED_AT = "lastBeamedAt";
    public ParseQuery<WorkAd> filteredQuery;

    //public static HashMap<String,List<WorkAd>> workAdsStorage = new HashMap<String, List<WorkAd>>();

    public static int sortedBy = 3;
    public static ParseQuery<WorkAd> globalQuery;

    public static final String[] SORT_TYPES = {
            "Wage: Higher to Lower",
            "Wage: Lower to Higher",
            "Date: Latest to Oldest",
            "Date: Oldest to Latest",
            "Distance: Nearest to Farthest"
    };
    public static String[] TITLES = {
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
    public static String[] HindiTITLES = {
            "डिश धोने",
            "घर सफाई",
            "कपड़ा धुलाई",
            "व्यंजन कला",
            "निर्माण",
            "दीवार पेंट",
            "चालक",
            "पहरेदार",
            "दुकान कार्यकर्ता",
            "बागवानी",
            "विविध"
    };

    public static HashMap<String, Integer> CategoryColor = new HashMap();

    public static HashMap<String, String> EnglishToHindi = new HashMap();
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
            "Manage Jobs",
            "Pending Requests",
            "Worker's Attendance",
            "Search in Map",
            "Employers",

    };

    public static final String[] drawerTitlesEmployer = {
            "Profile",
            "Manage Jobs",
            "Pending Requests",
            "Worker's Attendance",
            "Search in Map",


    };

    public static String[] drawerCandidate = {};



    //SP variables

    public static String SELECTED_LANGUAGE = "selectedLanguage";
    public static String SELECTED_USER_TYPE = "selectedUserType";
    public static int filteredBy = 0;


    public static HashMap myPendingWorksIds = new HashMap();
    public static HashMap myPendingEmployerIds = new HashMap();

    public static HashMap myWorksIds = new HashMap();
    public static HashMap myEmployerIds = new HashMap();

    public static ProgressDialog pd;

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

    public static void setSPLong(String key, long value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getSPLong(String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getInstance());
        return sharedPreferences.getLong(key, 0);
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

        FillHindiDictionary();
        SELECTED_LANGUAGE = "hindi";
        parseInit();
        CatColorInit();

    }

    public void FillHindiDictionary() {
        EnglishToHindi.put("Apply", "प्रार्थना-पत्र देना");
        EnglishToHindi.put("Pending", "रूका हुआ");
        EnglishToHindi.put("Filter", "छाँटना");
        EnglishToHindi.put("Filters", "छाँटना");
        EnglishToHindi.put("Sort", "क्रम में लगाना");
        EnglishToHindi.put("Sort By", "क्रम में लगाना");


        EnglishToHindi.put("My Employers", "मेरे नियोक्ता");
        EnglishToHindi.put("Profile", "पहचान");
        EnglishToHindi.put("Manage Jobs", "काम प्रबंध");
        EnglishToHindi.put("Pending Requests", "अनुरोध लंबित");
        EnglishToHindi.put("Worker's Attendance", "कार्यकर्ता की उपस्थिति");
        EnglishToHindi.put("Employers", "नियोक्ता");

        EnglishToHindi.put("Date of Birth", "जन्मतिथि");
        EnglishToHindi.put("Name", "नाम");
        EnglishToHindi.put("Adress", "पता");
        EnglishToHindi.put("Phone Number (10 digit)", "फोन नंबर (10 अंक)");
        EnglishToHindi.put("Male", "पुरुष");
        EnglishToHindi.put("Female", "स्त्री");
        EnglishToHindi.put("Interest", "पसंद");

        EnglishToHindi.put("Expected Wages\n(per month)", "वेतन\n"+"(प्रति माह)");
        EnglishToHindi.put("Add Category", "श्रेणी में जोड़ें");
        EnglishToHindi.put("Description", "विवरण");

        EnglishToHindi.put("One Day", "एक दिन");
        EnglishToHindi.put("Monthly", "मासिक");
        EnglishToHindi.put("Custom", "विकल्प");
        EnglishToHindi.put("Once a day", "एक दिन में एक बार");
        EnglishToHindi.put("Twice a day", "दिन में दो बार");
        EnglishToHindi.put("Lower Limit", "निचली सीमा");
        EnglishToHindi.put("Upper Limit", "ऊपरी सीमा");

        EnglishToHindi.put("Starting Time", "समय शुरू");
        EnglishToHindi.put("Ending Time", "समय में अंतिम");

        EnglishToHindi.put("Starting Date", "शुरू करने की तारीख");
        EnglishToHindi.put("Ending Date", "अंतिम दिनांक");

        EnglishToHindi.put("License Type:", "लाइसेंस के प्रकार:");
        EnglishToHindi.put("Four wheeler", "चार टायर वाहन");
        EnglishToHindi.put("Heavy", "भारी वाहन");
        EnglishToHindi.put("Language known:", "भाषा जाना जाता है:");
        EnglishToHindi.put("English", "अंग्रेजी");
        EnglishToHindi.put("Hindi", "हिंदी");
        EnglishToHindi.put("Set My Work Location", "मेरा कार्य स्थान निर्धारित");
        EnglishToHindi.put("Cancel", "रद्द करना");
        EnglishToHindi.put("Job Advertisement", "नौकरी विज्ञापन");
        EnglishToHindi.put("Attendance", "उपस्थिति");
        EnglishToHindi.put("My Employers", "मेरे नियोक्ता");
        EnglishToHindi.put("Set Location", "स्थान प्राप्त");
        EnglishToHindi.put("Search nearby jobs", "पास में नौकरियां खोजें");
        EnglishToHindi.put("Processing...", "प्रसंस्करण ...");

        EnglishToHindi.put("Please wait.", "कृपया प्रतीक्षा करें।");
        EnglishToHindi.put("No Internet Connectivity\n Tap to retry", "उपलब्ध नहीं इंटरनेट कनेक्टिविटी\n"+
                "पुन: प्रयास करने के लिए टैप");
        EnglishToHindi.put("Notification", "नोटिफिकेशन");
        EnglishToHindi.put("My Job Ads", "मेरी नौकरी के विज्ञापन");
        EnglishToHindi.put("Age", "आयु");
        EnglishToHindi.put("Attendance", "उपस्थिति");
        EnglishToHindi.put("Attendance", "उपस्थिति");

        EnglishToHindi.put("Dish Washing", "डिश धोने");
        EnglishToHindi.put("House Cleaning", "घर सफाई");
        EnglishToHindi.put("Cloth Washing", "कपड़ा धुलाई");
        EnglishToHindi.put("Cooking", "व्यंजन कला");
        EnglishToHindi.put("Construction", "निर्माण");
        EnglishToHindi.put("Construction", "निर्माण");
        EnglishToHindi.put("Driver", "चालक");
        EnglishToHindi.put("Guard", "पहरेदार");
        EnglishToHindi.put("Shop Worker", "दुकान कार्यकर्ता");
        EnglishToHindi.put("Gardening", "बागवानी");
        EnglishToHindi.put("Miscellaneous", "विविध");
        EnglishToHindi.put("Wall paint", "दीवार पेंट");

        EnglishToHindi.put("Accept", "स्वीकार");
        EnglishToHindi.put("Decline", "नकारना");

        //नकारना
        EnglishToHindi.put("Map", "नक़्शा");
        EnglishToHindi.put("Address", "पता");
        EnglishToHindi.put("Description", "विवरण");


        EnglishToHindi.put("Job Type", "कार्य का प्रकार");
        EnglishToHindi.put("Timings", "समय");
        EnglishToHindi.put("Wages (Rupees)", "वेतन (रुपये में)");
        EnglishToHindi.put("Slot", "निर्धारित समय");
        EnglishToHindi.put("Description", "विवरण");
        EnglishToHindi.put("Worker", "कार्यकर्ता");
        EnglishToHindi.put("Workers", "कार्यकर्ता");

        EnglishToHindi.put("Set Location", "स्थान चुनाव");

        EnglishToHindi.put("Long press me to drag to your Work Location", "आपके कार्य स्थान तक खींचने के लिए मुझे दबाएँ");
        EnglishToHindi.put("Long press me to drag to your Search Location", "आपके खोज स्थान तक खींचने के लिए मुझे दबाएँ");

        EnglishToHindi.put("Set My Work Location", "मेरा कार्य स्थान सेट करें");

        EnglishToHindi.put("Are you sure you want to apply for this work", "क्या आप इस काम के लिए आवेदन करना चाहते हैं");
        EnglishToHindi.put("Search in Map", "नक्शे में खोज");


    }

    public void parseInit() {
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


    public void CatColorInit() {

        CategoryColor.put("Dish Washing", R.color.LightGold);
        CategoryColor.put("House Cleaning", R.color.LightOrange);
        CategoryColor.put("Cloth Washing", R.color.LightViolet);
        CategoryColor.put("Cooking", R.color.Green);
        CategoryColor.put("Construction", R.color.SkyBlue);
        CategoryColor.put("Wall paint", R.color.Red);
        CategoryColor.put("Driver", R.color.Green_2);
        CategoryColor.put("Guard", R.color.Blue_1);
        CategoryColor.put("Shop Worker", R.color.LightViolet);
        CategoryColor.put("Gardening", R.color.Sienna);
        CategoryColor.put("Miscellaneous", R.color.gray8);


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

    public static void sendPushMessage(final String ReceiverUserId, final String SenderUserId, final String workAdId, final String title, final String msg, final String type) {


        ParseQuery pushQuery = ParseInstallation.getQuery();

        pushQuery.whereEqualTo("userId", ReceiverUserId);

        JSONObject data = new JSONObject();
        try {
            Log.d("raghav uri", "com.xplorer.hope.activity.PushNotificationActivity");
            // data.put("uri", Uri.parse("com.xplorer.hope.activity.PushNotificationActivity"));
            data.put("title", title);
            data.put("senderId", SenderUserId);
            data.put("workId", workAdId);
            data.put("message", msg);
            data.put("type", type);

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
                if (e == null) {
                    Toast.makeText(instance, "Request sent!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(instance, "Request not sent!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static void removeEWRelation(String workId, String workerId, String employerId) {
        ParseQuery<EWRelation> query = ParseQuery.getQuery("EWRelation");
        query.whereEqualTo("workID", workId);
        query.whereEqualTo("userID", workerId);
        query.whereEqualTo("employerID", employerId);
        query.findInBackground(new FindCallback<EWRelation>() {
            @Override
            public void done(List<EWRelation> parseObjects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
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

    public void setWorkAdSortBy(int selection) {

        sortedBy = selection;
    }

    public void setFilters(int[] filters) {
        for (int i = 0; i < filters.length; i++) {
            filterValues[i] = filters[i];
        }
        if (filteredBy == 0) {
            filteredBy = 1;
        } else {
            filteredBy = 0;
        }
    }

    public int filterValues[] = new int[]{1, 1, 1,
            1, 1,
            0, 40};

    public ParseQuery<WorkAd> applyFilteredQuery() {
        ParseQuery<WorkAd> filteredQuery = ParseQuery.getQuery("WorkAd");

        //filter

        List<String> dateTypes = new ArrayList<String>();
        List<String> frequency = new ArrayList<String>();
        if (filterValues[0] == 1) {
            dateTypes.add("One Day");
        }
        if (filterValues[1] == 1) {
            dateTypes.add("Monthly");
        }
        if (filterValues[2] == 1) {
            dateTypes.add("Custom");
        }
        if (filterValues[3] == 1) {
            frequency.add("Once a day");
        }
        if (filterValues[4] == 1) {
            frequency.add("Twice a day");
        }
        filteredQuery.whereContainedIn("dateType", dateTypes);
        filteredQuery.whereContainedIn("timeType", frequency);
        filteredQuery.whereGreaterThanOrEqualTo("wageLowerLimit", filterValues[5] * 500).whereLessThanOrEqualTo("wageHigherLimit", filterValues[6] * 500);


        // sort
        switch (sortedBy) {
            case 0: {
                filteredQuery.orderByDescending("wageHigherLimit");
            }
            break;
            case 1: {
                filteredQuery.orderByAscending("wageHigherLimit");

            }
            break;
            case 2: {
                filteredQuery.orderByDescending("createdAt");
            }
            break;
            case 3: {
                filteredQuery.orderByAscending("createdAt");

            }
            break;
        }
        return filteredQuery;
    }


    public void onPreExecute(Context context) {
        pd = new ProgressDialog(context);
        pd.setTitle(getHindiLanguage("Processing...", null, null));
        pd.setMessage(getHindiLanguage("Please wait.", null, null));
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();


    }


    public String getUpperCaseString(String myString) {
        String upperString = myString.substring(0, 1).toUpperCase() + myString.substring(1);
        upperString = upperString.replace("\n", " ");


        return upperString;
    }


    public String getHindiLanguage(String txtToConvert, BaseAdapter BA, String type) {

        if (SELECTED_LANGUAGE == "hindi") {
            if (EnglishToHindi.containsKey(txtToConvert) && EnglishToHindi.get(txtToConvert) != null) {
                //Log.d("EnglishToHindi.containsKey("+txtToConvert+")", EnglishToHindi.get(txtToConvert));
                return EnglishToHindi.get(txtToConvert);
            } else {
                EnglishToHindi.put(txtToConvert, getUpperCaseString(txtToConvert));
                if(BA!=null) new ATHttpRequest(txtToConvert, BA, type).execute();

                Log.d("ATHttpRequest", txtToConvert);
                return getUpperCaseString(txtToConvert);
            }
        }

        return getUpperCaseString(txtToConvert);
    }


    public class ATHttpRequest extends AsyncTask<String, Void, String> {

        public String response;
        public String responseArray[];
        String txtToConvert;
        int elementId;
        String type;


        String urlStr = "http://www.rogueinstincts.com/MajorProject/Translator.php";
        BaseAdapter BA;

        public ATHttpRequest(String txtToConvert, BaseAdapter BA, String type) {

            this.txtToConvert = txtToConvert;

            this.type = type;

            String query;
            NameValuePair item = new BasicNameValuePair("txtToConvert", txtToConvert);
            List<NameValuePair> LItem = new ArrayList<NameValuePair>();
            LItem.add(item);
            query = URLEncodedUtils.format(LItem, "utf-8");
            Log.d("query URLEncodedUtils", query);
            query = query.replace("\n", "%0A");
            Log.d("query URLEncodedUtils", query);
            urlStr += "?" + query;
            this.BA = BA;
        }

        @Override
        protected String doInBackground(String... params) {


            HttpURLConnection connection;
            OutputStreamWriter request = null;

            URL url = null;

            try {
                url = new URL(urlStr);
                Log.d("url", urlStr);

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);

                request = new OutputStreamWriter(connection.getOutputStream());
                //request.write(parameters);

                request.flush();
                request.close();

                String line = "";
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                // Response from server after login process will be stored in response variable.
                response = sb.toString();
                // You can perform UI operations here
                isr.close();
                reader.close();


            } catch (IOException e) {


                Log.e("httpRequest Error:", e.toString());

            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (response == null) {


            } else {
                Log.d("httpRequest Response:", response);


                if (response != null) {
                    Log.d("response.substring",response.substring(response.length() - 1, response.length()));

                    if(response.substring(response.length()-1, response.length()).equalsIgnoreCase("\n")) {
                        response = response.substring(0, response.length() - 1);
                    }
                    Log.d("httpRequest responseArray:", response);
                    EnglishToHindi.put(txtToConvert, response);


                    if (type.equalsIgnoreCase("LV")) {
                        ((ListViewAdapter) BA).notifyDataSetChanged();
                    } else if (type.equalsIgnoreCase("WA")) {
                        ((WorkAdaptor) BA).notifyDataSetChanged();

                    } else if (type.equalsIgnoreCase("WRA")) {
                        ((WorkerAdaptor) BA).notifyDataSetChanged();


                    } else if (type.equalsIgnoreCase("EA")) {
                        ((EmployerAdaptor) BA).notifyDataSetChanged();

                    } else if (type.equalsIgnoreCase("CB")) {
                        ((ListViewAdapter) BA).notifyDataSetChanged();

                    }

                }

            }
        }

    }


}
