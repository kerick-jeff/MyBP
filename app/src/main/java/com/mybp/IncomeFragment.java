package com.mybp;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends Fragment {
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static final String FRAGMENT = IncomeFragment.class.getSimpleName();

    public IncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appBarLayout = (AppBarLayout) getView().findViewById(R.id.appBarLayout);
        tabLayout = (TabLayout) getView().findViewById(R.id.tabs);
        viewPager = (ViewPager) getView().findViewById(R.id.view_pager);

        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
        int actionBarSize = getResources().getDimensionPixelSize(typedValue.resourceId);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) appBarLayout.getLayoutParams();
        params.topMargin = actionBarSize + 5;

        viewPager.setAdapter(new TabFragmentPagerAdapter(getChildFragmentManager(), FRAGMENT));
        tabLayout.setupWithViewPager(viewPager);
    }

}
