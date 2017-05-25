package com.mybp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kerick on 2/11/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mybp";
    private static final int DB_VERSION = 1;

    private static DatabaseHelper databaseHelper = null;
    private static Context dhContext = null;

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        dhContext = context;
    }

    // obtain singleton
    static synchronized DatabaseHelper getInstance(Context context) {
        if(databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context.getApplicationContext());
        }

        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            database.beginTransaction();

            new PlanTable().create(database);
            new IncomePlanTable().create(database);
            new ExpensesPlanTable().create(database);
            new IncomeTable().create(database);
            new ExpensesTable().create(database);

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int OldVersion, int newVersion) {

    }

    public static Context getDhContext() {
        return dhContext;
    }
}
