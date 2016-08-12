package com.example.aftor.izdanie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class DataBase {

    public static final int KOD = 0, NAME = 1, VALUE = 2;
    private static final String TAG = "myLogs";
    public DBHelper dbh;
    public SQLiteDatabase sqdb;

    public DataBase(){}

    public void OpenDB(Context context) {
        dbh = DBHelper.getInstance(context);
        sqdb = dbh.getWritableDatabase();

        String query = "create virtual table if not exists vTable using fts3 ("
                + "number text, name text, client text, status text)";
        executeSQL(query);
    }

    public void closeDB() {
        if (dbh != null) dbh.close();
    }

    public Cursor getSQLData(String sqlQuery, String[] Condition){
        return sqdb.rawQuery(sqlQuery, Condition);
    }

    public void clearTable(int table){

        String name = DataBaseTables.getTableName(table);
        int count = sqdb.delete(name, null, null);
        Log.d(TAG, "delete " + count + " rows, table - " + name);
    }

    public void clearTable1(String table){
        int count = sqdb.delete(table, null, null);
        Log.d(TAG, "delete " + count + " rows, table - " + table);
    }

    public void clearTableVirtual(){

        int count = sqdb.delete("vTable", null, null);
        Log.d(TAG, "delete " + count + " rows");
    }

    public void addData(ArrayList<String[]> arr, int table) {

        String      name    = DataBaseTables.getTableName(table);
        String[][]  fields  = DataBaseTables.getFields(table);

        ContentValues cv = new ContentValues();
        for (String[] data : arr) {

            cv.clear();
            for (int i = 0; i < data.length; i++) {

                cv.put(fields[i+1][0], data[i]);
            }
            sqdb.insert(name, null, cv);
        }
    }

    public void addData1(ArrayList<String[]> arr, int table) {

        String      name    = DataBaseTables.getTableName(table);
        String[][]  fields  = DataBaseTables.getFields(table);

        ContentValues cv = new ContentValues();
        for (String[] data : arr) {

            cv.clear();
            for (int i = 0; i < data.length; i++) {

                cv.put(fields[i+1][0], data[i]);
            }
            sqdb.insert("vTable", null, cv);
        }
    }

    public void updateData(String _id, String status) {

        //String      name    = DataBaseTables.getTableName(table);
        //String[][]  fields  = DataBaseTables.getFields(table);

        ContentValues cv = new ContentValues();
        //for (String[] data : arr) {

        /*cv.clear();
        for (int i = 0; i < arr.length; i++) {
            cv.put(fields[i+1][0], arr[i]);
        }
        sqdb.update(name, null, cv, condition, values);*/

        cv.put("status", status);

        sqdb.update("orders", cv, "_id=?", new String[] { _id });
        //}
    }

    public void executeSQL(String execString) {
        sqdb.execSQL(execString);
    }

    /*private void writeTRT(String table, ArrayList<Map<String, String>> data) throws IOException {

        ContentValues cv = new ContentValues();
        for (Map<String, String> map : data) {

            cv.clear();
            cv.put("kod",	map.get("1").toString());
            cv.put("name",	map.get("2").toString());
            cv.put("ol_id",	map.get("3").toString());
            sqdb.insert(table, null, cv);
        }
    }*/
}
