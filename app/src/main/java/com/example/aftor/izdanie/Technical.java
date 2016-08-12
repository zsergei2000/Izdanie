package com.example.aftor.izdanie;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

public class Technical extends Activity {

    private static final String TAG = "myLogs";
    private DataBase sqdb;
    private Spinner spinner;
    ArrayAdapter<String> adapter;
    GridView gvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tech_tabs);

        sqdb = new DataBase();
        sqdb.OpenDB(this);

        spinner = (Spinner) findViewById(R.id.spinner1);

        createTabelsMenu();
    }

    private void createTabelsMenu() {

        String[] tables = DataBaseTables.getAllTables();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tables);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void click_show(View v) {

        String thisTable = spinner.getSelectedItem().toString();

        //Log.d(TAG, "thisTable " + thisTable);

        String query = "select * "
                + "from "+thisTable+" as dataTable";

        Cursor c = sqdb.getSQLData(query, null);

        String[] tabelsData = c.getColumnNames();
        String[] tabelsData1 = new String[c.getCount()*tabelsData.length+tabelsData.length];

        System.arraycopy(tabelsData, 0, tabelsData1, 0, tabelsData.length);

        /*for (int i = 0; i < tabelsData.length; i++) {
            tabelsData1[i] = tabelsData[i];
        }*/

        int count = tabelsData.length;

        if (c.moveToFirst()) {
            do {
                for (int i = 0; i < tabelsData.length; i++) {

                    tabelsData1[count++] = c.getString(i)==null?"":c.getString(i);

                }
            } while (c.moveToNext());
        }
        c.close();

        adjustGridView(tabelsData1, tabelsData.length);

    }

    private void adjustGridView(String[] tabelsData, int columnLength) {

        adapter = new ArrayAdapter<>(this, R.layout.gride_item, R.id.tvText, tabelsData);
        gvMain = (GridView) findViewById(R.id.gvMain);
        gvMain.setAdapter(adapter);

        //gvMain.setNumColumns(GridView.AUTO_FIT);
        //gvMain.setColumnWidth(80);

        gvMain.setNumColumns(columnLength);
        gvMain.setVerticalSpacing(1);
        gvMain.setHorizontalSpacing(1);

    }

}
