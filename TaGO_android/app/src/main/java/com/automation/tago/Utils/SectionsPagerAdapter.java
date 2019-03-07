package com.automation.tago.Utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "SectionsPagerAdapter";

    public List<Fragment> mFragmentList = new ArrayList<>();

    private FragmentManager fm;

    private long baseId = 0;

    public SectionsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    public SectionsPagerAdapter(FragmentManager fm, List<Fragment> mFragmentList){
        super(fm);
        this.mFragmentList = mFragmentList;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

//    @Override
//    public int getItemPosition(Object object) {
//        Log.d(TAG, "getItemPosition: sequential change:" + PagerAdapter.POSITION_NONE);
//        // refresh all fragments when data set changed
//        return PagerAdapter.POSITION_NONE;
//    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
    }

    public void removeFragment(int position){
        mFragmentList.remove(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        fm.beginTransaction().remove((Fragment) object).commitNowAllowingStateLoss();
    }
}
