package com.xplorer.hope.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.edmodo.rangebar.RangeBar;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.xplorer.hope.R;
import com.xplorer.hope.adapter.NavDrawerListAdapter;
import com.xplorer.hope.adapter.TabsPagerAdapter;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.fragment.CategoryFragment;
import com.xplorer.hope.object.Attendance;
import com.xplorer.hope.object.EWRelation;
import com.xplorer.hope.object.UserInfo;
import com.xplorer.hope.object.WorkAd;
import com.xplorer.hope.service.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.nfc.NdefRecord.createMime;


public class MainActivity extends FragmentActivity implements QuickReturnInterface, NfcAdapter.CreateNdefMessageCallback,
        NfcAdapter.OnNdefPushCompleteCallback {

    @InjectView(R.id.tabs)
    PagerSlidingTabStrip pts_titleBar;
    @InjectView(R.id.pager)
    ViewPager vp_pager;
    @InjectView(R.id.tv_main_attendance)
    TextView tv_attendance;

    static public int currentFragment = 0;

    boolean isSetUp = false;
    private TabsPagerAdapter pagerAdapter;

    //---- drawer---//
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavDrawerListAdapter navDrawerListAdapter;
    Boolean isInternetPresent;
    ConnectionDetector cd;


    // region Member Variables
    @InjectView(R.id.ll_main_quick_return_footer)
    LinearLayout ll_quick_return_footer;
    @InjectView(R.id.b_main_sort)
    Button b_sort;
    @InjectView(R.id.b_main_filter)
    Button b_filter;


    //Menu View

    MenuItem searchMenuItem;
    SearchView searchView;

    //NFC

    NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
        } else {
            // Register callback to set NDEF message
            mNfcAdapter.setNdefPushMessageCallback(this, this);
            // Register callback to listen for message-sent success
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
        //showAttendance();
        // create an intent with tag data and deliver to this activity
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[] { ndefIntent };
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }

        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };

    }

    // region QuickReturnInterface Methods
    @Override
    public LinearLayout getFooter() {
        return ll_quick_return_footer;
    }


    @Override
    public void onPause() {
        super.onPause();
/*
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);

        Log.d("NFC Val", getIntent().getAction());
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()) && ParseUser.getCurrentUser()!=null && ((UserInfo)ParseUser.getCurrentUser()).getType().equalsIgnoreCase("Employer")) {
            processIntent(getIntent());
        }/*
       *//* // Check to see that the Activity started due to an Android Beam
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()) && ParseUser.getCurrentUser()!=null && ((UserInfo)ParseUser.getCurrentUser()).getType().equalsIgnoreCase("Employer")) {

            Log.d("NFC Val", getIntent().getAction());
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()) && ParseUser.getCurrentUser()!=null && ((UserInfo)ParseUser.getCurrentUser()).getType().equalsIgnoreCase("Employer")) {

                Log.d("NFC", "YES");
                String action = intent.getAction();
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                String s = action + "\n\n" + tag.toString();

                // parse through all NDEF messages and their records and pick text type only
                Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (data != null) {
                    try {
                        for (int i = 0; i < data.length; i++) {
                            NdefRecord[] recs = ((NdefMessage) data[i]).getRecords();
                            for (int j = 0; j < recs.length; j++) {
                                if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                        Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
                                    byte[] payload = recs[j].getPayload();
                                    String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                                    int langCodeLen = payload[0] & 0077;

                                    s += ("\n\nNdefMessage[" + i + "], NdefRecord[" + j + "]:\n\"" +
                                            new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1,
                                                    textEncoding) + "\"");
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e("TagDispatch", e.toString());
                    }
                }
                Toast.makeText(this, s, Toast.LENGTH_LONG).show();
            }*//*
*//*

            Log.d("NFC","YES");
            processIntent(getIntent());*//*
        }
        //Collapsing Search Bar if came back from search activity
*/
        if (searchMenuItem != null) {
            searchMenuItem.collapseActionView();
            searchView.setQuery("", false);
        }

        // language and user type selection
        if (!HopeApp.getSPString(HopeApp.SELECTED_LANGUAGE).equalsIgnoreCase("hindi") && !HopeApp.getSPString(HopeApp.SELECTED_LANGUAGE).equalsIgnoreCase("english")) {
            startActivity(new Intent(this, SelectLangActivity.class));
            return;
        } else if (!HopeApp.getSPString(HopeApp.SELECTED_USER_TYPE).equalsIgnoreCase("worker") && !HopeApp.getSPString(HopeApp.SELECTED_USER_TYPE).equalsIgnoreCase("employer")) {
            startActivity(new Intent(this, SelectUserTypeActivity.class));
            return;
        }
        //-------------UserInfo-------------//
        UserInfo user = (UserInfo) ParseUser.getCurrentUser();
        if (user == null) {
            cd = new ConnectionDetector(getApplicationContext());
            isInternetPresent = cd.isConnectingToInternet();

            if (isInternetPresent) {
                startActivity(new Intent(this, SignUpActivity.class).putExtra("from", "singup"));
                return;
            } else {
                startActivity(new Intent(this, NoInternetActivity.class));
                return;
            }
        }

        //-------------ParseInstallation-------------//


        //------USER LOST/UNREGISTERED PARSE PUSH INFO ------//
        if (ParseInstallation.getCurrentInstallation() == null || ParseInstallation.getCurrentInstallation().get("userId") == null || ParseInstallation.getCurrentInstallation().get("userId").toString().equalsIgnoreCase("unregistered")) {
            cd = new ConnectionDetector(getApplicationContext());
            isInternetPresent = cd.isConnectingToInternet();

            if (isInternetPresent) {
                parseInstallation();
            } else {
                startActivity(new Intent(this, NoInternetActivity.class));
                return;
            }


        }


        //-----SET UP MAIN ACTIVITY------------//
        if (!isSetUp) {//which sets up man activity

            getMyWorkids();
            setUpDrawer();
            setUpMainActivity();
            isSetUp = true;
        }
    }

    public void parseInstallation() {
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    UserInfo usr = (UserInfo) ParseUser.getCurrentUser();
                    if (usr != null) {
                        ParseInstallation.getCurrentInstallation().put("userId", usr.getObjectId());
                        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.d("hope parseInstallation", e.toString());
                                    if (e.toString().equalsIgnoreCase("object not found for update")) {
                                        deleteInstallationCache(MainActivity.this);
                                        parseInstallation();
                                    }
                                }
                            }
                        });
                    } else {
                        Log.e("hope parseInstallation", "User found null");
                        Intent intent = getIntent();
                        startActivity(intent);
                        finish();
                        return;

                    }
                } else {
                    Log.d("hope parseInstallation", e.toString());
                    if (e.toString().equalsIgnoreCase("object not found for update")) {
                        deleteInstallationCache(MainActivity.this);
                        parseInstallation();
                    }
                }
            }
        });
    }


    public static boolean deleteInstallationCache(Context context) {
        boolean deletedParseFolder = false;
        File cacheDir = context.getCacheDir();
        File parseApp = new File(cacheDir.getParent(), "app_Parse");
        File installationId = new File(parseApp, "installationId");
        File currentInstallation = new File(parseApp, "currentInstallation");
        if (installationId.exists()) {
            deletedParseFolder = deletedParseFolder || installationId.delete();
        }
        if (currentInstallation.exists()) {
            deletedParseFolder = deletedParseFolder && currentInstallation.delete();
        }
        return deletedParseFolder;
    }

    private void setUpMainActivity() {

        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), loadFragments());
        vp_pager.setAdapter(pagerAdapter);
        pts_titleBar.setViewPager(vp_pager);

        setBarColors(0);
        pts_titleBar.setTextColor(getResources().getColor(R.color.white));
        pts_titleBar.setIndicatorColor(getResources().getColor(R.color.white));
        pts_titleBar.setDividerColor(getResources().getColor(R.color.white));

        pts_titleBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setBarColors(position);
                // pagerAdapter.getFragment(position).checkIfFilterApplied();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp_pager.setOffscreenPageLimit(1);


        vp_pager.setCurrentItem(0);
        b_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortDialog();
            }
        });
        b_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });


    }


    public void setBarColors(int position) {
        currentFragment = position;
        Integer colorVal = HopeApp.CategoryColor.get(HopeApp.TITLES[position]);
        pts_titleBar.setBackgroundColor(getResources().getColor(colorVal));
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorVal)));
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(true);
        b_sort.setBackgroundColor(getResources().getColor(colorVal));
        b_filter.setBackgroundColor(getResources().getColor(colorVal));

    }

    public void getMyWorkids() {
        String myWorkerId = ((UserInfo) ParseUser.getCurrentUser()).getObjectId();

        ParseQuery<EWRelation> query = ParseQuery.getQuery("EWRelation");
        query.whereEqualTo("userID", myWorkerId);

        Log.d("hope getMyWorkids", myWorkerId);

        query.findInBackground(new FindCallback<EWRelation>() {
            @Override
            public void done(List<EWRelation> parseObjects, ParseException e) {

                if (e == null) {
                    Log.d("hope getMyWorkids(done)", String.valueOf(parseObjects.size()));
                    for (int i = 0; i < parseObjects.size(); i++) {
                        if (parseObjects.get(i).getApprove() == true) {
                            HopeApp.myWorksIds.put(parseObjects.get(i).getWorkID(), parseObjects.get(i).getWorkID());
                            HopeApp.myEmployerIds.put(parseObjects.get(i).getEmployerID(), parseObjects.get(i).getEmployerID());
                        } else {
                            HopeApp.myPendingWorksIds.put(parseObjects.get(i).getWorkID(), parseObjects.get(i).getWorkID());
                            HopeApp.myPendingEmployerIds.put(parseObjects.get(i).getEmployerID(), parseObjects.get(i).getEmployerID());

                        }
                        Log.d("hope getMyWorkids(workId)", parseObjects.get(i).getWorkID());

                    }

                    if (pagerAdapter != null && pagerAdapter.getFragment(vp_pager.getCurrentItem()).lva != null)
                        pagerAdapter.getFragment(vp_pager.getCurrentItem()).lva.notifyDataSetChanged();

                }
                //setUpMainActivity();
            }
        });

    }


    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filters");
        View filters = getLayoutInflater().inflate(R.layout.dialog_filter, null);
        builder.setView(filters);
        final LinearLayout ll_buttons = (LinearLayout) filters.findViewById(R.id.ll_filter_buttons);
        final CheckBox cb_filter_wt_oneDay = (CheckBox) filters.findViewById(R.id.cb_filter_wt_oneDay);
        final CheckBox cb_filter_wt_monthly = (CheckBox) filters.findViewById(R.id.cb_filter_wt_monthly);
        final CheckBox cb_filter_wt_custom = (CheckBox) filters.findViewById(R.id.cb_filter_wt_custom);
        final CheckBox cb_filter_f_once = (CheckBox) filters.findViewById(R.id.cb_filter_f_once);
        final CheckBox cb_filter_f_twice = (CheckBox) filters.findViewById(R.id.cb_filter_f_twice);
        final RangeBar rb_filter_wageLimit = (RangeBar) filters.findViewById(R.id.rb_filter_wageLimit);
        final TextView tv_filter_wl_lower = (TextView) filters.findViewById(R.id.tv_filter_wl_lower);
        final TextView tv_filter_wl_higher = (TextView) filters.findViewById(R.id.tv_filter_wl_higher);

        for (int i = 0; i < HopeApp.getInstance().filterValues.length; i++) {
            switch (i) {
                case 0: {
                    if (HopeApp.getInstance().filterValues[i] == 1) {
                        cb_filter_wt_oneDay.setChecked(true);
                    } else {
                        cb_filter_wt_oneDay.setChecked(false);
                    }
                }
                break;
                case 1: {
                    if (HopeApp.getInstance().filterValues[i] == 1) {
                        cb_filter_wt_monthly.setChecked(true);
                    } else {
                        cb_filter_wt_monthly.setChecked(false);
                    }
                }
                break;
                case 2: {
                    if (HopeApp.getInstance().filterValues[i] == 1) {
                        cb_filter_wt_custom.setChecked(true);
                    } else {
                        cb_filter_wt_custom.setChecked(false);
                    }
                }
                break;
                case 3: {
                    if (HopeApp.getInstance().filterValues[i] == 1) {
                        cb_filter_f_once.setChecked(true);
                    } else {
                        cb_filter_f_once.setChecked(false);
                    }
                }
                break;
                case 4: {
                    if (HopeApp.getInstance().filterValues[i] == 1) {
                        cb_filter_f_twice.setChecked(true);
                    } else {
                        cb_filter_f_twice.setChecked(false);
                    }
                }
                break;
                case 5: {
                    tv_filter_wl_lower.setText("Lower: ₹ " + HopeApp.getInstance().filterValues[i] * 500);
                }
                break;
                case 6: {
                    tv_filter_wl_higher.setText("Higher: ₹ " + HopeApp.getInstance().filterValues[i] * 500);
                    rb_filter_wageLimit.setThumbIndices(HopeApp.getInstance().filterValues[5], HopeApp.getInstance().filterValues[6]);
                }
                break;
            }
        }
        rb_filter_wageLimit.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int i, int i2) {
                tv_filter_wl_lower.setText("Lower: ₹ " + i * 500);
                tv_filter_wl_higher.setText("Higher: ₹ " + i2 * 500);
            }
        });

        builder.setPositiveButton("Apply",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ParseQuery<WorkAd> query = ParseQuery.getQuery("WorkAd");
                        int[] filters = new int[7];
                        if (cb_filter_wt_oneDay.isChecked()) {
                            filters[0] = 1;
                        } else {
                            filters[0] = 0;
                        }
                        if (cb_filter_wt_monthly.isChecked()) {
                            filters[1] = 1;
                        } else {
                            filters[1] = 0;
                        }
                        if (cb_filter_wt_custom.isChecked()) {
                            filters[2] = 1;
                        } else {
                            filters[2] = 0;
                        }
                        if (cb_filter_f_once.isChecked()) {
                            filters[3] = 1;
                        } else {
                            filters[3] = 0;
                        }
                        if (cb_filter_f_twice.isChecked()) {
                            filters[4] = 1;
                        } else {
                            filters[4] = 0;
                        }
                        filters[5] = rb_filter_wageLimit.getLeftIndex();
                        filters[6] = rb_filter_wageLimit.getRightIndex();
                        query.whereGreaterThanOrEqualTo("wageLowerLimit", rb_filter_wageLimit.getLeftIndex() * 500).whereLessThanOrEqualTo("wageHigherLimit", rb_filter_wageLimit.getRightIndex() * 500);
                        HopeApp.getInstance().setFilters(filters);
                        //  pagerAdapter.getFragment(vp_pager.getCurrentItem()).checkIfFilterApplied();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setNeutralButton("Reset",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                cb_filter_wt_oneDay.setChecked(true);
                                cb_filter_wt_monthly.setChecked(true);
                                cb_filter_wt_custom.setChecked(true);
                                cb_filter_f_once.setChecked(true);
                                cb_filter_f_twice.setChecked(true);
                                rb_filter_wageLimit.setThumbIndices(0, 40);
                                tv_filter_wl_lower.setText("Lower: ₹ " + 0 * 500);
                                tv_filter_wl_higher.setText("Higher: ₹ " + 40 * 500);
                            }
                        });
        builder.show();


    }

    private void showSortDialog() {
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_category_list);
        // Set dialog title
        dialog.setTitle("Select Filter");
        ListView lvD = (ListView) dialog.findViewById(R.id.lv_category_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, HopeApp.SORT_TYPES);
        lvD.setAdapter(adapter);
        lvD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 4) {
                    HopeApp.getInstance().setWorkAdSortBy(i);
                } else {
                    startActivity(new Intent(MainActivity.this, NearbyWorksActivity.class));
                }
                //  pagerAdapter.getFragment(vp_pager.getCurrentItem()).checkIfFilterApplied();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setUpDrawer() {
        if (HopeApp.getSPString(HopeApp.SELECTED_USER_TYPE).equalsIgnoreCase("worker")) {
            HopeApp.drawerCandidate = HopeApp.drawerTitlesWorker;
        } else {
            HopeApp.drawerCandidate = HopeApp.drawerTitlesEmployer;

        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        // setting the nav drawer list adapter
        navDrawerListAdapter = new NavDrawerListAdapter(getApplicationContext());
        mDrawerList.setAdapter(navDrawerListAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    startActivity(new Intent(MainActivity.this, SignUpActivity.class).putExtra("from", "menu"));
                } else if (i == 1) {
                    startActivity(new Intent(MainActivity.this, WorkActivity.class));
                } else if (i == 2) {
                    startActivity(new Intent(MainActivity.this, PendingActivity.class));
                } else if (i == 3) {
                    startActivity(new Intent(MainActivity.this, AttendanceActivity.class));
                } else if (i == 4) {
                    startActivity(new Intent(MainActivity.this, EmpolyerActivity.class));
                }


            }
        });


        // vp_pager.getCurrentItem()
        //getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_launcher, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                //invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                //invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        ComponentName cn = new ComponentName(this, SearchActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));


        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addAd) {
            startActivity(new Intent(this, AddActivity.class).putExtra("title", "add"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<CategoryFragment> loadFragments() {
        List<CategoryFragment> list = new ArrayList<CategoryFragment>();
        for (int i = 0; i < 11; i++) {
            list.add(new CategoryFragment().newInstance(i));
        }
        return list;
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {

        if(ParseUser.getCurrentUser() != null ) {
            long millis = System.currentTimeMillis() - HopeApp.getInstance().getSPLong(HopeApp.LAST_BEAMED_AT);
            int minutes = (int) (millis / (1000*60*60));
            if(minutes>=5) {
                JSONObject json = new JSONObject();
                try {
                    json.put("workerID", ((UserInfo) ParseUser.getCurrentUser()).getObjectId().toString());
                    json.put("name", ((UserInfo) ParseUser.getCurrentUser()).getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NdefMessage msg = new NdefMessage(
                        new NdefRecord[]{createMime(
                                "application/vnd.com.xplorer.hope", json.toString().getBytes()),
                                /**
                                 * The Android Application Record (AAR) is commented out. When a device
                                 * receives a push with an AAR in it, the application specified in the AAR
                                 * is guaranteed to run. The AAR overrides the tag dispatch system.
                                 * You can add it back in to guarantee that this
                                 * activity starts when receiving a beamed message. For now, this code
                                 * uses the tag dispatch system.
                                 */
                                NdefRecord.createApplicationRecord("com.xplorer.hope")
                        });
                return msg;
            }else{
                Toast.makeText(this,"Try After "+(5-minutes) , Toast.LENGTH_LONG).show();
            }
        }
        return null;
    }



    private static final int ATTENDANCE_MARKED = 1;

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        // A handler is needed to send messages to the activity when this
        // callback occurs, because it happens from a binder thread
        mHandler.obtainMessage(ATTENDANCE_MARKED).sendToTarget();

    }


    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        if(msg!= null){
            String messg = new String(msg.getRecords()[0].getPayload());
            // record 0 contains the MIME type, record 1 is the AAR, if present
            Toast.makeText(this,messg , Toast.LENGTH_LONG).show();

            JSONObject jo = null;
            try {
                jo = new JSONObject(messg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                markAttendance(jo.get("workerID").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("NFC Rec", getIntent().getAction());
        }
    }


    public static String getCurrentDateStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    private void markAttendance(String workerID) {
        HopeApp.getInstance().onPreExecute(this);
        Attendance rel = new Attendance();
        rel.setWorkerID(workerID);
        rel.setDate(getCurrentDateStamp());
        rel.setTime(getCurrentTimeStamp());
        rel.setEmployerID(ParseUser.getCurrentUser().getObjectId());
        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        rel.setACL(acl);
        rel.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();
                    HopeApp.getInstance().setSPLong(HopeApp.LAST_BEAMED_AT,System.currentTimeMillis());
                } else {
                    Toast.makeText(MainActivity.this, "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
                }

                HopeApp.pd.cancel();
            }
        });
    }

    /**
     * This handler receives a message from onNdefPushComplete
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ATTENDANCE_MARKED:
                    showAttendance();
                    break;
            }
        }
    };

    public void showAttendance() {
        if( ParseUser.getCurrentUser() != null&&((UserInfo)ParseUser.getCurrentUser()).getType().equalsIgnoreCase("Worker") ) {
            tv_attendance.setText(((UserInfo) ParseUser.getCurrentUser()).getName() + "\n" +
                    " your attendance \n" +
                    "has been marked");
            final Animation zoomin = AnimationUtils.loadAnimation(HopeApp.getInstance(), R.anim.zoom_in);
            final Animation zoomout = AnimationUtils.loadAnimation(HopeApp.getInstance(), R.anim.zoom_out);
            tv_attendance.startAnimation(zoomin);
            tv_attendance.setVisibility(View.VISIBLE);
            zoomin.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            tv_attendance.startAnimation(zoomout);
                            tv_attendance.setVisibility(View.GONE);
                        }
                    };

                    Handler h = new Handler();
                    h.postDelayed(r, 2000);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        //Toast.makeText(getApplicationContext(), "Your attendance is marked!",Toast.LENGTH_LONG).show();
    }
}
