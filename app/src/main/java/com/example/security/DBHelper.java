package com.example.security;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.os.Parcelable;


public class DBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "TABLITA";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String UUID = "uuid";
    static final String DB_NAME = "JESUSMADRID.DB";
    static final int DB_VERSION = 1;
    private SQLiteDatabase database;
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT , "+ EMAIL + " TEXT , "+ UUID + " TEXT);";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(String nombre, String email, String uuid, Context context){
        ContentValues contentValue = new ContentValues();
        contentValue.put(NAME,nombre);
        contentValue.put(EMAIL, email);
        contentValue.put(UUID, uuid);
        SQLiteOpenHelper dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        database.insert(TABLE_NAME, null, contentValue);
    }
    public int update(String nombre, String email, String uuid) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME,nombre);
        contentValues.put(EMAIL, email);
        contentValues.put(UUID, uuid);
        int i = database.update(TABLE_NAME, contentValues, _ID + " = " + uuid, null);
        return i;
    }
    public void delete(long _id) {
        database.delete(TABLE_NAME, _ID + "=" + _id, null);
    }


}

