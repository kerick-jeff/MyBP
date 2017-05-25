package com.mybp;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
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
        Cursor results = database.rawQuery("SELECT * FROM " + name, null);
        results.moveToFirst();
        return results;
    }

    protected abstract boolean insert();

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

    public void executeFindAllTask(Context context, String[] colIndices, PlansMasterFragment fragment) {
        executeAsyncTask(new FindAllTask(context, colIndices, fragment));
    }

    public void executeInsertTask(Context context, DatabaseTable databaseTable, String postString) {
        executeAsyncTask(new InsertTask(context, databaseTable, postString));
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

    private class FindAllTask extends AsyncTask <Void, Void, ArrayList<String> > {
        private Context context;
        private String[] colIndices;
        private ListMasterFragment fragment;

        public FindAllTask(Context context,String[] colIndices, ListMasterFragment fragment) {
            this.context = context;
            this.colIndices = colIndices;
            this.fragment = fragment;
        }

        @Override
        public ArrayList<String> doInBackground(Void... params) {
            Cursor results = findAll();
            ArrayList< String > planDefinitions = new ArrayList< String >();

            int rowNum = 1;
            while (!results.isAfterLast()) {
                String planDefinition = String.format("%02d\n", rowNum);
                for (int colIndex = 0; colIndex < colIndices.length; colIndex++) {
                    if(colIndex == colIndices.length - 1) {
                        planDefinition += DateUtils.formatDateTime(context, results.getLong(results.getColumnIndex(colIndices[colIndex])), DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_TIME);
                        break;
                    }
                    planDefinition += results.getString(results.getColumnIndex(colIndices[colIndex])) + "\n";
                }
                planDefinitions.add(planDefinition);
                results.moveToNext();
                rowNum++;
            }

            return planDefinitions;
        }

        @Override
        public void onPostExecute(ArrayList<String> planDefinitions) {
           // SimpleCursorAdapter adapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, results, new String[]{colIndexTitle}, new int[]{android.R.id.text1}, 0);

            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1, planDefinitions);
            fragment.setListAdapter(adapter);
            fragment.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            fragment.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(fragment.getOnPlansSelectedListener() != null) {
                        fragment.getOnPlansSelectedListener().onItemSelected(((TextView) view).getText().toString());
                    }
                }
            });
        }
    }

    private class InsertTask extends AsyncTask < Void, Void, Void > {
        private Context context;
        private DatabaseTable databaseTable;
        private String postString;

        public InsertTask(Context context, DatabaseTable databaseTable, String postString) {
            this.context = context;
            this.databaseTable = databaseTable;
            this.postString = postString;
        }

        @Override
        public Void doInBackground(Void... params) {
            databaseTable.insert();
            return null;
        }

        @Override
        public void onPostExecute(Void nothing) {
            if (!postString.equals(null) && !postString.equals("")) {
                Toast.makeText(context, postString, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class DeleteTask extends AsyncTask < Integer, Void, Boolean > {
        @Override
        public Boolean doInBackground(Integer... params) {
            return delete(params[0]);
        }
    }
}
