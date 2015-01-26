package com.xplorer.hope.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.xplorer.hope.R;
import com.xplorer.hope.adapter.NavDrawerListAdapter;
import com.xplorer.hope.adapter.TabsPagerAdapter;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.EWRelation;
import com.xplorer.hope.object.UserInfo;
import com.xplorer.hope.service.ConnectionDetector;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends FragmentActivity implements QuickReturnInterface {

    @InjectView(R.id.tabs)
    PagerSlidingTabStrip pts_titleBar;
    @InjectView(R.id.pager)
    ViewPager vp_pager;

    static public int currentFragment=0;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);


    }

    // region QuickReturnInterface Methods
    @Override
    public LinearLayout getFooter() {
        return ll_quick_return_footer;
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        if (!isSetUp) {
            getMyWorkids();//which sets up man activity
            setUpDrawer();
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

        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp_pager.setOffscreenPageLimit(3);

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

            }
        });

    }

    public void setBarColors(int position){
        currentFragment=position;
        Integer colorVal =  HopeApp.CategoryColor.get(HopeApp.TITLES[position]);
        pts_titleBar.setBackgroundColor(getResources().getColor(colorVal));
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorVal)));
    }

    public void getMyWorkids(){
        String myWorkerId= ((UserInfo) ParseUser.getCurrentUser()).getObjectId();

        ParseQuery<EWRelation> query = ParseQuery.getQuery("EWRelation");
        query.whereEqualTo("userID", myWorkerId);

        Log.d("hope getMyWorkids", myWorkerId);

            query.findInBackground(new FindCallback<EWRelation>() {
                @Override
                public void done(List<EWRelation> parseObjects, ParseException e) {

                    if (e == null) {
                        Log.d("hope getMyWorkids(done)", String.valueOf(parseObjects.size()));
                        for (int i = 0; i < parseObjects.size(); i++) {
                            if(parseObjects.get(i).getApprove()==true) {
                                HopeApp.myWorksIds.put(parseObjects.get(i).getWorkID(), parseObjects.get(i).getWorkID());
                                HopeApp.myEmployerIds.put(parseObjects.get(i).getEmployerID(), parseObjects.get(i).getEmployerID());
                            }else{
                                HopeApp.myPendingWorksIds.put(parseObjects.get(i).getWorkID(), parseObjects.get(i).getWorkID());
                                HopeApp.myPendingEmployerIds.put(parseObjects.get(i).getEmployerID(), parseObjects.get(i).getEmployerID());

                            }
                            Log.d("hope getMyWorkids(workId)", parseObjects.get(i).getWorkID());

                        }
                    }setUpMainActivity();
                }
            });


    }

    private void showSortDialog() {
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_category_list);
        // Set dialog title
        dialog.setTitle("Select Category");
        ListView lvD = (ListView) dialog.findViewById(R.id.lv_category_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, HopeApp.SORT_TYPES);
        lvD.setAdapter(adapter);
        lvD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HopeApp.getInstance().setWorkAdSortBy(i);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void setUpDrawer() {
        if(HopeApp.getSPString(HopeApp.SELECTED_USER_TYPE).equalsIgnoreCase("worker")){
            HopeApp.drawerCandidate = HopeApp.drawerTitlesWorker;
        }else{
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
                }else if(i==1){
                    startActivity(new Intent(MainActivity.this, WorkActivity.class));
                }else if(i==2){
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
        return super.onCreateOptionsMenu(menu);
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
}
