package com.mybp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */

public class PlanFragment extends Fragment {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private boolean notificationPeriodMorning = true;
    private boolean notificationPeriodAfternoon = true;
    private boolean notificationPeriodEvening = true;

    private String[] types = new String[]{"Income/Expenses Plan", "Income Plan", "Expenses Plan"};
    private EditText editTextTitle;
    private Spinner spinnerType;
    private Button buttonFrom;
    private Button buttonTo;
    private long from;
    private long to;
    private Button buttonCreate;
    private Calendar calendar = Calendar.getInstance();
    private Calendar varCalendar = Calendar.getInstance();
    private boolean isButtonFrom = true;

    // Income views
    private TextView textViewIncomePlanning;
    private TextView textViewSalary;
    private EditText editTextSalary;
    private TextView textViewDividends;
    private EditText editTextDividends;
    private TextView textViewInterests;
    private EditText editTextInterests;
    private TextView textViewProfit;
    private EditText editTextProfit;
    private TextView textViewRents;
    private EditText editTextRents;

    // Expenses views
    private TextView textViewExpensesPlanning;
    private TextView textViewFood;
    private EditText editTextFood;
    private TextView textViewHealth;
    private EditText editTextHealth;
    private TextView textViewClothes;
    private EditText editTextClothes;
    private TextView textViewTransport;
    private EditText editTextTransport;
    private TextView textViewHousehold;
    private EditText editTextHousehold;
    private TextView textViewGadgets;
    private EditText editTextGadgets;
    private TextView textViewActivities;
    private EditText editTextActivities;
    private TextView textViewRelaxation;
    private EditText editTextRelaxation;

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            if(isButtonFrom
                    && (year < calendar.get(Calendar.YEAR)
                    || (year == calendar.get(Calendar.YEAR) && month < calendar.get(Calendar.MONTH))
                    || (year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH) && dayOfMonth < calendar.get(Calendar.DAY_OF_MONTH)))) {
                Toast.makeText(getActivity(), "Sorry! Date must not be of the past", Toast.LENGTH_LONG).show();
                return;
            } else if(!isButtonFrom
                    && (year < calendar.get(Calendar.YEAR)
                    || (year == calendar.get(Calendar.YEAR) && month < calendar.get(Calendar.MONTH))
                    || (year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH) && dayOfMonth <= calendar.get(Calendar.DAY_OF_MONTH)))) {
                Toast.makeText(getActivity(), "Sorry! A future date is required", Toast.LENGTH_LONG).show();
                return;
            }

            varCalendar.set(Calendar.YEAR, year);
            varCalendar.set(Calendar.MONTH, month);
            varCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateButtonLabel(isButtonFrom ? buttonFrom : buttonTo);
        }
    };

    private TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            varCalendar.set(Calendar.HOUR_OF_DAY, hour);
            varCalendar.set(Calendar.MINUTE, minute);
            Toast.makeText(getActivity(), DateUtils.formatDateTime(getActivity(), varCalendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME), Toast.LENGTH_LONG).show();
        }
    };

    public PlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        preferences = getActivity().getSharedPreferences("MyBPreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextTitle = (EditText) getView().findViewById(R.id.editTextTitle);
        spinnerType = (Spinner) getView().findViewById(R.id.spinnerType);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(types[position] == "Income Plan") {
                    toggleExpensesPlanViews(false);
                    toggleIncomePlanViews(true);
                } else if(types[position] == "Expenses Plan") {
                    toggleIncomePlanViews(false);
                    toggleExpensesPlanViews(true);
                } else {
                    toggleIncomePlanViews(true);
                    toggleExpensesPlanViews(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "No type selected", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        buttonFrom = (Button) getView().findViewById(R.id.buttonFrom);
        buttonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFrom();
            }
        });

        buttonTo = (Button) getView().findViewById(R.id.buttonTo);
        buttonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickTo();
            }
        });

        buttonCreate = (Button) getView().findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCreate();
            }
        });

        referenceViews();
    }

    private boolean activateCreate() {
        if(!editTextTitle.getText().toString().equals("")
                && buttonFrom.getText().toString() != getResources().getString(R.string.choose_calendar)
                && buttonTo.getText().toString() != getResources().getString(R.string.choose_calendar)
                && (!editTextSalary.getText().toString().equals("")
                    || !editTextDividends.getText().toString().equals("")
                    || !editTextInterests.getText().toString().equals("")
                    || !editTextProfit.getText().toString().equals("")
                    || !editTextRents.getText().toString().equals("")
                    || !editTextFood.getText().toString().equals("")
                    || !editTextHealth.getText().toString().equals("")
                    || !editTextClothes.getText().toString().equals("")
                    || !editTextTransport.getText().toString().equals("")
                    || !editTextHousehold.getText().toString().equals("")
                    || !editTextGadgets.getText().toString().equals("")
                    || !editTextActivities.getText().toString().equals("")
                    || !editTextRelaxation.getText().toString().equals(""))){
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    private void toggleIncomePlanViews(boolean enabled) {
        textViewIncomePlanning.setTextColor(enabled ? getResources().getColor(R.color.teal) : getResources().getColor(R.color.textColor));
        textViewSalary.setTextColor(enabled ? getResources().getColor(R.color.textColor) : getResources().getColor(R.color.gray));
        editTextSalary.setEnabled(enabled);
        textViewDividends.setTextColor(enabled ? getResources().getColor(R.color.textColor) : getResources().getColor(R.color.gray));
        editTextDividends.setEnabled(enabled);
        textViewInterests.setTextColor(enabled ? getResources().getColor(R.color.textColor) : getResources().getColor(R.color.gray));
        editTextInterests.setEnabled(enabled);
        textViewProfit.setTextColor(enabled ? getResources().getColor(R.color.textColor) : getResources().getColor(R.color.gray));
        editTextProfit.setEnabled(enabled);
        textViewRents.setTextColor(enabled ? getResources().getColor(R.color.textColor) : getResources().getColor(R.color.gray));
        editTextRents.setEnabled(enabled);
    }

    @SuppressWarnings("deprecation")
    private void toggleExpensesPlanViews(boolean enabled) {
        textViewExpensesPlanning.setTextColor(enabled ? getResources().getColor(R.color.teal) : getResources().getColor(R.color.textColor));
        textViewFood.setTextColor(enabled ? getResources().getColor(R.color.textColor) : getResources().getColor(R.color.gray));
        editTextFood.setEnabled(enabled);
        textViewHealth.setTextColor(enabled ? getResources().getColor(R.color.textColor) : getResources().getColor(R.color.gray));
        editTextHealth.setEnabled(enabled);
        textViewClothes.setTextColor(enabled ? getResources().getColor(R.color.textColor) : getResources().getColor(R.color.gray));
        editTextClothes.setEnabled(enabled);
        textViewTransport.setTextColor(enabled ? getResources().getColor(R.color.textColor) : getResources().getColor(R.color.gray));
        editTextTransport.setEnabled(enabled);
        textViewHousehold.setTextColor(enabled ? getResources().getColor(R.color.textColor) : getResources().getColor(R.color.gray));
        editTextHousehold.setEnabled(enabled);
        textViewGadgets.setTextColor(enabled ? getResources().getColor(R.color.textColor) : getResources().getColor(R.color.gray));
        editTextGadgets.setEnabled(enabled);
        textViewActivities.setTextColor(enabled ? getResources().getColor(R.color.textColor) : getResources().getColor(R.color.gray));
        editTextActivities.setEnabled(enabled);
        textViewRelaxation.setTextColor(enabled ? getResources().getColor(R.color.textColor) : getResources().getColor(R.color.gray));
        editTextRelaxation.setEnabled(enabled);
    }

    private void referenceViews() {
        textViewIncomePlanning = (TextView) getView().findViewById(R.id.textViewIncomePlanning);
        textViewSalary = (TextView) getView().findViewById(R.id.textViewSalary);
        editTextSalary = (EditText) getView().findViewById(R.id.editTextSalary);
        textViewDividends = (TextView) getView().findViewById(R.id.textViewDividends);
        editTextDividends = (EditText) getView().findViewById(R.id.editTextDividends);
        textViewInterests = (TextView) getView().findViewById(R.id.textViewInterests);
        editTextInterests = (EditText) getView().findViewById(R.id.editTextInterests);
        textViewProfit = (TextView) getView().findViewById(R.id.textViewProfit);
        editTextProfit = (EditText) getView().findViewById(R.id.editTextProfit);
        textViewRents = (TextView) getView().findViewById(R.id.textViewRents);
        editTextRents = (EditText) getView().findViewById(R.id.editTextRents);

        textViewExpensesPlanning = (TextView) getView().findViewById(R.id.textViewExpensesPlanning);
        textViewFood = (TextView) getView().findViewById(R.id.textViewFood);
        editTextFood = (EditText) getView().findViewById(R.id.editTextFood);
        textViewHealth = (TextView) getView().findViewById(R.id.textViewHealth);
        editTextHealth = (EditText) getView().findViewById(R.id.editTextHealth);
        textViewClothes = (TextView) getView().findViewById(R.id.textViewClothes);
        editTextClothes = (EditText) getView().findViewById(R.id.editTextClothes);
        textViewTransport = (TextView) getView().findViewById(R.id.textViewTransport);
        editTextTransport = (EditText) getView().findViewById(R.id.editTextTransport);
        textViewHousehold = (TextView) getView().findViewById(R.id.textViewHousehold);
        editTextHousehold = (EditText) getView().findViewById(R.id.editTextHousehold);
        textViewGadgets = (TextView) getView().findViewById(R.id.textViewGadgets);
        editTextGadgets = (EditText) getView().findViewById(R.id.editTextGadgets);
        textViewActivities = (TextView) getView().findViewById(R.id.textViewActivities);
        editTextActivities = (EditText) getView().findViewById(R.id.editTextActivities);
        textViewRelaxation = (TextView) getView().findViewById(R.id.textViewRelaxation);
        editTextRelaxation = (EditText) getView().findViewById(R.id.editTextRelaxation);
    }

    public void onClickFrom() {
        isButtonFrom = true;

        new DatePickerDialog(
                getActivity(),
                onDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void onClickTo() {
        isButtonFrom = false;

        new DatePickerDialog(
                getActivity(),
                onDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void onClickCreate() {
        if (activateCreate()) {
            PlanTable newPlan = new PlanTable();
            newPlan.setTitle(editTextTitle.getText().toString());
            newPlan.setType(spinnerType.getSelectedItem().toString());
            newPlan.setFrom(from);
            newPlan.setTo(to);
            long now = Calendar.getInstance().getTimeInMillis();
            newPlan.setCreated(now);
            newPlan.setUpdated(now);
            newPlan.executeInsertTask(getActivity(), newPlan, "Plan created");

            Cursor result = newPlan.findByQuery("SELECT " + PlanTable.ID + " FROM " + newPlan.name);
            result.moveToLast();
            long newPlanId = result.getLong(result.getColumnIndex(PlanTable.ID));

            IncomePlanTable newIncomePlan = new IncomePlanTable();
            ExpensesPlanTable newExpensesPlan = new ExpensesPlanTable();

            switch (newPlan.getType()) {
                case "Income Plan":
                    Toast.makeText(getActivity(), "" + newPlanId, Toast.LENGTH_LONG).show();
                    newIncomePlan.setPlanId(newPlanId);
                    newIncomePlan.setSalary(editTextSalary.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextSalary.getText().toString()));
                    newIncomePlan.setDividends(editTextDividends.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextDividends.getText().toString()));
                    newIncomePlan.setInterest(editTextInterests.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextInterests.getText().toString()));
                    newIncomePlan.setProfit(editTextProfit.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextProfit.getText().toString()));
                    newIncomePlan.setRents(editTextRents.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextRents.getText().toString()));
                    newIncomePlan.executeInsertTask(getActivity(), newIncomePlan, null);
                    break;
                case "Expenses Plan":
                    newExpensesPlan.setPlanId(newPlanId);
                    newExpensesPlan.setFood(editTextFood.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextFood.getText().toString()));
                    newExpensesPlan.setHealth(editTextHealth.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextHealth.getText().toString()));
                    newExpensesPlan.setClothes(editTextClothes.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextClothes.getText().toString()));
                    newExpensesPlan.setHousehold(editTextHousehold.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextHousehold.getText().toString()));
                    newExpensesPlan.setGadgets(editTextGadgets.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextGadgets.getText().toString()));
                    newExpensesPlan.setActivities(editTextActivities.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextActivities.getText().toString()));
                    newExpensesPlan.setRelaxation(editTextRelaxation.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextRelaxation.getText().toString()));
                    newExpensesPlan.executeInsertTask(getActivity(), newExpensesPlan, null);
                    break;
                default:
                    newIncomePlan.setPlanId(newPlanId);
                    newIncomePlan.setSalary(editTextSalary.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextSalary.getText().toString()));
                    newIncomePlan.setDividends(editTextDividends.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextDividends.getText().toString()));
                    newIncomePlan.setInterest(editTextInterests.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextInterests.getText().toString()));
                    newIncomePlan.setProfit(editTextProfit.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextProfit.getText().toString()));
                    newIncomePlan.setRents(editTextRents.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextRents.getText().toString()));
                    newIncomePlan.executeInsertTask(getActivity(), newIncomePlan, null);

                    newExpensesPlan.setPlanId(newPlanId);
                    newExpensesPlan.setFood(editTextFood.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextFood.getText().toString()));
                    newExpensesPlan.setHealth(editTextHealth.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextHealth.getText().toString()));
                    newExpensesPlan.setClothes(editTextClothes.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextClothes.getText().toString()));
                    newExpensesPlan.setHousehold(editTextHousehold.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextHousehold.getText().toString()));
                    newExpensesPlan.setGadgets(editTextGadgets.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextGadgets.getText().toString()));
                    newExpensesPlan.setActivities(editTextActivities.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextActivities.getText().toString()));
                    newExpensesPlan.setRelaxation(editTextRelaxation.getText().toString().equals("") ? 0.0 : Double.valueOf(editTextRelaxation.getText().toString()));
                    newExpensesPlan.executeInsertTask(getActivity(), newExpensesPlan, null);
                    break;
            }
        } else {
            Toast.makeText(getActivity(), "Please fill all necessary fields", Toast.LENGTH_LONG).show();
        }
    }

    private void updateButtonLabel(Button button) {
        if(button.equals(buttonFrom)) {
            from = varCalendar.getTimeInMillis();
        } else {
            to = varCalendar.getTimeInMillis();
        }
        button.setText(DateUtils.formatDateTime(getActivity(), varCalendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE));
    }
}
