package com.mybp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 * Created by kerick on 2/11/17.
 */

public class PlanTable extends DatabaseTable {
    // field names
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final String FROM = "date_from";
    public static final String TO = "date_to";
    public static final String CREATED = "created";
    public static final String UPDATED = "updated";

    // fields
    private long id;
    private String title;
    private String type;
    private long from;
    private long to;
    private long created;
    private long updated;

    public PlanTable() {
        super("plans", new String[]{ID, TITLE, TYPE, FROM, TO, CREATED, UPDATED});
    }

    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE "
                + name + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT NOT NULL, "
                + TYPE + " VARCHAR(21) NOT NULL, "
                + FROM + " INTEGER, "
                + TO + " INTEGER, "
                + CREATED + " INTEGER, "
                + UPDATED + " INTEGER"
                + ");"
        );
    }

    public long insert() {
        SQLiteDatabase database = DatabaseHelper.getInstance(DatabaseHelper.getDhContext()).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE, getTitle());
        values.put(TYPE, getType());
        values.put(FROM, getFrom());
        values.put(TO, getTo());
        values.put(CREATED, getCreated());
        values.put(UPDATED, getUpdated());

        return database.insert(name, null, values);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getFrom() {
        return from;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public long getTo() {
        return to;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getCreated() {
        return created;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public long getUpdated() {
        return updated;
    }
}
