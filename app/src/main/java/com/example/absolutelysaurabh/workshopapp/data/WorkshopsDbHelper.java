package com.example.absolutelysaurabh.workshopapp.data;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.content.Context.MODE_PRIVATE;

public class WorkshopsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "workshopsDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME_WORKSHOPS = "workshops";
    public static final String TABLE_NAME_APPLIED_WORKSHOPS = "workshops_applied";

    public static final String COLUMN_ID = "_id";

    public static Context mContext;

    public final static String COLUMN_WORKSHOP_TITLE = "news_title";
    public final static String COLUMN_WORKSHOP_DESC = "news_desc";
    public final static String COLUMN_WORKSHOP_URL_TO_IMAGE = "news_urlToImage";
    //if is_applied = 1, do not show this workshop at the homepage, check this after logged-in only
    public final static String COLUMN_IS_APPLIED = "is_bookmarked";
    public final static String ITEM_INDEX = "item_index";


    public WorkshopsDbHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_WORKSHOP_TABLE_WORKSHOPS =  "CREATE TABLE " + TABLE_NAME_WORKSHOPS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_WORKSHOP_TITLE + " TEXT NOT NULL, "
                + COLUMN_WORKSHOP_DESC + " TEXT, "
                + COLUMN_WORKSHOP_URL_TO_IMAGE + " INTEGER,"
                + COLUMN_IS_APPLIED + " INTEGER DEFAULT 0);";

        String SQL_CREATE_WORKSHOP_TABLE_APPLIED =  "CREATE TABLE " + TABLE_NAME_APPLIED_WORKSHOPS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ITEM_INDEX + " INTEGER, "
                + COLUMN_WORKSHOP_TITLE + " TEXT NOT NULL, "
                + COLUMN_WORKSHOP_DESC + " TEXT, "
                + COLUMN_WORKSHOP_URL_TO_IMAGE + " INTEGER);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_WORKSHOP_TABLE_WORKSHOPS);
        db.execSQL(SQL_CREATE_WORKSHOP_TABLE_APPLIED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WORKSHOPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_APPLIED_WORKSHOPS);

        onCreate(db);
    }

    public boolean insertWorkshop(String title, String desc, int url_to_image, int isApplied) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_WORKSHOP_TITLE, title);
        values.put(COLUMN_WORKSHOP_DESC, desc);
        values.put(COLUMN_WORKSHOP_URL_TO_IMAGE, url_to_image);
        values.put(COLUMN_IS_APPLIED, isApplied);

        db.insert(TABLE_NAME_WORKSHOPS, null, values);
        return true;
    }

    public boolean insertWorkshopApplied(String title, String desc, int url_to_image) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_WORKSHOP_TITLE, title);
        values.put(COLUMN_WORKSHOP_DESC, desc);
        values.put(COLUMN_WORKSHOP_URL_TO_IMAGE, url_to_image);

        db.insert(TABLE_NAME_APPLIED_WORKSHOPS, null, values);
        return true;
    }

    public void deleteWorkshop(String id){

        String where = COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_WORKSHOPS, where, whereArgs);

    }

    public int numberOfRowsInWorkshops() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME_WORKSHOPS);
        return numRows;
    }

    public int numberOfRowsInAppliedWorkshops() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME_APPLIED_WORKSHOPS);
        return numRows;
    }

    public void deleteAllRecordWorkshops(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME_WORKSHOPS);
    }

    public void deleteAllRecordWorkshopsApplied(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME_APPLIED_WORKSHOPS);
    }
}