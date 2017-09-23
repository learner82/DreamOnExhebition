package com.example.android.dreamonexhebition.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by learner on 11/9/2017.
 */

public class CustProvider extends ContentProvider{

    public static final String LOG_TAG = CustProvider.class.getSimpleName();

    private static final int CUST = 100;

    private static final int CUST_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(CustContract.CONTENT_AUTHORITY, CustContract.PATH_CUST, CUST);

        sUriMatcher.addURI(CustContract.CONTENT_AUTHORITY, CustContract.PATH_CUST +"/#", CUST_ID);
    }

    private CustDbHelper mDbHelper;

    @Override
    public boolean onCreate(){
        mDbHelper = new CustDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs,
                         String sortOrder){
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match){
            case CUST:
                cursor = database.query(CustContract.CustEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case CUST_ID:
                selection = CustContract.CustEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(CustContract.CustEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public Uri insert (Uri uri, ContentValues contentValues){
        final int match = sUriMatcher.match(uri);
                switch (match){
                    case CUST:
                        return insertCust(uri,contentValues);
                    default:
                        throw new IllegalArgumentException("Insertion is not supported for" + uri);
        }
    }
    private Uri insertCust(Uri uri, ContentValues values){
        String name = values.getAsString(CustContract.CustEntry.COLUMN_CUSTOMER_NAME);
        if (name==null){
            throw new IllegalArgumentException("Customer requires a name");
        }

        String email = values.getAsString(CustContract.CustEntry.COLUMN_CUSTOMER_EMAIL);
        if (email==null){
            throw  new IllegalArgumentException("Customer requires an email");
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(CustContract.CustEntry.TABLE_NAME, null, values);

        if(id ==-1){
            Log.e(LOG_TAG,"Failed to insert row for " + uri );
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues,String selection,
                      String[] selectionArgs ){
        final int match = sUriMatcher.match(uri);

        switch (match){
            case CUST:
                return updateCust(uri, contentValues, selection, selectionArgs);
            case CUST_ID:
                selection = CustContract.CustEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateCust(uri, contentValues,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);

        }
    }

    private int updateCust(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        if (values.containsKey(CustContract.CustEntry.COLUMN_CUSTOMER_NAME)){
            String name = values.getAsString(CustContract.CustEntry.COLUMN_CUSTOMER_NAME);
            if(name==null){
                throw new IllegalArgumentException("Customer requires name");
            }
        }

        if (values.containsKey(CustContract.CustEntry.COLUMN_CUSTOMER_EMAIL)){
            String name = values.getAsString(CustContract.CustEntry.COLUMN_CUSTOMER_EMAIL);
            if(name==null){
                throw new IllegalArgumentException("Customer requires email");
            }
        }
        if (values.size()==0){
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(CustContract.CustEntry.TABLE_NAME, values,selection,selectionArgs);

        if (rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match){
            case CUST:
                rowsDeleted=database.delete(CustContract.CustEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case CUST_ID:
                selection = CustContract.CustEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted=database.delete(CustContract.CustEntry.TABLE_NAME,selection,selectionArgs);
                default:
                    throw new IllegalArgumentException("Deletion is not supported for " + uri );
        }
        if (rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri){
        final int match = sUriMatcher.match(uri);
        switch (match){
            case CUST:
                return CustContract.CustEntry.CONTENT_LIST_TYPE;
            case CUST_ID:
                return CustContract.CustEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri + "with match" + match);
        }
    }
}
