package com.example.android.dreamonexhebition;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.dreamonexhebition.data.CustContract;

/**
 * Created by learner on 12/9/2017.
 */

public class CustCursorAdapter extends CursorAdapter {

    public CustCursorAdapter(Context context, Cursor c){
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
        TextView locationTextView = (TextView) view.findViewById(R.id.location);

        int  nameColumnIndex = cursor.getColumnIndex(CustContract.CustEntry.COLUMN_CUSTOMER_NAME);
        int  emailColumnIndex = cursor.getColumnIndex(CustContract.CustEntry.COLUMN_CUSTOMER_EMAIL);
        int locationColumnIndex = cursor.getColumnIndex(CustContract.CustEntry.COLUMN_CUSTOMER_LOCATION);

        String custName = cursor.getString(nameColumnIndex);
        String custEmail = cursor.getString(emailColumnIndex);
        String custLocation=cursor.getString(locationColumnIndex);

        nameTextView.setText(custName);
        summaryTextView.setText(custEmail);
        locationTextView.setText(custLocation);
    }
}
