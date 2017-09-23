package com.example.android.dreamonexhebition;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.dreamonexhebition.data.CustContract;
import com.example.android.dreamonexhebition.data.CustContract.CustEntry;
import com.example.android.dreamonexhebition.data.CustDbHelper;

import java.io.File;
import java.io.FileWriter;

import au.com.bytecode.opencsv.CSVWriter;


/**
 * Created by learner on 11/9/2017.
 */


public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int CUST_LOADER = 0;

    CustCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView custListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        custListView.setEmptyView(emptyView);

        mCursorAdapter = new CustCursorAdapter(this, null);
        custListView.setAdapter(mCursorAdapter);

        custListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                Uri currentCustUri = ContentUris.withAppendedId(CustContract.CustEntry.CONTENT_URI, id);
                intent.setData(currentCustUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(CUST_LOADER,null, this);
    }
     /** for debug**/
    private void insertCustomer(){

        ContentValues values = new ContentValues();
        values.put(CustEntry.COLUMN_CUSTOMER_NAME, "elina kokkali");
        values.put(CustEntry.COLUMN_CUSTOMER_EMAIL, "smt@gmail.com");
        values.put(CustEntry.COLUMN_CUSTOMER_ADMIN_COMMENTS, "fghjkjhgk");
        values.put(CustEntry.COLUMN_CUSTOMER_DATE, "17/12/2017");
        values.put(CustEntry.COLUMN_CUSTOMER_LOCATION, "london");
        values.put(CustEntry.COLUMN_CUSTOMER_TELEPHONE, "6944564361");

        Uri newUri = getContentResolver().insert(CustEntry.CONTENT_URI,values);
    }

    private void deleteAllPets(){
        int rowsDeleted = getContentResolver().delete(CustEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + "rows deleted from cust db");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
           // case R.id.action_insert_dummy_data:
               // insertCustomer();
           //     return true;
            case R.id.action_extract_all_entries:
               if (exportDB()==true){
                   Toast.makeText(this, getString(R.string.db_extracted),
                           Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(this, getString(R.string.db_extracted_false),
                           Toast.LENGTH_SHORT).show();
               }
                return true;
            //case R.id.action_delete_all_entries:
                //deleteAllPets();
              //  return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {CustEntry._ID,
        CustEntry.COLUMN_CUSTOMER_NAME,
        CustEntry.COLUMN_CUSTOMER_EMAIL,
        CustEntry.COLUMN_CUSTOMER_LOCATION};

        return  new CursorLoader(this,
                CustEntry.CONTENT_URI,
                projection,
                null,
                null,
                null );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mCursorAdapter.swapCursor(null);
    }

    private boolean exportDB(){
        File dbFile = getDatabasePath("dreamon.db");
        System.out.println(dbFile);
        CustDbHelper custDbHelper = new CustDbHelper(getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(),"");
        if(!exportDir.exists()){
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "customers.csv");
        try{
            file.createNewFile();
            CSVWriter csvWriter = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = custDbHelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM customers", null);
            csvWriter.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()){
                String arrStr[]={curCSV.getString(0),curCSV.getString(1),curCSV.getString(2),
                        curCSV.getString(3),curCSV.getString(4),curCSV.getString(6)};
                csvWriter.writeNext(arrStr);
            }
            csvWriter.close();
            curCSV.close();
            return true;
        }catch (Exception sqlEx){
            Log.v("CatalogueActivity", sqlEx.getMessage(),sqlEx);
            return false;
        }
    }
}
