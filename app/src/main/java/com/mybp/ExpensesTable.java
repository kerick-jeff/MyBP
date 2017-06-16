package com.mybp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by kerick on 2/11/17.
 */

public class ExpensesTable extends DatabaseTable {
    // field names
    public static final String ID = "_id";
    public static final String FOOD = "food";
    public static final String HEALTH = "health";
    public static final String CLOTHES = "clothes";
    public static final String TRANSPORT_FUEL = "transport_fuel";
    public static final String HOUSEHOLD = "household";
    public static final String GADGETS = "gadgets";
    public static final String ACTIVITIES = "activities";
    public static final String RELAXATION = "relaxation";
    public static final String CREATED = "created";
    public static final String UPDATED = "updated";

    // fields
    private long id;
    private double food;
    private double health;
    private double clothes;
    private double transportFuel;
    private double household;
    private double gadgets;
    private double activities;
    private double relaxation;
    private long created;
    private long updated;

    public ExpensesTable() {
        super("expenses", new String[]{ID, FOOD, HEALTH, CLOTHES, TRANSPORT_FUEL, HOUSEHOLD, GADGETS, ACTIVITIES, RELAXATION, CREATED, UPDATED});
    }

    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE "
                + name + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FOOD + " DOUBLE, "
                + HEALTH + " DOUBLE, "
                + CLOTHES + " DOUBLE, "
                + TRANSPORT_FUEL + " DOUBLE, "
                + HOUSEHOLD + " DOUBLE, "
                + GADGETS + " DOUBLE, "
                + ACTIVITIES + " DOUBLE, "
                + RELAXATION + " DOUBLE, "
                + CREATED + " DATETIME, "
                + UPDATED + " DATETIME"
                + ");"
        );
    }

    @Override
    public long insert() {
        /*SQLiteDatabase database = DatabaseHelper.getInstance(DatabaseHelper.getDhContext()).getWritableDatabase();
        ContentValues values = new ContentValues();



        database.insert(name, null, values);*/

        return 0;
    }
}
