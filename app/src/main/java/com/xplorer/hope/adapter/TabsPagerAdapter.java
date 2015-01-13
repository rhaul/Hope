package com.xplorer.hope.adapter;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.fragment.CategoryFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        return new CategoryFragment().newInstance(index);
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 11;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return HopeApp.TITLES[position];
    }

}
