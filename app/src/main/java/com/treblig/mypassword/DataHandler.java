package com.treblig.mypassword;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gilbert on 11/2/2015.
 */
public class DataHandler {
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String TABLE_NAME = "mytable";
    public static final String DATA_BASE_NAME = "mydatabase";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_CREATE = "create table mytable (name text not null, " +
            "email text not null, password text not null);";

    DatabaseHelper dbHelper;
    Context ctx;
    SQLiteDatabase db;


    public DataHandler(Context ctx){
        this.ctx = ctx;
        dbHelper = new DatabaseHelper(ctx);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {

        public DatabaseHelper(Context ctx) {
            super(ctx, DATA_BASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(TABLE_CREATE);
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS mytable");
            onCreate(db);
        }
    }

    public DataHandler open()
    {
        db = dbHelper.getWritableDatabase();
        return  this;
    }

    public void close()
    {
        dbHelper.close();
    }

    public long insertData (String name, String email, String password){
        ContentValues content=  new ContentValues();
        content.put(NAME,name);
        content.put(EMAIL,email);
        content.put(PASSWORD, password);
        return db.insertOrThrow(TABLE_NAME,null,content);
    }

    public Cursor returnData()
    {
        return db.query(TABLE_NAME, new String[]{NAME, EMAIL, PASSWORD}, null, null, null, null, null);
    }

    public boolean updateDataPassword (String password,String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PASSWORD,password);
        return db.update (TABLE_NAME,contentValues,"email = ?", new String[]{email})>0;

    }

    public boolean deleteDataPassword (String email){
        return db.delete(TABLE_NAME,"email = ?", new String[]{email})>0;
    }




}
