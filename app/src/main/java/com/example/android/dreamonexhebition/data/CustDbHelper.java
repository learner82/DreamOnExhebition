package com.example.android.dreamonexhebition.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by learner on 11/9/2017.
 */
public class CustDbHelper extends SQLiteOpenHelper{

    public static final String LOG_TAG = CustDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "dreamon.db";

    private static final int DATABASE_VERSION = 1;

    public CustDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        String SQL_CREATE_CUST_TABLE = "CREATE TABLE " + CustContract.CustEntry.TABLE_NAME + "("
                + CustContract.CustEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CustContract.CustEntry.COLUMN_CUSTOMER_NAME + " TEXT NOT NULL, "
                + CustContract.CustEntry.COLUMN_CUSTOMER_EMAIL + " TEXT NOT NULL, "
                + CustContract.CustEntry.COLUMN_CUSTOMER_TELEPHONE + " TEXT, "
                + CustContract.CustEntry.COLUMN_CUSTOMER_DATE + " TEXT, "
                + CustContract.CustEntry.COLUMN_CUSTOMER_LOCATION + " TEXT, "
                + CustContract.CustEntry.COLUMN_CUSTOMER_ADMIN_COMMENTS + " TEXT ); ";
        Log.v(LOG_TAG, SQL_CREATE_CUST_TABLE);
        db.execSQL(SQL_CREATE_CUST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}
