package com.xplorer.hope.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.xplorer.hope.R;
import com.xplorer.hope.adapter.TabsPagerAdapter;
import com.xplorer.hope.config.HopeApp;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends FragmentActivity {

    @InjectView(R.id.tabs)
    PagerSlidingTabStrip pts_titleBar;
    @InjectView(R.id.pager) ViewPager vp_pager;

    private TabsPagerAdapter pagerAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        if(!HopeApp.getSPString(HopeApp.SELECTED_LANGUAGE).equalsIgnoreCase("hindi") && !HopeApp.getSPString(HopeApp.SELECTED_LANGUAGE).equalsIgnoreCase("english") ){
            startActivity(new Intent(this,SelectLangActivity.class));
            finish();
        }else if(!HopeApp.getSPString(HopeApp.SELECTED_USER_TYPE).equalsIgnoreCase("worker") && !HopeApp.getSPString(HopeApp.SELECTED_USER_TYPE).equalsIgnoreCase("employer") ){
            startActivity(new Intent(this,SelectUserTypeActivity.class));
            finish();
        }

        setUpMainActivity();
    }

    private void setUpMainActivity() {
        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        vp_pager.setAdapter(pagerAdapter);
        pts_titleBar.setViewPager(vp_pager);
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
