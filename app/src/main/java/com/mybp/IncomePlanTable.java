package com.mybp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by kerick on 2/11/17.
 */

public class IncomePlanTable extends DatabaseTable {
    // field names
    public static final String ID = "_id";
    public static final String PLAN_ID = "plan_id";
    public static final String SALARY = "salary";
    public static final String DIVIDENDS = "dividends";
    public static final String INTEREST = "interest";
    public static final String PROFIT = "profit";
    public static final String RENTS = "rents";

    // fields
    private long id;
    private long planId;
    private double salary;
    private double dividends;
    private double interest;
    private double profit;
    private double rents;

    public IncomePlanTable() {
        super("income_plans", new String[]{ID, PLAN_ID, SALARY, DIVIDENDS, INTEREST, PROFIT, RENTS});
    }

    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE "
                + name + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PLAN_ID + " INTEGER, "
                + SALARY + " DOUBLE, "
                + DIVIDENDS + " DOUBLE, "
                + INTEREST + " DOUBLE, "
                + PROFIT + " DOUBLE, "
                + RENTS + " DOUBLE"
                + ");"
        );
    }

    @Override
    public long insert() {
        SQLiteDatabase database = DatabaseHelper.getInstance(DatabaseHelper.getDhContext()).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PLAN_ID, getPlanId());
        values.put(SALARY, getSalary());
        values.put(DIVIDENDS, getDividends());
        values.put(INTEREST, getInterest());
        values.put(PROFIT, getProfit());
        values.put(RENTS, getRents());

        return database.insert(name, null, values);
    }

    public long getId() {
        return id;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public long getPlanId() {
        return planId;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public void setDividends(double dividends) {
        this.dividends = dividends;
    }

    public double getDividends() {
        return dividends;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public double getInterest() {
        return interest;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getProfit() {
        return profit;
    }

    public void setRents(double rents) {
        this.rents = rents;
    }

    public double getRents() {
        return rents;
    }
}
