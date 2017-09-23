package com.example.android.dreamonexhebition;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.dreamonexhebition.data.CustContract.CustEntry;

/**
 * Created by learner on 12/9/2017.
 */

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXISTING_CUST_LOADER = 0;

    private Uri mCurrentCustUri;

    private EditText mNameEditText;
    private EditText mEmailText;
    private EditText mTelephoneText;
    private EditText mDate;
    private EditText mLocation;
    private EditText mComments;

    private boolean mCustHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent){
            mCustHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentCustUri = intent.getData();

        if (mCurrentCustUri == null){
            setTitle(getString(R.string.editor_activity_title_new_cust));
            invalidateOptionsMenu();
        }else {
            setTitle(getString(R.string.editor_activity_edit_cust));
            getLoaderManager().initLoader(EXISTING_CUST_LOADER,null, this);
        }
        mNameEditText = (EditText)findViewById(R.id.edit_customer_name);
        mEmailText = (EditText) findViewById(R.id.edit_customer_email);
        mTelephoneText = (EditText) findViewById(R.id.edit_customer_telephone);
        mDate = (EditText) findViewById(R.id.edit_customer_date);
        mLocation = (EditText) findViewById(R.id.edit_customer_location);
        mComments = (EditText) findViewById(R.id.edit_admin_comments);

        mNameEditText.setOnTouchListener(mTouchListener);
        mEmailText.setOnTouchListener(mTouchListener);
        mTelephoneText.setOnTouchListener(mTouchListener);
        mDate.setOnTouchListener(mTouchListener);
        mLocation.setOnTouchListener(mTouchListener);
        mComments.setOnTouchListener(mTouchListener);

    }
    private void saveCustomer(){
        String nameString = mNameEditText.getText().toString().trim();
        String emailString = mEmailText.getText().toString().trim();
        String telephoneString = mTelephoneText.getText().toString().trim();
        String dateString = mDate.getText().toString().trim();
        String locationString = mLocation.getText().toString().trim();
        String commentsString = mComments.getText().toString().trim();

        if (mCurrentCustUri == null &&
                TextUtils.isEmpty(nameString)&& TextUtils.isEmpty(emailString)
                && TextUtils.isEmpty(telephoneString)&& TextUtils.isEmpty(dateString)
                && TextUtils.isEmpty(locationString) && TextUtils.isEmpty(commentsString)){
            return;
        }
        ContentValues values = new ContentValues();
        values.put(CustEntry.COLUMN_CUSTOMER_NAME, nameString);
        values.put(CustEntry.COLUMN_CUSTOMER_EMAIL, emailString);
        values.put(CustEntry.COLUMN_CUSTOMER_TELEPHONE, telephoneString);
        values.put(CustEntry.COLUMN_CUSTOMER_DATE, dateString);
        values.put(CustEntry.COLUMN_CUSTOMER_LOCATION, locationString);
        values.put(CustEntry.COLUMN_CUSTOMER_ADMIN_COMMENTS, commentsString);
        if (mCurrentCustUri == null) {
            Uri newUri = getContentResolver().insert(CustEntry.CONTENT_URI, values);

            if (newUri == null){
                Toast.makeText(this, getString(R.string.editor_insert_customer_failed),
                        Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, getString(R.string.editor_insert_customer_succesfull),
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            int rowsAffected = getContentResolver().update(mCurrentCustUri, values, null, null);
            if (rowsAffected == 0){
                Toast.makeText(this, getString(R.string.editor_update_customer_failed),
                        Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, getString(R.string.editor_update_customer_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        if(mCurrentCustUri == null){
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_save:

                if ( mEmailText.getText().toString().trim().equals("")&& mCurrentCustUri != null &&
                        !mNameEditText.getText().toString().trim().equals("")
                        && !mTelephoneText.getText().toString().trim().equals("")
                        && !mDate.getText().toString().trim().equals("")
                        && !mLocation.getText().toString().trim().equals("")
                        && !mComments.getText().toString().trim().equals("")){
                    Toast.makeText(this, getString(R.string.email_field_required),
                            Toast.LENGTH_SHORT).show();
                   return false;
                    // mEmailText.setError("Email is Required!");
                }else{
                saveCustomer();
                finish();
                return true;}
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mCustHasChanged){
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                }

            };
            showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        if(!mCustHasChanged){
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick (DialogInterface dialogInterface, int i){
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
        String[] projection ={
                CustEntry._ID,
                CustEntry.COLUMN_CUSTOMER_NAME,
                CustEntry.COLUMN_CUSTOMER_EMAIL,
                CustEntry.COLUMN_CUSTOMER_TELEPHONE,
                CustEntry.COLUMN_CUSTOMER_DATE,
                CustEntry.COLUMN_CUSTOMER_LOCATION,
                CustEntry.COLUMN_CUSTOMER_ADMIN_COMMENTS };

        return new CursorLoader(this,
                mCurrentCustUri,
                projection,
                null,
                null,
                null);
        }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        if (cursor==null ||cursor.getCount()<1){
            return;
        }

        if (cursor.moveToFirst()){
            int nameColumnIndex = cursor.getColumnIndex(CustEntry.COLUMN_CUSTOMER_NAME);
            int emailColumnIndex = cursor.getColumnIndex(CustEntry.COLUMN_CUSTOMER_EMAIL);
            int telephoneColumnIndex = cursor.getColumnIndex(CustEntry.COLUMN_CUSTOMER_TELEPHONE);
            int dateColumnIndex = cursor.getColumnIndex(CustEntry.COLUMN_CUSTOMER_DATE);
            int locationColumnIndex = cursor.getColumnIndex(CustEntry.COLUMN_CUSTOMER_LOCATION);
            int commentsColumnIndex = cursor.getColumnIndex(CustEntry.COLUMN_CUSTOMER_ADMIN_COMMENTS);

            String name = cursor.getString(nameColumnIndex);
            String email = cursor.getString(emailColumnIndex);
            String telephone = cursor.getString(telephoneColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String location = cursor.getString(locationColumnIndex);
            String comments = cursor.getString(commentsColumnIndex);

            mNameEditText.setText(name);
            mEmailText.setText(email);
            mTelephoneText.setText(telephone);
            mDate.setText(date);
            mLocation.setText(location);
            mComments.setText(comments);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mNameEditText.setText("");
        mEmailText.setText("");
        mTelephoneText.setText("");
        mDate.setText("");
        mLocation.setText("");
        mComments.setText("");
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard,discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                if (dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                deleteCustomer();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                if (dialog!=null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteCustomer(){
        if(mCurrentCustUri!=null){
            int rowsDeleted = getContentResolver().delete(mCurrentCustUri,null,null);

            if (rowsDeleted==0){
                Toast.makeText(this, getString(R.string.editor_delete_customer_failed),
                        Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, getString(R.string.editor_delete_customer_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}