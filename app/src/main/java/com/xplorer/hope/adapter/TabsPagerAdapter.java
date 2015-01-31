package com.xplorer.hope.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xplorer.hope.fragment.CategoryFragment;

import java.util.ArrayList;
import java.util.List;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private List<CategoryFragment> fragments = new ArrayList<CategoryFragment>();


    public static String[] TITLES;
    public TabsPagerAdapter(FragmentManager fm,List<CategoryFragment> list, String TITLES[]) {
        super(fm);
        fragments = list;
        this.TITLES=TITLES;
    }

    public CategoryFragment getFragment(int position){
        return fragments.get(position);
    }

    @Override
    public Fragment getItem(int index) {
        return fragments.get(index);
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 11;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

}
