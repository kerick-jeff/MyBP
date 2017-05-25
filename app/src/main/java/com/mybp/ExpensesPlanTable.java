package com.mybp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by kerick on 2/11/17.
 */

public class ExpensesPlanTable extends DatabaseTable {
    // field names
    public static final String ID = "_id";
    public static final String PLAN_ID = "plan_id";
    public static final String FOOD = "food";
    public static final String HEALTH = "health";
    public static final String CLOTHES = "clothes";
    public static final String TRANSPORT_FUEL = "transport_fuel";
    public static final String HOUSEHOLD = "household";
    public static final String GADGETS = "gadgets";
    public static final String ACTIVITIES = "activities";
    public static final String RELAXATION = "relaxation";

    // fields
    private long id;
    private long planId;
    private double food;
    private double health;
    private double clothes;
    private double transportFuel;
    private double household;
    private double gadgets;
    private double activities;
    private double relaxation;

    public ExpensesPlanTable() {
        super("expenses_plans", new String[]{ID, PLAN_ID, FOOD, HEALTH, CLOTHES, TRANSPORT_FUEL, HOUSEHOLD, GADGETS, ACTIVITIES, RELAXATION});
    }

    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE "
                + name + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PLAN_ID + " INTEGER, "
                + FOOD + " DOUBLE, "
                + HEALTH + " DOUBLE, "
                + CLOTHES + " DOUBLE, "
                + TRANSPORT_FUEL + " DOUBLE, "
                + HOUSEHOLD + " DOUBLE, "
                + GADGETS + " DOUBLE, "
                + ACTIVITIES + " DOUBLE, "
                + RELAXATION + " DOUBLE"
                + ");"
        );
    }

    @Override
    public boolean insert() {
        SQLiteDatabase database = DatabaseHelper.getInstance(DatabaseHelper.getDhContext()).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PLAN_ID, getPlanId());
        values.put(FOOD, getFood());
        values.put(HEALTH, getHealth());
        values.put(CLOTHES, getClothes());
        values.put(TRANSPORT_FUEL, getTransportFuel());
        values.put(HOUSEHOLD, getHousehold());
        values.put(GADGETS, getGadgets());
        values.put(ACTIVITIES, getActivities());
        values.put(RELAXATION, getRelaxation());
        database.insert(name, null, values);

        return true;
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

    public void setFood(double food) {
        this.food = food;
    }

    public double getFood() {
        return food;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getHealth() {
        return health;
    }

    public void setClothes(double clothes) {
        this.clothes = clothes;
    }

    public double getClothes() {
        return clothes;
    }

    public void setTransportFuel(double transportFuel) {
        this.transportFuel = transportFuel;
    }

    public double getTransportFuel() {
        return transportFuel;
    }

    public void setHousehold(double household) {
        this.household = household;
    }

    public double getHousehold() {
        return household;
    }

    public void setGadgets(double gadgets) {
        this.gadgets = gadgets;
    }

    public double getGadgets() {
        return gadgets;
    }

    public void setActivities(double activities) {
        this.activities = activities;
    }

    public double getActivities() {
        return activities;
    }

    public void setRelaxation(double relaxation) {
        this.relaxation = relaxation;
    }

    public double getRelaxation() {
        return relaxation;
    }
}
