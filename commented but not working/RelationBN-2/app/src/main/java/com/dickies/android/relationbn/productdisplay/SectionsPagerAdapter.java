package com.dickies.android.relationbn.productdisplay;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the three fragments which  act as tabs in the HomeActivity
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "SectionsPagerAdapter";

    /**
     * Arraylist to store the tree fragments
     */
    private final List<Fragment> mFragmentList = new ArrayList<>();


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Returns a specified fragment from the Arraylist
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    /**
     * Returns the number of fragments in the Arraylist
     * @return
     */
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /**
     * Adds fragments to the Arraylist
     * @param fragment
     */
    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }
}
