package com.xplorer.hope.activity;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.xplorer.hope.R;
import com.xplorer.hope.adapter.NavDrawerListAdapter;
import com.xplorer.hope.adapter.TabsPagerAdapter;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.UserInfo;
import com.xplorer.hope.service.ConnectionDetector;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends FragmentActivity implements QuickReturnInterface {

    @InjectView(R.id.tabs)
    PagerSlidingTabStrip pts_titleBar;
    @InjectView(R.id.pager) ViewPager vp_pager;

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
    LinearLayout b_sort;
    @InjectView(R.id.b_main_filter)
    LinearLayout b_filter;
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
        if(!HopeApp.getSPString(HopeApp.SELECTED_LANGUAGE).equalsIgnoreCase("hindi") && !HopeApp.getSPString(HopeApp.SELECTED_LANGUAGE).equalsIgnoreCase("english") ){
            startActivity(new Intent(this, SelectLangActivity.class));
            return;
        }else if(!HopeApp.getSPString(HopeApp.SELECTED_USER_TYPE).equalsIgnoreCase("worker") && !HopeApp.getSPString(HopeApp.SELECTED_USER_TYPE).equalsIgnoreCase("employer") ){
            startActivity(new Intent(this, SelectUserTypeActivity.class));
            return;
        }
        //-------------UserInfo-------------//
        UserInfo user = (UserInfo) ParseUser.getCurrentUser();
        if(user==null){
            cd = new ConnectionDetector(getApplicationContext());
            isInternetPresent= cd.isConnectingToInternet();

            if(isInternetPresent) {
                startActivity(new Intent(this,SignUpActivity.class).putExtra("from", "singup"));
                finish();
                return;
            }else{
                startActivity(new Intent(this, NoInternetActivity.class));
                return;
            }
        }

        //-------------ParseInstallation-------------//

      //if(ParseInstallation.getCurrentInstallation() == null || ParseInstallation.getCurrentInstallation().get("userId") == null || ParseInstallation.getCurrentInstallation().get("userId").toString().equalsIgnoreCase("unregistered")) {
            cd = new ConnectionDetector(getApplicationContext());
            isInternetPresent= cd.isConnectingToInternet();

            if(isInternetPresent) {
                ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){

                            UserInfo usr = (UserInfo) ParseUser.getCurrentUser();
                            if (usr != null) {
                                ParseInstallation.getCurrentInstallation().put("userId", usr.getObjectId());
                                ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e != null) {
                                            Log.d("error", e.toString());
                                        }
                                    }
                                });
                            } else {
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        }else{
                            Log.d("error", e.toString());
                        }
                    }
                });

            }else{
                startActivity(new Intent(this, NoInternetActivity.class));
                return;
            }


      // }
        if(!isSetUp) {
            setUpMainActivity();
            setUpDrawer();
            isSetUp=true;
        }
    }


    public void setUpUser(){

    }
    public void setUpPushInstallation(){

    }

    private void setUpMainActivity() {

        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        vp_pager.setAdapter(pagerAdapter);
        pts_titleBar.setViewPager(vp_pager);
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

     private void setUpDrawer(){

         mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
         mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
         // setting the nav drawer list adapter
         navDrawerListAdapter = new NavDrawerListAdapter(getApplicationContext());
         mDrawerList.setAdapter(navDrawerListAdapter);

         mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 if(i==0){
                     startActivity(new Intent(MainActivity.this, SignUpActivity.class).putExtra("from", "menu"));
                 }
             }
         });
         //getActionBar().setHomeButtonEnabled(true);

         mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                 R.drawable.ic_launcher, //nav menu toggle icon
                 R.string.app_name, // nav drawer open - description for accessibility
                 R.string.app_name // nav drawer close - description for accessibility
         ){
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
        if (id == R.id.action_add) {
            startActivity(new Intent(this,AddActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
