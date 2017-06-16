package com.mybp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by kerick on 6/6/17.
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int FRAGMENT_COUNT = 2;
    private String fragment;

    public TabFragmentPagerAdapter(FragmentManager fragmentManager, String fragment) {
        super(fragmentManager);

        this.fragment = fragment;
    }

    @Override
    public Fragment getItem(int position) {
        if(fragment == IncomeFragment.FRAGMENT) {
            switch (position) {
                case 0 :
                    return new TodayIncomeFragment();
                case 1 :
                    return new AllIncomeFragment();
            }
        } else {
            switch (position) {
                case 0 :
                    return new TodayExpensesFragment();
                case 1 :
                    return new AllExpensesFragment();
            }
        }

        return null;
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Today";
            case 1:
                return "All";
        }

        return null;
    }
}
