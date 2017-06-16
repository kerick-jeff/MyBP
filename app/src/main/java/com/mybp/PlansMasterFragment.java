package com.mybp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlansMasterFragment extends Fragment {
    private ListView listViewPlans;
    private OnPlanClickedListener onPlanClickedListener;

    public PlansMasterFragment() {
        onPlanClickedListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plans, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listViewPlans = (ListView) getView().findViewById(R.id.listViewPlans);
        listViewPlans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Toast.makeText(getActivity(), ((PlanTable) adapterView.getSelectedItem()).getTitle(), Toast.LENGTH_LONG).show();
                //onPlanClickedListener.onPlanClicked((PlanTable) adapterView.getItemAtPosition(i));
            }
        });

        listViewPlans.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

        PlanTable planTable = new PlanTable();
        DatabaseTable.FindAllTask findAllTask = planTable.executeFindAllTask();
        findAllTask.findAllTaskResponse = new DatabaseTable.FindAllTaskResponse() {
            @Override
            public void findAllTaskFinished(final Cursor plans) {
                if(plans.getCount() > 0) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            PlansCursorAdapter plansCursorAdapter = new PlansCursorAdapter(getActivity(), plans, 0);
                            listViewPlans.setAdapter(plansCursorAdapter);
                        }
                    });
                }
            }
        };
    }

    private class PlansCursorAdapter extends CursorAdapter {
        private LayoutInflater layoutInflater;
        private int index;

        public PlansCursorAdapter(Context context, Cursor plan, int flags) {
            super(context, plan, flags);

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            index = 0;
        }

        public void bindView(View view, Context context, Cursor plan) {
            TextView textViewIndex = (TextView) view.findViewById(R.id.textViewIndex);
            TextView textViewDescription = (TextView) view.findViewById(R.id.textViewDescription);

            textViewIndex.setText(String.valueOf(plan.getLong(plan.getColumnIndex(PlanTable.ID))));

            String description = "";
            if(plan.getString(plan.getColumnIndex(PlanTable.TITLE)).length() > 35) {
                description += plan.getString(plan.getColumnIndex(PlanTable.TITLE)).substring(0, 31) + "..." + "\n\n";
            } else {
                description = plan.getString(plan.getColumnIndex(PlanTable.TITLE)) + "\n\n";
            }

            description += "[" + plan.getString(plan.getColumnIndex(PlanTable.TYPE)) + "]" + "\n\n"
                    + "From: " + DateUtils.formatDateTime(context, plan.getLong(plan.getColumnIndex(PlanTable.FROM)), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME) + "\n"
                    + "To: " + DateUtils.formatDateTime(context, plan.getLong(plan.getColumnIndex(PlanTable.TO)), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);

            textViewDescription.setText(description);
        }

        public View newView(Context context, Cursor plan, ViewGroup parent) {
            /*if(plan.getLong(plan.getColumnIndex(PlanTable.FROM)) < Calendar.getInstance().getTimeInMillis()) {
                switch (plan.getString(plan.getColumnIndex(PlanTable.TYPE))) {
                    case "Income Plan" :
                        return layoutInflater.inflate(R.layout.mybp_obsolete_income_plan_layout, parent, false);
                    case "Expenses Plan" :
                        return layoutInflater.inflate(R.layout.mybp_obsolete_expenses_plan_layout, parent, false);
                    default :
                        return layoutInflater.inflate(R.layout.mybp_obsolete_income_expenses_plan_layout, parent, false);
                }
            } else {*/
                switch (plan.getString(plan.getColumnIndex(PlanTable.TYPE))) {
                    case "Income Plan" :
                        return layoutInflater.inflate(R.layout.mybp_income_plan_layout, parent, false);
                    case "Expenses Plan" :
                        return layoutInflater.inflate(R.layout.mybp_expenses_plan_layout, parent, false);
                    default :
                        return layoutInflater.inflate(R.layout.mybp_income_expenses_plan_layout, parent, false);
                }
            //}
        }
    }

    public void setOnPlanClickedListener(OnPlanClickedListener onPlanClickedListener) {
        this.onPlanClickedListener = onPlanClickedListener;
    }

    public OnPlanClickedListener getOnPlanClickedListener() {
        return onPlanClickedListener;
    }

    public interface OnPlanClickedListener {
        void onPlanClicked(PlanTable plan);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        DatabaseHelper.getInstance(DatabaseHelper.getDhContext()).getReadableDatabase().close();
    }
}
