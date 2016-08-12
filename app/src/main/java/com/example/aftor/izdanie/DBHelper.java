package com.example.aftor.izdanie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Aftor on 28.07.2016.
 */
class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    private static final String DATABASE_NAME = "myDB";
    private static final int DATABASE_VERSION = 13;
    private static final String TAG = "myLogs";

    public static synchronized DBHelper getInstance(Context context) {

        if (sInstance == null) {
            Log.d(TAG, "sInstance == null");
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqdb) {

        sqdb.beginTransaction();
        try {

            String[] tables = DataBaseTables.getAllTables();

            for (int i = 0; i < tables.length; i++) {

                String table = DataBaseTables.getTable(i);

                Log.d(TAG, table);

                sqdb.execSQL(table);
            }

            sqdb.setTransactionSuccessful();
            Log.d(TAG, "SQL database's are created");
        } catch (Exception e) {
            Log.d(TAG, "SQL database doesn't created");
        } finally {
            sqdb.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqdb, int oldVersion, int newVersion) {

        if (oldVersion == 12 & newVersion == 13) {

            sqdb.beginTransaction();
            Log.d(TAG, "begin transaction");
            try {

                sqdb.execSQL("create table dateServer ("
                        + "date text);");

                Log.d(TAG, "Transaction successful");

                sqdb.setTransactionSuccessful();
            } finally {
                sqdb.endTransaction();
            }
        }
    }
}
