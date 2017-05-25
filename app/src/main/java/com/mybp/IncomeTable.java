package com.mybp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by kerick on 2/11/17.
 */

public class IncomeTable extends DatabaseTable {
    // field names
    public static final String ID = "_id";
    public static final String SALARY = "salary";
    public static final String DIVIDENDS = "dividends";
    public static final String INTEREST = "interest";
    public static final String PROFIT = "profit";
    public static final String RENTS = "rents";
    public static final String CREATED = "created";
    public static final String UPDATED = "updated";

    // fields
    private long id;
    private double salary;
    private double dividends;
    private double interest;
    private double profit;
    private double rents;
    private long created;
    private long updated;

    public IncomeTable() {
        super("incomes", new String[]{ID, SALARY, DIVIDENDS, INTEREST, PROFIT, RENTS, CREATED, UPDATED});
    }

    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE "
                + name + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SALARY + " DOUBLE, "
                + DIVIDENDS + " DOUBLE, "
                + INTEREST + " DOUBLE, "
                + PROFIT + " DOUBLE, "
                + RENTS + " DOUBLE, "
                + CREATED + " DATETIME, "
                + UPDATED + " DATETIME"
                + ");"
        );
    }

    @Override
    public boolean insert() {
        SQLiteDatabase database = DatabaseHelper.getInstance(DatabaseHelper.getDhContext()).getWritableDatabase();
        ContentValues values = new ContentValues();



        database.insert(name, null, values);

        return true;
    }
}
