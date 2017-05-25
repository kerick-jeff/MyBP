package com.mybp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlansFragment extends Fragment {
    boolean dualPane;

    public PlansFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plans, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PlansMasterFragment plansMasterFragment = null;
        FrameLayout frameLayout = (FrameLayout) getView().findViewById(R.id.frameLayout);
        if (frameLayout != null) {
            dualPane=false;
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            plansMasterFragment = (PlansMasterFragment) getFragmentManager().findFragmentByTag("MASTER");
            if (plansMasterFragment == null) {
                plansMasterFragment = new PlansMasterFragment();
                fragmentTransaction.add(R.id.frameLayout, plansMasterFragment, "MASTER");
            }

            PlansDetailFragment plansDetailFragment = (PlansDetailFragment) getFragmentManager().findFragmentById(R.id.frameLayoutDetail);
            if (plansDetailFragment != null) {
                fragmentTransaction.remove(plansDetailFragment);
            }

            fragmentTransaction.commit();
        } else {
            dualPane=true;
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            plansMasterFragment = (PlansMasterFragment) getFragmentManager().findFragmentById(R.id.frameLayoutMaster);
            if (plansMasterFragment == null) {
                plansMasterFragment = new PlansMasterFragment();
                fragmentTransaction.add(R.id.frameLayoutMaster, plansMasterFragment);
            }

            PlansDetailFragment plansDetailFragment = (PlansDetailFragment) getFragmentManager().findFragmentById(R.id.frameLayoutDetail);
            if (plansDetailFragment == null) {
                plansDetailFragment = new PlansDetailFragment();
                fragmentTransaction.add(R.id.frameLayoutDetail, plansDetailFragment);
            }

            fragmentTransaction.commit();
        }

        plansMasterFragment.setOnPlansSelectedListener(new PlansMasterFragment.OnPlansSelectedListener() {
            @Override
            public void onItemSelected(String planDescription) {
                sendPlanDescription(planDescription);
            }
        });
    }

    private void sendPlanDescription(String planDescription) {
        PlansDetailFragment plansDetailFragment;
        if (dualPane) {
            //Two pane layout
            plansDetailFragment = (PlansDetailFragment) getFragmentManager().findFragmentById(R.id.frameLayoutDetail);
            plansDetailFragment.showSelectedPlan(planDescription);
        } else {
            // Single pane layout
            plansDetailFragment = new PlansDetailFragment();

            Bundle bundle = new Bundle();
            bundle.putString(PlansDetailFragment.KEY_PLAN_DESCRIPTION, planDescription);
            plansDetailFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, plansDetailFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

}
