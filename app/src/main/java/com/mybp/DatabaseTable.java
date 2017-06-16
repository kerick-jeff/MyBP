package com.mybp;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kerick on 2/11/17.
 */

public abstract class DatabaseTable {
    protected String name;
    protected String[] fields;

    public DatabaseTable(String name, String[] fields) {
        this.name = name;
        this.fields = fields;
    }

    protected abstract void create(SQLiteDatabase database);

    protected void columnNames(Context context) {
        SQLiteDatabase database = DatabaseHelper.getInstance(DatabaseHelper.getDhContext()).getReadableDatabase();
        Cursor results = database.rawQuery("SELECT * FROM " + name, null);
        for (String name: results.getColumnNames()) {
            Toast.makeText(context, name, Toast.LENGTH_LONG).show();
        }
    }

    protected Cursor find(int id) {
        SQLiteDatabase database = DatabaseHelper.getInstance(DatabaseHelper.getDhContext()).getReadableDatabase();
        Cursor result = database.rawQuery("SELECT * FROM " + name + " WHERE _id=" + id, null);
        if(result.getCount() == 1) {
            result.moveToFirst();
            return result;
        }

        return null;
    }

    protected Cursor findByQuery(String query) {
        SQLiteDatabase database = DatabaseHelper.getInstance(DatabaseHelper.getDhContext()).getReadableDatabase();
        return database.rawQuery(query, null);
    }

    protected Cursor findAll() {
        SQLiteDatabase database = DatabaseHelper.getInstance(DatabaseHelper.getDhContext()).getReadableDatabase();
        Cursor results = database.rawQuery("SELECT * FROM " + name + " ORDER BY _id DESC", null);
        results.moveToFirst();
        return results;
    }

    protected abstract long insert();

    protected boolean delete(int id) {
        int deleted = -1;
        SQLiteDatabase database = DatabaseHelper.getInstance(DatabaseHelper.getDhContext()).getWritableDatabase();
        deleted = database.delete(name, "_id=?", new String[]{String.valueOf(id)});
        if (deleted == 1) {
            return true;
        }

        return false;
    }

    // AsyncTask methods and inner classes inheriting from AsynTask class

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private < T > void executeAsyncTask(AsyncTask< T, ?, ? > asyncTask, T... params) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            asyncTask.execute(params);
        }
    }

    public void executeFindTask(int id) {
        executeAsyncTask(new FindTask(), id);
    }



    public void executeDeleteTask(int id) {
        executeAsyncTask(new DeleteTask(), id);
    }

    private class FindTask extends AsyncTask < Integer, Void, Cursor > {
        @Override
        public Cursor doInBackground(Integer... params) {
            return find(params[0]);
        }
    }

    /*
     * Defines the logic for finding all objects of a specific type or table from the database
     * executeFindAllTask(...) : a method allows the execution of the FindAllTask
     * FindAllTask{...} : an AsyncTask that handles the findAll process
     */
    public FindAllTask executeFindAllTask() {
        FindAllTask findAllTask = new FindAllTask();
        executeAsyncTask(findAllTask);

        return findAllTask;
    }

    public class FindAllTask extends AsyncTask <Void, Void, Cursor > {
        public FindAllTaskResponse findAllTaskResponse;

        public FindAllTask() {
            this.findAllTaskResponse = null;
        }

        @Override
        public Cursor doInBackground(Void... params) {
            return findAll();
        }

        @Override
        public void onPostExecute(Cursor results) {
            if(findAllTaskResponse != null) {
                findAllTaskResponse.findAllTaskFinished(results);
            }
        }
    }

    public interface FindAllTaskResponse {
        void findAllTaskFinished(Cursor results);
    }
    /*** $FindAll ***/

    /*
     * Defines the logic that takes place when an item is being inserted into the database
     * executeInsertTask(...) : a method allows the execution of the InsertTask
     * InsertTask{...} : an AsyncTask that handles the insert process
     * InsertTaskResponse {...} : an interface for passing the insertedId to a ui process
     */
    public InsertTask executeInsertTask(final Context context, DatabaseTable databaseTable, String postString) {
        InsertTask insertTask = new InsertTask(context, databaseTable, postString);
        executeAsyncTask(insertTask);

        return insertTask;
    }

    public class InsertTask extends AsyncTask < Void, Void, Long > {
        private Context context;
        private DatabaseTable databaseTable;
        private String postString;
        public InsertTaskResponse insertTaskResponse;

        public InsertTask(Context context, DatabaseTable databaseTable, String postString) {
            this.context = context;
            this.databaseTable = databaseTable;
            this.postString = postString;
            this.insertTaskResponse = null;
        }

        @Override
        public Long doInBackground(Void... params) {
            return databaseTable.insert();
        }

        @Override
        public void onPostExecute(Long lastId) {
            if (postString != null && !postString.equals("")) {
                Toast.makeText(context, postString, Toast.LENGTH_LONG).show();
            }

            if(insertTaskResponse != null) {
                insertTaskResponse.insertTaskFinished(lastId);
            }
        }
    }

    public interface InsertTaskResponse {
        void insertTaskFinished(long insertedId);
    }
    /*** $Insert ***/

    private class DeleteTask extends AsyncTask < Integer, Void, Boolean > {
        @Override
        public Boolean doInBackground(Integer... params) {
            return delete(params[0]);
        }
    }
}
