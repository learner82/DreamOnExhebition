package com.example.android.dreamonexhebition.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by learner on 11/9/2017.
 */

public class CustContract {

    private CustContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.dreamonexhebition";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_CUST = "Customers";

    public static final class CustEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CUST);

        public  static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CUST;

        public  static  final  String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CUST;

        public static final String TABLE_NAME = "customers";

        public static final String _ID = BaseColumns._ID ;
        public static final String COLUMN_CUSTOMER_NAME = "name";
        public static final String COLUMN_CUSTOMER_EMAIL = "email";
        public static final String COLUMN_CUSTOMER_TELEPHONE = "telephone";
        public static final String COLUMN_CUSTOMER_DATE = "potDate";
        public static final String COLUMN_CUSTOMER_LOCATION = "location";
        public static final String COLUMN_CUSTOMER_ADMIN_COMMENTS = "comments";
    }
}
