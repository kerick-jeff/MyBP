package com.mybp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlansMasterFragment extends ListMasterFragment {

    public PlansMasterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new PlanTable().executeFindAllTask(getActivity(), new String[]{PlanTable.TITLE, PlanTable.CREATED}, this);
    }
}
