package com.example.jacek.gympartner.aktywnosci.trening;

/**
 * Created by Jacek on 18.12.2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.jacek.gympartner.testy.test;
import com.example.jacek.gympartner.testy.test1;
import com.example.jacek.gympartner.testy.test2;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                test tab1 = new test();
                return tab1;
            case 1:
                test1 tab2 = new test1();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}