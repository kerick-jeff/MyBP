package com.mybp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlansDetailFragment extends Fragment {
    public static String KEY_PLAN_DESCRIPTION = "PLAN_DESCRIPTION";

    public PlansDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plans_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(KEY_PLAN_DESCRIPTION)) {
            showSelectedPlan(bundle.getString(KEY_PLAN_DESCRIPTION));
        }
    }

    public void showSelectedPlan(String planDescription) {
        ((TextView) getView().findViewById(R.id.textViewPlanDescription)).setText(planDescription);
    }
}
